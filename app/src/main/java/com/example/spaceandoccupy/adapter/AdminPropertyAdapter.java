package com.example.spaceandoccupy.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.databinding.ItemAdminPropertyCardBinding;
import com.example.spaceandoccupy.model.Property;

import java.util.ArrayList;
import java.util.List;

public class AdminPropertyAdapter extends RecyclerView.Adapter<AdminPropertyAdapter.AdminPropertyViewHolder> {

    public interface OnAdminPropertyActionListener {
        void onChangeStatus(Property property);
        void onDelete(Property property);
    }

    private final Context context;
    private final List<Property> properties = new ArrayList<>();
    private final OnAdminPropertyActionListener listener;

    public AdminPropertyAdapter(Context context, OnAdminPropertyActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateData(List<Property> newProperties) {
        this.properties.clear();
        if (newProperties != null) {
            this.properties.addAll(newProperties);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminPropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminPropertyCardBinding binding = ItemAdminPropertyCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AdminPropertyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPropertyViewHolder holder, int position) {
        holder.bind(properties.get(position));
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    class AdminPropertyViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminPropertyCardBinding binding;

        public AdminPropertyViewHolder(@NonNull ItemAdminPropertyCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Property property) {
            binding.tvTitle.setText(property.getTitle());
            String agentInfo = "Agent: " + (property.getAgentName() != null ? property.getAgentName() : "System") + " • " + property.getFormattedPrice();
            binding.tvAgentInfo.setText(agentInfo);

            String status = property.getStatus() != null ? property.getStatus().toUpperCase() : "AVAILABLE";
            binding.tvStatusBadge.setText(status);

            int statusBgColor;
            if ("SOLD".equals(status)) {
                statusBgColor = ContextCompat.getColor(context, R.color.status_sold);
            } else if ("RENTED".equals(status)) {
                statusBgColor = ContextCompat.getColor(context, R.color.status_rented);
            } else if ("LEASED".equals(status)) {
                statusBgColor = ContextCompat.getColor(context, R.color.status_leased);
            } else {
                statusBgColor = ContextCompat.getColor(context, R.color.status_available);
            }
            binding.tvStatusBadge.setBackgroundTintList(ColorStateList.valueOf(statusBgColor));

            if (property.getImageUrl() != null && !property.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(property.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .into(binding.imgProperty);
            } else {
                binding.imgProperty.setImageResource(R.drawable.placeholder);
            }

            binding.btnChangeStatus.setOnClickListener(v -> {
                if (listener != null) listener.onChangeStatus(property);
            });

            binding.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(property);
            });
        }
    }
}
