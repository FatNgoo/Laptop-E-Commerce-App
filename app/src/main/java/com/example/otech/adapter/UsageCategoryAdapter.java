package com.example.otech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;

import java.util.ArrayList;

public class UsageCategoryAdapter extends RecyclerView.Adapter<UsageCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category, int position);
    }

    public UsageCategoryAdapter(Context context, ArrayList<String> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usage_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.tvUsageCategory.setText(category);
        holder.layoutUsageCategory.setSelected(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            listener.onCategoryClick(category, selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPosition);
        notifyItemChanged(selectedPosition);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutUsageCategory;
        TextView tvUsageCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutUsageCategory = itemView.findViewById(R.id.layoutUsageCategory);
            tvUsageCategory = itemView.findViewById(R.id.tvUsageCategory);
        }
    }
}
