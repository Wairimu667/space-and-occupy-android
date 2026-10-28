package com.example.spaceandoccupy.network;

import com.example.spaceandoccupy.model.DashboardStats;
import com.example.spaceandoccupy.model.Inquiry;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.SiteVisit;
import com.example.spaceandoccupy.model.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Properties
    @GET("api/properties")
    Call<List<Property>> getProperties();

    @GET("api/properties/{id}")
    Call<Property> getPropertyById(@Path("id") String id);

    @GET("api/properties/search")
    Call<List<Property>> searchProperties(
            @Query("query") String query,
            @Query("type") String propertyType,
            @Query("listing") String listingType,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice
    );

    @POST("api/properties")
    Call<Property> createProperty(@Body Property property);

    @PUT("api/properties/{id}")
    Call<Property> updateProperty(@Path("id") String id, @Body Property property);

    @DELETE("api/properties/{id}")
    Call<Void> deleteProperty(@Path("id") String id);

    // Auth & Users
    @POST("api/auth/login")
    Call<User> login(@Body User credentials);

    @POST("api/auth/register")
    Call<User> register(@Body User user);

    @GET("api/users/agents/pending")
    Call<List<User>> getPendingAgents();

    @PUT("api/users/{id}/verify")
    Call<User> verifyAgent(@Path("id") String id, @Query("verified") boolean verified);

    // Inquiries
    @GET("api/inquiries")
    Call<List<Inquiry>> getInquiries(@Query("userId") String userId, @Query("agentId") String agentId);

    @POST("api/inquiries")
    Call<Inquiry> createInquiry(@Body Inquiry inquiry);

    // Site Visits
    @GET("api/site-visits")
    Call<List<SiteVisit>> getSiteVisits(@Query("userId") String userId, @Query("agentId") String agentId);

    @POST("api/site-visits")
    Call<SiteVisit> createSiteVisit(@Body SiteVisit siteVisit);

    // Admin Stats
    @GET("api/admin/stats")
    Call<DashboardStats> getDashboardStats();
}
