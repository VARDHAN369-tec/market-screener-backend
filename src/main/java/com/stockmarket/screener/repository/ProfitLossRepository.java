package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.FinancialPeriod;
import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfitLossRepository extends JpaRepository<ProfitLoss, Long> {
    List<ProfitLoss> findByCompanyOrderByPeriod_PeriodDateAsc(Company company);
    List<ProfitLoss> findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(Long companyId);
    Optional<ProfitLoss> findByCompanyAndPeriod(Company company, FinancialPeriod period);
    Optional<ProfitLoss> findFirstByCompanyOrderByPeriod_PeriodDateDesc(Company company);
}
