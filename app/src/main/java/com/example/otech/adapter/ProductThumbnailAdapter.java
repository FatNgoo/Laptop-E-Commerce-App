package com.example.otech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ProductThumbnailAdapter extends RecyclerView.Adapter<ProductThumbnailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> imageUrls;
    private int selectedPosition = 0;
    private OnThumbnailClickListener listener;

    public interface OnThumbnailClickListener {
        void onThumbnailClick(int position);
    }

    public ProductThumbnailAdapter(Context context, ArrayList<String> imageUrls, OnThumbnailClickListener listener) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_thumbnail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        
        // Load image
        loadImage(holder.ivThumbnail, imageUrl);
        
        // Highlight selected thumbnail
        if (position == selectedPosition) {
            holder.cardThumbnail.setStrokeColor(context.getResources().getColor(R.color.colorPrimary));
            holder.cardThumbnail.setStrokeWidth(4);
        } else {
            holder.cardThumbnail.setStrokeColor(context.getResources().getColor(android.R.color.transparent));
            holder.cardThumbnail.setStrokeWidth(2);
        }
        
        // Click listener
        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            
            if (listener != null) {
                listener.onThumbnailClick(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPosition);
        notifyItemChanged(selectedPosition);
    }

    private void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
            return;
        }

        try {
            if (imageUrl.startsWith("file://")) {
                android.net.Uri uri = android.net.Uri.parse(imageUrl);
                java.io.File file = new java.io.File(uri.getPath());
                if (file.exists()) {
                    imageView.setImageURI(uri);
                    return;
                }
            } else if (imageUrl.startsWith("content://")) {
                imageView.setImageURI(android.net.Uri.parse(imageUrl));
                return;
            } else if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                // Ignore placeholder URLs
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
                return;
            } else {
                // Try as drawable resource name
                int resId = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        imageView.setImageResource(R.drawable.ic_launcher_foreground);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardThumbnail;
        ImageView ivThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardThumbnail = itemView.findViewById(R.id.cardThumbnail);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
