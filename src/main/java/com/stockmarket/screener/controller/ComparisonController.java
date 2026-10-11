package com.stockmarket.screener.controller;

import com.stockmarket.screener.dto.ApiResponse;
import com.stockmarket.screener.dto.ComparisonDTO;
import com.stockmarket.screener.service.ComparisonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compare")
public class ComparisonController {

    private final ComparisonService comparisonService;

    public ComparisonController(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ComparisonDTO>> compareCompanies(
            @RequestParam(name = "companyIds") List<Long> companyIds) {
        ComparisonDTO result = comparisonService.compareCompanies(companyIds);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
