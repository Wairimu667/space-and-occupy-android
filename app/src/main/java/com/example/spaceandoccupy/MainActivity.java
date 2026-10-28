package com.example.spaceandoccupy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.spaceandoccupy.adapter.TikTokPropertyAdapter;
import com.example.spaceandoccupy.admin.AdminHomeActivity;
import com.example.spaceandoccupy.agent.AgentHomeActivity;
import com.example.spaceandoccupy.auth.LoginActivity;
import com.example.spaceandoccupy.auth.PendingAgentActivity;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPagerTiktok;
    private TikTokPropertyAdapter adapter;
    private PropertyRepository repository;
    private SessionManager sessionManager;
    private MaterialButton btnPortalLogin;
    private ChipGroup cgCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Render TikTok Vertical Feed
        setContentView(R.layout.activity_main);

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        viewPagerTiktok = findViewById(R.id.view_pager_tiktok);
        btnPortalLogin = findViewById(R.id.btn_portal_login);
        cgCategories = findViewById(R.id.cg_categories);

        // Configure Vertical ViewPager2
        viewPagerTiktok.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        adapter = new TikTokPropertyAdapter(this);
        viewPagerTiktok.setAdapter(adapter);

        // Update Portal Login button label based on active session
        updatePortalButton();

        btnPortalLogin.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                User user = sessionManager.getCurrentUser();
                if (user != null) {
                    navigateToRoleScreen(user);
                    return;
                }
            }
            // Navigate to Agent / Admin Login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Category Filter Chip Listeners
        cgCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_all) {
                loadAllProperties();
            } else if (checkedId == R.id.chip_for_sale) {
                filterProperties(null, "For Sale");
            } else if (checkedId == R.id.chip_for_rent) {
                filterProperties(null, "For Rent");
            } else if (checkedId == R.id.chip_land) {
                filterProperties("Land", null);
            } else if (checkedId == R.id.chip_commercial) {
                filterProperties("Commercial", null);
            }
        });

        // Load initial feed
        loadAllProperties();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePortalButton();
    }

    private void updatePortalButton() {
        if (sessionManager.isLoggedIn()) {
            User user = sessionManager.getCurrentUser();
            if (user != null) {
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    btnPortalLogin.setText("Admin Portal");
                } else if ("AGENT".equalsIgnoreCase(user.getRole())) {
                    btnPortalLogin.setText("Agent Dashboard");
                } else {
                    btnPortalLogin.setText("My Account");
                }
                return;
            }
        }
        btnPortalLogin.setText("Agent/Admin");
    }

    private void loadAllProperties() {
        repository.getProperties(new PropertyRepository.RepositoryCallback<List<Property>>() {
            @Override
            public void onSuccess(List<Property> result) {
                adapter.setProperties(result);
            }

            @Override
            public void onError(String errorMessage) {
                // Keep current list on error
            }
        });
    }

    private void filterProperties(String type, String listing) {
        repository.searchProperties(null, type, listing, null, null, new PropertyRepository.RepositoryCallback<List<Property>>() {
            @Override
            public void onSuccess(List<Property> result) {
                adapter.setProperties(result);
            }

            @Override
            public void onError(String errorMessage) {
                // Fallback
            }
        });
    }

    private void navigateToRoleScreen(User user) {
        Intent intent;
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            intent = new Intent(MainActivity.this, AdminHomeActivity.class);
        } else if ("AGENT".equalsIgnoreCase(user.getRole())) {
            if (user.isVerified()) {
                intent = new Intent(MainActivity.this, AgentHomeActivity.class);
            } else {
                intent = new Intent(MainActivity.this, PendingAgentActivity.class);
            }
        } else {
            // Standard client user dashboard or feed
            intent = new Intent(MainActivity.this, MainActivity.class);
        }
        startActivity(intent);
    }
}
