package com.example.spaceandoccupy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceandoccupy.databinding.ItemInquiryCardBinding;
import com.example.spaceandoccupy.model.Inquiry;

import java.util.ArrayList;
import java.util.List;

public class InquiryAdapter extends RecyclerView.Adapter<InquiryAdapter.InquiryViewHolder> {

    private final List<Inquiry> inquiries = new ArrayList<>();

    public void updateData(List<Inquiry> newInquiries) {
        this.inquiries.clear();
        if (newInquiries != null) {
            this.inquiries.addAll(newInquiries);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInquiryCardBinding binding = ItemInquiryCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new InquiryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InquiryViewHolder holder, int position) {
        holder.bind(inquiries.get(position));
    }

    @Override
    public int getItemCount() {
        return inquiries.size();
    }

    static class InquiryViewHolder extends RecyclerView.ViewHolder {
        private final ItemInquiryCardBinding binding;

        public InquiryViewHolder(@NonNull ItemInquiryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Inquiry inquiry) {
            binding.tvPropertyTitle.setText(inquiry.getPropertyTitle());
            binding.tvMessage.setText(inquiry.getMessage());
            binding.tvStatus.setText(inquiry.getStatus());
            binding.tvDate.setText("Submitted: " + (inquiry.getCreatedDate() != null ? inquiry.getCreatedDate() : "Recently"));
        }
    }
}
