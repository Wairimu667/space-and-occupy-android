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
    private boolean hiddenFromPublicFeed;
    private boolean concealExactLocation;
    private int approximateRadiusMeters;
    private String landmarkName;
    private String videoUrl;
    private String transcodedImageUrl;
    private String transcodedVideoUrl;
    private boolean mediaTranscoded;
    private int feedPriority;
    private boolean freshlyCaptured;
    private boolean agentVerified;

    public Property() {
        this.additionalImageUrls = new ArrayList<>();
        this.amenities = new ArrayList<>();
        this.approximateRadiusMeters = 500;
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
        this.approximateRadiusMeters = 500;
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

    public boolean isHiddenFromPublicFeed() { return hiddenFromPublicFeed; }
    public void setHiddenFromPublicFeed(boolean hiddenFromPublicFeed) { this.hiddenFromPublicFeed = hiddenFromPublicFeed; }

    public boolean isConcealExactLocation() { return concealExactLocation; }
    public void setConcealExactLocation(boolean concealExactLocation) { this.concealExactLocation = concealExactLocation; }

    public int getApproximateRadiusMeters() { return approximateRadiusMeters; }
    public void setApproximateRadiusMeters(int approximateRadiusMeters) { this.approximateRadiusMeters = approximateRadiusMeters; }

    public String getLandmarkName() { return landmarkName; }
    public void setLandmarkName(String landmarkName) { this.landmarkName = landmarkName; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getTranscodedImageUrl() { return transcodedImageUrl; }
    public void setTranscodedImageUrl(String transcodedImageUrl) { this.transcodedImageUrl = transcodedImageUrl; }

    public String getTranscodedVideoUrl() { return transcodedVideoUrl; }
    public void setTranscodedVideoUrl(String transcodedVideoUrl) { this.transcodedVideoUrl = transcodedVideoUrl; }

    public boolean isMediaTranscoded() { return mediaTranscoded; }
    public void setMediaTranscoded(boolean mediaTranscoded) { this.mediaTranscoded = mediaTranscoded; }

    public int getFeedPriority() { return feedPriority; }
    public void setFeedPriority(int feedPriority) { this.feedPriority = feedPriority; }

    public boolean isFreshlyCaptured() { return freshlyCaptured; }
    public void setFreshlyCaptured(boolean freshlyCaptured) { this.freshlyCaptured = freshlyCaptured; }

    public boolean isAgentVerified() { return agentVerified; }
    public void setAgentVerified(boolean agentVerified) { this.agentVerified = agentVerified; }

    public String getFormattedPrice() {
        String unitStr = (priceUnit != null && !priceUnit.isEmpty() && !priceUnit.equalsIgnoreCase("total")) ? " " + priceUnit : "";
        return String.format("KSh %,.0f%s", price, unitStr);
    }

    public String getPublicLocationLabel() {
        if (concealExactLocation) {
            int radius = approximateRadiusMeters > 0 ? approximateRadiusMeters : 500;
            String landmark = (landmarkName != null && !landmarkName.trim().isEmpty())
                    ? landmarkName.trim()
                    : (location != null ? location : "this landmark");
            return "Within " + radius + "m of " + landmark;
        }
        return location;
    }

    public String getDisplayImageUrl() {
        if (transcodedImageUrl != null && !transcodedImageUrl.isEmpty()) {
            return transcodedImageUrl;
        }
        return imageUrl;
    }
}
