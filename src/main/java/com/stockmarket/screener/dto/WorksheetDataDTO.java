package com.stockmarket.screener.dto;

import java.util.List;

public class WorksheetDataDTO {

    private String companyName;
    private String sheetName;
    private List<WorksheetRowDTO> rows;

    public WorksheetDataDTO() {}

    public WorksheetDataDTO(String companyName, String sheetName, List<WorksheetRowDTO> rows) {
        this.companyName = companyName;
        this.sheetName = sheetName;
        this.rows = rows;
    }

    public static class WorksheetRowDTO {
        private int rowOrder;
        private String lineItem;
        private List<String> cells;

        public WorksheetRowDTO() {}

        public WorksheetRowDTO(int rowOrder, String lineItem, List<String> cells) {
            this.rowOrder = rowOrder;
            this.lineItem = lineItem;
            this.cells = cells;
        }

        public int getRowOrder() { return rowOrder; }
        public void setRowOrder(int rowOrder) { this.rowOrder = rowOrder; }
        public String getLineItem() { return lineItem; }
        public void setLineItem(String lineItem) { this.lineItem = lineItem; }
        public List<String> getCells() { return cells; }
        public void setCells(List<String> cells) { this.cells = cells; }
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getSheetName() { return sheetName; }
    public void setSheetName(String sheetName) { this.sheetName = sheetName; }
    public List<WorksheetRowDTO> getRows() { return rows; }
    public void setRows(List<WorksheetRowDTO> rows) { this.rows = rows; }
}
