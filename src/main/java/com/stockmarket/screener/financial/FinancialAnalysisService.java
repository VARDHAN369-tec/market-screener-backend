package com.stockmarket.screener.financial;

import com.stockmarket.screener.dto.FinancialAnalysisDTO;
import com.stockmarket.screener.entity.*;
import com.stockmarket.screener.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinancialAnalysisService {

    private final CompanyRepository companyRepository;
    private final MarketDataRepository marketDataRepository;
    private final FinancialPeriodRepository periodRepository;
    private final ProfitLossRepository profitLossRepository;
    private final QuarterlyResultRepository quarterlyResultRepository;
    private final BalanceSheetRepository balanceSheetRepository;
    private final CashFlowRepository cashFlowRepository;
    private final FinancialMetricRepository financialMetricRepository;

    private final ProfitabilityService profitabilityService;
    private final GrowthService growthService;
    private final LiquidityService liquidityService;
    private final SolvencyService solvencyService;
    private final ReturnsService returnsService;
    private final CashFlowService cashFlowService;
    private final ValuationService valuationService;
    private final QuarterlyAnalysisService quarterlyAnalysisService;

    public FinancialAnalysisService(CompanyRepository companyRepository,
                                  MarketDataRepository marketDataRepository,
                                  FinancialPeriodRepository periodRepository,
                                  ProfitLossRepository profitLossRepository,
                                  QuarterlyResultRepository quarterlyResultRepository,
                                  BalanceSheetRepository balanceSheetRepository,
                                  CashFlowRepository cashFlowRepository,
                                  FinancialMetricRepository financialMetricRepository,
                                  ProfitabilityService profitabilityService,
                                  GrowthService growthService,
                                  LiquidityService liquidityService,
                                  SolvencyService solvencyService,
                                  ReturnsService returnsService,
                                  CashFlowService cashFlowService,
                                  ValuationService valuationService,
                                  QuarterlyAnalysisService quarterlyAnalysisService) {
        this.companyRepository = companyRepository;
        this.marketDataRepository = marketDataRepository;
        this.periodRepository = periodRepository;
        this.profitLossRepository = profitLossRepository;
        this.quarterlyResultRepository = quarterlyResultRepository;
        this.balanceSheetRepository = balanceSheetRepository;
        this.cashFlowRepository = cashFlowRepository;
        this.financialMetricRepository = financialMetricRepository;
        this.profitabilityService = profitabilityService;
        this.growthService = growthService;
        this.liquidityService = liquidityService;
        this.solvencyService = solvencyService;
        this.returnsService = returnsService;
        this.cashFlowService = cashFlowService;
        this.valuationService = valuationService;
        this.quarterlyAnalysisService = quarterlyAnalysisService;
    }

    @Transactional
    public void calculateAndPersistMetrics(Company company) {
        if (company == null) return;

        MarketData md = marketDataRepository.findByCompany(company).orElse(null);
        List<FinancialPeriod> annualPeriods = periodRepository.findByCompanyAndPeriodTypeOrderByPeriodDateAsc(company, FinancialPeriod.PeriodType.ANNUAL);

        List<ProfitLoss> pls = profitLossRepository.findByCompanyOrderByPeriod_PeriodDateAsc(company);
        List<BalanceSheet> bss = balanceSheetRepository.findByCompanyOrderByPeriod_PeriodDateAsc(company);
        List<CashFlow> cfs = cashFlowRepository.findByCompanyOrderByPeriod_PeriodDateAsc(company);

        Map<Long, ProfitLoss> plMap = pls.stream().collect(Collectors.toMap(p -> p.getPeriod().getPeriodId(), p -> p, (a, b) -> a));
        Map<Long, BalanceSheet> bsMap = bss.stream().collect(Collectors.toMap(b -> b.getPeriod().getPeriodId(), b -> b, (a, b) -> a));
        Map<Long, CashFlow> cfMap = cfs.stream().collect(Collectors.toMap(c -> c.getPeriod().getPeriodId(), c -> c, (a, b) -> a));

        ProfitLoss prevPl = null;
        BalanceSheet prevBs = null;

        for (FinancialPeriod period : annualPeriods) {
            ProfitLoss pl = plMap.get(period.getPeriodId());
            BalanceSheet bs = bsMap.get(period.getPeriodId());
            CashFlow cf = cfMap.get(period.getPeriodId());

            FinancialMetric metric = financialMetricRepository.findByCompanyAndPeriod(company, period)
                    .orElseGet(() -> {
                        FinancialMetric m = new FinancialMetric();
                        m.setCompany(company);
                        m.setPeriod(period);
                        return m;
                    });

            // Profitability
            if (pl != null) {
                metric.setOperatingProfitMargin(profitabilityService.calculateOperatingProfitMargin(pl));
                metric.setNetProfitMargin(profitabilityService.calculateNetProfitMargin(pl));
                metric.setEffectiveTaxRate(profitabilityService.calculateEffectiveTaxRate(pl));
            }

            // Growth
            if (pl != null && prevPl != null) {
                metric.setSalesGrowth(growthService.calculateSalesGrowth(pl, prevPl));
                metric.setOperatingProfitGrowth(growthService.calculateOperatingProfitGrowth(pl, prevPl));
                metric.setNetProfitGrowth(growthService.calculateNetProfitGrowth(pl, prevPl));
            }

            // Liquidity
            if (bs != null) {
                metric.setCurrentRatio(liquidityService.calculateCurrentRatio(bs));
                if (pl != null) {
                    metric.setDebtorDays(liquidityService.calculateDebtorDays(bs, pl));
                    metric.setInventoryTurnover(liquidityService.calculateInventoryTurnover(bs, pl));
                }
            }

            // Solvency
            if (bs != null) {
                metric.setDebtToEquity(solvencyService.calculateDebtToEquity(bs));
            }
            if (pl != null) {
                metric.setInterestCoverage(solvencyService.calculateInterestCoverage(pl));
            }

            // Returns
            if (pl != null && bs != null) {
                metric.setRoe(returnsService.calculateRoe(pl, bs));
                metric.setRoce(returnsService.calculateRoce(pl, bs));
            }

            // Cash Flow
            if (cf != null) {
                if (pl != null) {
                    metric.setOcfEfficiency(cashFlowService.calculateOcfEfficiency(cf, pl));
                }
                metric.setFreeCashFlow(cashFlowService.calculateFreeCashFlow(cf, bs, prevBs, pl));
            }

            // Valuation
            if (bs != null && md != null) {
                BigDecimal bvps = valuationService.calculateBvps(bs, md);
                metric.setBvps(bvps);
                metric.setPbRatio(valuationService.calculatePbRatio(md, bvps));
            }
            if (pl != null && md != null) {
                metric.setPeRatio(valuationService.calculatePeRatio(md, pl));
                metric.setDividendPayoutRatio(valuationService.calculateDividendPayoutRatio(pl));
            }

            financialMetricRepository.save(metric);

            prevPl = pl != null ? pl : prevPl;
            prevBs = bs != null ? bs : prevBs;
        }

        // Quarterly metrics
        List<FinancialPeriod> quarterlyPeriods = periodRepository.findByCompanyAndPeriodTypeOrderByPeriodDateAsc(company, FinancialPeriod.PeriodType.QUARTERLY);
        List<QuarterlyResult> qrs = quarterlyResultRepository.findByCompanyOrderByPeriod_PeriodDateAsc(company);
        Map<Long, QuarterlyResult> qrMap = qrs.stream().collect(Collectors.toMap(q -> q.getPeriod().getPeriodId(), q -> q, (a, b) -> a));

        QuarterlyResult prevQr = null;
        for (FinancialPeriod qPeriod : quarterlyPeriods) {
            QuarterlyResult qr = qrMap.get(qPeriod.getPeriodId());
            if (qr == null) continue;

            FinancialMetric metric = financialMetricRepository.findByCompanyAndPeriod(company, qPeriod)
                    .orElseGet(() -> {
                        FinancialMetric m = new FinancialMetric();
                        m.setCompany(company);
                        m.setPeriod(qPeriod);
                        return m;
                    });

            if (prevQr != null) {
                metric.setQoqSalesGrowth(quarterlyAnalysisService.calculateQoqSalesGrowth(qr, prevQr));
                metric.setQoqOperatingProfitGrowth(quarterlyAnalysisService.calculateQoqOperatingProfitGrowth(qr, prevQr));
                metric.setQoqNetProfitGrowth(quarterlyAnalysisService.calculateQoqNetProfitGrowth(qr, prevQr));
            }

            financialMetricRepository.save(metric);
            prevQr = qr;
        }
    }

    @Transactional(readOnly = true)
    public FinancialAnalysisDTO getFinancialAnalysis(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        FinancialAnalysisDTO dto = new FinancialAnalysisDTO();
        dto.setCompanyId(company.getCompanyId());
        dto.setCompanyName(company.getCompanyName());
        dto.setSector(company.getSector());
        dto.setIndustry(company.getIndustry());

        FinancialMetric latestMetric = financialMetricRepository
                .findFirstByCompany_CompanyIdAndPeriod_PeriodTypeOrderByPeriod_PeriodDateDesc(companyId, FinancialPeriod.PeriodType.ANNUAL)
                .orElse(null);

        if (latestMetric != null && latestMetric.getPeriod() != null) {
            dto.setLatestPeriod(latestMetric.getPeriod().getPeriodLabel());

            // Profitability
            FinancialAnalysisDTO.ProfitabilityDTO prof = new FinancialAnalysisDTO.ProfitabilityDTO();
            prof.setOperatingProfitMargin(latestMetric.getOperatingProfitMargin());
            prof.setNetProfitMargin(latestMetric.getNetProfitMargin());
            prof.setEffectiveTaxRate(latestMetric.getEffectiveTaxRate());
            prof.setRoe(latestMetric.getRoe());
            prof.setRoce(latestMetric.getRoce());
            dto.setProfitability(prof);

            // Growth
            FinancialAnalysisDTO.GrowthDTO growth = new FinancialAnalysisDTO.GrowthDTO();
            growth.setSalesGrowth(latestMetric.getSalesGrowth());
            growth.setOperatingProfitGrowth(latestMetric.getOperatingProfitGrowth());
            growth.setNetProfitGrowth(latestMetric.getNetProfitGrowth());
            dto.setGrowth(growth);

            // Liquidity
            FinancialAnalysisDTO.LiquidityDTO liq = new FinancialAnalysisDTO.LiquidityDTO();
            liq.setCurrentRatio(latestMetric.getCurrentRatio());
            liq.setDebtorDays(latestMetric.getDebtorDays());
            liq.setInventoryTurnover(latestMetric.getInventoryTurnover());
            dto.setLiquidity(liq);

            // Solvency
            FinancialAnalysisDTO.SolvencyDTO solv = new FinancialAnalysisDTO.SolvencyDTO();
            solv.setDebtToEquity(latestMetric.getDebtToEquity());
            solv.setInterestCoverage(latestMetric.getInterestCoverage());
            dto.setSolvency(solv);

            // Returns
            FinancialAnalysisDTO.ReturnsDTO ret = new FinancialAnalysisDTO.ReturnsDTO();
            ret.setRoe(latestMetric.getRoe());
            ret.setRoce(latestMetric.getRoce());
            dto.setReturns(ret);

            // Cash Flow
            FinancialAnalysisDTO.CashFlowDTO cf = new FinancialAnalysisDTO.CashFlowDTO();
            cf.setOcfEfficiency(latestMetric.getOcfEfficiency());
            cf.setFreeCashFlow(latestMetric.getFreeCashFlow());
            dto.setCashFlow(cf);

            // Valuation
            FinancialAnalysisDTO.ValuationDTO val = new FinancialAnalysisDTO.ValuationDTO();
            val.setPeRatio(latestMetric.getPeRatio());
            val.setPbRatio(latestMetric.getPbRatio());
            val.setDividendPayoutRatio(latestMetric.getDividendPayoutRatio());
            val.setBvps(latestMetric.getBvps());
            dto.setValuation(val);
        }

        // Quarterly
        FinancialAnalysisDTO.QuarterlyAnalysisDTO qDto = getQuarterly(companyId);
        dto.setQuarterly(qDto);

        // Historical annual metrics list
        List<FinancialMetric> annualMetrics = financialMetricRepository.findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(companyId)
                .stream()
                .filter(m -> m.getPeriod() != null && m.getPeriod().getPeriodType() == FinancialPeriod.PeriodType.ANNUAL)
                .toList();

        List<ProfitLoss> pls = profitLossRepository.findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(companyId);
        Map<Long, ProfitLoss> plMap = pls.stream().collect(Collectors.toMap(p -> p.getPeriod().getPeriodId(), p -> p, (a, b) -> a));

        List<FinancialAnalysisDTO.PeriodMetricDTO> histList = new ArrayList<>();
        for (FinancialMetric m : annualMetrics) {
            FinancialAnalysisDTO.PeriodMetricDTO pDto = new FinancialAnalysisDTO.PeriodMetricDTO();
            pDto.setPeriodLabel(m.getPeriod().getPeriodLabel());
            pDto.setPeriodDate(m.getPeriod().getPeriodDate().toString());
            ProfitLoss pl = plMap.get(m.getPeriod().getPeriodId());
            if (pl != null) {
                pDto.setSales(pl.getSales());
                pDto.setOperatingProfit(pl.getOperatingProfit());
                pDto.setNetProfit(pl.getNetProfit());
            }
            pDto.setOperatingProfitMargin(m.getOperatingProfitMargin());
            pDto.setNetProfitMargin(m.getNetProfitMargin());
            pDto.setRoe(m.getRoe());
            pDto.setRoce(m.getRoce());
            pDto.setDebtToEquity(m.getDebtToEquity());
            pDto.setFreeCashFlow(m.getFreeCashFlow());
            histList.add(pDto);
        }
        dto.setHistoricalMetrics(histList);

        return dto;
    }

    @Transactional(readOnly = true)
    public FinancialAnalysisDTO.QuarterlyAnalysisDTO getQuarterly(Long companyId) {
        FinancialAnalysisDTO.QuarterlyAnalysisDTO qDto = new FinancialAnalysisDTO.QuarterlyAnalysisDTO();
        List<QuarterlyResult> qrs = quarterlyResultRepository.findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(companyId);
        List<FinancialMetric> qMetrics = financialMetricRepository.findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(companyId)
                .stream()
                .filter(m -> m.getPeriod() != null && m.getPeriod().getPeriodType() == FinancialPeriod.PeriodType.QUARTERLY)
                .toList();
        Map<Long, FinancialMetric> metricMap = qMetrics.stream().collect(Collectors.toMap(m -> m.getPeriod().getPeriodId(), m -> m, (a, b) -> a));

        List<FinancialAnalysisDTO.QuarterMetricDTO> list = new ArrayList<>();
        for (QuarterlyResult qr : qrs) {
            FinancialAnalysisDTO.QuarterMetricDTO item = new FinancialAnalysisDTO.QuarterMetricDTO();
            item.setPeriodLabel(qr.getPeriod().getPeriodLabel());
            item.setPeriodDate(qr.getPeriod().getPeriodDate().toString());
            item.setSales(qr.getSales());
            item.setOperatingProfit(qr.getOperatingProfit());
            item.setNetProfit(qr.getNetProfit());

            FinancialMetric m = metricMap.get(qr.getPeriod().getPeriodId());
            if (m != null) {
                item.setQoqSalesGrowth(m.getQoqSalesGrowth());
                item.setQoqOperatingProfitGrowth(m.getQoqOperatingProfitGrowth());
                item.setQoqNetProfitGrowth(m.getQoqNetProfitGrowth());
            }
            list.add(item);
        }
        qDto.setQuarters(list);

        if (!list.isEmpty()) {
            FinancialAnalysisDTO.QuarterMetricDTO latest = list.get(list.size() - 1);
            qDto.setLatestQuarter(latest.getPeriodLabel());
            qDto.setQoqSalesGrowth(latest.getQoqSalesGrowth());
            qDto.setQoqOperatingProfitGrowth(latest.getQoqOperatingProfitGrowth());
            qDto.setQoqNetProfitGrowth(latest.getQoqNetProfitGrowth());
        }

        return qDto;
    }
}
