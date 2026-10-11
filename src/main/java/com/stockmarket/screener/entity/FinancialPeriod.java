package com.stockmarket.screener.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "financial_periods", uniqueConstraints = {
    @UniqueConstraint(name = "uk_company_period", columnNames = {"company_id", "period_type", "period_date"})
}, indexes = {
    @Index(name = "idx_period_company", columnList = "company_id"),
    @Index(name = "idx_period_date", columnList = "period_date")
})
public class FinancialPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "period_id")
    private Long periodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 20)
    private PeriodType periodType;

    @Column(name = "period_label", length = 50)
    private String periodLabel;

    @Column(name = "period_date", nullable = false)
    private LocalDate periodDate;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "quarter")
    private String quarter;

    public enum PeriodType {
        ANNUAL,
        QUARTERLY
    }

    public FinancialPeriod() {}

    public FinancialPeriod(Company company, PeriodType periodType, String periodLabel, LocalDate periodDate, Integer fiscalYear, String quarter) {
        this.company = company;
        this.periodType = periodType;
        this.periodLabel = periodLabel;
        this.periodDate = periodDate;
        this.fiscalYear = fiscalYear;
        this.quarter = quarter;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public LocalDate getPeriodDate() {
        return periodDate;
    }

    public void setPeriodDate(LocalDate periodDate) {
        this.periodDate = periodDate;
    }

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
