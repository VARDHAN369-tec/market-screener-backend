package com.stockmarket.screener.screener;

import com.stockmarket.screener.dto.RankingDTO;
import com.stockmarket.screener.dto.ScreenerDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ScreenerService {

    private final RankingService rankingService;

    public ScreenerService(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    public List<ScreenerDTO> getAvailableScreeners() {
        List<ScreenerDTO> list = new ArrayList<>();

        list.add(new ScreenerDTO(
                "profitability",
                "Profitability Screener",
                "Analyze company earning power and operational profitability using ROE, ROCE, and profit margins.",
                Arrays.asList("ROE", "ROCE", "Operating Profit Margin", "Net Profit Margin", "Effective Tax Rate")
        ));

        list.add(new ScreenerDTO(
                "growth",
                "Growth Screener",
                "Evaluate historical revenue and earnings expansion trajectory across annual financial statements.",
                Arrays.asList("Sales Growth", "Operating Profit Growth", "Net Profit Growth")
        ));

        list.add(new ScreenerDTO(
                "financial-strength",
                "Financial Strength Screener",
                "Assess balance sheet solvency, leverage exposure, and short-term liquidity headroom.",
                Arrays.asList("Debt-to-Equity", "Interest Coverage", "Current Ratio")
        ));

        list.add(new ScreenerDTO(
                "cash-flow",
                "Cash Flow Screener",
                "Measure real cash generation quality, operational cash efficiency, and free cash flow generation.",
                Arrays.asList("Operating Cash Flow Efficiency", "Free Cash Flow")
        ));

        list.add(new ScreenerDTO(
                "valuation",
                "Valuation Screener",
                "Screen companies by market multiples and valuation attractiveness relative to fundamentals.",
                Arrays.asList("P/E Ratio", "P/B Ratio", "Dividend Payout Ratio")
        ));

        list.add(new ScreenerDTO(
                "quarterly",
                "Quarterly Performance Screener",
                "Track near-term momentum and quarter-over-quarter financial performance trajectory.",
                Arrays.asList("QoQ Sales Growth", "QoQ Operating Profit Growth", "QoQ Net Profit Growth")
        ));

        list.add(new ScreenerDTO(
                "sector",
                "Sector Screener",
                "Compare and rank companies operating within the same economic and industry sector.",
                Arrays.asList("Sector Filtering", "Profitability Score", "Composite Metrics")
        ));

        list.add(new ScreenerDTO(
                "industry",
                "Industry Screener",
                "Evaluate peer companies categorized by specialized industry classifications.",
                Arrays.asList("Industry Filtering", "Profitability Score", "Key Fundamentals")
        ));

        return list;
    }

    public List<RankingDTO> getRanking(String screenerType, String sector, String industry, String search) {
        return rankingService.getRanking(screenerType, sector, industry, search);
    }
}
