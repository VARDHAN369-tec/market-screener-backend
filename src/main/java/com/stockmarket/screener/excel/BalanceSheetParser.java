package com.stockmarket.screener.excel;

import com.stockmarket.screener.entity.BalanceSheet;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import com.stockmarket.screener.repository.BalanceSheetRepository;
import com.stockmarket.screener.repository.FinancialPeriodRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class BalanceSheetParser {

    private final FinancialPeriodRepository periodRepository;
    private final BalanceSheetRepository balanceSheetRepository;

    public BalanceSheetParser(FinancialPeriodRepository periodRepository, BalanceSheetRepository balanceSheetRepository) {
        this.periodRepository = periodRepository;
        this.balanceSheetRepository = balanceSheetRepository;
    }

    public List<BalanceSheet> parseBalanceSheet(Sheet sheet, Company company) {
        List<BalanceSheet> results = new ArrayList<>();
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

        Map<Integer, BalanceSheet> colToRecord = new HashMap<>();
        for (Map.Entry<Integer, FinancialPeriod> entry : colToPeriod.entrySet()) {
            FinancialPeriod period = entry.getValue();
            BalanceSheet bs = balanceSheetRepository.findByCompanyAndPeriod(company, period)
                    .orElseGet(() -> {
                        BalanceSheet newBs = new BalanceSheet();
                        newBs.setCompany(company);
                        newBs.setPeriod(period);
                        return newBs;
                    });
            colToRecord.put(entry.getKey(), bs);
        }

        boolean seenNetBlock = false;
        for (int r = 3; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String label = PoiUtils.getStringValue(row.getCell(0));
            if (label == null || label.trim().isEmpty()) continue;
            String norm = label.trim().toLowerCase();

            if (norm.contains("net block")) {
                seenNetBlock = true;
            }

            for (Map.Entry<Integer, BalanceSheet> entry : colToRecord.entrySet()) {
                int col = entry.getKey();
                BalanceSheet bs = entry.getValue();
                BigDecimal val = PoiUtils.getBigDecimalValue(row.getCell(col));

                if (norm.contains("equity share capital")) {
                    bs.setEquityShareCapital(val);
                } else if (norm.equals("reserves")) {
                    bs.setReserves(val);
                } else if (norm.equals("borrowings")) {
                    bs.setBorrowings(val);
                } else if (norm.contains("other liabilities")) {
                    bs.setOtherLiabilities(val);
                } else if (norm.equals("total") && !seenNetBlock) {
                    bs.setTotalLiabilities(val);
                } else if (norm.contains("net block")) {
                    bs.setNetBlock(val);
                } else if (norm.contains("capital work in progress") || norm.contains("cwip")) {
                    bs.setCapitalWorkInProgress(val);
                } else if (norm.equals("investments")) {
                    bs.setInvestments(val);
                } else if (norm.contains("other assets")) {
                    bs.setOtherAssets(val);
                } else if (norm.equals("total") && seenNetBlock) {
                    bs.setTotalAssets(val);
                } else if (norm.contains("working capital")) {
                    bs.setWorkingCapital(val);
                } else if (norm.contains("debtors") || norm.contains("receivables")) {
                    bs.setDebtors(val);
                } else if (norm.contains("inventory") && !norm.contains("turnover")) {
                    bs.setInventory(val);
                }
            }
        }

        for (BalanceSheet bs : colToRecord.values()) {
            results.add(balanceSheetRepository.save(bs));
        }

        return results;
    }
}
