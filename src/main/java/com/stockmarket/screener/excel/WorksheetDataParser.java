package com.stockmarket.screener.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.WorksheetData;
import com.stockmarket.screener.repository.WorksheetDataRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WorksheetDataParser {

    private final WorksheetDataRepository worksheetDataRepository;
    private final ObjectMapper objectMapper;

    public WorksheetDataParser(WorksheetDataRepository worksheetDataRepository) {
        this.worksheetDataRepository = worksheetDataRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void parseAndSaveSheetData(Sheet sheet, String sheetName, Company company) {
        if (sheet == null || company == null) return;

        // Delete existing worksheet data for this sheet
        List<WorksheetData> existing = worksheetDataRepository.findByCompanyAndSheetNameOrderByRowOrderAsc(company, sheetName);
        if (!existing.isEmpty()) {
            worksheetDataRepository.deleteAll(existing);
        }

        List<WorksheetData> records = new ArrayList<>();
        int maxRows = sheet.getLastRowNum();

        for (int r = 0; r <= maxRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            String label = PoiUtils.getStringValue(row.getCell(0));
            int lastCell = row.getLastCellNum();
            if (lastCell <= 0 && (label == null || label.trim().isEmpty())) continue;

            List<String> cells = new ArrayList<>();
            for (int c = 0; c < Math.min(lastCell, 30); c++) {
                Cell cell = row.getCell(c);
                String val = PoiUtils.getStringValue(cell);
                cells.add(val != null ? val : "");
            }

            // Check if entire row is empty
            boolean hasContent = cells.stream().anyMatch(s -> !s.trim().isEmpty());
            if (!hasContent) continue;

            try {
                String json = objectMapper.writeValueAsString(cells);
                WorksheetData wd = new WorksheetData(company, sheetName, r, label != null ? label : "", json);
                records.add(wd);
            } catch (Exception ignored) {}
        }

        if (!records.isEmpty()) {
            worksheetDataRepository.saveAll(records);
        }
    }
}
