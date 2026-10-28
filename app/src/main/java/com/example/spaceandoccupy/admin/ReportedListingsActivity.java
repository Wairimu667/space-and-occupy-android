package com.example.spaceandoccupy.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.adapter.ListingReportAdapter;
import com.example.spaceandoccupy.databinding.ActivityReportedListingsBinding;
import com.example.spaceandoccupy.model.ListingReport;
import com.example.spaceandoccupy.repository.PropertyRepository;

import java.util.List;

public class ReportedListingsActivity extends AppCompatActivity implements ListingReportAdapter.OnReportActionListener {

    private ActivityReportedListingsBinding binding;
    private PropertyRepository repository;
    private ListingReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityReportedListingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        adapter = new ListingReportAdapter(this);
        binding.rvReports.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReports.setAdapter(adapter);
        loadReports();
    }

    private void loadReports() {
        repository.getListingReports(new PropertyRepository.RepositoryCallback<List<ListingReport>>() {
            @Override
            public void onSuccess(List<ListingReport> result) {
                adapter.updateData(result);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ReportedListingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMarkReviewed(ListingReport report) {
        repository.updateReportStatus(report.getId(), "REVIEWED", new PropertyRepository.RepositoryCallback<ListingReport>() {
            @Override
            public void onSuccess(ListingReport result) {
                Toast.makeText(ReportedListingsActivity.this, "Report marked reviewed", Toast.LENGTH_SHORT).show();
                loadReports();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ReportedListingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
