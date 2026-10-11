package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import com.stockmarket.screener.entity.QuarterlyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuarterlyResultRepository extends JpaRepository<QuarterlyResult, Long> {
    List<QuarterlyResult> findByCompanyOrderByPeriod_PeriodDateAsc(Company company);
    List<QuarterlyResult> findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(Long companyId);
    Optional<QuarterlyResult> findByCompanyAndPeriod(Company company, FinancialPeriod period);
    Optional<QuarterlyResult> findFirstByCompanyOrderByPeriod_PeriodDateDesc(Company company);
}
