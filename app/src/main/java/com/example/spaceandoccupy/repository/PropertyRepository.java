package com.example.spaceandoccupy.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.spaceandoccupy.model.DashboardStats;
import com.example.spaceandoccupy.model.Inquiry;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.SiteVisit;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.network.ApiClient;
import com.example.spaceandoccupy.network.ApiService;

import java.util.ArrayList;
import java.util.Arrays;
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
        User agentUser = new User("usr_agent_1", "Space & Occupy Agent", "agent@spaceandoccupy.com", "password123", "AGENT", true, "+254787900284", "https://images.unsplash.com/photo-1560250097-0b93528c311a");
        agentUser.setAgencyName("Space & Occupy Investments");
        agentUser.setLicenseNumber("SO-884920");

        User pendingAgent = new User("usr_agent_2", "Elena Rostova", "elena@spaceandoccupy.com", "password123", "AGENT", false, "+254 787 900284", "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2");
        pendingAgent.setAgencyName("Space & Occupy Partner");
        pendingAgent.setLicenseNumber("SO-910234");

        User adminUser = new User("usr_admin_1", "Administrator", "admin@spaceandoccupy.com", "admin123", "ADMIN", true, "+254 787 900284", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e");

        mockUsers.add(clientUser);
        mockUsers.add(agentUser);
        mockUsers.add(pendingAgent);
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
            try {
                Response<List<Property>> response = apiService.getProperties().execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Property> list = syncFavorites(response.body());
                    mainHandler.post(() -> callback.onSuccess(list));
                    return;
                }
            } catch (Exception ignored) {}

            // Fallback to Mock Data
            List<Property> list = syncFavorites(new ArrayList<>(mockProperties));
            mainHandler.post(() -> callback.onSuccess(list));
        });
    }

    public void searchProperties(String query, String type, String listing, Double minPrice, Double maxPrice,
                                 RepositoryCallback<List<Property>> callback) {
        executor.execute(() -> {
            try {
                Response<List<Property>> response = apiService.searchProperties(query, type, listing, minPrice, maxPrice).execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Property> list = syncFavorites(response.body());
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
                    match = p.getTitle().toLowerCase().contains(q) ||
                            p.getLocation().toLowerCase().contains(q) ||
                            p.getDescription().toLowerCase().contains(q);
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

            List<Property> list = syncFavorites(filtered);
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

            if (property.getId() == null || property.getId().isEmpty()) {
                property.setId("prop_" + UUID.randomUUID().toString().substring(0, 8));
            }
            property.setCreatedDate("2025-02-01");
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

    public void getDashboardStats(RepositoryCallback<DashboardStats> callback) {
        executor.execute(() -> {
            int pendingAgents = 0;
            for (User u : mockUsers) {
                if ("AGENT".equalsIgnoreCase(u.getRole()) && !u.isVerified()) {
                    pendingAgents++;
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
            mainHandler.post(() -> callback.onSuccess(stats));
        });
    }

    private List<Property> syncFavorites(List<Property> list) {
        for (Property p : list) {
            p.setFavorite(sessionManager.isFavorite(p.getId()));
        }
        return list;
    }
}
