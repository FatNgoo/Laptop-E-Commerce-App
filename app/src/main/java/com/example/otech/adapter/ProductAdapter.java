package com.example.otech.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.model.Product;
import com.example.otech.util.FormatUtils;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onFavoriteClick(Product product, int position);
    }

    public ProductAdapter(Context context, ArrayList<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        
        holder.tvProductName.setText(product.getName());
        holder.tvPrice.setText(FormatUtils.formatCurrency(product.getPrice()));
        holder.ratingBar.setRating(product.getRating());
        holder.tvRating.setText(String.format("(%.1f)", product.getRating()));
        holder.tvSoldCount.setText("Đã bán: " + product.getSoldCount());
        
        // Old price (strikethrough)
        if (product.getOldPrice() > product.getPrice()) {
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setText(FormatUtils.formatCurrency(product.getOldPrice()));
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            
            // Discount badge
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText(FormatUtils.formatDiscount(product.getDiscountPercent()));
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
            holder.tvDiscount.setVisibility(View.GONE);
        }
        
        // Favorite icon (heart shape with pink/red color)
        if (product.isFavorite()) {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
        
        // Click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
        
        holder.ivFavorite.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClick(product, position);
            }
        });
        
        // Load first image from imageUrls
        ArrayList<String> imageUrls = product.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String firstImage = imageUrls.get(0);
            try {
                // Try to parse as URI first (for content:// or file:// URIs)
                android.net.Uri imageUri = android.net.Uri.parse(firstImage);
                holder.ivProductImage.setImageURI(imageUri);
            } catch (Exception e) {
                // Fallback: Try as drawable resource name
                int resId = context.getResources().getIdentifier(firstImage, "drawable", context.getPackageName());
                if (resId != 0) {
                    holder.ivProductImage.setImageResource(resId);
                } else {
                    holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(ArrayList<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, ivFavorite;
        TextView tvProductName, tvPrice, tvOldPrice, tvDiscount, tvRating, tvSoldCount;
        RatingBar ratingBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvSoldCount = itemView.findViewById(R.id.tvSoldCount);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
