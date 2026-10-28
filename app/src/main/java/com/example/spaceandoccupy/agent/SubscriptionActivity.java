package com.example.spaceandoccupy.agent;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.databinding.ActivitySubscriptionBinding;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.subscription.SubscriptionPolicy;
import com.example.spaceandoccupy.subscription.SubscriptionTier;
import com.google.android.material.card.MaterialCardView;

public class SubscriptionActivity extends AppCompatActivity {

    private ActivitySubscriptionBinding binding;
    private PropertyRepository repository;
    private SubscriptionTier selectedTier = SubscriptionTier.BASIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        refreshStatus();
        highlightSelection();

        binding.cardBasic.setOnClickListener(v -> selectTier(SubscriptionTier.BASIC));
        binding.cardPro.setOnClickListener(v -> selectTier(SubscriptionTier.PRO));
        binding.cardPremium.setOnClickListener(v -> selectTier(SubscriptionTier.PREMIUM));
        binding.btnActivatePlan.setOnClickListener(v -> activatePlan());
    }

    private void selectTier(SubscriptionTier tier) {
        selectedTier = tier;
        highlightSelection();
    }

    private void highlightSelection() {
        styleCard(binding.cardBasic, selectedTier == SubscriptionTier.BASIC);
        styleCard(binding.cardPro, selectedTier == SubscriptionTier.PRO);
        styleCard(binding.cardPremium, selectedTier == SubscriptionTier.PREMIUM);
        binding.btnActivatePlan.setText("Activate " + selectedTier.getDisplayName() + " • " + selectedTier.getFormattedPrice());
    }

    private void styleCard(MaterialCardView card, boolean selected) {
        card.setStrokeWidth(selected ? 3 : 1);
        card.setStrokeColor(getColor(selected ? R.color.primary_brown : R.color.border_color));
    }

    private void refreshStatus() {
        User user = repository.getSessionManager().getCurrentUser();
        SubscriptionTier tier = SubscriptionPolicy.currentTier(user);
        binding.tvCurrentPlan.setText("Current plan: " + tier.getDisplayName());
        binding.tvPlanStatus.setText(SubscriptionPolicy.statusLabel(user, System.currentTimeMillis()));
    }

    private void activatePlan() {
        User user = repository.getSessionManager().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Please sign in as an agent first.", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.btnActivatePlan.setEnabled(false);
        repository.activateSubscription(user.getId(), selectedTier, new PropertyRepository.RepositoryCallback<User>() {
            @Override
            public void onSuccess(User result) {
                binding.btnActivatePlan.setEnabled(true);
                Toast.makeText(SubscriptionActivity.this,
                        selectedTier.getDisplayName() + " activated. Public listings are visible again.",
                        Toast.LENGTH_LONG).show();
                refreshStatus();
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnActivatePlan.setEnabled(true);
                Toast.makeText(SubscriptionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
