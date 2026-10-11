package com.stockmarket.screener.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "profit_loss", uniqueConstraints = {
    @UniqueConstraint(name = "uk_pl_company_period", columnNames = {"company_id", "period_id"})
}, indexes = {
    @Index(name = "idx_pl_company", columnList = "company_id"),
    @Index(name = "idx_pl_period", columnList = "period_id")
})
public class ProfitLoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profit_loss_id")
    private Long profitLossId;

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

    @Column(name = "price_to_earning", precision = 18, scale = 2)
    private BigDecimal priceToEarning;

    @Column(precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "dividend_amount", precision = 18, scale = 2)
    private BigDecimal dividendAmount;

    public ProfitLoss() {}

    public Long getProfitLossId() {
        return profitLossId;
    }

    public void setProfitLossId(Long profitLossId) {
        this.profitLossId = profitLossId;
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

    public BigDecimal getPriceToEarning() {
        return priceToEarning;
    }

    public void setPriceToEarning(BigDecimal priceToEarning) {
        this.priceToEarning = priceToEarning;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(BigDecimal dividendAmount) {
        this.dividendAmount = dividendAmount;
    }
}
