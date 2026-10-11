package com.stockmarket.screener.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ComparisonDTO {

    private List<CompanySummaryDTO> companies;
    private List<ComparisonMetricRow> metrics;

    public ComparisonDTO() {}

    public ComparisonDTO(List<CompanySummaryDTO> companies, List<ComparisonMetricRow> metrics) {
        this.companies = companies;
        this.metrics = metrics;
    }

    public static class CompanySummaryDTO {
        private Long companyId;
        private String companyName;
        private String sector;
        private String industry;
        private BigDecimal currentPrice;
        private BigDecimal marketCap;

        public CompanySummaryDTO() {}

        public CompanySummaryDTO(Long companyId, String companyName, String sector, String industry, BigDecimal currentPrice, BigDecimal marketCap) {
            this.companyId = companyId;
            this.companyName = companyName;
            this.sector = sector;
            this.industry = industry;
            this.currentPrice = currentPrice;
            this.marketCap = marketCap;
        }

        public Long getCompanyId() { return companyId; }
        public void setCompanyId(Long companyId) { this.companyId = companyId; }
        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }
        public String getSector() { return sector; }
        public void setSector(String sector) { this.sector = sector; }
        public String getIndustry() { return industry; }
        public void setIndustry(String industry) { this.industry = industry; }
        public BigDecimal getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
        public BigDecimal getMarketCap() { return marketCap; }
        public void setMarketCap(BigDecimal marketCap) { this.marketCap = marketCap; }
    }

    public static class ComparisonMetricRow {
        private String category;
        private String metricName;
        private String unit;
        private Map<Long, Object> valuesByCompanyId;

        public ComparisonMetricRow() {}

        public ComparisonMetricRow(String category, String metricName, String unit, Map<Long, Object> valuesByCompanyId) {
            this.category = category;
            this.metricName = metricName;
            this.unit = unit;
            this.valuesByCompanyId = valuesByCompanyId;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getMetricName() { return metricName; }
        public void setMetricName(String metricName) { this.metricName = metricName; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public Map<Long, Object> getValuesByCompanyId() { return valuesByCompanyId; }
        public void setValuesByCompanyId(Map<Long, Object> valuesByCompanyId) { this.valuesByCompanyId = valuesByCompanyId; }
    }

    public List<CompanySummaryDTO> getCompanies() { return companies; }
    public void setCompanies(List<CompanySummaryDTO> companies) { this.companies = companies; }
    public List<ComparisonMetricRow> getMetrics() { return metrics; }
    public void setMetrics(List<ComparisonMetricRow> metrics) { this.metrics = metrics; }
}
