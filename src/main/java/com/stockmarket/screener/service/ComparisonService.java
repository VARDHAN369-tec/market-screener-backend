package com.stockmarket.screener.service;

import com.stockmarket.screener.dto.ComparisonDTO;
import com.stockmarket.screener.entity.*;
import com.stockmarket.screener.repository.CompanyRepository;
import com.stockmarket.screener.repository.FinancialMetricRepository;
import com.stockmarket.screener.repository.MarketDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ComparisonService {

    private final CompanyRepository companyRepository;
    private final MarketDataRepository marketDataRepository;
    private final FinancialMetricRepository metricRepository;

    public ComparisonService(CompanyRepository companyRepository,
                             MarketDataRepository marketDataRepository,
                             FinancialMetricRepository metricRepository) {
        this.companyRepository = companyRepository;
        this.marketDataRepository = marketDataRepository;
        this.metricRepository = metricRepository;
    }

    @Transactional(readOnly = true)
    public ComparisonDTO compareCompanies(List<Long> companyIds) {
        if (companyIds == null || companyIds.isEmpty()) {
            return new ComparisonDTO(Collections.emptyList(), Collections.emptyList());
        }

        List<ComparisonDTO.CompanySummaryDTO> summaries = new ArrayList<>();
        Map<Long, FinancialMetric> metricMap = new HashMap<>();

        for (Long id : companyIds) {
            companyRepository.findById(id).ifPresent(c -> {
                MarketData md = marketDataRepository.findByCompany(c).orElse(null);
                summaries.add(new ComparisonDTO.CompanySummaryDTO(
                        c.getCompanyId(),
                        c.getCompanyName(),
                        c.getSector(),
                        c.getIndustry(),
                        md != null ? md.getCurrentPrice() : null,
                        md != null ? md.getMarketCapitalization() : null
                ));

                metricRepository.findFirstByCompany_CompanyIdAndPeriod_PeriodTypeOrderByPeriod_PeriodDateDesc(
                        c.getCompanyId(), FinancialPeriod.PeriodType.ANNUAL
                ).ifPresent(m -> metricMap.put(c.getCompanyId(), m));
            });
        }

        List<ComparisonDTO.ComparisonMetricRow> rows = new ArrayList<>();

        addRow(rows, "Profitability", "Operating Profit Margin", "%", metricMap, FinancialMetric::getOperatingProfitMargin);
        addRow(rows, "Profitability", "Net Profit Margin", "%", metricMap, FinancialMetric::getNetProfitMargin);
        addRow(rows, "Returns", "Return on Equity (ROE)", "%", metricMap, FinancialMetric::getRoe);
        addRow(rows, "Returns", "Return on Capital Employed (ROCE)", "%", metricMap, FinancialMetric::getRoce);
        addRow(rows, "Growth", "Sales Growth (YoY)", "%", metricMap, FinancialMetric::getSalesGrowth);
        addRow(rows, "Growth", "Operating Profit Growth (YoY)", "%", metricMap, FinancialMetric::getOperatingProfitGrowth);
        addRow(rows, "Growth", "Net Profit Growth (YoY)", "%", metricMap, FinancialMetric::getNetProfitGrowth);
        addRow(rows, "Solvency", "Debt-to-Equity", "x", metricMap, FinancialMetric::getDebtToEquity);
        addRow(rows, "Solvency", "Interest Coverage", "x", metricMap, FinancialMetric::getInterestCoverage);
        addRow(rows, "Liquidity", "Current Ratio", "x", metricMap, FinancialMetric::getCurrentRatio);
        addRow(rows, "Liquidity", "Debtor Days", "days", metricMap, FinancialMetric::getDebtorDays);
        addRow(rows, "Cash Flow", "Operating Cash Flow Efficiency", "x", metricMap, FinancialMetric::getOcfEfficiency);
        addRow(rows, "Cash Flow", "Free Cash Flow", "₹ Cr", metricMap, FinancialMetric::getFreeCashFlow);
        addRow(rows, "Valuation", "P/E Ratio", "x", metricMap, FinancialMetric::getPeRatio);
        addRow(rows, "Valuation", "P/B Ratio", "x", metricMap, FinancialMetric::getPbRatio);
        addRow(rows, "Valuation", "Dividend Payout Ratio", "%", metricMap, FinancialMetric::getDividendPayoutRatio);

        return new ComparisonDTO(summaries, rows);
    }

    private interface MetricExtractor {
        Object extract(FinancialMetric m);
    }

    private void addRow(List<ComparisonDTO.ComparisonMetricRow> rows, String category, String metricName, String unit,
                        Map<Long, FinancialMetric> map, MetricExtractor extractor) {
        Map<Long, Object> valMap = new HashMap<>();
        for (Map.Entry<Long, FinancialMetric> e : map.entrySet()) {
            valMap.put(e.getKey(), extractor.extract(e.getValue()));
        }
        rows.add(new ComparisonDTO.ComparisonMetricRow(category, metricName, unit, valMap));
    }
}
