package com.stockmarket.screener.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cash_flow", uniqueConstraints = {
    @UniqueConstraint(name = "uk_cf_company_period", columnNames = {"company_id", "period_id"})
}, indexes = {
    @Index(name = "idx_cf_company", columnList = "company_id"),
    @Index(name = "idx_cf_period", columnList = "period_id")
})
public class CashFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cash_flow_id")
    private Long cashFlowId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private FinancialPeriod period;

    @Column(name = "cash_operating_activity", precision = 18, scale = 2)
    private BigDecimal cashFromOperatingActivity;

    @Column(name = "cash_investing_activity", precision = 18, scale = 2)
    private BigDecimal cashFromInvestingActivity;

    @Column(name = "cash_financing_activity", precision = 18, scale = 2)
    private BigDecimal cashFromFinancingActivity;

    @Column(name = "net_cash_flow", precision = 18, scale = 2)
    private BigDecimal netCashFlow;

    public CashFlow() {}

    public Long getCashFlowId() {
        return cashFlowId;
    }

    public void setCashFlowId(Long cashFlowId) {
        this.cashFlowId = cashFlowId;
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

    public BigDecimal getCashFromOperatingActivity() {
        return cashFromOperatingActivity;
    }

    public void setCashFromOperatingActivity(BigDecimal cashFromOperatingActivity) {
        this.cashFromOperatingActivity = cashFromOperatingActivity;
    }

    public BigDecimal getCashFromInvestingActivity() {
        return cashFromInvestingActivity;
    }

    public void setCashFromInvestingActivity(BigDecimal cashFromInvestingActivity) {
        this.cashFromInvestingActivity = cashFromInvestingActivity;
    }

    public BigDecimal getCashFromFinancingActivity() {
        return cashFromFinancingActivity;
    }

    public void setCashFromFinancingActivity(BigDecimal cashFromFinancingActivity) {
        this.cashFromFinancingActivity = cashFromFinancingActivity;
    }

    public BigDecimal getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(BigDecimal netCashFlow) {
        this.netCashFlow = netCashFlow;
    }
}
