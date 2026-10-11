package com.stockmarket.screener.controller;

import com.stockmarket.screener.dto.ApiResponse;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.ImportLog;
import com.stockmarket.screener.excel.ExcelImportService;
import com.stockmarket.screener.financial.FinancialAnalysisService;
import com.stockmarket.screener.repository.CompanyRepository;
import com.stockmarket.screener.repository.ImportLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminImportController {

    private final ExcelImportService excelImportService;
    private final ImportLogRepository importLogRepository;
    private final CompanyRepository companyRepository;
    private final FinancialAnalysisService financialAnalysisService;

    public AdminImportController(ExcelImportService excelImportService,
                                 ImportLogRepository importLogRepository,
                                 CompanyRepository companyRepository,
                                 FinancialAnalysisService financialAnalysisService) {
        this.excelImportService = excelImportService;
        this.importLogRepository = importLogRepository;
        this.companyRepository = companyRepository;
        this.financialAnalysisService = financialAnalysisService;
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse<Map<String, Object>>> triggerBulkImport() {
        Map<String, Object> result = excelImportService.importAllWorkbooks();
        return ResponseEntity.ok(ApiResponse.success(result, "Bulk import completed"));
    }

    @GetMapping("/import-logs")
    public ResponseEntity<ApiResponse<List<ImportLog>>> getImportLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<ImportLog> logs = importLogRepository.findAllByOrderByTimestampDesc(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(logs.getContent()));
    }

    @GetMapping("/import-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getImportStatus() {
        List<ImportLog> recent = importLogRepository.findTop50ByOrderByTimestampDesc();
        long success = recent.stream().filter(l -> "SUCCESS".equalsIgnoreCase(l.getStatus())).count();
        long errors = recent.stream().filter(l -> "ERROR".equalsIgnoreCase(l.getStatus())).count();

        Map<String, Object> status = new HashMap<>();
        status.put("totalCompanies", companyRepository.count());
        status.put("recentLogsCount", recent.size());
        status.put("recentSuccessCount", success);
        status.put("recentErrorCount", errors);
        status.put("latestLogs", recent.stream().limit(10).toList());

        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @PostMapping("/recalculate-metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> recalculateAllMetrics() {
        List<Company> companies = companyRepository.findAll();
        int count = 0;
        for (Company c : companies) {
            try {
                financialAnalysisService.calculateAndPersistMetrics(c);
                count++;
            } catch (Exception ignored) {}
        }
        Map<String, Object> res = new HashMap<>();
        res.put("recalculatedCount", count);
        res.put("totalCompanies", companies.size());
        return ResponseEntity.ok(ApiResponse.success(res, "Recalculation completed"));
    }
}
