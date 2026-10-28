package com.example.spaceandoccupy.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceandoccupy.agent.AgentHomeActivity;
import com.example.spaceandoccupy.databinding.ActivityPendingAgentBinding;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;

public class PendingAgentActivity extends AppCompatActivity {

    private ActivityPendingAgentBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityPendingAgentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = PropertyRepository.getInstance(this).getSessionManager();
        User currentUser = sessionManager.getCurrentUser();

        if (currentUser != null) {
            binding.tvAgentName.setText("Welcome, " + currentUser.getName());
        }

        binding.btnCheckStatus.setOnClickListener(v -> {
            User refreshedUser = sessionManager.getCurrentUser();
            if (refreshedUser != null && refreshedUser.isVerified()) {
                Toast.makeText(PendingAgentActivity.this, "Your account has been verified!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PendingAgentActivity.this, AgentHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(PendingAgentActivity.this, "Your account is still pending Administrator verification.", Toast.LENGTH_LONG).show();
            }
        });

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(PendingAgentActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
