package com.example.spaceandoccupy.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.adapter.AdminPropertyAdapter;
import com.example.spaceandoccupy.databinding.ActivityListingModerationBinding;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.repository.PropertyRepository;

import java.util.List;

public class ListingModerationActivity extends AppCompatActivity implements AdminPropertyAdapter.OnAdminPropertyActionListener {

    private ActivityListingModerationBinding binding;
    private PropertyRepository repository;
    private AdminPropertyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityListingModerationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new AdminPropertyAdapter(this, this);
        binding.rvModeration.setLayoutManager(new LinearLayoutManager(this));
        binding.rvModeration.setAdapter(adapter);

        loadProperties();
    }

    private void loadProperties() {
        repository.getProperties(new PropertyRepository.RepositoryCallback<List<Property>>() {
            @Override
            public void onSuccess(List<Property> result) {
                adapter.updateData(result);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ListingModerationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onChangeStatus(Property property) {
        String[] options = {"AVAILABLE", "SOLD", "RENTED", "LEASED"};
        new AlertDialog.Builder(this)
                .setTitle("Moderate Status for: " + property.getTitle())
                .setItems(options, (dialog, which) -> {
                    String selected = options[which];
                    repository.updatePropertyStatus(property.getId(), selected, new PropertyRepository.RepositoryCallback<Property>() {
                        @Override
                        public void onSuccess(Property result) {
                            Toast.makeText(ListingModerationActivity.this, "Property status updated to " + selected, Toast.LENGTH_SHORT).show();
                            loadProperties();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(ListingModerationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDelete(Property property) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Property Listing")
                .setMessage("Are you sure you want to delete '" + property.getTitle() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Toast.makeText(ListingModerationActivity.this, "Property deleted by Admin", Toast.LENGTH_SHORT).show();
                    loadProperties();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
