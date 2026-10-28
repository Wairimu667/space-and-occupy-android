package com.example.spaceandoccupy.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.adapter.PendingAgentAdapter;
import com.example.spaceandoccupy.databinding.ActivityAgentVerificationBinding;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;

import java.util.List;

public class AgentVerificationActivity extends AppCompatActivity implements PendingAgentAdapter.OnAgentActionListener {

    private ActivityAgentVerificationBinding binding;
    private PropertyRepository repository;
    private PendingAgentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAgentVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new PendingAgentAdapter(this);
        binding.rvPendingAgents.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPendingAgents.setAdapter(adapter);

        loadPendingAgents();
    }

    private void loadPendingAgents() {
        repository.getPendingAgents(new PropertyRepository.RepositoryCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (result == null || result.isEmpty()) {
                    binding.rvPendingAgents.setVisibility(View.GONE);
                    binding.layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.rvPendingAgents.setVisibility(View.VISIBLE);
                    binding.layoutEmpty.setVisibility(View.GONE);
                    adapter.updateData(result);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AgentVerificationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onApprove(User agent) {
        repository.verifyAgent(agent.getId(), true, new PropertyRepository.RepositoryCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Toast.makeText(AgentVerificationActivity.this, "Agent " + agent.getName() + " Approved!", Toast.LENGTH_SHORT).show();
                loadPendingAgents();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AgentVerificationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onReject(User agent) {
        repository.verifyAgent(agent.getId(), false, new PropertyRepository.RepositoryCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Toast.makeText(AgentVerificationActivity.this, "Agent " + agent.getName() + " Application Rejected", Toast.LENGTH_SHORT).show();
                loadPendingAgents();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AgentVerificationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
