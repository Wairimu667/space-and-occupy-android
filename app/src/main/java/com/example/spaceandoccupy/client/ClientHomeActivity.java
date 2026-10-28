package com.example.spaceandoccupy.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceandoccupy.auth.LoginActivity;
import com.example.spaceandoccupy.databinding.ActivityClientHomeBinding;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;

public class ClientHomeActivity extends AppCompatActivity {

    private ActivityClientHomeBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityClientHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = PropertyRepository.getInstance(this).getSessionManager();
        User currentUser = sessionManager.getCurrentUser();

        if (currentUser != null) {
            binding.tvWelcomeName.setText("Welcome, " + currentUser.getName());
            binding.tvUserEmail.setText(currentUser.getEmail());
        }

        binding.btnBrowseProperties.setOnClickListener(v -> {
            startActivity(new Intent(ClientHomeActivity.this, PropertyListActivity.class));
        });

        binding.btnSavedFavorites.setOnClickListener(v -> {
            startActivity(new Intent(ClientHomeActivity.this, SavedFavoritesActivity.class));
        });

        binding.btnMyInquiries.setOnClickListener(v -> {
            startActivity(new Intent(ClientHomeActivity.this, MyInquiriesAndVisitsActivity.class));
        });

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(ClientHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
