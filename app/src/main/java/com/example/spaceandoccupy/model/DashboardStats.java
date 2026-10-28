package com.example.spaceandoccupy.model;

import java.io.Serializable;

public class DashboardStats implements Serializable {
    private int totalProperties;
    private int totalClients;
    private int totalAgents;
    private int pendingAgentVerifications;
    private int pendingListingApprovals;
    private int totalInquiries;
    private int totalSiteVisits;
    private int openListingReports;
    private int adImpressions;
    private int appDownloads;

    public DashboardStats() {}

    public DashboardStats(int totalProperties, int totalClients, int totalAgents, int pendingAgentVerifications,
                          int pendingListingApprovals, int totalInquiries, int totalSiteVisits) {
        this.totalProperties = totalProperties;
        this.totalClients = totalClients;
        this.totalAgents = totalAgents;
        this.pendingAgentVerifications = pendingAgentVerifications;
        this.pendingListingApprovals = pendingListingApprovals;
        this.totalInquiries = totalInquiries;
        this.totalSiteVisits = totalSiteVisits;
    }

    public int getTotalProperties() { return totalProperties; }
    public void setTotalProperties(int totalProperties) { this.totalProperties = totalProperties; }

    public int getTotalClients() { return totalClients; }
    public void setTotalClients(int totalClients) { this.totalClients = totalClients; }

    public int getTotalAgents() { return totalAgents; }
    public void setTotalAgents(int totalAgents) { this.totalAgents = totalAgents; }

    public int getPendingAgentVerifications() { return pendingAgentVerifications; }
    public void setPendingAgentVerifications(int pendingAgentVerifications) { this.pendingAgentVerifications = pendingAgentVerifications; }

    public int getPendingListingApprovals() { return pendingListingApprovals; }
    public void setPendingListingApprovals(int pendingListingApprovals) { this.pendingListingApprovals = pendingListingApprovals; }

    public int getTotalInquiries() { return totalInquiries; }
    public void setTotalInquiries(int totalInquiries) { this.totalInquiries = totalInquiries; }

    public int getTotalSiteVisits() { return totalSiteVisits; }
    public void setTotalSiteVisits(int totalSiteVisits) { this.totalSiteVisits = totalSiteVisits; }

    public int getOpenListingReports() { return openListingReports; }
    public void setOpenListingReports(int openListingReports) { this.openListingReports = openListingReports; }

    public int getAdImpressions() { return adImpressions; }
    public void setAdImpressions(int adImpressions) { this.adImpressions = adImpressions; }

    public int getAppDownloads() { return appDownloads; }
    public void setAppDownloads(int appDownloads) { this.appDownloads = appDownloads; }
}
