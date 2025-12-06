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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<CartItem> cartItems;
    private ArrayList<CartItem> selectedItems;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemDeleted(CartItem item);
        void onItemSelectionChanged(ArrayList<CartItem> selectedItems);
    }

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.selectedItems = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        
        holder.tvProductName.setText(item.getProduct().getName());
        holder.tvPrice.setText(FormatUtils.formatCurrency(item.getProduct().getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        
        // Checkbox state
        holder.cbSelectItem.setChecked(selectedItems.contains(item));
        holder.cbSelectItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedItems.contains(item)) {
                    selectedItems.add(item);
                }
            } else {
                selectedItems.remove(item);
            }
            if (listener != null) {
                listener.onItemSelectionChanged(selectedItems);
            }
        });
        
        // Quantity controls
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1 && listener != null) {
                listener.onQuantityChanged(item, item.getQuantity() - 1);
            }
        });
        
        holder.btnPlus.setOnClickListener(v -> {
            if (item.getQuantity() < item.getProduct().getStock() && listener != null) {
                listener.onQuantityChanged(item, item.getQuantity() + 1);
            }
        });
        
        // Delete button
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemDeleted(item);
            }
        });
        
        // Load first image from imageUrls
        ArrayList<String> imageUrls = item.getProduct().getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String firstImage = imageUrls.get(0);
            
            // Check if it's a drawable resource name (no scheme like content:// or file://)
            if (!firstImage.contains("://")) {
                // It's a drawable name (laptop1, banner2, etc.)
                int resId = context.getResources().getIdentifier(
                    firstImage.replace(".jpg", "").replace(".png", ""), 
                    "drawable", 
                    context.getPackageName()
                );
                if (resId != 0) {
                    holder.ivProductImage.setImageResource(resId);
                } else {
                    holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else {
                // It's a URI (content://, file://, etc.)
                try {
                    android.net.Uri imageUri = android.net.Uri.parse(firstImage);
                    holder.ivProductImage.setImageURI(imageUri);
                } catch (Exception e) {
                    holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCart(ArrayList<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }
    
    public void selectAll(boolean select) {
        selectedItems.clear();
        if (select) {
            selectedItems.addAll(cartItems);
        }
        notifyDataSetChanged();
        if (listener != null) {
            listener.onItemSelectionChanged(selectedItems);
        }
    }
    
    public ArrayList<CartItem> getSelectedItems() {
        return selectedItems;
    }
    
    public boolean isAllSelected() {
        return selectedItems.size() == cartItems.size() && !cartItems.isEmpty();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, btnDelete;
        TextView tvProductName, tvPrice, tvQuantity;
        android.widget.CheckBox cbSelectItem;
        com.google.android.material.button.MaterialButton btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelectItem = itemView.findViewById(R.id.cbSelectItem);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
