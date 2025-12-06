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
import com.example.otech.model.Product;
import com.example.otech.util.FormatUtils;

import java.io.File;
import java.util.ArrayList;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public RecentlyViewedAdapter(Context context, ArrayList<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recently_viewed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvPrice.setText(FormatUtils.formatCurrency(product.getPrice()));
        holder.tvBrand.setText(product.getBrand());

        // Load product image
        ArrayList<String> imageUrls = product.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String firstImageUrl = imageUrls.get(0);
            loadProductImage(holder.ivProductImage, firstImageUrl);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void loadProductImage(ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
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
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
        // Check if it's a content:// URI (uploaded image)
        else if (imageUrl.startsWith("content://")) {
            try {
                imageView.setImageURI(Uri.parse(imageUrl));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
        // Otherwise, load from drawable resources
        else {
            try {
                String imageNameWithoutExt = imageUrl.replace(".jpg", "").replace(".png", "");
                int resId = context.getResources().getIdentifier(
                    imageNameWithoutExt, "drawable", context.getPackageName()
                );
                
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
    }

    public void updateProducts(ArrayList<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvPrice, tvBrand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvBrand = itemView.findViewById(R.id.tvBrand);
        }
    }
}
