package com.stockmarket.screener.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "financial_metrics", uniqueConstraints = {
    @UniqueConstraint(name = "uk_metric_company_period", columnNames = {"company_id", "period_id"})
}, indexes = {
    @Index(name = "idx_metric_company", columnList = "company_id"),
    @Index(name = "idx_metric_period", columnList = "period_id")
})
public class FinancialMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Long metricId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private FinancialPeriod period;

    // Profitability
    @Column(name = "operating_profit_margin", precision = 12, scale = 4)
    private BigDecimal operatingProfitMargin;

    @Column(name = "net_profit_margin", precision = 12, scale = 4)
    private BigDecimal netProfitMargin;

    @Column(name = "effective_tax_rate", precision = 12, scale = 4)
    private BigDecimal effectiveTaxRate;

    // Growth
    @Column(name = "sales_growth", precision = 12, scale = 4)
    private BigDecimal salesGrowth;

    @Column(name = "operating_profit_growth", precision = 12, scale = 4)
    private BigDecimal operatingProfitGrowth;

    @Column(name = "net_profit_growth", precision = 12, scale = 4)
    private BigDecimal netProfitGrowth;

    // Liquidity
    @Column(name = "current_ratio", precision = 12, scale = 4)
    private BigDecimal currentRatio;

    @Column(name = "debtor_days", precision = 12, scale = 2)
    private BigDecimal debtorDays;

    @Column(name = "inventory_turnover", precision = 12, scale = 4)
    private BigDecimal inventoryTurnover;

    // Solvency
    @Column(name = "debt_to_equity", precision = 12, scale = 4)
    private BigDecimal debtToEquity;

    @Column(name = "interest_coverage", precision = 12, scale = 4)
    private BigDecimal interestCoverage;

    // Returns
    @Column(name = "roe", precision = 12, scale = 4)
    private BigDecimal roe;

    @Column(name = "roce", precision = 12, scale = 4)
    private BigDecimal roce;

    // Cash Flow
    @Column(name = "ocf_efficiency", precision = 12, scale = 4)
    private BigDecimal ocfEfficiency;

    @Column(name = "free_cash_flow", precision = 18, scale = 2)
    private BigDecimal freeCashFlow;

    // Valuation
    @Column(name = "pe_ratio", precision = 12, scale = 4)
    private BigDecimal peRatio;

    @Column(name = "pb_ratio", precision = 12, scale = 4)
    private BigDecimal pbRatio;

    @Column(name = "dividend_payout_ratio", precision = 12, scale = 4)
    private BigDecimal dividendPayoutRatio;

    @Column(name = "bvps", precision = 14, scale = 2)
    private BigDecimal bvps;

    // Quarterly QoQ (if quarterly period)
    @Column(name = "qoq_sales_growth", precision = 12, scale = 4)
    private BigDecimal qoqSalesGrowth;

    @Column(name = "qoq_op_growth", precision = 12, scale = 4)
    private BigDecimal qoqOperatingProfitGrowth;

    @Column(name = "qoq_net_profit_growth", precision = 12, scale = 4)
    private BigDecimal qoqNetProfitGrowth;

    public FinancialMetric() {}

    public Long getMetricId() {
        return metricId;
    }

    public void setMetricId(Long metricId) {
        this.metricId = metricId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public FinancialPeriod getPeriod() {
        return period;
    }

    public void setPeriod(FinancialPeriod period) {
        this.period = period;
    }

    public BigDecimal getOperatingProfitMargin() {
        return operatingProfitMargin;
    }

    public void setOperatingProfitMargin(BigDecimal operatingProfitMargin) {
        this.operatingProfitMargin = operatingProfitMargin;
    }

    public BigDecimal getNetProfitMargin() {
        return netProfitMargin;
    }

    public void setNetProfitMargin(BigDecimal netProfitMargin) {
        this.netProfitMargin = netProfitMargin;
    }

    public BigDecimal getEffectiveTaxRate() {
        return effectiveTaxRate;
    }

    public void setEffectiveTaxRate(BigDecimal effectiveTaxRate) {
        this.effectiveTaxRate = effectiveTaxRate;
    }

    public BigDecimal getSalesGrowth() {
        return salesGrowth;
    }

    public void setSalesGrowth(BigDecimal salesGrowth) {
        this.salesGrowth = salesGrowth;
    }

    public BigDecimal getOperatingProfitGrowth() {
        return operatingProfitGrowth;
    }

    public void setOperatingProfitGrowth(BigDecimal operatingProfitGrowth) {
        this.operatingProfitGrowth = operatingProfitGrowth;
    }

    public BigDecimal getNetProfitGrowth() {
        return netProfitGrowth;
    }

    public void setNetProfitGrowth(BigDecimal netProfitGrowth) {
        this.netProfitGrowth = netProfitGrowth;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    public BigDecimal getDebtorDays() {
        return debtorDays;
    }

    public void setDebtorDays(BigDecimal debtorDays) {
        this.debtorDays = debtorDays;
    }

    public BigDecimal getInventoryTurnover() {
        return inventoryTurnover;
    }

    public void setInventoryTurnover(BigDecimal inventoryTurnover) {
        this.inventoryTurnover = inventoryTurnover;
    }

    public BigDecimal getDebtToEquity() {
        return debtToEquity;
    }

    public void setDebtToEquity(BigDecimal debtToEquity) {
        this.debtToEquity = debtToEquity;
    }

    public BigDecimal getInterestCoverage() {
        return interestCoverage;
    }

    public void setInterestCoverage(BigDecimal interestCoverage) {
        this.interestCoverage = interestCoverage;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public BigDecimal getRoce() {
        return roce;
    }

    public void setRoce(BigDecimal roce) {
        this.roce = roce;
    }

    public BigDecimal getOcfEfficiency() {
        return ocfEfficiency;
    }

    public void setOcfEfficiency(BigDecimal ocfEfficiency) {
        this.ocfEfficiency = ocfEfficiency;
    }

    public BigDecimal getFreeCashFlow() {
        return freeCashFlow;
    }

    public void setFreeCashFlow(BigDecimal freeCashFlow) {
        this.freeCashFlow = freeCashFlow;
    }

    public BigDecimal getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(BigDecimal peRatio) {
        this.peRatio = peRatio;
    }

    public BigDecimal getPbRatio() {
        return pbRatio;
    }

    public void setPbRatio(BigDecimal pbRatio) {
        this.pbRatio = pbRatio;
    }

    public BigDecimal getDividendPayoutRatio() {
        return dividendPayoutRatio;
    }

    public void setDividendPayoutRatio(BigDecimal dividendPayoutRatio) {
        this.dividendPayoutRatio = dividendPayoutRatio;
    }

    public BigDecimal getBvps() {
        return bvps;
    }

    public void setBvps(BigDecimal bvps) {
        this.bvps = bvps;
    }

    public BigDecimal getQoqSalesGrowth() {
        return qoqSalesGrowth;
    }

    public void setQoqSalesGrowth(BigDecimal qoqSalesGrowth) {
        this.qoqSalesGrowth = qoqSalesGrowth;
    }

    public BigDecimal getQoqOperatingProfitGrowth() {
        return qoqOperatingProfitGrowth;
    }

    public void setQoqOperatingProfitGrowth(BigDecimal qoqOperatingProfitGrowth) {
        this.qoqOperatingProfitGrowth = qoqOperatingProfitGrowth;
    }

    public BigDecimal getQoqNetProfitGrowth() {
        return qoqNetProfitGrowth;
    }

    public void setQoqNetProfitGrowth(BigDecimal qoqNetProfitGrowth) {
        this.qoqNetProfitGrowth = qoqNetProfitGrowth;
    }
}
