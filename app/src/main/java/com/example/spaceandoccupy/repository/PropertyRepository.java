package com.example.spaceandoccupy.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.spaceandoccupy.media.MediaTranscoder;
import com.example.spaceandoccupy.model.DashboardStats;
import com.example.spaceandoccupy.model.Inquiry;
import com.example.spaceandoccupy.model.ListingReport;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.SiteVisit;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.network.ApiClient;
import com.example.spaceandoccupy.network.ApiService;
import com.example.spaceandoccupy.subscription.SubscriptionPolicy;
import com.example.spaceandoccupy.subscription.SubscriptionTier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class PropertyRepository {
    private static PropertyRepository instance;
    private final ApiService apiService;
    private final SessionManager sessionManager;
    private final ExecutorService executor;
    private final Handler mainHandler;

    // In-memory mock storage
    private final List<Property> mockProperties = new ArrayList<>();
    private final List<User> mockUsers = new ArrayList<>();
    private final List<Inquiry> mockInquiries = new ArrayList<>();
    private final List<SiteVisit> mockSiteVisits = new ArrayList<>();
    private final List<ListingReport> mockReports = new ArrayList<>();
    private int totalAdImpressions = 1420;
    private int totalAppDownloads = 3850;

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    private PropertyRepository(Context context) {
        this.apiService = ApiClient.getApiService();
        this.sessionManager = new SessionManager(context);
        this.executor = Executors.newFixedThreadPool(4);
        this.mainHandler = new Handler(Looper.getMainLooper());

        initMockData();
    }

    public static synchronized PropertyRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PropertyRepository(context);
        }
        return instance;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private void initMockData() {
        // Users
        User clientUser = new User("usr_client_1", "Sarah Johnson", "client@spaceandoccupy.com", "password123", "CLIENT", true, "+254 787 900284", "https://images.unsplash.com/photo-1494790108377-be9c29b29330");
        long now = System.currentTimeMillis();
        User agentUser = new User("usr_agent_1", "Space & Occupy Agent", "agent@spaceandoccupy.com", "password123", "AGENT", true, "+254787900284", "https://images.unsplash.com/photo-1560250097-0b93528c311a");
        agentUser.setAgencyName("Space & Occupy Investments");
        agentUser.setLicenseNumber("SO-884920");
        SubscriptionPolicy.activatePaidPlan(agentUser, SubscriptionTier.PREMIUM, now);

        User pendingAgent = new User("usr_agent_2", "Elena Rostova", "elena@spaceandoccupy.com", "password123", "AGENT", false, "+254 787 900284", "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2");
        pendingAgent.setAgencyName("Space & Occupy Partner");
        pendingAgent.setLicenseNumber("SO-910234");
        SubscriptionPolicy.startFreeTrial(pendingAgent, now);

        User delinquentAgent = new User("usr_agent_3", "James Otieno", "james@spaceandoccupy.com", "password123", "AGENT", true, "+254 700 111222", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e");
        delinquentAgent.setAgencyName("Nairobi Listings Co.");
        delinquentAgent.setLicenseNumber("SO-441120");
        SubscriptionPolicy.startFreeTrial(delinquentAgent, now - (5L * 24 * 60 * 60 * 1000));

        User adminUser = new User("usr_admin_1", "Administrator", "admin@spaceandoccupy.com", "admin123", "ADMIN", true, "+254 787 900284", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e");

        mockUsers.add(clientUser);
        mockUsers.add(agentUser);
        mockUsers.add(pendingAgent);
        mockUsers.add(delinquentAgent);
        mockUsers.add(adminUser);

        // Real Space & Occupy Website Properties
        mockProperties.add(new Property(
                "prop_1",
                "Maragua Hotel Commercial Property & 1½ Acres Land",
                "1½ Acres prime commercial land with hotel development in Maragua, Murang'a County. Ideal for hospitality, resort, or commercial project with clear road access and utilities ready.",
                "Commercial",
                "For Sale",
                35000000.0,
                "total",
                "Maragua, Murang'a County, Kenya",
                "Maragua Town, Murang'a Highway",
                0,
                0,
                65340.0,
                "AVAILABLE",
                true,
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=1200",
                Arrays.asList("1.5 Acre Land", "Hotel Structure", "Title Deed Ready", "Highway Access", "Utilities Connected"),
                agentUser.getId(),
                agentUser.getName(),
                agentUser.getPhone(),
                agentUser.getEmail(),
                "2025-02-01"
        ));
        mockProperties.get(mockProperties.size() - 1).setFeedPriority(SubscriptionTier.PREMIUM.getFeedPriority());
        mockProperties.get(mockProperties.size() - 1).setFeatured(true);

        mockProperties.add(new Property(
                "prop_2",
                "Karen Architectural Luxury Villa",
                "Architect-designed contemporary 5-bedroom mansion secluded in prime Karen residential estate. Includes gourmet chef's kitchen, floor heating, landscaped private garden, outdoor pool, and automated 3-car garage.",
                "House",
                "For Sale",
                85000000.0,
                "total",
                "Karen, Nairobi, Kenya",
                "Dagoretti Road, Karen",
                5,
                6,
                5200.0,
                "AVAILABLE",
                true,
                "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=1200",
                Arrays.asList("Private Garden", "Swimming Pool", "Gourmet Kitchen", "Gated Security", "Solar Water"),
                agentUser.getId(),
                agentUser.getName(),
                agentUser.getPhone(),
                agentUser.getEmail(),
                "2025-02-02"
        ));
        Property karenVilla = mockProperties.get(mockProperties.size() - 1);
        karenVilla.setConcealExactLocation(true);
        karenVilla.setLandmarkName("Karen Shopping Centre");
        karenVilla.setApproximateRadiusMeters(500);
        karenVilla.setFeedPriority(SubscriptionTier.PREMIUM.getFeedPriority());
        karenVilla.setFeatured(true);

        mockProperties.add(new Property(
                "prop_3",
                "Westlands Prime Executive Office Suite",
                "Grade-A commercial office space located in the vibrant Westlands financial district. Fully partitioned with executive boardrooms, open workstation zones, high-speed fiber internet, and dedicated underground parking spots.",
                "Commercial",
                "Lease",
                120000.0,
                "/ month",
                "Westlands, Nairobi, Kenya",
                "Waiyaki Way, 8th Floor",
                0,
                3,
                2800.0,
                "AVAILABLE",
                false,
                "https://images.unsplash.com/photo-1497366216548-37526070297c?w=1200",
                Arrays.asList("Executive Boardroom", "High-Speed Fiber", "Underground Parking", "Central HVAC", "Card Access"),
                agentUser.getId(),
                agentUser.getName(),
                agentUser.getPhone(),
                agentUser.getEmail(),
                "2025-02-03"
        ));

        mockProperties.add(new Property(
                "prop_4",
                "Kilimani Modern Residency & Pool",
                "Luxury 3-bedroom apartment with floor-to-ceiling windows, open plan modern kitchen, balcony overlooking Nairobi skyline, swimming pool, fully equipped gym, and backup generator.",
                "Apartment",
                "For Rent",
                95000.0,
                "/ month",
                "Kilimani, Nairobi, Kenya",
                "Argwings Kodhek Road",
                3,
                3,
                1850.0,
                "AVAILABLE",
                false,
                "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=1200",
                Arrays.asList("Swimming Pool", "Equipped Gym", "Skyline Balcony", "Borehole Water", "24/7 Security"),
                agentUser.getId(),
                agentUser.getName(),
                agentUser.getPhone(),
                agentUser.getEmail(),
                "2025-02-04"
        ));

        mockProperties.add(new Property(
                "prop_5",
                "Kitengela Serviced 1-Acre Plot",
                "Fully serviced prime 1-acre land parcel in rapidly developing residential zone. Ready clean title deed, clear road access, electricity and water mains already connected on site.",
                "Land",
                "For Sale",
                6500000.0,
                "total",
                "Kitengela, Kajiado County, Kenya",
                "Kitengela-Namanga Highway",
                0,
                0,
                43560.0,
                "AVAILABLE",
                false,
                "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=1200",
                Arrays.asList("Ready Clean Title", "Utilities Connected", "Fenced Security", "Paved Road Access"),
                agentUser.getId(),
                agentUser.getName(),
                agentUser.getPhone(),
                agentUser.getEmail(),
                "2025-02-05"
        ));

        mockProperties.add(new Property(
                "prop_hidden_1",
                "Thika Road Studio (Hidden — Trial Expired)",
                "Studio apartment withdrawn from public feeds after the listing agent's free trial expired.",
                "Apartment",
                "For Rent",
                28000.0,
                "/ month",
                "Roysambu, Nairobi, Kenya",
                "Thika Superhighway, Roysambu",
                1,
                1,
                450.0,
                "AVAILABLE",
                false,
                "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1200",
                Arrays.asList("Parking", "Security"),
                delinquentAgent.getId(),
                delinquentAgent.getName(),
                delinquentAgent.getPhone(),
                delinquentAgent.getEmail(),
                "2025-01-10"
        ));

        for (Property p : mockProperties) {
            applyTranscoding(p);
            if (p.getFeedPriority() == 0 && agentUser.getId().equals(p.getAgentId())) {
                p.setFeedPriority(SubscriptionTier.PREMIUM.getFeedPriority());
                p.setFeatured(true);
            }
        }

        mockReports.add(new ListingReport(
                "rpt_1",
                "prop_4",
                "Kilimani Modern Residency & Pool",
                clientUser.getId(),
                clientUser.getName(),
                "Inaccurate pricing",
                "Asking rent does not match the advertised unit size.",
                "OPEN",
                "2025-02-06"
        ));

        // Sample Inquiries
        mockInquiries.add(new Inquiry(
                "inq_1",
                "prop_1",
                "Grand Heights Luxury Penthouse",
                clientUser.getId(),
                clientUser.getName(),
                clientUser.getEmail(),
                clientUser.getPhone(),
                "Is the penthose available for an immediate inspection this Saturday?",
                agentUser.getId(),
                "PENDING",
                "2025-01-26"
        ));

        // Sample Site Visits
        mockSiteVisits.add(new SiteVisit(
                "sv_1",
                "prop_2",
                "Whispering Pines Modern Villa",
                "1042 Pinecrest Boulevard",
                clientUser.getId(),
                clientUser.getName(),
                clientUser.getPhone(),
                "2025-02-05",
                "10:00 AM",
                agentUser.getId(),
                "CONFIRMED",
                "2025-01-27"
        ));
    }

    // --- Property Methods ---

    public void getProperties(RepositoryCallback<List<Property>> callback) {
        executor.execute(() -> {
            applyDelinquencyAutomation();
            try {
                Response<List<Property>> response = apiService.getProperties().execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Property> list = syncFavorites(sortPublicFeed(filterPublic(response.body())));
                    mainHandler.post(() -> callback.onSuccess(list));
                    return;
                }
            } catch (Exception ignored) {}

            List<Property> list = syncFavorites(sortPublicFeed(filterPublic(new ArrayList<>(mockProperties))));
            mainHandler.post(() -> callback.onSuccess(list));
        });
    }

    public void getAllPropertiesForAdmin(RepositoryCallback<List<Property>> callback) {
        executor.execute(() -> {
            applyDelinquencyAutomation();
            List<Property> list = syncFavorites(new ArrayList<>(mockProperties));
            mainHandler.post(() -> callback.onSuccess(list));
        });
    }

    public void getListingsForAgent(String agentId, RepositoryCallback<List<Property>> callback) {
        executor.execute(() -> {
            applyDelinquencyAutomation();
            List<Property> list = new ArrayList<>();
            for (Property p : mockProperties) {
                if (agentId != null && agentId.equals(p.getAgentId())) {
                    list.add(p);
                }
            }
            mainHandler.post(() -> callback.onSuccess(syncFavorites(list)));
        });
    }

    public void searchProperties(String query, String type, String listing, Double minPrice, Double maxPrice,
                                 RepositoryCallback<List<Property>> callback) {
        executor.execute(() -> {
            applyDelinquencyAutomation();
            try {
                Response<List<Property>> response = apiService.searchProperties(query, type, listing, minPrice, maxPrice).execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Property> list = syncFavorites(sortPublicFeed(filterPublic(response.body())));
                    mainHandler.post(() -> callback.onSuccess(list));
                    return;
                }
            } catch (Exception ignored) {}

            // Mock Search Filter Fallback
            List<Property> filtered = new ArrayList<>();
            for (Property p : mockProperties) {
                boolean match = true;

                if (query != null && !query.trim().isEmpty()) {
                    String q = query.toLowerCase().trim();
                    match = (p.getTitle() != null && p.getTitle().toLowerCase().contains(q)) ||
                            (p.getLocation() != null && p.getLocation().toLowerCase().contains(q)) ||
                            (p.getDescription() != null && p.getDescription().toLowerCase().contains(q)) ||
                            (p.getPublicLocationLabel() != null && p.getPublicLocationLabel().toLowerCase().contains(q));
                }

                if (match && type != null && !type.equalsIgnoreCase("All") && !type.trim().isEmpty()) {
                    match = p.getPropertyType().equalsIgnoreCase(type);
                }

                if (match && listing != null && !listing.equalsIgnoreCase("All") && !listing.trim().isEmpty()) {
                    match = p.getListingType().equalsIgnoreCase(listing);
                }

                if (match && minPrice != null) {
                    match = p.getPrice() >= minPrice;
                }

                if (match && maxPrice != null) {
                    match = p.getPrice() <= maxPrice;
                }

                if (match) {
                    filtered.add(p);
                }
            }

            List<Property> list = syncFavorites(sortPublicFeed(filterPublic(filtered)));
            mainHandler.post(() -> callback.onSuccess(list));
        });
    }

    public void getPropertyById(String id, RepositoryCallback<Property> callback) {
        executor.execute(() -> {
            try {
                Response<Property> response = apiService.getPropertyById(id).execute();
                if (response.isSuccessful() && response.body() != null) {
                    Property p = response.body();
                    p.setFavorite(sessionManager.isFavorite(p.getId()));
                    mainHandler.post(() -> callback.onSuccess(p));
                    return;
                }
            } catch (Exception ignored) {}

            for (Property p : mockProperties) {
                if (p.getId().equals(id)) {
                    p.setFavorite(sessionManager.isFavorite(p.getId()));
                    mainHandler.post(() -> callback.onSuccess(p));
                    return;
                }
            }
            mainHandler.post(() -> callback.onError("Property not found"));
        });
    }

    public void createProperty(Property property, RepositoryCallback<Property> callback) {
        executor.execute(() -> {
            try {
                Response<Property> response = apiService.createProperty(property).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mainHandler.post(() -> callback.onSuccess(response.body()));
                    return;
                }
            } catch (Exception ignored) {}

            User agent = findUserById(property.getAgentId());
            long now = System.currentTimeMillis();
            if (agent != null && !SubscriptionPolicy.isSubscriptionActive(agent, now)) {
                mainHandler.post(() -> callback.onError("Your trial or subscription has expired. Activate a paid plan to publish listings."));
                return;
            }
            SubscriptionTier tier = SubscriptionPolicy.currentTier(agent);
            int listingCount = countAgentListings(property.getAgentId());
            if (listingCount >= tier.getMaxListings()) {
                mainHandler.post(() -> callback.onError("Listing limit reached for " + tier.getDisplayName() + " (" + tier.getMaxListings() + "). Upgrade to add more."));
                return;
            }
            int videoCount = countAgentVideos(property.getAgentId());
            boolean addingVideo = property.getVideoUrl() != null && !property.getVideoUrl().trim().isEmpty();
            if (addingVideo && videoCount >= tier.getMaxVideos()) {
                mainHandler.post(() -> callback.onError("Video limit reached for " + tier.getDisplayName() + " (" + tier.getMaxVideos() + "). Upgrade for more capacity."));
                return;
            }

            if (property.getId() == null || property.getId().isEmpty()) {
                property.setId("prop_" + UUID.randomUUID().toString().substring(0, 8));
            }
            property.setCreatedDate("2025-02-01");
            property.setFeedPriority(tier.getFeedPriority());
            property.setFeatured(tier == SubscriptionTier.PREMIUM);
            applyTranscoding(property);
            mockProperties.add(0, property);
            mainHandler.post(() -> callback.onSuccess(property));
        });
    }

    public void updatePropertyStatus(String id, String newStatus, RepositoryCallback<Property> callback) {
        executor.execute(() -> {
            for (Property p : mockProperties) {
                if (p.getId().equals(id)) {
                    p.setStatus(newStatus);
                    mainHandler.post(() -> callback.onSuccess(p));
                    return;
                }
            }
            mainHandler.post(() -> callback.onError("Property not found"));
        });
    }

    // --- Auth & User Methods ---

    public void login(String email, String password, RepositoryCallback<User> callback) {
        executor.execute(() -> {
            User credentials = new User();
            credentials.setEmail(email);
            credentials.setPassword(password);

            try {
                Response<User> response = apiService.login(credentials).execute();
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveUserSession(response.body());
                    mainHandler.post(() -> callback.onSuccess(response.body()));
                    return;
                }
            } catch (Exception ignored) {}

            for (User u : mockUsers) {
                if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                    sessionManager.saveUserSession(u);
                    mainHandler.post(() -> callback.onSuccess(u));
                    return;
                }
            }
            mainHandler.post(() -> callback.onError("Invalid email or password"));
        });
    }

    public void register(User user, RepositoryCallback<User> callback) {
        executor.execute(() -> {
            try {
                Response<User> response = apiService.register(user).execute();
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveUserSession(response.body());
                    mainHandler.post(() -> callback.onSuccess(response.body()));
                    return;
                }
            } catch (Exception ignored) {}

            user.setId("usr_" + UUID.randomUUID().toString().substring(0, 8));
            if ("CLIENT".equalsIgnoreCase(user.getRole())) {
                user.setVerified(true);
            } else if ("AGENT".equalsIgnoreCase(user.getRole())) {
                user.setVerified(false); // require admin verification
                SubscriptionPolicy.startFreeTrial(user, System.currentTimeMillis());
            }
            mockUsers.add(user);
            sessionManager.saveUserSession(user);
            mainHandler.post(() -> callback.onSuccess(user));
        });
    }

    public void getPendingAgents(RepositoryCallback<List<User>> callback) {
        executor.execute(() -> {
            try {
                Response<List<User>> response = apiService.getPendingAgents().execute();
                if (response.isSuccessful() && response.body() != null) {
                    mainHandler.post(() -> callback.onSuccess(response.body()));
                    return;
                }
            } catch (Exception ignored) {}

            List<User> pending = new ArrayList<>();
            for (User u : mockUsers) {
                if ("AGENT".equalsIgnoreCase(u.getRole()) && !u.isVerified()) {
                    pending.add(u);
                }
            }
            mainHandler.post(() -> callback.onSuccess(pending));
        });
    }

    public void verifyAgent(String agentId, boolean verify, RepositoryCallback<User> callback) {
        executor.execute(() -> {
            try {
                Response<User> response = apiService.verifyAgent(agentId, verify).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mainHandler.post(() -> callback.onSuccess(response.body()));
                    return;
                }
            } catch (Exception ignored) {}

            for (User u : mockUsers) {
                if (u.getId().equals(agentId)) {
                    u.setVerified(verify);
                    mainHandler.post(() -> callback.onSuccess(u));
                    return;
                }
            }
            mainHandler.post(() -> callback.onError("Agent not found"));
        });
    }

    // --- Inquiries & Visits ---

    public void createInquiry(Inquiry inquiry, RepositoryCallback<Inquiry> callback) {
        executor.execute(() -> {
            inquiry.setId("inq_" + UUID.randomUUID().toString().substring(0, 8));
            inquiry.setCreatedDate("2025-02-01");
            inquiry.setStatus("PENDING");
            mockInquiries.add(0, inquiry);
            mainHandler.post(() -> callback.onSuccess(inquiry));
        });
    }

    public void createSiteVisit(SiteVisit siteVisit, RepositoryCallback<SiteVisit> callback) {
        executor.execute(() -> {
            siteVisit.setId("sv_" + UUID.randomUUID().toString().substring(0, 8));
            siteVisit.setCreatedDate("2025-02-01");
            siteVisit.setStatus("PENDING");
            mockSiteVisits.add(0, siteVisit);
            mainHandler.post(() -> callback.onSuccess(siteVisit));
        });
    }

    public void getInquiriesForUser(String userId, RepositoryCallback<List<Inquiry>> callback) {
        executor.execute(() -> {
            List<Inquiry> list = new ArrayList<>();
            for (Inquiry inq : mockInquiries) {
                if (userId != null && userId.equals(inq.getUserId())) {
                    list.add(inq);
                }
            }
            mainHandler.post(() -> callback.onSuccess(list));
        });
    }

    public void getSiteVisitsForUser(String userId, RepositoryCallback<List<SiteVisit>> callback) {
        executor.execute(() -> {
            List<SiteVisit> list = new ArrayList<>();
            for (SiteVisit sv : mockSiteVisits) {
                if (userId != null && userId.equals(sv.getUserId())) {
                    list.add(sv);
                }
            }
            mainHandler.post(() -> callback.onSuccess(list));
        });
    }

    public void recordAdImpression() {
        totalAdImpressions++;
    }

    public void getDashboardStats(RepositoryCallback<DashboardStats> callback) {
        executor.execute(() -> {
            int pendingAgents = 0;
            for (User u : mockUsers) {
                if ("AGENT".equalsIgnoreCase(u.getRole()) && !u.isVerified()) {
                    pendingAgents++;
                }
            }

            applyDelinquencyAutomation();
            int openReports = 0;
            for (ListingReport report : mockReports) {
                if ("OPEN".equalsIgnoreCase(report.getStatus())) {
                    openReports++;
                }
            }
            DashboardStats stats = new DashboardStats(
                    mockProperties.size(),
                    18,
                    mockUsers.size(),
                    pendingAgents,
                    1,
                    mockInquiries.size(),
                    mockSiteVisits.size()
            );
            stats.setOpenListingReports(openReports);
            stats.setAdImpressions(totalAdImpressions);
            stats.setAppDownloads(totalAppDownloads);
            mainHandler.post(() -> callback.onSuccess(stats));
        });
    }

    public void reportListing(ListingReport report, RepositoryCallback<ListingReport> callback) {
        executor.execute(() -> {
            report.setId("rpt_" + UUID.randomUUID().toString().substring(0, 8));
            report.setStatus("OPEN");
            report.setCreatedDate("2025-02-01");
            mockReports.add(0, report);
            mainHandler.post(() -> callback.onSuccess(report));
        });
    }

    public void getListingReports(RepositoryCallback<List<ListingReport>> callback) {
        executor.execute(() -> mainHandler.post(() -> callback.onSuccess(new ArrayList<>(mockReports))));
    }

    public void updateReportStatus(String reportId, String status, RepositoryCallback<ListingReport> callback) {
        executor.execute(() -> {
            for (ListingReport report : mockReports) {
                if (report.getId().equals(reportId)) {
                    report.setStatus(status);
                    mainHandler.post(() -> callback.onSuccess(report));
                    return;
                }
            }
            mainHandler.post(() -> callback.onError("Report not found"));
        });
    }

    public void activateSubscription(String userId, SubscriptionTier tier, RepositoryCallback<User> callback) {
        executor.execute(() -> {
            User user = findUserById(userId);
            if (user == null) {
                mainHandler.post(() -> callback.onError("Agent not found"));
                return;
            }
            SubscriptionPolicy.activatePaidPlan(user, tier, System.currentTimeMillis());
            applyDelinquencyAutomation();
            sessionManager.saveUserSession(user);
            mainHandler.post(() -> callback.onSuccess(user));
        });
    }

    private void applyDelinquencyAutomation() {
        long now = System.currentTimeMillis();
        for (User user : mockUsers) {
            if (!SubscriptionPolicy.isAgent(user)) {
                continue;
            }
            boolean active = SubscriptionPolicy.isSubscriptionActive(user, now);
            if (!active) {
                user.setPaymentDelinquent(SubscriptionPolicy.currentTier(user) != SubscriptionTier.FREE_TRIAL
                        || now > user.getRegisteredAtMillis() + SubscriptionPolicy.TRIAL_DURATION_MS);
            }
            for (Property property : mockProperties) {
                if (user.getId() != null && user.getId().equals(property.getAgentId())) {
                    property.setHiddenFromPublicFeed(!active);
                    if (active) {
                        property.setFeedPriority(SubscriptionPolicy.currentTier(user).getFeedPriority());
                        property.setFeatured(SubscriptionPolicy.currentTier(user) == SubscriptionTier.PREMIUM);
                    }
                }
            }
        }
    }

    private List<Property> filterPublic(List<Property> source) {
        List<Property> visible = new ArrayList<>();
        for (Property property : source) {
            if (property != null && !property.isHiddenFromPublicFeed()) {
                visible.add(property);
            }
        }
        return visible;
    }

    private List<Property> sortPublicFeed(List<Property> source) {
        Collections.sort(source, (a, b) -> Integer.compare(b.getFeedPriority(), a.getFeedPriority()));
        return source;
    }

    private void applyTranscoding(Property property) {
        if (property.getImageUrl() != null && !property.getImageUrl().isEmpty()) {
            property.setTranscodedImageUrl(MediaTranscoder.transcodeToMobile(property.getImageUrl()));
        }
        if (property.getVideoUrl() != null && !property.getVideoUrl().trim().isEmpty()) {
            property.setTranscodedVideoUrl(MediaTranscoder.transcodeToMobile(property.getVideoUrl()));
        }
        property.setMediaTranscoded(true);
    }

    private int countAgentListings(String agentId) {
        int count = 0;
        for (Property p : mockProperties) {
            if (agentId != null && agentId.equals(p.getAgentId())) {
                count++;
            }
        }
        return count;
    }

    private int countAgentVideos(String agentId) {
        int count = 0;
        for (Property p : mockProperties) {
            if (agentId != null && agentId.equals(p.getAgentId())
                    && p.getVideoUrl() != null && !p.getVideoUrl().trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private User findUserById(String userId) {
        if (userId == null) {
            return null;
        }
        for (User user : mockUsers) {
            if (userId.equals(user.getId())) {
                return user;
            }
        }
        User sessionUser = sessionManager.getCurrentUser();
        if (sessionUser != null && userId.equals(sessionUser.getId())) {
            return sessionUser;
        }
        return null;
    }

    private List<Property> syncFavorites(List<Property> list) {
        for (Property p : list) {
            p.setFavorite(sessionManager.isFavorite(p.getId()));
        }
        return list;
    }
}
