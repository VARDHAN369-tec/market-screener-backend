package com.stockmarket.screener.dto;

import java.math.BigDecimal;
import java.util.Map;

public class RankingDTO {

    private int rank;
    private Long companyId;
    private String companyName;
    private String sector;
    private String industry;
    private BigDecimal score;
    private Map<String, Object> keyMetrics;
    private Map<String, Object> scoreBreakdown;

    public RankingDTO() {}

    public RankingDTO(int rank, Long companyId, String companyName, String sector, String industry,
                      BigDecimal score, Map<String, Object> keyMetrics, Map<String, Object> scoreBreakdown) {
        this.rank = rank;
        this.companyId = companyId;
        this.companyName = companyName;
        this.sector = sector;
        this.industry = industry;
        this.score = score;
        this.keyMetrics = keyMetrics;
        this.scoreBreakdown = scoreBreakdown;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Map<String, Object> getKeyMetrics() {
        return keyMetrics;
    }

    public void setKeyMetrics(Map<String, Object> keyMetrics) {
        this.keyMetrics = keyMetrics;
    }

    public Map<String, Object> getScoreBreakdown() {
        return scoreBreakdown;
    }

    public void setScoreBreakdown(Map<String, Object> scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }
}
