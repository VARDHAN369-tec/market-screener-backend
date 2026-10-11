package com.stockmarket.screener.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class FinancialAnalysisDTO {

    private Long companyId;
    private String companyName;
    private String sector;
    private String industry;
    private String latestPeriod;

    private ProfitabilityDTO profitability;
    private GrowthDTO growth;
    private LiquidityDTO liquidity;
    private SolvencyDTO solvency;
    private ReturnsDTO returns;
    private CashFlowDTO cashFlow;
    private ValuationDTO valuation;
    private QuarterlyAnalysisDTO quarterly;

    private List<PeriodMetricDTO> historicalMetrics;

    public FinancialAnalysisDTO() {}

    public static class ProfitabilityDTO {
        private BigDecimal operatingProfitMargin;
        private BigDecimal netProfitMargin;
        private BigDecimal effectiveTaxRate;
        private BigDecimal roe;
        private BigDecimal roce;

        public BigDecimal getOperatingProfitMargin() { return operatingProfitMargin; }
        public void setOperatingProfitMargin(BigDecimal opm) { this.operatingProfitMargin = opm; }
        public BigDecimal getNetProfitMargin() { return netProfitMargin; }
        public void setNetProfitMargin(BigDecimal npm) { this.netProfitMargin = npm; }
        public BigDecimal getEffectiveTaxRate() { return effectiveTaxRate; }
        public void setEffectiveTaxRate(BigDecimal tax) { this.effectiveTaxRate = tax; }
        public BigDecimal getRoe() { return roe; }
        public void setRoe(BigDecimal roe) { this.roe = roe; }
        public BigDecimal getRoce() { return roce; }
        public void setRoce(BigDecimal roce) { this.roce = roce; }
    }

    public static class GrowthDTO {
        private BigDecimal salesGrowth;
        private BigDecimal operatingProfitGrowth;
        private BigDecimal netProfitGrowth;

        public BigDecimal getSalesGrowth() { return salesGrowth; }
        public void setSalesGrowth(BigDecimal sg) { this.salesGrowth = sg; }
        public BigDecimal getOperatingProfitGrowth() { return operatingProfitGrowth; }
        public void setOperatingProfitGrowth(BigDecimal opg) { this.operatingProfitGrowth = opg; }
        public BigDecimal getNetProfitGrowth() { return netProfitGrowth; }
        public void setNetProfitGrowth(BigDecimal npg) { this.netProfitGrowth = npg; }
    }

    public static class LiquidityDTO {
        private BigDecimal currentRatio;
        private BigDecimal debtorDays;
        private BigDecimal inventoryTurnover;

        public BigDecimal getCurrentRatio() { return currentRatio; }
        public void setCurrentRatio(BigDecimal cr) { this.currentRatio = cr; }
        public BigDecimal getDebtorDays() { return debtorDays; }
        public void setDebtorDays(BigDecimal dd) { this.debtorDays = dd; }
        public BigDecimal getInventoryTurnover() { return inventoryTurnover; }
        public void setInventoryTurnover(BigDecimal it) { this.inventoryTurnover = it; }
    }

    public static class SolvencyDTO {
        private BigDecimal debtToEquity;
        private BigDecimal interestCoverage;

        public BigDecimal getDebtToEquity() { return debtToEquity; }
        public void setDebtToEquity(BigDecimal de) { this.debtToEquity = de; }
        public BigDecimal getInterestCoverage() { return interestCoverage; }
        public void setInterestCoverage(BigDecimal ic) { this.interestCoverage = ic; }
    }

    public static class ReturnsDTO {
        private BigDecimal roe;
        private BigDecimal roce;

        public BigDecimal getRoe() { return roe; }
        public void setRoe(BigDecimal roe) { this.roe = roe; }
        public BigDecimal getRoce() { return roce; }
        public void setRoce(BigDecimal roce) { this.roce = roce; }
    }

    public static class CashFlowDTO {
        private BigDecimal ocfEfficiency;
        private BigDecimal freeCashFlow;

        public BigDecimal getOcfEfficiency() { return ocfEfficiency; }
        public void setOcfEfficiency(BigDecimal ocf) { this.ocfEfficiency = ocf; }
        public BigDecimal getFreeCashFlow() { return freeCashFlow; }
        public void setFreeCashFlow(BigDecimal fcf) { this.freeCashFlow = fcf; }
    }

    public static class ValuationDTO {
        private BigDecimal peRatio;
        private BigDecimal pbRatio;
        private BigDecimal dividendPayoutRatio;
        private BigDecimal bvps;

        public BigDecimal getPeRatio() { return peRatio; }
        public void setPeRatio(BigDecimal pe) { this.peRatio = pe; }
        public BigDecimal getPbRatio() { return pbRatio; }
        public void setPbRatio(BigDecimal pb) { this.pbRatio = pb; }
        public BigDecimal getDividendPayoutRatio() { return dividendPayoutRatio; }
        public void setDividendPayoutRatio(BigDecimal dpr) { this.dividendPayoutRatio = dpr; }
        public BigDecimal getBvps() { return bvps; }
        public void setBvps(BigDecimal bvps) { this.bvps = bvps; }
    }

    public static class QuarterlyAnalysisDTO {
        private String latestQuarter;
        private BigDecimal qoqSalesGrowth;
        private BigDecimal qoqOperatingProfitGrowth;
        private BigDecimal qoqNetProfitGrowth;
        private List<QuarterMetricDTO> quarters;

        public String getLatestQuarter() { return latestQuarter; }
        public void setLatestQuarter(String lq) { this.latestQuarter = lq; }
        public BigDecimal getQoqSalesGrowth() { return qoqSalesGrowth; }
        public void setQoqSalesGrowth(BigDecimal qsg) { this.qoqSalesGrowth = qsg; }
        public BigDecimal getQoqOperatingProfitGrowth() { return qoqOperatingProfitGrowth; }
        public void setQoqOperatingProfitGrowth(BigDecimal qopg) { this.qoqOperatingProfitGrowth = qopg; }
        public BigDecimal getQoqNetProfitGrowth() { return qoqNetProfitGrowth; }
        public void setQoqNetProfitGrowth(BigDecimal qnpg) { this.qoqNetProfitGrowth = qnpg; }
        public List<QuarterMetricDTO> getQuarters() { return quarters; }
        public void setQuarters(List<QuarterMetricDTO> quarters) { this.quarters = quarters; }
    }

    public static class QuarterMetricDTO {
        private String periodLabel;
        private String periodDate;
        private BigDecimal sales;
        private BigDecimal operatingProfit;
        private BigDecimal netProfit;
        private BigDecimal qoqSalesGrowth;
        private BigDecimal qoqOperatingProfitGrowth;
        private BigDecimal qoqNetProfitGrowth;

        public String getPeriodLabel() { return periodLabel; }
        public void setPeriodLabel(String l) { this.periodLabel = l; }
        public String getPeriodDate() { return periodDate; }
        public void setPeriodDate(String d) { this.periodDate = d; }
        public BigDecimal getSales() { return sales; }
        public void setSales(BigDecimal s) { this.sales = s; }
        public BigDecimal getOperatingProfit() { return operatingProfit; }
        public void setOperatingProfit(BigDecimal op) { this.operatingProfit = op; }
        public BigDecimal getNetProfit() { return netProfit; }
        public void setNetProfit(BigDecimal np) { this.netProfit = np; }
        public BigDecimal getQoqSalesGrowth() { return qoqSalesGrowth; }
        public void setQoqSalesGrowth(BigDecimal g) { this.qoqSalesGrowth = g; }
        public BigDecimal getQoqOperatingProfitGrowth() { return qoqOperatingProfitGrowth; }
        public void setQoqOperatingProfitGrowth(BigDecimal g) { this.qoqOperatingProfitGrowth = g; }
        public BigDecimal getQoqNetProfitGrowth() { return qoqNetProfitGrowth; }
        public void setQoqNetProfitGrowth(BigDecimal g) { this.qoqNetProfitGrowth = g; }
    }

    public static class PeriodMetricDTO {
        private String periodLabel;
        private String periodDate;
        private BigDecimal sales;
        private BigDecimal operatingProfit;
        private BigDecimal netProfit;
        private BigDecimal operatingProfitMargin;
        private BigDecimal netProfitMargin;
        private BigDecimal roe;
        private BigDecimal roce;
        private BigDecimal debtToEquity;
        private BigDecimal freeCashFlow;

        public String getPeriodLabel() { return periodLabel; }
        public void setPeriodLabel(String l) { this.periodLabel = l; }
        public String getPeriodDate() { return periodDate; }
        public void setPeriodDate(String d) { this.periodDate = d; }
        public BigDecimal getSales() { return sales; }
        public void setSales(BigDecimal s) { this.sales = s; }
        public BigDecimal getOperatingProfit() { return operatingProfit; }
        public void setOperatingProfit(BigDecimal op) { this.operatingProfit = op; }
        public BigDecimal getNetProfit() { return netProfit; }
        public void setNetProfit(BigDecimal np) { this.netProfit = np; }
        public BigDecimal getOperatingProfitMargin() { return operatingProfitMargin; }
        public void setOperatingProfitMargin(BigDecimal opm) { this.operatingProfitMargin = opm; }
        public BigDecimal getNetProfitMargin() { return netProfitMargin; }
        public void setNetProfitMargin(BigDecimal npm) { this.netProfitMargin = npm; }
        public BigDecimal getRoe() { return roe; }
        public void setRoe(BigDecimal roe) { this.roe = roe; }
        public BigDecimal getRoce() { return roce; }
        public void setRoce(BigDecimal roce) { this.roce = roce; }
        public BigDecimal getDebtToEquity() { return debtToEquity; }
        public void setDebtToEquity(BigDecimal de) { this.debtToEquity = de; }
        public BigDecimal getFreeCashFlow() { return freeCashFlow; }
        public void setFreeCashFlow(BigDecimal fcf) { this.freeCashFlow = fcf; }
    }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getLatestPeriod() { return latestPeriod; }
    public void setLatestPeriod(String latestPeriod) { this.latestPeriod = latestPeriod; }
    public ProfitabilityDTO getProfitability() { return profitability; }
    public void setProfitability(ProfitabilityDTO profitability) { this.profitability = profitability; }
    public GrowthDTO getGrowth() { return growth; }
    public void setGrowth(GrowthDTO growth) { this.growth = growth; }
    public LiquidityDTO getLiquidity() { return liquidity; }
    public void setLiquidity(LiquidityDTO liquidity) { this.liquidity = liquidity; }
    public SolvencyDTO getSolvency() { return solvency; }
    public void setSolvency(SolvencyDTO solvency) { this.solvency = solvency; }
    public ReturnsDTO getReturns() { return returns; }
    public void setReturns(ReturnsDTO returns) { this.returns = returns; }
    public CashFlowDTO getCashFlow() { return cashFlow; }
    public void setCashFlow(CashFlowDTO cashFlow) { this.cashFlow = cashFlow; }
    public ValuationDTO getValuation() { return valuation; }
    public void setValuation(ValuationDTO valuation) { this.valuation = valuation; }
    public QuarterlyAnalysisDTO getQuarterly() { return quarterly; }
    public void setQuarterly(QuarterlyAnalysisDTO quarterly) { this.quarterly = quarterly; }
    public List<PeriodMetricDTO> getHistoricalMetrics() { return historicalMetrics; }
    public void setHistoricalMetrics(List<PeriodMetricDTO> historicalMetrics) { this.historicalMetrics = historicalMetrics; }
}
