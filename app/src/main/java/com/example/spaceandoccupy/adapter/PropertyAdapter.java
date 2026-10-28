package com.example.spaceandoccupy.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.databinding.ItemPropertyCardBinding;
import com.example.spaceandoccupy.model.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    public interface OnPropertyClickListener {
        void onPropertyClick(Property property);
        void onFavoriteClick(Property property, boolean isFavorite);
    }

    private final Context context;
    private final List<Property> properties;
    private final OnPropertyClickListener listener;

    public PropertyAdapter(Context context, List<Property> properties, OnPropertyClickListener listener) {
        this.context = context;
        this.properties = properties != null ? properties : new ArrayList<>();
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
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPropertyCardBinding binding = ItemPropertyCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new PropertyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        holder.bind(properties.get(position));
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    class PropertyViewHolder extends RecyclerView.ViewHolder {
        private final ItemPropertyCardBinding binding;

        public PropertyViewHolder(@NonNull ItemPropertyCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Property property) {
            binding.tvTitle.setText(property.getTitle());
            binding.tvLocation.setText(property.getLocation());
            binding.tvPrice.setText(property.getFormattedPrice() + (property.getPriceUnit() != null ? " " + property.getPriceUnit() : ""));
            binding.tvPropertyType.setText(property.getPropertyType());
            binding.tvListingType.setText(property.getListingType() != null ? property.getListingType().toUpperCase() : "");

            binding.tvBeds.setText("🛏 " + property.getBedrooms() + " Beds");
            binding.tvBaths.setText("🛁 " + property.getBathrooms() + " Baths");
            binding.tvArea.setText("📐 " + (int) property.getAreaSqFt() + " sq ft");

            // Status Badge Color
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

            // Favorite Icon
            updateFavoriteState(property.isFavorite());

            binding.btnFavorite.setOnClickListener(v -> {
                boolean newState = !property.isFavorite();
                property.setFavorite(newState);
                updateFavoriteState(newState);
                if (listener != null) {
                    listener.onFavoriteClick(property, newState);
                }
            });

            // Image Glide Loading
            if (property.getImageUrl() != null && !property.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(property.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .centerCrop()
                        .into(binding.imgProperty);
            } else {
                binding.imgProperty.setImageResource(R.drawable.placeholder);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPropertyClick(property);
                }
            });
        }

        private void updateFavoriteState(boolean isFavorite) {
            if (isFavorite) {
                binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                binding.btnFavorite.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_brown)));
            } else {
                binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                binding.btnFavorite.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey)));
            }
        }
    }
}
