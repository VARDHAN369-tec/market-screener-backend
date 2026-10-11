package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByCompanyNameIgnoreCase(String companyName);

    @Query("SELECT c FROM Company c WHERE " +
           "(:search IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:sector IS NULL OR c.sector = :sector) AND " +
           "(:industry IS NULL OR c.industry = :industry)")
    Page<Company> searchCompanies(@Param("search") String search,
                                 @Param("sector") String sector,
                                 @Param("industry") String industry,
                                 Pageable pageable);

    @Query("SELECT DISTINCT c.sector FROM Company c WHERE c.sector IS NOT NULL AND TRIM(c.sector) <> '' ORDER BY c.sector")
    List<String> findDistinctSectors();

    @Query("SELECT DISTINCT c.industry FROM Company c WHERE c.industry IS NOT NULL AND TRIM(c.industry) <> '' ORDER BY c.industry")
    List<String> findDistinctIndustries();

    List<Company> findBySector(String sector);

    List<Company> findByIndustry(String industry);
}
