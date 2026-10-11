package com.stockmarket.screener.controller;

import com.stockmarket.screener.dto.ApiResponse;
import com.stockmarket.screener.dto.RankingDTO;
import com.stockmarket.screener.dto.ScreenerDTO;
import com.stockmarket.screener.screener.ScreenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screeners")
public class ScreenerController {

    private final ScreenerService screenerService;

    public ScreenerController(ScreenerService screenerService) {
        this.screenerService = screenerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScreenerDTO>>> getAvailableScreeners() {
        List<ScreenerDTO> screeners = screenerService.getAvailableScreeners();
        return ResponseEntity.ok(ApiResponse.success(screeners));
    }

    @GetMapping("/{screenerType}/ranking")
    public ResponseEntity<ApiResponse<List<RankingDTO>>> getRanking(
            @PathVariable String screenerType,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String search) {
        List<RankingDTO> ranking = screenerService.getRanking(screenerType, sector, industry, search);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    @GetMapping("/sector")
    public ResponseEntity<ApiResponse<List<RankingDTO>>> getSectorRanking(
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String search) {
        List<RankingDTO> ranking = screenerService.getRanking("sector", sector, null, search);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    @GetMapping("/industry")
    public ResponseEntity<ApiResponse<List<RankingDTO>>> getIndustryRanking(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String search) {
        List<RankingDTO> ranking = screenerService.getRanking("industry", null, industry, search);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }
}
