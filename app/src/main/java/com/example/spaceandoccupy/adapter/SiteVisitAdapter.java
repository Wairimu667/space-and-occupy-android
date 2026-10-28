package com.example.spaceandoccupy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceandoccupy.databinding.ItemSiteVisitCardBinding;
import com.example.spaceandoccupy.model.SiteVisit;

import java.util.ArrayList;
import java.util.List;

public class SiteVisitAdapter extends RecyclerView.Adapter<SiteVisitAdapter.SiteVisitViewHolder> {

    private final List<SiteVisit> siteVisits = new ArrayList<>();

    public void updateData(List<SiteVisit> newVisits) {
        this.siteVisits.clear();
        if (newVisits != null) {
            this.siteVisits.addAll(newVisits);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SiteVisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSiteVisitCardBinding binding = ItemSiteVisitCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new SiteVisitViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteVisitViewHolder holder, int position) {
        holder.bind(siteVisits.get(position));
    }

    @Override
    public int getItemCount() {
        return siteVisits.size();
    }

    static class SiteVisitViewHolder extends RecyclerView.ViewHolder {
        private final ItemSiteVisitCardBinding binding;

        public SiteVisitViewHolder(@NonNull ItemSiteVisitCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SiteVisit visit) {
            binding.tvPropertyTitle.setText(visit.getPropertyTitle());
            binding.tvStatus.setText(visit.getStatus());
            binding.tvDateTime.setText("📅 " + visit.getPreferredDate() + " @ " + visit.getPreferredTime());
            binding.tvAddress.setText("📍 " + (visit.getPropertyAddress() != null ? visit.getPropertyAddress() : "Location details"));
        }
    }
}
