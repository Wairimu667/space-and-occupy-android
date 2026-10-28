package com.example.spaceandoccupy.client;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.adapter.InquiryAdapter;
import com.example.spaceandoccupy.adapter.SiteVisitAdapter;
import com.example.spaceandoccupy.databinding.ActivityMyInquiriesVisitsBinding;
import com.example.spaceandoccupy.model.Inquiry;
import com.example.spaceandoccupy.model.SiteVisit;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MyInquiriesAndVisitsActivity extends AppCompatActivity {

    private ActivityMyInquiriesVisitsBinding binding;
    private PropertyRepository repository;
    private SessionManager sessionManager;

    private InquiryAdapter inquiryAdapter;
    private SiteVisitAdapter siteVisitAdapter;
    private int currentTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityMyInquiriesVisitsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        inquiryAdapter = new InquiryAdapter();
        siteVisitAdapter = new SiteVisitAdapter();

        binding.rvItems.setLayoutManager(new LinearLayoutManager(this));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                loadTabContent();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        loadTabContent();
    }

    private void loadTabContent() {
        User user = sessionManager.getCurrentUser();
        String userId = user != null ? user.getId() : "usr_client_1";

        if (currentTabPosition == 0) {
            binding.rvItems.setAdapter(inquiryAdapter);
            repository.getInquiriesForUser(userId, new PropertyRepository.RepositoryCallback<List<Inquiry>>() {
                @Override
                public void onSuccess(List<Inquiry> result) {
                    if (result == null || result.isEmpty()) {
                        binding.rvItems.setVisibility(View.GONE);
                        binding.layoutEmpty.setVisibility(View.VISIBLE);
                        binding.tvEmptyTitle.setText("No Inquiries Sent Yet");
                    } else {
                        binding.rvItems.setVisibility(View.VISIBLE);
                        binding.layoutEmpty.setVisibility(View.GONE);
                        inquiryAdapter.updateData(result);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(MyInquiriesAndVisitsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.rvItems.setAdapter(siteVisitAdapter);
            repository.getSiteVisitsForUser(userId, new PropertyRepository.RepositoryCallback<List<SiteVisit>>() {
                @Override
                public void onSuccess(List<SiteVisit> result) {
                    if (result == null || result.isEmpty()) {
                        binding.rvItems.setVisibility(View.GONE);
                        binding.layoutEmpty.setVisibility(View.VISIBLE);
                        binding.tvEmptyTitle.setText("No Site Visits Scheduled");
                    } else {
                        binding.rvItems.setVisibility(View.VISIBLE);
                        binding.layoutEmpty.setVisibility(View.GONE);
                        siteVisitAdapter.updateData(result);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(MyInquiriesAndVisitsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
