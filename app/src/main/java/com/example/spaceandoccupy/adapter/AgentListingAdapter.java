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
import com.example.spaceandoccupy.databinding.ItemAgentListingCardBinding;
import com.example.spaceandoccupy.model.Property;

import java.util.ArrayList;
import java.util.List;

public class AgentListingAdapter extends RecyclerView.Adapter<AgentListingAdapter.ListingViewHolder> {

    public interface OnStatusChangeListener {
        void onChangeStatus(Property property);
    }

    private final Context context;
    private final List<Property> properties = new ArrayList<>();
    private final OnStatusChangeListener listener;

    public AgentListingAdapter(Context context, OnStatusChangeListener listener) {
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
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAgentListingCardBinding binding = ItemAgentListingCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ListingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        holder.bind(properties.get(position));
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    class ListingViewHolder extends RecyclerView.ViewHolder {
        private final ItemAgentListingCardBinding binding;

        public ListingViewHolder(@NonNull ItemAgentListingCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Property property) {
            binding.tvTitle.setText(property.getTitle());
            binding.tvPriceLocation.setText(property.getFormattedPrice() + " • " + property.getLocation());

            String status = property.isHiddenFromPublicFeed()
                    ? "HIDDEN"
                    : (property.getStatus() != null ? property.getStatus().toUpperCase() : "AVAILABLE");
            binding.tvStatusBadge.setText(status);

            int statusBgColor;
            if ("HIDDEN".equals(status) || "SOLD".equals(status)) {
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
        }
    }
}
