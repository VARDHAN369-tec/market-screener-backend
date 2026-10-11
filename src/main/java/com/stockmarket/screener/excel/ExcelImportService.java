package com.stockmarket.screener.excel;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.ImportLog;
import com.stockmarket.screener.financial.FinancialAnalysisService;
import com.stockmarket.screener.repository.CompanyRepository;
import com.stockmarket.screener.repository.ImportLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ExcelImportService {

    private static final Logger log = LoggerFactory.getLogger(ExcelImportService.class);

    @Value("${app.data.directory:../Data}")
    private String configuredDataDir;

    private final WorkbookParserService workbookParserService;
    private final FinancialAnalysisService financialAnalysisService;
    private final CompanyRepository companyRepository;
    private final ImportLogRepository importLogRepository;

    public ExcelImportService(WorkbookParserService workbookParserService,
                              FinancialAnalysisService financialAnalysisService,
                              CompanyRepository companyRepository,
                              ImportLogRepository importLogRepository) {
        this.workbookParserService = workbookParserService;
        this.financialAnalysisService = financialAnalysisService;
        this.companyRepository = companyRepository;
        this.importLogRepository = importLogRepository;
    }

    public File resolveDataDirectory() {
        // Check configured path, then relative to current dir, parent dir
        File dir = new File(configuredDataDir);
        if (dir.exists() && dir.isDirectory()) return dir;

        File alt1 = new File("Data");
        if (alt1.exists() && alt1.isDirectory()) return alt1;

        File alt2 = new File("../Data");
        if (alt2.exists() && alt2.isDirectory()) return alt2;

        return dir;
    }

    public Map<String, Object> importAllWorkbooks() {
        File dataDir = resolveDataDirectory();
        log.info("Scanning data directory for Excel workbooks: {}", dataDir.getAbsolutePath());

        Map<String, Object> summary = new HashMap<>();
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            summary.put("success", false);
            summary.put("message", "Data directory not found at " + dataDir.getAbsolutePath());
            return summary;
        }

        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".xlsx") && !name.startsWith("~$"));
        if (files == null || files.length == 0) {
            summary.put("success", false);
            summary.put("message", "No .xlsx files found in " + dataDir.getAbsolutePath());
            return summary;
        }

        log.info("Found {} Excel files to process", files.length);
        int successCount = 0;
        int failureCount = 0;
        int totalRecords = 0;

        for (File file : files) {
            try {
                WorkbookParserService.ParseResult result = workbookParserService.processWorkbook(file);
                if (result.success) {
                    successCount++;
                    totalRecords += result.recordsProcessed;

                    // Calculate metrics for company
                    if (result.companyName != null) {
                        companyRepository.findByCompanyNameIgnoreCase(result.companyName)
                                .ifPresent(financialAnalysisService::calculateAndPersistMetrics);
                    }

                    importLogRepository.save(new ImportLog(file.getName(), result.companyName, "SUCCESS",
                            result.recordsProcessed, result.recordsInserted, result.recordsUpdated, null));
                } else {
                    failureCount++;
                    log.warn("Failed to import {}: {}", file.getName(), result.errorMessage);
                    importLogRepository.save(new ImportLog(file.getName(), result.companyName, "ERROR",
                            0, 0, 0, result.errorMessage));
                }
            } catch (Exception e) {
                failureCount++;
                log.error("Exception processing workbook {}: {}", file.getName(), e.getMessage());
                importLogRepository.save(new ImportLog(file.getName(), null, "ERROR",
                        0, 0, 0, e.getMessage()));
            }
        }

        summary.put("success", true);
        summary.put("totalFiles", files.length);
        summary.put("successCount", successCount);
        summary.put("failureCount", failureCount);
        summary.put("totalRecords", totalRecords);
        summary.put("message", String.format("Processed %d files: %d succeeded, %d failed", files.length, successCount, failureCount));
        return summary;
    }
}
