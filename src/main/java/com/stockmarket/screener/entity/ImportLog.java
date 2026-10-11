package com.stockmarket.screener.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_logs")
public class ImportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_id")
    private Long importId;

    @Column(nullable = false, length = 150)
    private String filename;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(nullable = false, length = 20)
    private String status; // SUCCESS, PARTIAL, ERROR

    @Column(name = "records_processed")
    private Integer recordsProcessed = 0;

    @Column(name = "records_inserted")
    private Integer recordsInserted = 0;

    @Column(name = "records_updated")
    private Integer recordsUpdated = 0;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public ImportLog() {}

    public ImportLog(String filename, String companyName, String status, Integer recordsProcessed, Integer recordsInserted, Integer recordsUpdated, String errorMessage) {
        this.filename = filename;
        this.companyName = companyName;
        this.status = status;
        this.recordsProcessed = recordsProcessed;
        this.recordsInserted = recordsInserted;
        this.recordsUpdated = recordsUpdated;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
    }

    public Long getImportId() {
        return importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public Integer getRecordsInserted() {
        return recordsInserted;
    }

    public void setRecordsInserted(Integer recordsInserted) {
        this.recordsInserted = recordsInserted;
    }

    public Integer getRecordsUpdated() {
        return recordsUpdated;
    }

    public void setRecordsUpdated(Integer recordsUpdated) {
        this.recordsUpdated = recordsUpdated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
