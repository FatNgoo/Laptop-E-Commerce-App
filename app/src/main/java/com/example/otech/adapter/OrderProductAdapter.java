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
import com.example.otech.model.CartItem;
import com.example.otech.util.FormatUtils;

import java.util.ArrayList;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {

    private Context context;
    private ArrayList<CartItem> products;

    public OrderProductAdapter(Context context, ArrayList<CartItem> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        CartItem item = products.get(position);
        
        holder.tvProductName.setText(item.getProduct().getName());
        holder.tvQuantity.setText("x" + item.getQuantity());
        holder.tvPrice.setText(FormatUtils.formatCurrency(item.getProduct().getPrice()));
        holder.tvTotalPrice.setText(FormatUtils.formatCurrency(item.getProduct().getPrice() * item.getQuantity()));
        
        // Load first image from imageUrls
        ArrayList<String> imageUrls = item.getProduct().getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String firstImageName = imageUrls.get(0);
            int resId = context.getResources().getIdentifier(firstImageName, "drawable", context.getPackageName());
            if (resId != 0) {
                holder.ivProductImage.setImageResource(resId);
            } else {
                holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class OrderProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvQuantity, tvPrice, tvTotalPrice;

        OrderProductViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
