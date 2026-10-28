package com.example.spaceandoccupy.subscription;

import com.example.spaceandoccupy.model.User;

public final class SubscriptionPolicy {
    public static final long TRIAL_DURATION_MS = 3L * 24 * 60 * 60 * 1000;
    public static final long PAID_DURATION_MS = 30L * 24 * 60 * 60 * 1000;

    private SubscriptionPolicy() {}

    public static boolean isAgent(User user) {
        return user != null && "AGENT".equalsIgnoreCase(user.getRole());
    }

    public static SubscriptionTier currentTier(User user) {
        if (user == null || user.getSubscriptionTier() == null) {
            return SubscriptionTier.FREE_TRIAL;
        }
        return SubscriptionTier.fromName(user.getSubscriptionTier());
    }

    public static boolean isSubscriptionActive(User user, long nowMillis) {
        if (!isAgent(user)) {
            return true;
        }
        if (user.isPaymentDelinquent()) {
            return false;
        }
        SubscriptionTier tier = currentTier(user);
        if (tier == SubscriptionTier.FREE_TRIAL) {
            if (user.getRegisteredAtMillis() <= 0) {
                return false;
            }
            return nowMillis <= user.getRegisteredAtMillis() + TRIAL_DURATION_MS;
        }
        return user.getSubscriptionExpiresAtMillis() > nowMillis;
    }

    public static long remainingMillis(User user, long nowMillis) {
        if (!isAgent(user)) {
            return 0;
        }
        SubscriptionTier tier = currentTier(user);
        long expiresAt;
        if (tier == SubscriptionTier.FREE_TRIAL) {
            long start = user.getRegisteredAtMillis() > 0 ? user.getRegisteredAtMillis() : nowMillis;
            expiresAt = start + TRIAL_DURATION_MS;
        } else {
            expiresAt = user.getSubscriptionExpiresAtMillis();
        }
        return Math.max(0, expiresAt - nowMillis);
    }

    public static void startFreeTrial(User user, long nowMillis) {
        if (user == null) {
            return;
        }
        user.setSubscriptionTier(SubscriptionTier.FREE_TRIAL.name());
        user.setRegisteredAtMillis(nowMillis);
        user.setSubscriptionExpiresAtMillis(nowMillis + TRIAL_DURATION_MS);
        user.setPaymentDelinquent(false);
    }

    public static void activatePaidPlan(User user, SubscriptionTier tier, long nowMillis) {
        if (user == null || tier == null || tier == SubscriptionTier.FREE_TRIAL) {
            return;
        }
        user.setSubscriptionTier(tier.name());
        user.setPaymentDelinquent(false);
        user.setSubscriptionExpiresAtMillis(nowMillis + PAID_DURATION_MS);
        if (user.getRegisteredAtMillis() <= 0) {
            user.setRegisteredAtMillis(nowMillis);
        }
    }

    public static String statusLabel(User user, long nowMillis) {
        if (!isAgent(user)) {
            return "";
        }
        if (!isSubscriptionActive(user, nowMillis)) {
            return "Listings hidden until a paid plan is activated";
        }
        long remaining = remainingMillis(user, nowMillis);
        long days = Math.max(1, remaining / (24L * 60 * 60 * 1000));
        SubscriptionTier tier = currentTier(user);
        if (tier == SubscriptionTier.FREE_TRIAL) {
            return "Trial active • " + days + " day" + (days == 1 ? "" : "s") + " remaining";
        }
        return tier.getDisplayName() + " • " + days + " day" + (days == 1 ? "" : "s") + " remaining";
    }
}
