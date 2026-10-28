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

import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.admin.AdminHomeActivity;
import com.example.spaceandoccupy.agent.AgentHomeActivity;
import com.example.spaceandoccupy.client.ClientHomeActivity;
import com.example.spaceandoccupy.databinding.ActivityRegisterBinding;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private PropertyRepository repository;
    private String selectedRole = "CLIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.cgRoles.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_agent)) {
                selectedRole = "AGENT";
                binding.cardAgentNotice.setVisibility(View.VISIBLE);
                binding.tilAgency.setVisibility(View.VISIBLE);
                binding.tilLicense.setVisibility(View.VISIBLE);
            } else if (checkedIds.contains(R.id.chip_admin)) {
                selectedRole = "ADMIN";
                binding.cardAgentNotice.setVisibility(View.GONE);
                binding.tilAgency.setVisibility(View.GONE);
                binding.tilLicense.setVisibility(View.GONE);
            } else {
                selectedRole = "CLIENT";
                binding.cardAgentNotice.setVisibility(View.GONE);
                binding.tilAgency.setVisibility(View.GONE);
                binding.tilLicense.setVisibility(View.GONE);
            }
        });

        binding.btnRegister.setOnClickListener(v -> performRegistration());

        binding.tvGoToLogin.setOnClickListener(v -> finish());
    }

    private void performRegistration() {
        String name = binding.etName.getText() != null ? binding.etName.getText().toString().trim() : "";
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String phone = binding.etPhone.getText() != null ? binding.etPhone.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
        String agency = binding.etAgency.getText() != null ? binding.etAgency.getText().toString().trim() : "";
        String license = binding.etLicense.getText() != null ? binding.etLicense.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            binding.tilName.setError("Full Name is required");
            return;
        } else {
            binding.tilName.setError(null);
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Valid email is required");
            return;
        } else {
            binding.tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            binding.tilPhone.setError("Phone number is required");
            return;
        } else {
            binding.tilPhone.setError(null);
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.tilPassword.setError("Password must be at least 6 characters");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        if ("AGENT".equals(selectedRole)) {
            if (TextUtils.isEmpty(agency)) {
                binding.tilAgency.setError("Agency Name is required for Agents");
                return;
            } else {
                binding.tilAgency.setError(null);
            }

            if (TextUtils.isEmpty(license)) {
                binding.tilLicense.setError("License Number is required for Agents");
                return;
            } else {
                binding.tilLicense.setError(null);
            }
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPassword(password);
        newUser.setRole(selectedRole);

        if ("AGENT".equals(selectedRole)) {
            newUser.setAgencyName(agency);
            newUser.setLicenseNumber(license);
            newUser.setVerified(false); // require admin verification
        } else if ("ADMIN".equals(selectedRole)) {
            newUser.setVerified(true);
        } else {
            newUser.setVerified(true);
        }

        binding.btnRegister.setEnabled(false);

        repository.register(newUser, new PropertyRepository.RepositoryCallback<User>() {
            @Override
            public void onSuccess(User user) {
                binding.btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                navigateToUserRoleScreen(user);
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToUserRoleScreen(User user) {
        Intent intent;
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            intent = new Intent(RegisterActivity.this, AdminHomeActivity.class);
        } else if ("AGENT".equalsIgnoreCase(user.getRole())) {
            if (user.isVerified()) {
                intent = new Intent(RegisterActivity.this, AgentHomeActivity.class);
            } else {
                intent = new Intent(RegisterActivity.this, PendingAgentActivity.class);
            }
        } else {
            intent = new Intent(RegisterActivity.this, ClientHomeActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
