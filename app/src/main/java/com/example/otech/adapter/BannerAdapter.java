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

    public void updateBanners(ArrayList<Banner> newBanners) {
        this.banners = newBanners;
        notifyDataSetChanged();
    }

    private void loadBannerImage(ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.banner1);
            return;
        }

        // Check if it's a file:// URI (from internal storage)
        if (imageUrl.startsWith("file://")) {
            try {
                Uri uri = Uri.parse(imageUrl);
                File file = new File(uri.getPath());
                if (file.exists()) {
                    imageView.setImageURI(uri);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    // File not found, fallback to default
                    imageView.setImageResource(R.drawable.banner1);
                }
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.banner1);
            }
        } 
        // Check if it's a content:// URI (uploaded image)
        else if (imageUrl.startsWith("content://")) {
            try {
                Uri uri = Uri.parse(imageUrl);
                imageView.setImageURI(null); // Clear previous image
                imageView.setImageURI(uri);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.banner1);
            }
        } 
        // Otherwise, load from drawable resources
        else {
            try {
                // Remove .jpg or .png extension
                String imageNameWithoutExt = imageUrl.replace(".jpg", "").replace(".png", "");
                int resId = context.getResources().getIdentifier(
                    imageNameWithoutExt, "drawable", context.getPackageName()
                );
                
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
