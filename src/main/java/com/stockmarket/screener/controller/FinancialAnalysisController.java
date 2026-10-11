package com.stockmarket.screener.controller;

import com.stockmarket.screener.dto.ApiResponse;
import com.stockmarket.screener.dto.FinancialAnalysisDTO;
import com.stockmarket.screener.financial.FinancialAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies/{id}")
public class FinancialAnalysisController {

    private final FinancialAnalysisService financialAnalysisService;

    public FinancialAnalysisController(FinancialAnalysisService financialAnalysisService) {
        this.financialAnalysisService = financialAnalysisService;
    }

    @GetMapping("/financial-analysis")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO>> getFinancialAnalysis(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/profitability")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.ProfitabilityDTO>> getProfitability(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto.getProfitability()));
    }

    @GetMapping("/growth")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.GrowthDTO>> getGrowth(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto.getGrowth()));
    }

    @GetMapping("/liquidity")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.LiquidityDTO>> getLiquidity(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto.getLiquidity()));
    }

    @GetMapping("/solvency")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.SolvencyDTO>> getSolvency(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto.getSolvency()));
    }

    @GetMapping("/returns")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.ReturnsDTO>> getReturns(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto.getReturns()));
    }

    @GetMapping("/cash-flow")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.CashFlowDTO>> getCashFlow(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto.getCashFlow()));
    }

    @GetMapping("/valuation")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.ValuationDTO>> getValuation(@PathVariable Long id) {
        FinancialAnalysisDTO dto = financialAnalysisService.getFinancialAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(dto.getValuation()));
    }

    @GetMapping("/quarterly")
    public ResponseEntity<ApiResponse<FinancialAnalysisDTO.QuarterlyAnalysisDTO>> getQuarterly(@PathVariable Long id) {
        FinancialAnalysisDTO.QuarterlyAnalysisDTO dto = financialAnalysisService.getQuarterly(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
