package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialPeriodRepository extends JpaRepository<FinancialPeriod, Long> {

    List<FinancialPeriod> findByCompanyOrderByPeriodDateAsc(Company company);

    List<FinancialPeriod> findByCompanyAndPeriodTypeOrderByPeriodDateAsc(Company company, FinancialPeriod.PeriodType periodType);

    List<FinancialPeriod> findByCompany_CompanyIdAndPeriodTypeOrderByPeriodDateAsc(Long companyId, FinancialPeriod.PeriodType periodType);

    Optional<FinancialPeriod> findByCompanyAndPeriodTypeAndPeriodDate(Company company, FinancialPeriod.PeriodType periodType, LocalDate periodDate);

    Optional<FinancialPeriod> findFirstByCompanyAndPeriodTypeOrderByPeriodDateDesc(Company company, FinancialPeriod.PeriodType periodType);
}
