package com.example.spaceandoccupy.client;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.databinding.ActivityPropertyDetailBinding;
import com.example.spaceandoccupy.databinding.DialogScheduleVisitBinding;
import com.example.spaceandoccupy.databinding.DialogSubmitInquiryBinding;
import com.example.spaceandoccupy.model.Inquiry;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.SiteVisit;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;

import java.util.Calendar;

public class PropertyDetailActivity extends AppCompatActivity {

    private ActivityPropertyDetailBinding binding;
    private PropertyRepository repository;
    private SessionManager sessionManager;
    private Property property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityPropertyDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        if (getIntent() != null && getIntent().hasExtra("property")) {
            property = (Property) getIntent().getSerializableExtra("property");
        }

        if (property == null) {
            Toast.makeText(this, "Property details unavailable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        populatePropertyDetails();
        setupInteractiveActions();
    }

    private void populatePropertyDetails() {
        binding.tvTitle.setText(property.getTitle());
        binding.tvPrice.setText(property.getFormattedPrice() + (property.getPriceUnit() != null ? " " + property.getPriceUnit() : ""));
        binding.tvLocation.setText(property.getPublicLocationLabel());
        binding.tvListingType.setText(property.getListingType() != null ? property.getListingType().toUpperCase() : "");

        binding.tvBeds.setText(String.valueOf(property.getBedrooms()));
        binding.tvBaths.setText(String.valueOf(property.getBathrooms()));
        binding.tvArea.setText(String.format("%,.0f", property.getAreaSqFt()));

        binding.tvDescription.setText(property.getDescription());

        // Status Badge
        String status = property.getStatus() != null ? property.getStatus().toUpperCase() : "AVAILABLE";
        binding.tvStatusBadge.setText(status);
        int statusBgColor;
        if ("SOLD".equals(status)) {
            statusBgColor = ContextCompat.getColor(this, R.color.status_sold);
        } else if ("RENTED".equals(status)) {
            statusBgColor = ContextCompat.getColor(this, R.color.status_rented);
        } else if ("LEASED".equals(status)) {
            statusBgColor = ContextCompat.getColor(this, R.color.status_leased);
        } else {
            statusBgColor = ContextCompat.getColor(this, R.color.status_available);
        }
        binding.tvStatusBadge.setBackgroundTintList(ColorStateList.valueOf(statusBgColor));

        // Freshly Captured Badge
        if (property.isFreshlyCaptured()) {
            binding.tvFreshlyCapturedBadge.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.tvFreshlyCapturedBadge.setVisibility(android.view.View.GONE);
        }

        // Agent Info with [✓] Verified Badge
        String agentName = property.getAgentName() != null ? property.getAgentName() : "Space & Occupy Agent";
        binding.tvAgentName.setText(agentName + " [✓] Verified Agent");
        binding.tvAgentPhone.setText(property.getAgentPhone() != null ? property.getAgentPhone() : "+254 787 900284");

        // Favorite State
        updateFavoriteFabState(sessionManager.isFavorite(property.getId()));

        // Image
        String imageUrl = property.getDisplayImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .into(binding.imgPropertyMain);
        }

        // Amenities Chips
        binding.cgAmenities.removeAllViews();
        if (property.getAmenities() != null) {
            for (String amenity : property.getAmenities()) {
                Chip chip = new Chip(this);
                chip.setText(amenity);
                chip.setChipBackgroundColorResource(R.color.light_cream);
                chip.setChipStrokeColorResource(R.color.border_color);
                chip.setTextColor(ContextCompat.getColor(this, R.color.deep_brown));
                binding.cgAmenities.addView(chip);
            }
        }
    }

    private void setupInteractiveActions() {
        binding.fabFavorite.setOnClickListener(v -> {
            boolean isFav = !sessionManager.isFavorite(property.getId());
            sessionManager.toggleFavorite(property.getId());
            property.setFavorite(isFav);
            updateFavoriteFabState(isFav);
            Toast.makeText(this, isFav ? "Saved to Favorites" : "Removed from Favorites", Toast.LENGTH_SHORT).show();
        });

        binding.btnRouteToProperty.setOnClickListener(v -> {
            String locationQuery = property.getPublicLocationLabel();
            android.net.Uri gmmIntentUri = android.net.Uri.parse("geo:0,0?q=" + android.net.Uri.encode(locationQuery));
            android.content.Intent mapIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(mapIntent);
            } catch (Exception e) {
                // Fallback to web maps browser
                android.content.Intent browserIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("https://www.google.com/maps/search/?api=1&query=" + android.net.Uri.encode(locationQuery)));
                startActivity(browserIntent);
            }
        });

        binding.btnShareMapLink.setOnClickListener(v -> {
            String locationQuery = property.getPublicLocationLabel();
            String mapDeepLink = "https://maps.google.com/?q=" + android.net.Uri.encode(locationQuery);
            android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "View map location for " + property.getTitle() + ":\n" + mapDeepLink);
            startActivity(android.content.Intent.createChooser(shareIntent, "Share Map Deep Link"));
        });

        binding.btnSubmitInquiry.setOnClickListener(v -> showInquiryDialog());
        binding.btnScheduleVisit.setOnClickListener(v -> showScheduleVisitDialog());
        binding.btnFlagListing.setOnClickListener(v -> ReportListingHelper.show(this, property));
    }

    private void updateFavoriteFabState(boolean isFavorite) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            binding.fabFavorite.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_brown)));
        } else {
            binding.fabFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            binding.fabFavorite.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
        }
    }

    private void showInquiryDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        DialogSubmitInquiryBinding sheetBinding = DialogSubmitInquiryBinding.inflate(getLayoutInflater());
        dialog.setContentView(sheetBinding.getRoot());

        sheetBinding.tvPropertyTitle.setText(property.getTitle());

        sheetBinding.btnSendInquiry.setOnClickListener(v -> {
            String msg = sheetBinding.etMessage.getText() != null ? sheetBinding.etMessage.getText().toString().trim() : "";
            if (TextUtils.isEmpty(msg)) {
                sheetBinding.tilMessage.setError("Please enter your message");
                return;
            }

            User user = sessionManager.getCurrentUser();
            Inquiry inquiry = new Inquiry(
                    null,
                    property.getId(),
                    property.getTitle(),
                    user != null ? user.getId() : "usr_client_1",
                    user != null ? user.getName() : "Sarah Johnson",
                    user != null ? user.getEmail() : "client@spaceandoccupy.com",
                    user != null ? user.getPhone() : "+1 (555) 234-5678",
                    msg,
                    property.getAgentId(),
                    "PENDING",
                    null
            );

            repository.createInquiry(inquiry, new PropertyRepository.RepositoryCallback<Inquiry>() {
                @Override
                public void onSuccess(Inquiry result) {
                    dialog.dismiss();
                    Toast.makeText(PropertyDetailActivity.this, "Inquiry submitted successfully!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(PropertyDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void showScheduleVisitDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        DialogScheduleVisitBinding sheetBinding = DialogScheduleVisitBinding.inflate(getLayoutInflater());
        dialog.setContentView(sheetBinding.getRoot());

        sheetBinding.tvPropertyTitle.setText(property.getTitle());

        // Date Picker
        sheetBinding.etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> sheetBinding.etDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        // Time Picker
        sheetBinding.etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            TimePickerDialog tpd = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> sheetBinding.etTime.setText(String.format("%02d:%02d %s", (hourOfDay % 12 == 0 ? 12 : hourOfDay % 12), minute, hourOfDay >= 12 ? "PM" : "AM")),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            tpd.show();
        });

        sheetBinding.btnConfirmVisit.setOnClickListener(v -> {
            String date = sheetBinding.etDate.getText() != null ? sheetBinding.etDate.getText().toString().trim() : "";
            String time = sheetBinding.etTime.getText() != null ? sheetBinding.etTime.getText().toString().trim() : "";

            if (TextUtils.isEmpty(date)) {
                sheetBinding.tilDate.setError("Please select date");
                return;
            } else {
                sheetBinding.tilDate.setError(null);
            }

            if (TextUtils.isEmpty(time)) {
                sheetBinding.tilTime.setError("Please select time");
                return;
            } else {
                sheetBinding.tilTime.setError(null);
            }

            User user = sessionManager.getCurrentUser();
            SiteVisit visit = new SiteVisit(
                    null,
                    property.getId(),
                    property.getTitle(),
                    property.getAddress(),
                    user != null ? user.getId() : "usr_client_1",
                    user != null ? user.getName() : "Sarah Johnson",
                    user != null ? user.getPhone() : "+1 (555) 234-5678",
                    date,
                    time,
                    property.getAgentId(),
                    "PENDING",
                    null
            );

            repository.createSiteVisit(visit, new PropertyRepository.RepositoryCallback<SiteVisit>() {
                @Override
                public void onSuccess(SiteVisit result) {
                    dialog.dismiss();
                    Toast.makeText(PropertyDetailActivity.this, "Site Visit scheduled for " + date + " at " + time, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(PropertyDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
}
