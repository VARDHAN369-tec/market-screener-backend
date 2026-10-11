package com.stockmarket.screener.excel;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import com.stockmarket.screener.entity.QuarterlyResult;
import com.stockmarket.screener.repository.FinancialPeriodRepository;
import com.stockmarket.screener.repository.QuarterlyResultRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class QuarterParser {

    private final FinancialPeriodRepository periodRepository;
    private final QuarterlyResultRepository quarterlyResultRepository;

    public QuarterParser(FinancialPeriodRepository periodRepository, QuarterlyResultRepository quarterlyResultRepository) {
        this.periodRepository = periodRepository;
        this.quarterlyResultRepository = quarterlyResultRepository;
    }

    public List<QuarterlyResult> parseQuarters(Sheet sheet, Company company) {
        List<QuarterlyResult> results = new ArrayList<>();
        if (sheet == null || company == null) return results;

        Row dateRow = sheet.getRow(2);
        if (dateRow == null) return results;

        Map<Integer, FinancialPeriod> colToPeriod = new HashMap<>();
        Set<Long> seenPeriodIds = new HashSet<>();
        for (int col = 1; col < dateRow.getLastCellNum(); col++) {
            Cell cell = dateRow.getCell(col);
            LocalDate date = PoiUtils.getDateValue(cell);
            if (date != null) {
                int month = date.getMonthValue();
                int fiscalYear = month >= 4 ? date.getYear() + 1 : date.getYear();
                String qStr = (month == 6) ? "Q1" : (month == 9) ? "Q2" : (month == 12) ? "Q3" : "Q4";
                String label = qStr + " FY" + fiscalYear;
                LocalDate finalDate = date;
                FinancialPeriod period = periodRepository
                        .findByCompanyAndPeriodTypeAndPeriodDate(company, FinancialPeriod.PeriodType.QUARTERLY, date)
                        .orElseGet(() -> {
                            FinancialPeriod p = new FinancialPeriod(company, FinancialPeriod.PeriodType.QUARTERLY, label, finalDate, fiscalYear, qStr);
                            return periodRepository.save(p);
                        });
                if (seenPeriodIds.add(period.getPeriodId())) {
                    colToPeriod.put(col, period);
                }
            }
        }

        Map<Integer, QuarterlyResult> colToRecord = new HashMap<>();
        for (Map.Entry<Integer, FinancialPeriod> entry : colToPeriod.entrySet()) {
            FinancialPeriod period = entry.getValue();
            QuarterlyResult qr = quarterlyResultRepository.findByCompanyAndPeriod(company, period)
                    .orElseGet(() -> {
                        QuarterlyResult newQr = new QuarterlyResult();
                        newQr.setCompany(company);
                        newQr.setPeriod(period);
                        return newQr;
                    });
            colToRecord.put(entry.getKey(), qr);
        }

        for (int r = 3; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String label = PoiUtils.getStringValue(row.getCell(0));
            if (label == null || label.trim().isEmpty()) continue;
            String norm = label.trim().toLowerCase();

            for (Map.Entry<Integer, QuarterlyResult> entry : colToRecord.entrySet()) {
                int col = entry.getKey();
                QuarterlyResult qr = entry.getValue();
                BigDecimal val = PoiUtils.getBigDecimalValue(row.getCell(col));

                if (norm.equals("sales")) {
                    qr.setSales(val);
                } else if (norm.equals("expenses")) {
                    qr.setExpenses(val);
                } else if (norm.contains("operating profit") && !norm.contains("margin") && !norm.contains("growth")) {
                    qr.setOperatingProfit(val);
                } else if (norm.contains("other income")) {
                    qr.setOtherIncome(val);
                } else if (norm.contains("depreciation")) {
                    qr.setDepreciation(val);
                } else if (norm.equals("interest")) {
                    qr.setInterest(val);
                } else if (norm.contains("profit before tax") || norm.equals("pbt")) {
                    qr.setProfitBeforeTax(val);
                } else if (norm.equals("tax")) {
                    qr.setTax(val);
                } else if (norm.contains("net profit") && !norm.contains("margin") && !norm.contains("growth")) {
                    qr.setNetProfit(val);
                } else if (norm.equals("eps")) {
                    qr.setEps(val);
                }
            }
        }

        for (QuarterlyResult qr : colToRecord.values()) {
            results.add(quarterlyResultRepository.save(qr));
        }

        return results;
    }
}
