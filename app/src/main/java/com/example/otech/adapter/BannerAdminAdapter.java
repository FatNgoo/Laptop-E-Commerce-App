package com.example.otech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.model.Banner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class BannerAdminAdapter extends RecyclerView.Adapter<BannerAdminAdapter.BannerViewHolder> {

    private Context context;
    private ArrayList<Banner> banners;
    private ArrayList<Banner> bannersFiltered;
    private OnBannerActionListener listener;

    public interface OnBannerActionListener {
        void onEditClick(Banner banner);
        void onDeleteClick(Banner banner);
    }

    public BannerAdminAdapter(Context context, ArrayList<Banner> banners, OnBannerActionListener listener) {
        this.context = context;
        this.banners = banners;
        this.bannersFiltered = new ArrayList<>(banners);
        this.listener = listener;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner_admin, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = bannersFiltered.get(position);
        holder.bind(banner);
    }

    @Override
    public int getItemCount() {
        return bannersFiltered.size();
    }

    public void filter(String query) {
        bannersFiltered.clear();
        
        if (query == null || query.trim().isEmpty()) {
            bannersFiltered.addAll(banners);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (Banner banner : banners) {
                if (banner.getTitle().toLowerCase().contains(lowerQuery) ||
                    banner.getLink().toLowerCase().contains(lowerQuery)) {
                    bannersFiltered.add(banner);
                }
            }
        }
        
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<Banner> newBanners) {
        this.banners = newBanners;
        this.bannersFiltered = new ArrayList<>(newBanners);
        notifyDataSetChanged();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBannerPreview;
        TextView tvBannerTitle, tvBannerLink, tvBannerOrder, tvBannerStatus;
        MaterialButton btnEdit, btnDelete;
        MaterialCardView cardStatus;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBannerPreview = itemView.findViewById(R.id.ivBannerPreview);
            tvBannerTitle = itemView.findViewById(R.id.tvBannerTitle);
            tvBannerLink = itemView.findViewById(R.id.tvBannerLink);
            tvBannerOrder = itemView.findViewById(R.id.tvBannerOrder);
            tvBannerStatus = itemView.findViewById(R.id.tvBannerStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            cardStatus = itemView.findViewById(R.id.cardStatus);
        }

        public void bind(Banner banner) {
            // Set title
            tvBannerTitle.setText(banner.getTitle());
            
            // Set link
            tvBannerLink.setText("Link: " + banner.getLink());
            
            // Set order
            tvBannerOrder.setText("Thứ tự: " + banner.getOrder());
            
            // Set status
            if (banner.isActive()) {
                tvBannerStatus.setText("Hoạt động");
                cardStatus.setCardBackgroundColor(context.getColor(R.color.colorSuccess));
            } else {
                tvBannerStatus.setText("Tạm dừng");
                cardStatus.setCardBackgroundColor(context.getColor(R.color.colorError));
            }
            
            // Load image
            loadBannerImage(banner.getImageUrl());
            
            // Set click listeners
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(banner);
                }
            });
            
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(banner);
                }
            });
        }

        private void loadBannerImage(String imageUrl) {
            if (imageUrl == null || imageUrl.isEmpty()) {
                ivBannerPreview.setImageResource(R.drawable.ic_category);
                return;
            }

            // Check if it's a URI (uploaded image)
            if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
                try {
                    ivBannerPreview.setImageURI(android.net.Uri.parse(imageUrl));
                    ivBannerPreview.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                } catch (Exception e) {
                    ivBannerPreview.setImageResource(R.drawable.ic_category);
                }
            } else {
                // Try to load from drawable resources
                try {
                    int resId = context.getResources().getIdentifier(
                        imageUrl, "drawable", context.getPackageName()
                    );
                    
                    if (resId != 0) {
                        ivBannerPreview.setImageResource(resId);
                    } else {
                        // If not found, show default image
                        ivBannerPreview.setImageResource(R.drawable.ic_category);
                    }
                } catch (Exception e) {
                    ivBannerPreview.setImageResource(R.drawable.ic_category);
                }
            }
        }
    }
}
