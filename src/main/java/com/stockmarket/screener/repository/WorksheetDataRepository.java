package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.WorksheetData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorksheetDataRepository extends JpaRepository<WorksheetData, Long> {
    List<WorksheetData> findByCompanyAndSheetNameOrderByRowOrderAsc(Company company, String sheetName);
    List<WorksheetData> findByCompany_CompanyIdAndSheetNameOrderByRowOrderAsc(Long companyId, String sheetName);
    void deleteByCompany(Company company);
}
