package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.CashFlow;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashFlowRepository extends JpaRepository<CashFlow, Long> {
    List<CashFlow> findByCompanyOrderByPeriod_PeriodDateAsc(Company company);
    List<CashFlow> findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(Long companyId);
    Optional<CashFlow> findByCompanyAndPeriod(Company company, FinancialPeriod period);
    Optional<CashFlow> findFirstByCompanyOrderByPeriod_PeriodDateDesc(Company company);
}
