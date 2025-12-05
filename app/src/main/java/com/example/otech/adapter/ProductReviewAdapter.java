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
import com.example.otech.model.Product;
import com.example.otech.repository.DataRepository;

import java.util.ArrayList;
import java.util.Locale;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private ArrayList<Product> productsFiltered;
    private OnProductClickListener listener;
    private DataRepository repository;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductReviewAdapter(Context context, ArrayList<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.productsFiltered = new ArrayList<>(products);
        this.listener = listener;
        this.repository = DataRepository.getInstance(context.getApplicationContext());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productsFiltered.get(position);
        
        holder.tvProductName.setText(product.getName());
        holder.tvProductBrand.setText(product.getBrand());
        
        // Load product image
        loadProductImage(product.getImageUrl(), holder.ivProductImage);
        
        // Rating
        holder.tvRating.setText(String.format(Locale.getDefault(), "%.1f", product.getRating()));
        
        // Review count - load async
        repository.getProductReviews(product.getId(), new DataRepository.DataCallback<java.util.List<com.example.otech.model.Review>>() {
            @Override
            public void onSuccess(java.util.List<com.example.otech.model.Review> reviews) {
                holder.tvReviewCount.setText(String.format("(%d đánh giá)", reviews.size()));
            }

            @Override
            public void onError(Exception e) {
                holder.tvReviewCount.setText("(0 đánh giá)");
            }
        });
        
        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsFiltered.size();
    }

    private void loadProductImage(String imageUrl, ImageView imageView) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_category);
            return;
        }

        // Check if it's a URI (uploaded image)
        if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
            try {
                imageView.setImageURI(android.net.Uri.parse(imageUrl));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_category);
            }
        } else {
            // Try to load from drawable resources
            try {
                int resId = context.getResources().getIdentifier(
                    imageUrl, "drawable", context.getPackageName()
                );
                
                if (resId != 0) {
                    imageView.setImageResource(resId);
                } else {
                    imageView.setImageResource(R.drawable.ic_category);
                }
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_category);
            }
        }
    }

    public void filter(String query, float minRating, String brand) {
        productsFiltered.clear();
        
        if (query.isEmpty() && minRating == 0 && (brand == null || brand.isEmpty())) {
            productsFiltered.addAll(products);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            
            for (Product product : products) {
                boolean matchesSearch = query.isEmpty() || 
                    product.getName().toLowerCase().contains(lowerCaseQuery) ||
                    product.getBrand().toLowerCase().contains(lowerCaseQuery);
                
                boolean matchesRating = minRating == 0 || product.getRating() >= minRating;
                
                boolean matchesBrand = (brand == null || brand.isEmpty()) ||
                    product.getBrand().equalsIgnoreCase(brand);
                
                if (matchesSearch && matchesRating && matchesBrand) {
                    productsFiltered.add(product);
                }
            }
        }
        
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<Product> newProducts) {
        this.products = newProducts;
        this.productsFiltered = new ArrayList<>(newProducts);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductBrand, tvRating, tvReviewCount;

        ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductBrand = itemView.findViewById(R.id.tvProductBrand);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviewCount = itemView.findViewById(R.id.tvReviewCount);
        }
    }
}
