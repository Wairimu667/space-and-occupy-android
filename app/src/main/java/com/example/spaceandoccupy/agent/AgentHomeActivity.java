package com.example.spaceandoccupy.agent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceandoccupy.auth.LoginActivity;
import com.example.spaceandoccupy.databinding.ActivityAgentHomeBinding;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;
import com.example.spaceandoccupy.subscription.SubscriptionPolicy;
import com.example.spaceandoccupy.subscription.SubscriptionTier;

public class AgentHomeActivity extends AppCompatActivity {

    private ActivityAgentHomeBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAgentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = PropertyRepository.getInstance(this).getSessionManager();
        bindAgentHeader();

        binding.btnManageSubscription.setOnClickListener(v ->
                startActivity(new Intent(AgentHomeActivity.this, SubscriptionActivity.class)));

        binding.btnAddListing.setOnClickListener(v -> {
            User currentUser = sessionManager.getCurrentUser();
            if (!SubscriptionPolicy.isSubscriptionActive(currentUser, System.currentTimeMillis())) {
                startActivity(new Intent(AgentHomeActivity.this, SubscriptionActivity.class));
                return;
            }
            startActivity(new Intent(AgentHomeActivity.this, AddPropertyActivity.class));
        });

        binding.btnMyListings.setOnClickListener(v -> {
            startActivity(new Intent(AgentHomeActivity.this, ManageAgentListingsActivity.class));
        });

        binding.btnInquiriesVisits.setOnClickListener(v -> {
            startActivity(new Intent(AgentHomeActivity.this, AgentInquiriesActivity.class));
        });

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(AgentHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindAgentHeader();
    }

    private void bindAgentHeader() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        binding.tvAgentName.setText(currentUser.getName());
        String agencyInfo = (currentUser.getAgencyName() != null ? currentUser.getAgencyName() : "Independent Agent")
                + (currentUser.getLicenseNumber() != null ? " • License #" + currentUser.getLicenseNumber() : "");
        binding.tvAgencyName.setText(agencyInfo);
        SubscriptionTier tier = SubscriptionPolicy.currentTier(currentUser);
        binding.tvSubscriptionStatus.setText(tier.getDisplayName() + " • "
                + SubscriptionPolicy.statusLabel(currentUser, System.currentTimeMillis()));
    }
}
