package com.example.otech.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<Uri> imageUris;
    private OnImageRemoveListener listener;

    public interface OnImageRemoveListener {
        void onRemoveImage(int position);
    }

    public ProductImageAdapter(Context context, ArrayList<Uri> imageUris, OnImageRemoveListener listener) {
        this.context = context;
        this.imageUris = imageUris;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        holder.ivProductImage.setImageURI(imageUri);
        
        // Show "Ảnh chính" badge for first image
        if (position == 0) {
            holder.tvPrimaryBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvPrimaryBadge.setVisibility(View.GONE);
        }

        holder.btnRemoveImage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveImage(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        MaterialButton btnRemoveImage;
        TextView tvPrimaryBadge;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnRemoveImage = itemView.findViewById(R.id.btnRemoveImage);
            tvPrimaryBadge = itemView.findViewById(R.id.tvPrimaryBadge);
        }
    }
}
