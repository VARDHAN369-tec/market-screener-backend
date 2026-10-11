package com.stockmarket.screener.excel;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.MarketData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSheetParser {

    public static class ParsedCompanyMeta {
        public String companyName;
        public String sector;
        public String industry;
        public BigDecimal currentPrice;
        public BigDecimal marketCap;
        public BigDecimal numberOfShares;
        public BigDecimal faceValue;
    }

    public ParsedCompanyMeta parseMetadata(Sheet sheet) {
        if (sheet == null) return null;
        ParsedCompanyMeta meta = new ParsedCompanyMeta();

        for (int i = 0; i <= Math.min(sheet.getLastRowNum(), 30); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            String label = PoiUtils.getStringValue(row.getCell(0));
            if (label == null) continue;
            label = label.trim().toUpperCase();

            if (label.contains("COMPANY NAME")) {
                meta.companyName = clean(PoiUtils.getStringValue(row.getCell(1)));
            } else if (label.equals("SECTOR")) {
                meta.sector = clean(PoiUtils.getStringValue(row.getCell(1)));
            } else if (label.equals("INDUSTRY")) {
                meta.industry = clean(PoiUtils.getStringValue(row.getCell(1)));
            } else if (label.contains("CURRENT PRICE")) {
                meta.currentPrice = PoiUtils.getBigDecimalValue(row.getCell(1));
            } else if (label.contains("MARKET CAPITALIZATION") || label.contains("MARKET CAP")) {
                meta.marketCap = PoiUtils.getBigDecimalValue(row.getCell(1));
            } else if (label.contains("NUMBER OF SHARES")) {
                meta.numberOfShares = PoiUtils.getBigDecimalValue(row.getCell(1));
            } else if (label.contains("FACE VALUE")) {
                meta.faceValue = PoiUtils.getBigDecimalValue(row.getCell(1));
            }
        }

        return meta;
    }

    private String clean(String s) {
        if (s == null) return null;
        String c = s.replace('\u00A0', ' ').replace('\uFEFF', ' ').trim();
        return c.isEmpty() ? null : c;
    }
}
