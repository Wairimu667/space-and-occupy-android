package com.example.spaceandoccupy.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.client.PropertyDetailActivity;
import com.example.spaceandoccupy.client.ReportListingHelper;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TikTokPropertyAdapter extends RecyclerView.Adapter<TikTokPropertyAdapter.ViewHolder> {

    private final Context context;
    private final List<Property> properties = new ArrayList<>();
    private final PropertyRepository repository;

    public TikTokPropertyAdapter(Context context) {
        this.context = context;
        this.repository = PropertyRepository.getInstance(context);
    }

    public void setProperties(List<Property> newProperties) {
        this.properties.clear();
        if (newProperties != null) {
            this.properties.addAll(newProperties);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tiktok_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = properties.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvPrice.setText(property.getFormattedPrice());
        holder.tvLocation.setText(property.getPublicLocationLabel());
        holder.tvDesc.setText(property.getDescription());

        // Listing Badge (with Anti-Fraud Freshly Captured badge if recorded in-app)
        String freshBadge = property.isFreshlyCaptured() ? "📷 FRESHLY CAPTURED • " : "";
        String badgeText = String.format("%s%s • %s",
                freshBadge,
                property.getListingType() != null ? property.getListingType().toUpperCase() : "FOR SALE",
                property.getPropertyType() != null ? property.getPropertyType().toUpperCase() : "PROPERTY");
        holder.tvBadge.setText(badgeText);

        // Specs summary
        StringBuilder specs = new StringBuilder();
        if (property.getBedrooms() > 0) specs.append(property.getBedrooms()).append(" Beds • ");
        if (property.getBathrooms() > 0) specs.append(property.getBathrooms()).append(" Baths • ");
        if (property.getAreaSqFt() > 0) specs.append(String.format("%,.0f sq ft", property.getAreaSqFt()));
        if (specs.length() == 0 && property.getAmenities() != null && !property.getAmenities().isEmpty()) {
            specs.append(String.join(" • ", property.getAmenities()));
        }
        holder.tvSpecs.setText(specs.toString().isEmpty() ? "Prime Space & Occupy Listing" : specs.toString());

        // Agent Name
        if (property.getAgentName() != null && !property.getAgentName().isEmpty()) {
            holder.tvAgentName.setText(property.getAgentName());
        } else {
            holder.tvAgentName.setText("Space & Occupy");
        }

        // Load Background Image
        String imageUrl = property.getDisplayImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .into(holder.imgPropertyBg);
        } else {
            holder.imgPropertyBg.setImageResource(R.drawable.placeholder);
        }

        // Favorite State
        boolean isFav = repository.getSessionManager().isFavorite(property.getId());
        holder.btnFavorite.setImageResource(isFav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);

        // Click ENQUIRE -> Open Enquiry Bottom Sheet
        holder.btnEnquire.setOnClickListener(v -> showEnquiryBottomSheet(property));

        // Click Favorite
        holder.btnFavorite.setOnClickListener(v -> {
            boolean currentlyFav = repository.getSessionManager().isFavorite(property.getId());
            repository.getSessionManager().toggleFavorite(property.getId());
            property.setFavorite(!currentlyFav);
            holder.btnFavorite.setImageResource(!currentlyFav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            Toast.makeText(context, !currentlyFav ? "Saved to Favorites!" : "Removed from Favorites", Toast.LENGTH_SHORT).show();
        });

        // Click Share
        holder.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = String.format("Check out %s (%s) listed on Space & Occupy!\nPrice: %s\nLocation: %s\nhttps://wairimu667.github.io/space-and-occupy",
                    property.getTitle(), property.getListingType(), property.getFormattedPrice(), property.getLocation());
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "Share Property Listing"));
        });

        // Click Details
        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, PropertyDetailActivity.class);
            intent.putExtra("property", property);
            context.startActivity(intent);
        });

        holder.btnFlag.setOnClickListener(v -> ReportListingHelper.show(context, property));
    }

    private void showEnquiryBottomSheet(Property property) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.dialog_enquiry_options, null);
        dialog.setContentView(sheetView);

        TextView tvSheetTitle = sheetView.findViewById(R.id.tv_sheet_property_title);
        TextView tvSheetPrice = sheetView.findViewById(R.id.tv_sheet_property_price);
        TextView tvSheetAgent = sheetView.findViewById(R.id.tv_sheet_property_agent);
        ImageView imgSheet = sheetView.findViewById(R.id.img_sheet_property);

        tvSheetTitle.setText(property.getTitle());
        tvSheetPrice.setText(property.getFormattedPrice());
        String agentPhone = property.getAgentPhone() != null && !property.getAgentPhone().isEmpty() ? property.getAgentPhone() : "+254787900284";
        tvSheetAgent.setText("Listing Agent: " + (property.getAgentName() != null ? property.getAgentName() : "Space & Occupy") + " (" + agentPhone + ")");

        if (property.getImageUrl() != null && !property.getImageUrl().isEmpty()) {
            Glide.with(context).load(property.getImageUrl()).centerCrop().into(imgSheet);
        }

        MaterialButton btnWhatsApp = sheetView.findViewById(R.id.btn_enquire_whatsapp);
        MaterialButton btnCall = sheetView.findViewById(R.id.btn_enquire_call);
        MaterialButton btnInApp = sheetView.findViewById(R.id.btn_enquire_in_app);

        // WhatsApp direct action
        btnWhatsApp.setOnClickListener(v -> {
            dialog.dismiss();
            String cleanPhone = agentPhone.replaceAll("[^0-9+]", "");
            if (cleanPhone.startsWith("0")) {
                cleanPhone = "+254" + cleanPhone.substring(1);
            }
            String message = String.format("Hi Space & Occupy! I am interested in inquiring about: %s located at %s (%s). Is it currently available?",
                    property.getTitle(), property.getLocation(), property.getFormattedPrice());
            try {
                String url = "https://api.whatsapp.com/send?phone=" + cleanPhone + "&text=" + Uri.encode(message);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "WhatsApp not installed. Calling agent...", Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + cleanPhone));
                context.startActivity(callIntent);
            }
        });

        // Phone call direct action
        btnCall.setOnClickListener(v -> {
            dialog.dismiss();
            String cleanPhone = agentPhone.replaceAll("[^0-9+]", "");
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + cleanPhone));
            context.startActivity(callIntent);
        });

        // In-App Detail / Visit
        btnInApp.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(context, PropertyDetailActivity.class);
            intent.putExtra("property", property);
            context.startActivity(intent);
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPropertyBg, imgAgentAvatar, btnFavorite, btnShare, btnDetails, btnFlag;
        View btnEnquire;
        TextView tvBadge, tvAgentName, tvTitle, tvPrice, tvLocation, tvDesc, tvSpecs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPropertyBg = itemView.findViewById(R.id.img_property_bg);
            imgAgentAvatar = itemView.findViewById(R.id.img_agent_avatar);
            btnEnquire = itemView.findViewById(R.id.btn_action_enquire);
            btnFavorite = itemView.findViewById(R.id.btn_action_favorite);
            btnShare = itemView.findViewById(R.id.btn_action_share);
            btnDetails = itemView.findViewById(R.id.btn_action_details);
            btnFlag = itemView.findViewById(R.id.btn_action_flag);

            tvBadge = itemView.findViewById(R.id.tv_listing_badge);
            tvAgentName = itemView.findViewById(R.id.tv_agent_name);
            tvTitle = itemView.findViewById(R.id.tv_property_title);
            tvPrice = itemView.findViewById(R.id.tv_property_price);
            tvLocation = itemView.findViewById(R.id.tv_property_location);
            tvDesc = itemView.findViewById(R.id.tv_property_desc);
            tvSpecs = itemView.findViewById(R.id.tv_property_specs);
        }
    }
}
