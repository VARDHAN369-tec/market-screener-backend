package com.stockmarket.screener.excel;

import com.stockmarket.screener.entity.CashFlow;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import com.stockmarket.screener.repository.CashFlowRepository;
import com.stockmarket.screener.repository.FinancialPeriodRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class CashFlowParser {

    private final FinancialPeriodRepository periodRepository;
    private final CashFlowRepository cashFlowRepository;

    public CashFlowParser(FinancialPeriodRepository periodRepository, CashFlowRepository cashFlowRepository) {
        this.periodRepository = periodRepository;
        this.cashFlowRepository = cashFlowRepository;
    }

    public List<CashFlow> parseCashFlow(Sheet sheet, Company company) {
        List<CashFlow> results = new ArrayList<>();
        if (sheet == null || company == null) return results;

        Row dateRow = sheet.getRow(2);
        if (dateRow == null) return results;

        Map<Integer, FinancialPeriod> colToPeriod = new HashMap<>();
        Set<Long> seenPeriodIds = new HashSet<>();
        for (int col = 1; col < dateRow.getLastCellNum(); col++) {
            Cell cell = dateRow.getCell(col);
            LocalDate date = PoiUtils.getDateValue(cell);
            if (date != null) {
                int fiscalYear = date.getMonthValue() >= 4 ? date.getYear() + 1 : date.getYear();
                String label = "FY " + fiscalYear;
                LocalDate finalDate = date;
                FinancialPeriod period = periodRepository
                        .findByCompanyAndPeriodTypeAndPeriodDate(company, FinancialPeriod.PeriodType.ANNUAL, date)
                        .orElseGet(() -> {
                            FinancialPeriod p = new FinancialPeriod(company, FinancialPeriod.PeriodType.ANNUAL, label, finalDate, fiscalYear, null);
                            return periodRepository.save(p);
                        });
                if (seenPeriodIds.add(period.getPeriodId())) {
                    colToPeriod.put(col, period);
                }
            }
        }

        Map<Integer, CashFlow> colToRecord = new HashMap<>();
        for (Map.Entry<Integer, FinancialPeriod> entry : colToPeriod.entrySet()) {
            FinancialPeriod period = entry.getValue();
            CashFlow cf = cashFlowRepository.findByCompanyAndPeriod(company, period)
                    .orElseGet(() -> {
                        CashFlow newCf = new CashFlow();
                        newCf.setCompany(company);
                        newCf.setPeriod(period);
                        return newCf;
                    });
            colToRecord.put(entry.getKey(), cf);
        }

        for (int r = 3; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String label = PoiUtils.getStringValue(row.getCell(0));
            if (label == null || label.trim().isEmpty()) continue;
            String norm = label.trim().toLowerCase();

            for (Map.Entry<Integer, CashFlow> entry : colToRecord.entrySet()) {
                int col = entry.getKey();
                CashFlow cf = entry.getValue();
                BigDecimal val = PoiUtils.getBigDecimalValue(row.getCell(col));

                if (norm.contains("operating activity") || norm.contains("operating activities")) {
                    cf.setCashFromOperatingActivity(val);
                } else if (norm.contains("investing activity") || norm.contains("investing activities")) {
                    cf.setCashFromInvestingActivity(val);
                } else if (norm.contains("financing activity") || norm.contains("financing activities")) {
                    cf.setCashFromFinancingActivity(val);
                } else if (norm.contains("net cash flow")) {
                    cf.setNetCashFlow(val);
                }
            }
        }

        for (CashFlow cf : colToRecord.values()) {
            results.add(cashFlowRepository.save(cf));
        }

        return results;
    }
}
