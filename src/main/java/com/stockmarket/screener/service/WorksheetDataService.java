package com.stockmarket.screener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.screener.dto.WorksheetDataDTO;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.WorksheetData;
import com.stockmarket.screener.repository.CompanyRepository;
import com.stockmarket.screener.repository.WorksheetDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorksheetDataService {

    private final CompanyRepository companyRepository;
    private final WorksheetDataRepository worksheetDataRepository;
    private final ObjectMapper objectMapper;

    public WorksheetDataService(CompanyRepository companyRepository,
                                WorksheetDataRepository worksheetDataRepository) {
        this.companyRepository = companyRepository;
        this.worksheetDataRepository = worksheetDataRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional(readOnly = true)
    public WorksheetDataDTO getWorksheet(Long companyId, String sheetName) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        // Normalize sheetName
        String targetSheet = normalizeSheetName(sheetName);

        List<WorksheetData> entities = worksheetDataRepository
                .findByCompany_CompanyIdAndSheetNameOrderByRowOrderAsc(companyId, targetSheet);

        List<WorksheetDataDTO.WorksheetRowDTO> rows = new ArrayList<>();
        for (WorksheetData wd : entities) {
            List<String> cells = new ArrayList<>();
            if (wd.getRowDataJson() != null && !wd.getRowDataJson().isEmpty()) {
                try {
                    cells = objectMapper.readValue(wd.getRowDataJson(), new TypeReference<List<String>>() {});
                } catch (Exception ignored) {}
            }
            rows.add(new WorksheetDataDTO.WorksheetRowDTO(wd.getRowOrder(), wd.getLineItem(), cells));
        }

        return new WorksheetDataDTO(company.getCompanyName(), targetSheet, rows);
    }

    private String normalizeSheetName(String sheetName) {
        if (sheetName == null) return "Profit & Loss";
        String lower = sheetName.trim().toLowerCase();
        if (lower.contains("profit") || lower.contains("p&l") || lower.equals("profit-loss")) {
            return "Profit & Loss";
        } else if (lower.contains("quarter")) {
            return "Quarters";
        } else if (lower.contains("balance") || lower.equals("balance-sheet")) {
            return "Balance Sheet";
        } else if (lower.contains("cash") || lower.equals("cash-flow")) {
            return "Cash Flow";
        } else if (lower.contains("data")) {
            return "Data Sheet";
        }
        return sheetName;
    }
}
