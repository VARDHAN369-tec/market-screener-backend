package com.stockmarket.screener.controller;

import com.stockmarket.screener.dto.ApiResponse;
import com.stockmarket.screener.dto.WorksheetDataDTO;
import com.stockmarket.screener.service.WorksheetDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies/{id}/financial-data")
public class FinancialDataController {

    private final WorksheetDataService worksheetDataService;

    public FinancialDataController(WorksheetDataService worksheetDataService) {
        this.worksheetDataService = worksheetDataService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<WorksheetDataDTO>> getFinancialData(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Profit & Loss") String sheet) {
        WorksheetDataDTO dto = worksheetDataService.getWorksheet(id, sheet);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/profit-loss")
    public ResponseEntity<ApiResponse<WorksheetDataDTO>> getProfitLoss(@PathVariable Long id) {
        WorksheetDataDTO dto = worksheetDataService.getWorksheet(id, "Profit & Loss");
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/quarters")
    public ResponseEntity<ApiResponse<WorksheetDataDTO>> getQuarters(@PathVariable Long id) {
        WorksheetDataDTO dto = worksheetDataService.getWorksheet(id, "Quarters");
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/balance-sheet")
    public ResponseEntity<ApiResponse<WorksheetDataDTO>> getBalanceSheet(@PathVariable Long id) {
        WorksheetDataDTO dto = worksheetDataService.getWorksheet(id, "Balance Sheet");
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/cash-flow")
    public ResponseEntity<ApiResponse<WorksheetDataDTO>> getCashFlow(@PathVariable Long id) {
        WorksheetDataDTO dto = worksheetDataService.getWorksheet(id, "Cash Flow");
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/data")
    public ResponseEntity<ApiResponse<WorksheetDataDTO>> getDataSheet(@PathVariable Long id) {
        WorksheetDataDTO dto = worksheetDataService.getWorksheet(id, "Data Sheet");
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
