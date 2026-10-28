package com.example.spaceandoccupy.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.model.ListingReport;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public final class ReportListingHelper {

    private ReportListingHelper() {}

    public static void show(Context context, Property property) {
        if (property == null) {
            return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_report_listing, null);
        TextInputEditText etDetails = view.findViewById(R.id.et_report_details);
        MaterialButton btnPricing = view.findViewById(R.id.btn_reason_pricing);
        MaterialButton btnMedia = view.findViewById(R.id.btn_reason_media);
        MaterialButton btnExpired = view.findViewById(R.id.btn_reason_expired);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Report listing")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .create();

        View.OnClickListener submit = v -> {
            String reason;
            if (v.getId() == R.id.btn_reason_media) {
                reason = "Fake media";
            } else if (v.getId() == R.id.btn_reason_expired) {
                reason = "Expired listing";
            } else {
                reason = "Inaccurate pricing";
            }
            String details = etDetails.getText() != null ? etDetails.getText().toString().trim() : "";
            submitReport(context, property, reason, details);
            dialog.dismiss();
        };
        btnPricing.setOnClickListener(submit);
        btnMedia.setOnClickListener(submit);
        btnExpired.setOnClickListener(submit);
        dialog.show();
    }

    private static void submitReport(Context context, Property property, String reason, String details) {
        PropertyRepository repository = PropertyRepository.getInstance(context);
        User user = repository.getSessionManager().getCurrentUser();
        ListingReport report = new ListingReport();
        report.setPropertyId(property.getId());
        report.setPropertyTitle(property.getTitle());
        report.setReporterId(user != null ? user.getId() : "guest");
        report.setReporterName(user != null ? user.getName() : "Guest viewer");
        report.setReason(reason);
        report.setDetails(details);
        repository.reportListing(report, new PropertyRepository.RepositoryCallback<ListingReport>() {
            @Override
            public void onSuccess(ListingReport result) {
                Toast.makeText(context, "Report sent to admin moderation.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
