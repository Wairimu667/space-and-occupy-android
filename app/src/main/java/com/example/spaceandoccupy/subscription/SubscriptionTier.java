package com.example.spaceandoccupy.subscription;

public enum SubscriptionTier {
    FREE_TRIAL("Free Agent Trial", 0, 5, 1, 0),
    BASIC("Basic Agent", 1500, 15, 3, 1),
    PRO("Pro Agent", 2000, 40, 8, 5),
    PREMIUM("Premium Agent", 2500, 100, 20, 10);

    private final String displayName;
    private final int monthlyPriceKes;
    private final int maxListings;
    private final int maxVideos;
    private final int feedPriority;

    SubscriptionTier(String displayName, int monthlyPriceKes, int maxListings, int maxVideos, int feedPriority) {
        this.displayName = displayName;
        this.monthlyPriceKes = monthlyPriceKes;
        this.maxListings = maxListings;
        this.maxVideos = maxVideos;
        this.feedPriority = feedPriority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMonthlyPriceKes() {
        return monthlyPriceKes;
    }

    public int getMaxListings() {
        return maxListings;
    }

    public int getMaxVideos() {
        return maxVideos;
    }

    public int getFeedPriority() {
        return feedPriority;
    }

    public String getFormattedPrice() {
        if (monthlyPriceKes <= 0) {
            return "0 KES";
        }
        return String.format("%,d KES / Month", monthlyPriceKes);
    }

    public static SubscriptionTier fromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return FREE_TRIAL;
        }
        try {
            return SubscriptionTier.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return FREE_TRIAL;
        }
    }
}
