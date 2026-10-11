package com.stockmarket.screener.controller;

import com.stockmarket.screener.dto.ApiResponse;
import com.stockmarket.screener.dto.CompanyDTO;
import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.MarketData;
import com.stockmarket.screener.repository.CompanyRepository;
import com.stockmarket.screener.repository.MarketDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final MarketDataRepository marketDataRepository;

    public CompanyController(CompanyRepository companyRepository, MarketDataRepository marketDataRepository) {
        this.companyRepository = companyRepository;
        this.marketDataRepository = marketDataRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCompanies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String sectorFilter = (sector != null && !sector.trim().isEmpty() && !sector.equalsIgnoreCase("all")) ? sector.trim() : null;
        String industryFilter = (industry != null && !industry.trim().isEmpty() && !industry.equalsIgnoreCase("all")) ? industry.trim() : null;
        String searchFilter = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

        Page<Company> companyPage = companyRepository.searchCompanies(searchFilter, sectorFilter, industryFilter, pageable);

        List<CompanyDTO> dtoList = companyPage.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("companies", dtoList);
        response.put("currentPage", companyPage.getNumber());
        response.put("totalItems", companyPage.getTotalElements());
        response.put("totalPages", companyPage.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> getCompany(@PathVariable Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        return ResponseEntity.ok(ApiResponse.success(toDTO(company)));
    }

    @GetMapping("/sectors")
    public ResponseEntity<ApiResponse<List<String>>> getSectors() {
        return ResponseEntity.ok(ApiResponse.success(companyRepository.findDistinctSectors()));
    }

    @GetMapping("/industries")
    public ResponseEntity<ApiResponse<List<String>>> getIndustries() {
        return ResponseEntity.ok(ApiResponse.success(companyRepository.findDistinctIndustries()));
    }

    private CompanyDTO toDTO(Company c) {
        CompanyDTO dto = new CompanyDTO();
        dto.setCompanyId(c.getCompanyId());
        dto.setCompanyName(c.getCompanyName());
        dto.setSector(c.getSector());
        dto.setIndustry(c.getIndustry());

        MarketData md = marketDataRepository.findByCompany(c).orElse(null);
        if (md != null) {
            dto.setCurrentPrice(md.getCurrentPrice());
            dto.setMarketCapitalization(md.getMarketCapitalization());
            dto.setNumberOfShares(md.getNumberOfShares());
            dto.setFaceValue(md.getFaceValue());
        }
        return dto;
    }
}
