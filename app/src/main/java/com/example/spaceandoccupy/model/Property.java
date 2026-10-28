package com.example.spaceandoccupy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Property implements Serializable {
    private String id;
    private String title;
    private String description;
    private String propertyType; // "House", "Apartment", "Commercial", "Land"
    private String listingType;  // "For Sale", "For Rent", "Lease"
    private double price;
    private String priceUnit;    // e.g. "/ month", "total", "/ sq ft"
    private String location;     // e.g. "Westlands, Nairobi"
    private String address;
    private int bedrooms;
    private int bathrooms;
    private double areaSqFt;
    private String status;       // "AVAILABLE", "SOLD", "RENTED", "LEASED", "PENDING_APPROVAL"
    private boolean featured;
    private String imageUrl;
    private List<String> additionalImageUrls;
    private List<String> amenities;
    private String agentId;
    private String agentName;
    private String agentPhone;
    private String agentEmail;
    private String createdDate;
    private boolean isFavorite;

    public Property() {
        this.additionalImageUrls = new ArrayList<>();
        this.amenities = new ArrayList<>();
    }

    public Property(String id, String title, String description, String propertyType, String listingType,
                    double price, String priceUnit, String location, String address, int bedrooms,
                    int bathrooms, double areaSqFt, String status, boolean featured, String imageUrl,
                    List<String> amenities, String agentId, String agentName, String agentPhone,
                    String agentEmail, String createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.propertyType = propertyType;
        this.listingType = listingType;
        this.price = price;
        this.priceUnit = priceUnit;
        this.location = location;
        this.address = address;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.areaSqFt = areaSqFt;
        this.status = status;
        this.featured = featured;
        this.imageUrl = imageUrl;
        this.additionalImageUrls = new ArrayList<>();
        this.amenities = amenities != null ? amenities : new ArrayList<>();
        this.agentId = agentId;
        this.agentName = agentName;
        this.agentPhone = agentPhone;
        this.agentEmail = agentEmail;
        this.createdDate = createdDate;
        this.isFavorite = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public String getListingType() { return listingType; }
    public void setListingType(String listingType) { this.listingType = listingType; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPriceUnit() { return priceUnit; }
    public void setPriceUnit(String priceUnit) { this.priceUnit = priceUnit; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public int getBathrooms() { return bathrooms; }
    public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }

    public double getAreaSqFt() { return areaSqFt; }
    public void setAreaSqFt(double areaSqFt) { this.areaSqFt = areaSqFt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getAdditionalImageUrls() { return additionalImageUrls; }
    public void setAdditionalImageUrls(List<String> additionalImageUrls) { this.additionalImageUrls = additionalImageUrls; }

    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getAgentPhone() { return agentPhone; }
    public void setAgentPhone(String agentPhone) { this.agentPhone = agentPhone; }

    public String getAgentEmail() { return agentEmail; }
    public void setAgentEmail(String agentEmail) { this.agentEmail = agentEmail; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getFormattedPrice() {
        String unitStr = (priceUnit != null && !priceUnit.isEmpty() && !priceUnit.equalsIgnoreCase("total")) ? " " + priceUnit : "";
        return String.format("KSh %,.0f%s", price, unitStr);
    }
}
