package com.example.spaceandoccupy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceandoccupy.databinding.ItemListingReportCardBinding;
import com.example.spaceandoccupy.model.ListingReport;

import java.util.ArrayList;
import java.util.List;

public class ListingReportAdapter extends RecyclerView.Adapter<ListingReportAdapter.ReportViewHolder> {

    public interface OnReportActionListener {
        void onMarkReviewed(ListingReport report);
    }

    private final List<ListingReport> reports = new ArrayList<>();
    private final OnReportActionListener listener;

    public ListingReportAdapter(OnReportActionListener listener) {
        this.listener = listener;
    }

    public void updateData(List<ListingReport> newReports) {
        reports.clear();
        if (newReports != null) {
            reports.addAll(newReports);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListingReportCardBinding binding = ItemListingReportCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ReportViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.bind(reports.get(position));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        private final ItemListingReportCardBinding binding;

        ReportViewHolder(ItemListingReportCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ListingReport report) {
            binding.tvReportTitle.setText(report.getPropertyTitle());
            binding.tvReportReason.setText(report.getReason() + " • " + report.getStatus());
            String details = report.getDetails() != null && !report.getDetails().isEmpty()
                    ? " — " + report.getDetails() : "";
            binding.tvReportMeta.setText("From " + report.getReporterName() + details);
            binding.btnMarkReviewed.setEnabled(!"REVIEWED".equalsIgnoreCase(report.getStatus()));
            binding.btnMarkReviewed.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMarkReviewed(report);
                }
            });
        }
    }
}
