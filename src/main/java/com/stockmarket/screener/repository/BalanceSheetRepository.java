package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.BalanceSheet;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceSheetRepository extends JpaRepository<BalanceSheet, Long> {
    List<BalanceSheet> findByCompanyOrderByPeriod_PeriodDateAsc(Company company);
    List<BalanceSheet> findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(Long companyId);
    Optional<BalanceSheet> findByCompanyAndPeriod(Company company, FinancialPeriod period);
    Optional<BalanceSheet> findFirstByCompanyOrderByPeriod_PeriodDateDesc(Company company);
}
