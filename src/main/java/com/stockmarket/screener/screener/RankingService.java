package com.stockmarket.screener.screener;

import com.stockmarket.screener.dto.RankingDTO;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialMetric;
import com.stockmarket.screener.entity.FinancialPeriod;
import com.stockmarket.screener.repository.CompanyRepository;
import com.stockmarket.screener.repository.FinancialMetricRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private final CompanyRepository companyRepository;
    private final FinancialMetricRepository financialMetricRepository;
    private final ScoringService scoringService;

    public RankingService(CompanyRepository companyRepository,
                          FinancialMetricRepository financialMetricRepository,
                          ScoringService scoringService) {
        this.companyRepository = companyRepository;
        this.financialMetricRepository = financialMetricRepository;
        this.scoringService = scoringService;
    }

    @Transactional(readOnly = true)
    public List<RankingDTO> getRanking(String screenerType, String sector, String industry, String search) {
        String type = (screenerType != null) ? screenerType.trim().toLowerCase().replace("_", "-") : "profitability";

        List<Company> companies = companyRepository.findAll();

        if (sector != null && !sector.trim().isEmpty() && !sector.equalsIgnoreCase("all")) {
            companies = companies.stream()
                    .filter(c -> c.getSector() != null && c.getSector().trim().equalsIgnoreCase(sector.trim()))
                    .collect(Collectors.toList());
        }

        if (industry != null && !industry.trim().isEmpty() && !industry.equalsIgnoreCase("all")) {
            companies = companies.stream()
                    .filter(c -> c.getIndustry() != null && c.getIndustry().trim().equalsIgnoreCase(industry.trim()))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.trim().isEmpty()) {
            String q = search.trim().toLowerCase();
            companies = companies.stream()
                    .filter(c -> c.getCompanyName() != null && c.getCompanyName().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        FinancialPeriod.PeriodType periodType = type.equals("quarterly")
                ? FinancialPeriod.PeriodType.QUARTERLY
                : FinancialPeriod.PeriodType.ANNUAL;

        List<RankingDTO> items = new ArrayList<>();

        for (Company company : companies) {
            FinancialMetric metric = financialMetricRepository
                    .findFirstByCompany_CompanyIdAndPeriod_PeriodTypeOrderByPeriod_PeriodDateDesc(company.getCompanyId(), periodType)
                    .orElse(null);

            ScoringService.ScoreResult scoreResult;
            switch (type) {
                case "growth":
                    scoreResult = scoringService.calculateGrowthScore(metric);
                    break;
                case "financial-strength":
                case "solvency":
                    scoreResult = scoringService.calculateFinancialStrengthScore(metric);
                    break;
                case "cash-flow":
                    scoreResult = scoringService.calculateCashFlowScore(metric);
                    break;
                case "valuation":
                    scoreResult = scoringService.calculateValuationScore(metric);
                    break;
                case "quarterly":
                    scoreResult = scoringService.calculateQuarterlyScore(metric);
                    break;
                case "profitability":
                case "sector":
                case "industry":
                default:
                    scoreResult = scoringService.calculateProfitabilityScore(metric);
                    break;
            }

            RankingDTO dto = new RankingDTO(
                    0,
                    company.getCompanyId(),
                    company.getCompanyName(),
                    company.getSector(),
                    company.getIndustry(),
                    scoreResult.totalScore,
                    scoreResult.keyMetrics,
                    scoreResult.breakdown
            );
            items.add(dto);
        }

        // Deterministic sorting:
        // 1. Score DESC
        // 2. Company Name ASC (strictly deterministic tie-breaker)
        items.sort((a, b) -> {
            int scoreCmp = b.getScore().compareTo(a.getScore());
            if (scoreCmp != 0) return scoreCmp;
            return a.getCompanyName().compareToIgnoreCase(b.getCompanyName());
        });

        // Assign Rank (1, 2, 3...)
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setRank(i + 1);
        }

        return items;
    }
}
