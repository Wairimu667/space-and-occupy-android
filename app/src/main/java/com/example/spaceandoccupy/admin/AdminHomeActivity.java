package com.example.spaceandoccupy.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceandoccupy.auth.LoginActivity;
import com.example.spaceandoccupy.databinding.ActivityAdminHomeBinding;
import com.example.spaceandoccupy.model.DashboardStats;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;

public class AdminHomeActivity extends AppCompatActivity {

    private ActivityAdminHomeBinding binding;
    private SessionManager sessionManager;
    private PropertyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();
        User currentUser = sessionManager.getCurrentUser();

        if (currentUser != null) {
            binding.tvAdminName.setText(currentUser.getName() + " (Administrator)");
        }

        loadDashboardStats();

        binding.btnVerifyAgents.setOnClickListener(v -> {
            startActivity(new Intent(AdminHomeActivity.this, AgentVerificationActivity.class));
        });

        binding.btnModerateListings.setOnClickListener(v -> {
            startActivity(new Intent(AdminHomeActivity.this, ListingModerationActivity.class));
        });

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadDashboardStats() {
        repository.getDashboardStats(new PropertyRepository.RepositoryCallback<DashboardStats>() {
            @Override
            public void onSuccess(DashboardStats stats) {
                if (stats != null) {
                    binding.tvStatPendingAgents.setText(String.valueOf(stats.getPendingAgentVerifications()));
                    binding.tvStatTotalProps.setText(String.valueOf(stats.getTotalProperties()));
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AdminHomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardStats();
    }
}
