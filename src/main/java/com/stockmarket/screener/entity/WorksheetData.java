package com.stockmarket.screener.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "worksheet_data", indexes = {
    @Index(name = "idx_ws_company_sheet", columnList = "company_id, sheet_name")
})
public class WorksheetData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worksheet_data_id")
    private Long worksheetDataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "sheet_name", nullable = false, length = 50)
    private String sheetName;

    @Column(name = "row_order", nullable = false)
    private Integer rowOrder;

    @Column(name = "line_item", length = 150)
    private String lineItem;

    @Lob
    @Column(name = "row_data_json", columnDefinition = "TEXT")
    private String rowDataJson;

    public WorksheetData() {}

    public WorksheetData(Company company, String sheetName, Integer rowOrder, String lineItem, String rowDataJson) {
        this.company = company;
        this.sheetName = sheetName;
        this.rowOrder = rowOrder;
        this.lineItem = lineItem;
        this.rowDataJson = rowDataJson;
    }

    public Long getWorksheetDataId() {
        return worksheetDataId;
    }

    public void setWorksheetDataId(Long worksheetDataId) {
        this.worksheetDataId = worksheetDataId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getRowOrder() {
        return rowOrder;
    }

    public void setRowOrder(Integer rowOrder) {
        this.rowOrder = rowOrder;
    }

    public String getLineItem() {
        return lineItem;
    }

    public void setLineItem(String lineItem) {
        this.lineItem = lineItem;
    }

    public String getRowDataJson() {
        return rowDataJson;
    }

    public void setRowDataJson(String rowDataJson) {
        this.rowDataJson = rowDataJson;
    }
}
