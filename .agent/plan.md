# Project Plan

Space & Occupy Android App built with Java, XML layouts, Material Design 3, Retrofit 2, Glide, Spring Boot REST API integration, and MySQL database.
Supports 3 roles: Client, Agent (verified by Admin), Administrator.
Stages matching business requirements:
Stage 1: Inspection of existing code/backend & Android project configuration & branded home screen setup.
Stage 2: Property listing, search, filters, and property details.
Stage 3: Authentication, registration, login, and role-based navigation (Client, Agent, Admin).
Stage 4: Client features (favourites, enquiries, site visit requests).
Stage 5: Agent dashboard, listing submission, image upload, and approval status.
Stage 6: Admin dashboard, agent verification, listing moderation, user management.
Stage 7: Integration testing, verification, bug fixes, and final quality check.

## Project Brief

# Space & Occupy - Project Brief

## Features

1. **Property Exploration & Client Portal**: Browse, filter, and search property listings with photo galleries, amenity details, saved favorites, inquiry submissions, and site visit scheduling.
2. **Agent Listing & Status Management**: Agent registration flow requiring admin verification, property listing submissions with image uploads, and status management (Available, Sold, Rented, Leased).
3. **Admin Dashboard & Listing Moderation**: Administrator management panel featuring dashboard statistics, agent verification, property listing moderation (approve/reject/create/edit/delete), and inquiry/visit tracking.
4. **User Profile & Activity Tracking**: Account management for clients, agents, and administrators to view active inquiries, scheduled visits, and assigned properties.

---

## High-Level Tech Stack

- **Language**: Java
- **UI Framework**: Android XML Layouts with Material Design 3 components and ViewBinding
- **Architecture**: MVVM (Model-View-ViewModel) with Repository pattern and LiveData / ViewModel
- **Networking**: Retrofit 2 & OkHttp (integrating with Spring Boot REST API at `10.0.2.2:8080`)
- **Image Loading**: Glide or Picasso for Android XML `ImageView` components
- **Brand Styling & Theme**: Custom Material Design 3 theme matching brand palette:
  - Primary Brown: `#8B4F2F`
  - Dark Brown: `#5F321E`
  - Light Brown: `#B97850`
  - Cream: `#F7F2EC`
  - Light Cream: `#EEE3D8`
  - Deep Brown: `#3A2117`
  - Black: `#191512`
  - Grey: `#746B64`
  - Border: `#E5D8CC`

---

## UI Design Image

![UI Design](C:/Users/Administrator/Desktop/spaceandoccupy/input_images/image_0.png)
Image path = C:/Users/Administrator/Desktop/spaceandoccupy/input_images/image_0.png

## Implementation Steps
**Total Duration:** 25h 5m 56s

### Task_1_SetupThemeAndDataLayer: Configure Material Design 3 theme with brand brown palette (#8B4F2F, #5F321E, #B97850, #F7F2EC, #EEE3D8, #3A2117), adaptive launcher icon using design in C:/Users/Administrator/Desktop/spaceandoccupy/input_images/image_0.png, edge-to-edge display, core domain data models, Room/DataStore, and Retrofit network client with mock repository fallback.
- **Status:** COMPLETED
- **Updates:** Successfully configured Material Design 3 theme with brand brown palette (#8B4F2F, #5F321E, #B97850, #F7F2EC, #EEE3D8, #3A2117), generated adaptive app launcher icon matching image_0.png, implemented edge-to-edge layout in MainActivity, created Java data models (Property, User, Inquiry, SiteVisit, DashboardStats), set up ApiService/ApiClient targeting 10.0.2.2:8080, and created PropertyRepository/SessionManager with mock data fallback. Build verified with assembleDebug and testDebugUnitTest.
- **Acceptance Criteria:**
  - Material Design 3 brand theme with brown palette and edge-to-edge implemented
  - Adaptive app icon matching logo configured
  - Data models, local storage, and API/Mock Repository setup complete
  - The implemented UI must match the design provided in C:/Users/Administrator/Desktop/spaceandoccupy/input_images/image_0.png.
- **Duration:** 21m 38s

### Task_2_AuthAndRoleNavigation: Implement authentication flow including user registration, login, session persistence, and role-based navigation routing for Client, Agent, and Administrator roles.
- **Status:** COMPLETED
- **Updates:** Implemented LoginActivity, RegisterActivity with role selection (Client, Agent, Admin), PendingAgentActivity, SessionManager session persistence, role-based auto-routing in MainActivity, and ClientHomeActivity, AgentHomeActivity, AdminHomeActivity. Build verified with assembleDebug.
- **Acceptance Criteria:**
  - Registration and Login screens functional with role selection (Client, Agent, Admin)
  - Session state persisted across launches
  - Role-based navigation routes user to appropriate section upon login
- **Duration:** 24h 31m 57s

### Task_3_PropertyExplorationAndClientPortal: Build property exploration UI featuring list search, filtering by type/price/location, property detail page with gallery and amenities, saved favorites, inquiry submission, and site visit scheduling.
- **Status:** COMPLETED
- **Updates:** Completed PropertyListActivity with search, category chips, and filter bottom sheet; PropertyDetailActivity with gallery, specs, amenities chips, and inquiry & site visit modal sheets; SavedFavoritesActivity; MyInquiriesAndVisitsActivity with tabbed inquiry and site visit tracking; and wired up ClientHomeActivity navigation. Verified assembleDebug.
- **Acceptance Criteria:**
  - Property listing feed with search and filter controls functional
  - Property details view displays gallery, pricing, and specs
  - Client interactions (favorites, inquiries, site visit requests) working
- **Duration:** 6m 12s

### Task_4_AgentAndAdminDashboards: Implement Agent dashboard for submitting new property listings with image management and status tracking (Available, Sold, Rented, Leased), alongside Admin dashboard for agent verification and listing moderation (approve/reject).
- **Status:** COMPLETED
- **Updates:** Implemented Agent dashboard (AgentHomeActivity, AddPropertyActivity, ManageAgentListingsActivity, AgentInquiriesActivity) and Admin dashboard (AdminHomeActivity, AgentVerificationActivity, ListingModerationActivity, ManageAllPropertiesActivity). Built property listing submission with image URL support, agent status updates (Available, Sold, Rented, Leased), agent approval/rejection moderation, and property moderation. Verified assembleDebug.
- **Acceptance Criteria:**
  - Agent dashboard supports property listing creation and status updates
  - Pending agent verification state handled clearly
  - Admin dashboard allows approving/rejecting listings and verifying agents
- **Duration:** 6m 9s

### Task_5_RunAndVerify: Build, run, and perform comprehensive verification on app stability, verify user role workflows, check edge-to-edge rendering, and confirm no crashes occur.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - make sure all existing tests pass
  - build pass
  - app does not crash
  - Application verified for full end-to-end user workflows and UI polish
- **StartTime:** 2026-10-23 11:30:26 EAT

