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
import com.example.otech.model.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private ArrayList<Notification> notifications;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification, int position);
    }

    public NotificationAdapter(Context context, ArrayList<Notification> notifications, OnNotificationClickListener listener) {
        this.context = context;
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        
        holder.tvTitle.setText(notification.getTitle());
        holder.tvMessage.setText(notification.getMessage());
        holder.tvTime.setText(notification.getTimeAgo());
        
        // Show/hide unread indicator
        if (notification.isRead()) {
            holder.viewUnreadIndicator.setVisibility(View.GONE);
            holder.itemView.setAlpha(0.7f);
        } else {
            holder.viewUnreadIndicator.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(1.0f);
        }
        
        // Set icon based on notification type
        int iconRes = R.drawable.ic_notifications;
        switch (notification.getType()) {
            case "ORDER_PLACED":
                iconRes = R.drawable.ic_shopping_cart;
                break;
            case "ORDER_CONFIRMED":
                iconRes = R.drawable.ic_check;
                break;
            case "ORDER_SHIPPING":
                iconRes = R.drawable.ic_delivery;
                break;
            case "ORDER_DELIVERED":
                iconRes = R.drawable.ic_check;
                break;
            case "ORDER_CANCELLED":
                iconRes = R.drawable.ic_delete;
                break;
        }
        holder.ivIcon.setImageResource(iconRes);
        
        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(notification, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle, tvMessage, tvTime;
        View viewUnreadIndicator;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivNotificationIcon);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvTime = itemView.findViewById(R.id.tvNotificationTime);
            viewUnreadIndicator = itemView.findViewById(R.id.viewUnreadIndicator);
        }
    }
}
