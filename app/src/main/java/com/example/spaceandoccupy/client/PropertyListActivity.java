package com.example.spaceandoccupy.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.adapter.PropertyAdapter;
import com.example.spaceandoccupy.databinding.ActivityPropertyListBinding;
import com.example.spaceandoccupy.databinding.BottomSheetFilterBinding;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class PropertyListActivity extends AppCompatActivity implements PropertyAdapter.OnPropertyClickListener {

    private ActivityPropertyListBinding binding;
    private PropertyRepository repository;
    private SessionManager sessionManager;
    private PropertyAdapter adapter;

    // Active Filter State
    private String selectedType = "All";
    private String selectedListing = "All";
    private Double selectedMaxPrice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityPropertyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        setupRecyclerView();
        setupSearchAndFilterListeners();
        loadProperties();
    }

    private void setupRecyclerView() {
        adapter = new PropertyAdapter(this, null, this);
        binding.rvProperties.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProperties.setAdapter(adapter);
    }

    private void setupSearchAndFilterListeners() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadProperties();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.cgPropertyTypes.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_type_house)) {
                selectedType = "House";
            } else if (checkedIds.contains(R.id.chip_type_apartment)) {
                selectedType = "Apartment";
            } else if (checkedIds.contains(R.id.chip_type_commercial)) {
                selectedType = "Commercial";
            } else if (checkedIds.contains(R.id.chip_type_land)) {
                selectedType = "Land";
            } else {
                selectedType = "All";
            }
            loadProperties();
        });

        binding.btnOpenFilters.setOnClickListener(v -> showFilterBottomSheet());
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        BottomSheetFilterBinding sheetBinding = BottomSheetFilterBinding.inflate(getLayoutInflater());
        dialog.setContentView(sheetBinding.getRoot());

        if (selectedMaxPrice != null) {
            sheetBinding.etMaxPrice.setText(String.valueOf(selectedMaxPrice));
        }

        sheetBinding.btnResetFilter.setOnClickListener(v -> {
            selectedListing = "All";
            selectedMaxPrice = null;
            sheetBinding.chipListingAll.setChecked(true);
            sheetBinding.etMaxPrice.setText("");
            dialog.dismiss();
            loadProperties();
        });

        sheetBinding.btnApplyFilter.setOnClickListener(v -> {
            int listingCheckedId = sheetBinding.cgListingType.getCheckedChipId();
            if (listingCheckedId == R.id.chip_listing_sale) {
                selectedListing = "For Sale";
            } else if (listingCheckedId == R.id.chip_listing_rent) {
                selectedListing = "For Rent";
            } else if (listingCheckedId == R.id.chip_listing_lease) {
                selectedListing = "Lease";
            } else {
                selectedListing = "All";
            }

            String maxPriceStr = sheetBinding.etMaxPrice.getText() != null ? sheetBinding.etMaxPrice.getText().toString().trim() : "";
            if (!maxPriceStr.isEmpty()) {
                try {
                    selectedMaxPrice = Double.parseDouble(maxPriceStr);
                } catch (NumberFormatException ignored) {
                    selectedMaxPrice = null;
                }
            } else {
                selectedMaxPrice = null;
            }

            dialog.dismiss();
            loadProperties();
        });

        dialog.show();
    }

    private void loadProperties() {
        String searchQuery = binding.etSearch.getText() != null ? binding.etSearch.getText().toString().trim() : "";

        repository.searchProperties(searchQuery, selectedType, selectedListing, null, selectedMaxPrice,
                new PropertyRepository.RepositoryCallback<List<Property>>() {
                    @Override
                    public void onSuccess(List<Property> result) {
                        if (result == null || result.isEmpty()) {
                            binding.rvProperties.setVisibility(View.GONE);
                            binding.layoutEmpty.setVisibility(View.VISIBLE);
                        } else {
                            binding.rvProperties.setVisibility(View.VISIBLE);
                            binding.layoutEmpty.setVisibility(View.GONE);
                            adapter.updateData(result);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(PropertyListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onPropertyClick(Property property) {
        Intent intent = new Intent(PropertyListActivity.this, PropertyDetailActivity.class);
        intent.putExtra("property", property);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Property property, boolean isFavorite) {
        sessionManager.toggleFavorite(property.getId());
        Toast.makeText(this, isFavorite ? "Added to Saved Favorites" : "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProperties();
    }
}
