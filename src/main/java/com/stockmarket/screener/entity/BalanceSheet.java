package com.stockmarket.screener.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "balance_sheet", uniqueConstraints = {
    @UniqueConstraint(name = "uk_bs_company_period", columnNames = {"company_id", "period_id"})
}, indexes = {
    @Index(name = "idx_bs_company", columnList = "company_id"),
    @Index(name = "idx_bs_period", columnList = "period_id")
})
public class BalanceSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_sheet_id")
    private Long balanceSheetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private FinancialPeriod period;

    @Column(name = "equity_share_capital", precision = 18, scale = 2)
    private BigDecimal equityShareCapital;

    @Column(precision = 18, scale = 2)
    private BigDecimal reserves;

    @Column(precision = 18, scale = 2)
    private BigDecimal borrowings;

    @Column(name = "other_liabilities", precision = 18, scale = 2)
    private BigDecimal otherLiabilities;

    @Column(name = "total_liabilities", precision = 18, scale = 2)
    private BigDecimal totalLiabilities;

    @Column(name = "net_block", precision = 18, scale = 2)
    private BigDecimal netBlock;

    @Column(name = "capital_work_in_progress", precision = 18, scale = 2)
    private BigDecimal capitalWorkInProgress;

    @Column(precision = 18, scale = 2)
    private BigDecimal investments;

    @Column(name = "other_assets", precision = 18, scale = 2)
    private BigDecimal otherAssets;

    @Column(name = "total_assets", precision = 18, scale = 2)
    private BigDecimal totalAssets;

    @Column(name = "working_capital", precision = 18, scale = 2)
    private BigDecimal workingCapital;

    @Column(precision = 18, scale = 2)
    private BigDecimal debtors;

    @Column(precision = 18, scale = 2)
    private BigDecimal inventory;

    public BalanceSheet() {}

    public Long getBalanceSheetId() {
        return balanceSheetId;
    }

    public void setBalanceSheetId(Long balanceSheetId) {
        this.balanceSheetId = balanceSheetId;
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

    public BigDecimal getEquityShareCapital() {
        return equityShareCapital;
    }

    public void setEquityShareCapital(BigDecimal equityShareCapital) {
        this.equityShareCapital = equityShareCapital;
    }

    public BigDecimal getReserves() {
        return reserves;
    }

    public void setReserves(BigDecimal reserves) {
        this.reserves = reserves;
    }

    public BigDecimal getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(BigDecimal borrowings) {
        this.borrowings = borrowings;
    }

    public BigDecimal getOtherLiabilities() {
        return otherLiabilities;
    }

    public void setOtherLiabilities(BigDecimal otherLiabilities) {
        this.otherLiabilities = otherLiabilities;
    }

    public BigDecimal getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(BigDecimal totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public BigDecimal getNetBlock() {
        return netBlock;
    }

    public void setNetBlock(BigDecimal netBlock) {
        this.netBlock = netBlock;
    }

    public BigDecimal getCapitalWorkInProgress() {
        return capitalWorkInProgress;
    }

    public void setCapitalWorkInProgress(BigDecimal capitalWorkInProgress) {
        this.capitalWorkInProgress = capitalWorkInProgress;
    }

    public BigDecimal getInvestments() {
        return investments;
    }

    public void setInvestments(BigDecimal investments) {
        this.investments = investments;
    }

    public BigDecimal getOtherAssets() {
        return otherAssets;
    }

    public void setOtherAssets(BigDecimal otherAssets) {
        this.otherAssets = otherAssets;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getWorkingCapital() {
        return workingCapital;
    }

    public void setWorkingCapital(BigDecimal workingCapital) {
        this.workingCapital = workingCapital;
    }

    public BigDecimal getDebtors() {
        return debtors;
    }

    public void setDebtors(BigDecimal debtors) {
        this.debtors = debtors;
    }

    public BigDecimal getInventory() {
        return inventory;
    }

    public void setInventory(BigDecimal inventory) {
        this.inventory = inventory;
    }
}
