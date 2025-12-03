package com.example.otech.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.model.Banner;

import java.io.File;
import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private Context context;
    private ArrayList<Banner> banners;

    public BannerAdapter(Context context, ArrayList<Banner> banners) {
        this.context = context;
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = banners.get(position);
        loadBannerImage(holder.ivBanner, banner.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    private void loadBannerImage(ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.banner1);
            return;
        }

        // Check if it's a URI (uploaded image)
        if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
            try {
                imageView.setImageURI(Uri.parse(imageUrl));
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.banner1);
            }
        } else {
            // Load from drawable resources
            try {
                int resId = context.getResources().getIdentifier(
                    imageUrl, "drawable", context.getPackageName()
                );
                
                if (resId != 0) {
                    imageView.setImageResource(resId);
                } else {
                    imageView.setImageResource(R.drawable.banner1);
                }
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.banner1);
            }
        }
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBanner;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBanner = itemView.findViewById(R.id.ivBanner);
        }
    }
}
