package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialMetric;
import com.stockmarket.screener.entity.FinancialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialMetricRepository extends JpaRepository<FinancialMetric, Long> {

    List<FinancialMetric> findByCompanyOrderByPeriod_PeriodDateAsc(Company company);

    List<FinancialMetric> findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(Long companyId);

    Optional<FinancialMetric> findByCompanyAndPeriod(Company company, FinancialPeriod period);

    Optional<FinancialMetric> findFirstByCompanyAndPeriod_PeriodTypeOrderByPeriod_PeriodDateDesc(Company company, FinancialPeriod.PeriodType periodType);

    Optional<FinancialMetric> findFirstByCompany_CompanyIdAndPeriod_PeriodTypeOrderByPeriod_PeriodDateDesc(Long companyId, FinancialPeriod.PeriodType periodType);

    @Query("SELECT m FROM FinancialMetric m WHERE m.period.periodType = :periodType AND m.period.periodDate = " +
           "(SELECT MAX(m2.period.periodDate) FROM FinancialMetric m2 WHERE m2.company = m.company AND m2.period.periodType = :periodType)")
    List<FinancialMetric> findAllLatestAnnualMetrics(@Param("periodType") FinancialPeriod.PeriodType periodType);
}
