package com.stockmarket.screener.excel;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import com.stockmarket.screener.entity.ProfitLoss;
import com.stockmarket.screener.repository.FinancialPeriodRepository;
import com.stockmarket.screener.repository.ProfitLossRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class ProfitLossParser {

    private final FinancialPeriodRepository periodRepository;
    private final ProfitLossRepository profitLossRepository;

    public ProfitLossParser(FinancialPeriodRepository periodRepository, ProfitLossRepository profitLossRepository) {
        this.periodRepository = periodRepository;
        this.profitLossRepository = profitLossRepository;
    }

    public List<ProfitLoss> parseProfitLoss(Sheet sheet, Company company) {
        List<ProfitLoss> results = new ArrayList<>();
        if (sheet == null || company == null) return results;

        // Row 3 (index 2) contains date headers
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

        // Map column -> ProfitLoss entity
        Map<Integer, ProfitLoss> colToRecord = new HashMap<>();
        for (Map.Entry<Integer, FinancialPeriod> entry : colToPeriod.entrySet()) {
            FinancialPeriod period = entry.getValue();
            ProfitLoss pl = profitLossRepository.findByCompanyAndPeriod(company, period)
                    .orElseGet(() -> {
                        ProfitLoss newPl = new ProfitLoss();
                        newPl.setCompany(company);
                        newPl.setPeriod(period);
                        return newPl;
                    });
            colToRecord.put(entry.getKey(), pl);
        }

        // Parse line items
        for (int r = 3; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String label = PoiUtils.getStringValue(row.getCell(0));
            if (label == null || label.trim().isEmpty()) continue;
            String norm = label.trim().toLowerCase();

            for (Map.Entry<Integer, ProfitLoss> entry : colToRecord.entrySet()) {
                int col = entry.getKey();
                ProfitLoss pl = entry.getValue();
                BigDecimal val = PoiUtils.getBigDecimalValue(row.getCell(col));

                if (norm.equals("sales")) {
                    pl.setSales(val);
                } else if (norm.equals("expenses")) {
                    pl.setExpenses(val);
                } else if (norm.contains("operating profit") && !norm.contains("margin") && !norm.contains("growth")) {
                    pl.setOperatingProfit(val);
                } else if (norm.contains("other income")) {
                    pl.setOtherIncome(val);
                } else if (norm.contains("depreciation")) {
                    pl.setDepreciation(val);
                } else if (norm.equals("interest")) {
                    pl.setInterest(val);
                } else if (norm.contains("profit before tax") || norm.equals("pbt")) {
                    pl.setProfitBeforeTax(val);
                } else if (norm.equals("tax")) {
                    pl.setTax(val);
                } else if (norm.contains("net profit") && !norm.contains("margin") && !norm.contains("growth")) {
                    pl.setNetProfit(val);
                } else if (norm.equals("eps")) {
                    pl.setEps(val);
                } else if (norm.contains("price to earning")) {
                    pl.setPriceToEarning(val);
                } else if (norm.equals("price")) {
                    pl.setPrice(val);
                } else if (norm.contains("dividend amount")) {
                    pl.setDividendAmount(val);
                }
            }
        }

        for (ProfitLoss pl : colToRecord.values()) {
            results.add(profitLossRepository.save(pl));
        }

        return results;
    }
}
