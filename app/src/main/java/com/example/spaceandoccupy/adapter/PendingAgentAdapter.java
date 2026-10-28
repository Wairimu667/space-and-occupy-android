package com.example.spaceandoccupy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceandoccupy.databinding.ItemPendingAgentBinding;
import com.example.spaceandoccupy.model.User;

import java.util.ArrayList;
import java.util.List;

public class PendingAgentAdapter extends RecyclerView.Adapter<PendingAgentAdapter.AgentViewHolder> {

    public interface OnAgentActionListener {
        void onApprove(User agent);
        void onReject(User agent);
    }

    private final List<User> agents = new ArrayList<>();
    private final OnAgentActionListener listener;

    public PendingAgentAdapter(OnAgentActionListener listener) {
        this.listener = listener;
    }

    public void updateData(List<User> newAgents) {
        this.agents.clear();
        if (newAgents != null) {
            this.agents.addAll(newAgents);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPendingAgentBinding binding = ItemPendingAgentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AgentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        holder.bind(agents.get(position));
    }

    @Override
    public int getItemCount() {
        return agents.size();
    }

    class AgentViewHolder extends RecyclerView.ViewHolder {
        private final ItemPendingAgentBinding binding;

        public AgentViewHolder(@NonNull ItemPendingAgentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User agent) {
            binding.tvAgentName.setText(agent.getName());
            binding.tvAgentEmail.setText(agent.getEmail());
            String agency = "Agency: " + (agent.getAgencyName() != null ? agent.getAgencyName() : "N/A")
                    + " • License #" + (agent.getLicenseNumber() != null ? agent.getLicenseNumber() : "Pending");
            binding.tvAgencyLicense.setText(agency);

            binding.btnApprove.setOnClickListener(v -> {
                if (listener != null) listener.onApprove(agent);
            });

            binding.btnReject.setOnClickListener(v -> {
                if (listener != null) listener.onReject(agent);
            });
        }
    }
}
