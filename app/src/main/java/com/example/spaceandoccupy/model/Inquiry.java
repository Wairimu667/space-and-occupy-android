package com.example.spaceandoccupy.model;

import java.io.Serializable;

public class Inquiry implements Serializable {
    private String id;
    private String propertyId;
    private String propertyTitle;
    private String userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String message;
    private String agentId;
    private String status; // "PENDING", "REPLIED", "CLOSED"
    private String createdDate;

    public Inquiry() {}

    public Inquiry(String id, String propertyId, String propertyTitle, String userId, String userName,
                   String userEmail, String userPhone, String message, String agentId, String status, String createdDate) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.message = message;
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

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
