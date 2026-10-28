package com.example.spaceandoccupy.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role; // "CLIENT", "AGENT", "ADMIN"
    private boolean verified; // true for approved agents/admins, false for pending agent verification
    private String phone;
    private String avatarUrl;
    private String agencyName;
    private String licenseNumber;
    private String subscriptionTier; // FREE_TRIAL, BASIC, PRO, PREMIUM
    private long registeredAtMillis;
    private long subscriptionExpiresAtMillis;
    private boolean paymentDelinquent;

    public User() {}

    public User(String id, String name, String email, String password, String role, boolean verified, String phone, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.verified = verified;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getAgencyName() { return agencyName; }
    public void setAgencyName(String agencyName) { this.agencyName = agencyName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getSubscriptionTier() { return subscriptionTier; }
    public void setSubscriptionTier(String subscriptionTier) { this.subscriptionTier = subscriptionTier; }

    public long getRegisteredAtMillis() { return registeredAtMillis; }
    public void setRegisteredAtMillis(long registeredAtMillis) { this.registeredAtMillis = registeredAtMillis; }

    public long getSubscriptionExpiresAtMillis() { return subscriptionExpiresAtMillis; }
    public void setSubscriptionExpiresAtMillis(long subscriptionExpiresAtMillis) { this.subscriptionExpiresAtMillis = subscriptionExpiresAtMillis; }

    public boolean isPaymentDelinquent() { return paymentDelinquent; }
    public void setPaymentDelinquent(boolean paymentDelinquent) { this.paymentDelinquent = paymentDelinquent; }
}
