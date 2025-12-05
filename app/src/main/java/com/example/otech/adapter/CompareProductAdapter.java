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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CompareProductAdapter extends RecyclerView.Adapter<CompareProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private OnProductSelectedListener listener;

    public interface OnProductSelectedListener {
        void onProductSelected(Product product);
    }

    public CompareProductAdapter(Context context, ArrayList<Product> products, OnProductSelectedListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_compare_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        
        holder.tvBrand.setText(product.getBrand());
        holder.tvProductName.setText(product.getName());
        holder.tvPrice.setText(formatPrice(product.getPrice()));
        
        // Build specs preview from specs string
        String specsStr = product.getSpecs() != null ? product.getSpecs() : "";
        String specsPreview = buildSpecsPreview(specsStr);
        holder.tvSpecs.setText(specsPreview);
        
        // Load product image - use imageUrls list instead of placeholder imageUrl
        String imageUrl = null;
        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            imageUrl = product.getImageUrls().get(0);
        }
        loadProductImage(holder.ivProductImage, imageUrl);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductSelected(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(ArrayList<Product> newProducts) {
        this.products.clear();
        this.products.addAll(newProducts);
        notifyDataSetChanged();
    }

    private void loadProductImage(ImageView imageView, String imageUrl) {
        if (imageView == null) return;
        
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return;
        }

        try {
            if (imageUrl.startsWith("file://")) {
                android.net.Uri uri = android.net.Uri.parse(imageUrl);
                java.io.File file = new java.io.File(uri.getPath());
                if (file.exists()) {
                    imageView.setImageURI(null); // Clear cache
                    imageView.setImageURI(uri);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return;
                }
            } else if (imageUrl.startsWith("content://")) {
                imageView.setImageURI(null); // Clear cache
                imageView.setImageURI(android.net.Uri.parse(imageUrl));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return;
            } else if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                // Ignore placeholder URLs from mock data
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return;
            } else {
                // Try as drawable resource name
                int resId = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Fallback to placeholder
        imageView.setImageResource(R.drawable.ic_launcher_foreground);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private String buildSpecsPreview(String specs) {
        if (specs == null || specs.isEmpty()) return "N/A";
        
        StringBuilder preview = new StringBuilder();
        // Parse comma-separated format like ProductDetailActivity
        String[] specLines = specs.split(",");
        int count = 0;
        
        for (String spec : specLines) {
            if (count >= 3) break; // Max 3 specs
            String[] parts = spec.trim().split(":");
            if (parts.length == 2) {
                String value = parts[1].trim();
                if (preview.length() > 0) preview.append(" • ");
                preview.append(value);
                count++;
            }
        }
        
        return preview.length() > 0 ? preview.toString() : "N/A";
    }

    private String formatPrice(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvBrand, tvProductName, tvPrice, tvSpecs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSpecs = itemView.findViewById(R.id.tvSpecs);
        }
    }
}
