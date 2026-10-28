package com.example.spaceandoccupy.model;

import java.io.Serializable;

public class ListingReport implements Serializable {
    private String id;
    private String propertyId;
    private String propertyTitle;
    private String reporterId;
    private String reporterName;
    private String reason;
    private String details;
    private String status; // OPEN, REVIEWED
    private String createdDate;

    public ListingReport() {}

    public ListingReport(String id, String propertyId, String propertyTitle, String reporterId,
                         String reporterName, String reason, String details, String status, String createdDate) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.reason = reason;
        this.details = details;
        this.status = status;
        this.createdDate = createdDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPropertyId() { return propertyId; }
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }

    public String getReporterId() { return reporterId; }
    public void setReporterId(String reporterId) { this.reporterId = reporterId; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
