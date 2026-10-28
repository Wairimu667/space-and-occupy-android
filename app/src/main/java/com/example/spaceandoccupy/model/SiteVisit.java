package com.example.spaceandoccupy.model;

import java.io.Serializable;

public class SiteVisit implements Serializable {
    private String id;
    private String propertyId;
    private String propertyTitle;
    private String propertyAddress;
    private String userId;
    private String userName;
    private String userPhone;
    private String preferredDate;
    private String preferredTime;
    private String agentId;
    private String status; // "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"
    private String createdDate;

    public SiteVisit() {}

    public SiteVisit(String id, String propertyId, String propertyTitle, String propertyAddress, String userId,
                     String userName, String userPhone, String preferredDate, String preferredTime,
                     String agentId, String status, String createdDate) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.propertyAddress = propertyAddress;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.agentId = agentId;
        this.status = status;
        this.createdDate = createdDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPropertyId() { return propertyId; }
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }

    public String getPropertyAddress() { return propertyAddress; }
    public void setPropertyAddress(String propertyAddress) { this.propertyAddress = propertyAddress; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getPreferredDate() { return preferredDate; }
    public void setPreferredDate(String preferredDate) { this.preferredDate = preferredDate; }

    public String getPreferredTime() { return preferredTime; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
