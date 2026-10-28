package com.example.spaceandoccupy.agent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.adapter.InquiryAdapter;
import com.example.spaceandoccupy.databinding.ActivityAgentInquiriesBinding;
import com.example.spaceandoccupy.model.Inquiry;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;

import java.util.List;

public class AgentInquiriesActivity extends AppCompatActivity {

    private ActivityAgentInquiriesBinding binding;
    private PropertyRepository repository;
    private SessionManager sessionManager;
    private InquiryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAgentInquiriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new InquiryAdapter();
        binding.rvRequests.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRequests.setAdapter(adapter);

        loadAgentInquiries();
    }

    private void loadAgentInquiries() {
        User agent = sessionManager.getCurrentUser();
        String userId = agent != null ? agent.getId() : "usr_client_1";

        repository.getInquiriesForUser(userId, new PropertyRepository.RepositoryCallback<List<Inquiry>>() {
            @Override
            public void onSuccess(List<Inquiry> result) {
                adapter.updateData(result);
            }

            @Override
            public void onError(String errorMessage) {}
        });
    }
}
