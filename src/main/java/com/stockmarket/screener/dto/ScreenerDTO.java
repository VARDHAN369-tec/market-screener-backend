package com.stockmarket.screener.dto;

import java.util.List;

public class ScreenerDTO {

    private String type;
    private String name;
    private String description;
    private List<String> metrics;

    public ScreenerDTO() {}

    public ScreenerDTO(String type, String name, String description, List<String> metrics) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.metrics = metrics;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }
}
