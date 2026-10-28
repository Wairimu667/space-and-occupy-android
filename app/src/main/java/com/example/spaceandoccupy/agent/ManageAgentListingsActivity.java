package com.example.spaceandoccupy.agent;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.adapter.AgentListingAdapter;
import com.example.spaceandoccupy.databinding.ActivityManageAgentListingsBinding;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ManageAgentListingsActivity extends AppCompatActivity implements AgentListingAdapter.OnStatusChangeListener {

    private ActivityManageAgentListingsBinding binding;
    private PropertyRepository repository;
    private SessionManager sessionManager;
    private AgentListingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityManageAgentListingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new AgentListingAdapter(this, this);
        binding.rvListings.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListings.setAdapter(adapter);

        loadAgentProperties();
    }

    private void loadAgentProperties() {
        User currentAgent = sessionManager.getCurrentUser();
        String agentId = currentAgent != null ? currentAgent.getId() : "usr_agent_1";

        repository.getListingsForAgent(agentId, new PropertyRepository.RepositoryCallback<List<Property>>() {
            @Override
            public void onSuccess(List<Property> result) {
                List<Property> agentProps = result != null ? result : new ArrayList<>();

                if (agentProps.isEmpty()) {
                    binding.rvListings.setVisibility(View.GONE);
                    binding.layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.rvListings.setVisibility(View.VISIBLE);
                    binding.layoutEmpty.setVisibility(View.GONE);
                    adapter.updateData(agentProps);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ManageAgentListingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onChangeStatus(Property property) {
        String[] statusOptions = {"AVAILABLE", "SOLD", "RENTED", "LEASED"};
        new AlertDialog.Builder(this)
                .setTitle("Update Listing Status for: " + property.getTitle())
                .setItems(statusOptions, (dialog, which) -> {
                    String selectedStatus = statusOptions[which];
                    repository.updatePropertyStatus(property.getId(), selectedStatus, new PropertyRepository.RepositoryCallback<Property>() {
                        @Override
                        public void onSuccess(Property result) {
                            Toast.makeText(ManageAgentListingsActivity.this, "Status updated to " + selectedStatus, Toast.LENGTH_SHORT).show();
                            loadAgentProperties();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(ManageAgentListingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAgentProperties();
    }
}
