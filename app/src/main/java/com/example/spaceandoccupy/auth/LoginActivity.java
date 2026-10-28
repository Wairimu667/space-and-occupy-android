package com.example.spaceandoccupy.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceandoccupy.admin.AdminHomeActivity;
import com.example.spaceandoccupy.agent.AgentHomeActivity;
import com.example.spaceandoccupy.client.ClientHomeActivity;
import com.example.spaceandoccupy.databinding.ActivityLoginBinding;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PropertyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);

        // Secret Easter Egg Admin Portal Gate (Tap Logo 5 Times Rapidly)
        com.example.spaceandoccupy.admin.AdminEasterEggPortal.attachTrigger(binding.imgLogo, LoginActivity.this);

        binding.btnLogin.setOnClickListener(v -> performLogin());

        binding.tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Quick demo credential autofill
        binding.btnDemoClient.setOnClickListener(v -> {
            binding.etEmail.setText("client@spaceandoccupy.com");
            binding.etPassword.setText("password123");
            performLogin();
        });

        binding.btnDemoAgent.setOnClickListener(v -> {
            binding.etEmail.setText("agent@spaceandoccupy.com");
            binding.etPassword.setText("password123");
            performLogin();
        });

        binding.btnDemoAdmin.setOnClickListener(v -> {
            binding.etEmail.setText("admin@spaceandoccupy.com");
            binding.etPassword.setText("admin123");
            performLogin();
        });
    }

    private void performLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Enter a valid email address");
            return;
        } else {
            binding.tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Password cannot be empty");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        binding.btnLogin.setEnabled(false);

        repository.login(email, password, new PropertyRepository.RepositoryCallback<User>() {
            @Override
            public void onSuccess(User user) {
                binding.btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Welcome back, " + user.getName(), Toast.LENGTH_SHORT).show();
                navigateToUserRoleScreen(user);
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToUserRoleScreen(User user) {
        Intent intent;
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
        } else if ("AGENT".equalsIgnoreCase(user.getRole())) {
            if (user.isVerified()) {
                intent = new Intent(LoginActivity.this, AgentHomeActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, PendingAgentActivity.class);
            }
        } else { // CLIENT
            intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
