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

import java.util.ArrayList;

public class ProductImagePreviewAdapter extends RecyclerView.Adapter<ProductImagePreviewAdapter.ImageViewHolder> {

    private Context context;
    private int[] imageResources;
    private ArrayList<String> imageUrls;
    private boolean useResources;

    // Constructor for drawable resources (backward compatibility)
    public ProductImagePreviewAdapter(Context context, int[] imageResources) {
        this.context = context;
        this.imageResources = imageResources;
        this.useResources = true;
    }
    
    // Constructor for URI strings
    public ProductImagePreviewAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.useResources = false;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_image_preview, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (useResources) {
            holder.imageView.setImageResource(imageResources[position]);
        } else {
            String imageUrl = imageUrls.get(position);
            
            // Check if it's a drawable resource name (no scheme like content:// or file://)
            if (!imageUrl.contains("://")) {
                // It's a drawable name (laptop1, banner2, etc.)
                int resId = context.getResources().getIdentifier(
                    imageUrl.replace(".jpg", "").replace(".png", ""), 
                    "drawable", 
                    context.getPackageName()
                );
                if (resId != 0) {
                    holder.imageView.setImageResource(resId);
                } else {
                    holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else {
                // It's a URI (content://, file://, etc.)
                try {
                    Uri uri = Uri.parse(imageUrl);
                    holder.imageView.setImageURI(uri);
                } catch (Exception e) {
                    holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return useResources ? imageResources.length : (imageUrls != null ? imageUrls.size() : 0);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivProductImagePreview);
        }
    }
}
