package com.stockmarket.screener.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quarterly_results", uniqueConstraints = {
    @UniqueConstraint(name = "uk_quarter_company_period", columnNames = {"company_id", "period_id"})
}, indexes = {
    @Index(name = "idx_quarter_company", columnList = "company_id"),
    @Index(name = "idx_quarter_period", columnList = "period_id")
})
public class QuarterlyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quarterly_id")
    private Long quarterlyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private FinancialPeriod period;

    @Column(precision = 18, scale = 2)
    private BigDecimal sales;

    @Column(precision = 18, scale = 2)
    private BigDecimal expenses;

    @Column(name = "operating_profit", precision = 18, scale = 2)
    private BigDecimal operatingProfit;

    @Column(name = "other_income", precision = 18, scale = 2)
    private BigDecimal otherIncome;

    @Column(precision = 18, scale = 2)
    private BigDecimal depreciation;

    @Column(precision = 18, scale = 2)
    private BigDecimal interest;

    @Column(name = "profit_before_tax", precision = 18, scale = 2)
    private BigDecimal profitBeforeTax;

    @Column(precision = 18, scale = 2)
    private BigDecimal tax;

    @Column(name = "net_profit", precision = 18, scale = 2)
    private BigDecimal netProfit;

    @Column(precision = 18, scale = 2)
    private BigDecimal eps;

    public QuarterlyResult() {}

    public Long getQuarterlyId() {
        return quarterlyId;
    }

    public void setQuarterlyId(Long quarterlyId) {
        this.quarterlyId = quarterlyId;
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

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getOperatingProfit() {
        return operatingProfit;
    }

    public void setOperatingProfit(BigDecimal operatingProfit) {
        this.operatingProfit = operatingProfit;
    }

    public BigDecimal getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(BigDecimal otherIncome) {
        this.otherIncome = otherIncome;
    }

    public BigDecimal getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(BigDecimal depreciation) {
        this.depreciation = depreciation;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getProfitBeforeTax() {
        return profitBeforeTax;
    }

    public void setProfitBeforeTax(BigDecimal profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getEps() {
        return eps;
    }

    public void setEps(BigDecimal eps) {
        this.eps = eps;
    }
}
