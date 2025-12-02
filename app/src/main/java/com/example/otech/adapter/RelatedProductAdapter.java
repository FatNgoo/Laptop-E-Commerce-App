package com.example.otech.adapter;

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

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public RelatedProductAdapter(ArrayList<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_related_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tvBrand.setText(product.getBrand());
        holder.tvProductName.setText(product.getName());
        holder.tvPrice.setText(FormatUtils.formatCurrency(product.getPrice()));
        holder.ratingBar.setRating(product.getRating());
        holder.tvRating.setText(String.format("(%.1f)", product.getRating()));
        holder.tvSoldCount.setText("Đã bán: " + product.getSoldCount());

        // Old price
        if (product.getOldPrice() > product.getPrice()) {
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setText(FormatUtils.formatCurrency(product.getOldPrice()));
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
        }

        // Load first image from imageUrls
        ArrayList<String> imageUrls = product.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String firstImageName = imageUrls.get(0);
            int resId = holder.itemView.getContext().getResources().getIdentifier(
                    firstImageName, "drawable", holder.itemView.getContext().getPackageName());
            if (resId != 0) {
                holder.ivProductImage.setImageResource(resId);
            } else {
                holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // Click listener
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvBrand, tvProductName, tvPrice, tvOldPrice, tvRating, tvSoldCount;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvSoldCount = itemView.findViewById(R.id.tvSoldCount);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
