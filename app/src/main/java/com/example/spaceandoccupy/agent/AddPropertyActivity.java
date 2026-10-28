package com.example.spaceandoccupy.agent;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.databinding.ActivityAddPropertyBinding;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;

import java.util.Arrays;

public class AddPropertyActivity extends AppCompatActivity {

    private ActivityAddPropertyBinding binding;
    private PropertyRepository repository;
    private SessionManager sessionManager;

    private String selectedType = "House";
    private String selectedListing = "For Sale";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAddPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        setupTypeAndListingSelectors();

        binding.btnSubmitProperty.setOnClickListener(v -> performPropertySubmission());
    }

    private void setupTypeAndListingSelectors() {
        binding.cgPropertyType.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_apartment)) {
                selectedType = "Apartment";
            } else if (checkedIds.contains(R.id.chip_commercial)) {
                selectedType = "Commercial";
            } else if (checkedIds.contains(R.id.chip_land)) {
                selectedType = "Land";
            } else {
                selectedType = "House";
            }
        });

        binding.cgListingType.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_rent)) {
                selectedListing = "For Rent";
            } else if (checkedIds.contains(R.id.chip_lease)) {
                selectedListing = "Lease";
            } else {
                selectedListing = "For Sale";
            }
        });
    }

    private void performPropertySubmission() {
        String title = binding.etTitle.getText() != null ? binding.etTitle.getText().toString().trim() : "";
        String priceStr = binding.etPrice.getText() != null ? binding.etPrice.getText().toString().trim() : "";
        String priceUnit = binding.etPriceUnit.getText() != null ? binding.etPriceUnit.getText().toString().trim() : "";
        String location = binding.etLocation.getText() != null ? binding.etLocation.getText().toString().trim() : "";
        String address = binding.etAddress.getText() != null ? binding.etAddress.getText().toString().trim() : "";
        String bedsStr = binding.etBeds.getText() != null ? binding.etBeds.getText().toString().trim() : "0";
        String bathsStr = binding.etBaths.getText() != null ? binding.etBaths.getText().toString().trim() : "0";
        String areaStr = binding.etArea.getText() != null ? binding.etArea.getText().toString().trim() : "0";
        String imageUrl = binding.etImageUrl.getText() != null ? binding.etImageUrl.getText().toString().trim() : "";
        String description = binding.etDescription.getText() != null ? binding.etDescription.getText().toString().trim() : "";

        if (TextUtils.isEmpty(title)) {
            binding.tilTitle.setError("Title is required");
            return;
        } else {
            binding.tilTitle.setError(null);
        }

        if (TextUtils.isEmpty(priceStr)) {
            binding.tilPrice.setError("Price is required");
            return;
        } else {
            binding.tilPrice.setError(null);
        }

        if (TextUtils.isEmpty(location)) {
            binding.tilLocation.setError("Location is required");
            return;
        } else {
            binding.tilLocation.setError(null);
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException ignored) {}

        int beds = 0;
        try {
            beds = Integer.parseInt(bedsStr);
        } catch (NumberFormatException ignored) {}

        int baths = 0;
        try {
            baths = Integer.parseInt(bathsStr);
        } catch (NumberFormatException ignored) {}

        double area = 0;
        try {
            area = Double.parseDouble(areaStr);
        } catch (NumberFormatException ignored) {}

        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1200";
        }

        if (TextUtils.isEmpty(priceUnit)) {
            priceUnit = "For Sale".equals(selectedListing) ? "total" : "/ month";
        }

        User agent = sessionManager.getCurrentUser();

        Property property = new Property(
                null,
                title,
                description.isEmpty() ? "Modern " + selectedType + " located in " + location + "." : description,
                selectedType,
                selectedListing,
                price,
                priceUnit,
                location,
                address.isEmpty() ? location : address,
                beds,
                baths,
                area,
                "AVAILABLE",
                true,
                imageUrl,
                Arrays.asList("Parking", "Security", "High-Speed Internet", "Garden"),
                agent != null ? agent.getId() : "usr_agent_1",
                agent != null ? agent.getName() : "Marcus Vance",
                agent != null ? agent.getPhone() : "+1 (555) 876-5432",
                agent != null ? agent.getEmail() : "agent@spaceandoccupy.com",
                null
        );

        binding.btnSubmitProperty.setEnabled(false);

        repository.createProperty(property, new PropertyRepository.RepositoryCallback<Property>() {
            @Override
            public void onSuccess(Property result) {
                binding.btnSubmitProperty.setEnabled(true);
                Toast.makeText(AddPropertyActivity.this, "Property listing created successfully!", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnSubmitProperty.setEnabled(true);
                Toast.makeText(AddPropertyActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
