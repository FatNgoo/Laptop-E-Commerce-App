package com.example.otech.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.model.Order;
import com.example.otech.util.Constants;
import com.example.otech.util.FormatUtils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    
    private ArrayList<Order> orders;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onViewDetailsClick(Order order);
        void onCancelOrderClick(Order order);
    }

    public OrderAdapter(ArrayList<Order> orders, OnOrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(ArrayList<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvOrderDate, tvOrderStatus, tvItemsCount, tvOrderTotal;
        private MaterialButton btnCancelOrder, btnViewDetails;
        private ImageView ivOrderStatusIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvItemsCount = itemView.findViewById(R.id.tvItemsCount);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            ivOrderStatusIcon = itemView.findViewById(R.id.ivOrderStatusIcon);
        }

        public void bind(Order order) {
            // Order ID
            tvOrderId.setText("#" + order.getId().substring(0, 8));

            // Order Date
            tvOrderDate.setText(FormatUtils.formatDate(order.getOrderDate()));

            // Order Status
            tvOrderStatus.setText(getStatusText(order.getStatus()));
            int statusColor = getStatusColor(order.getStatus());
            tvOrderStatus.setTextColor(itemView.getContext().getColor(statusColor));
            
            // Status Icon
            ivOrderStatusIcon.setColorFilter(itemView.getContext().getColor(statusColor));
            int statusIcon = getStatusIcon(order.getStatus());
            ivOrderStatusIcon.setImageResource(statusIcon);

            // Items Count
            int itemCount = order.getItems().size();
            tvItemsCount.setText(itemCount + " sản phẩm");

            // Total Amount
            tvOrderTotal.setText(FormatUtils.formatCurrency(order.getTotalAmount()));

            // Cancel button visibility
            btnCancelOrder.setVisibility(order.canBeCancelled() ? View.VISIBLE : View.GONE);

            // Click listeners
            btnViewDetails.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewDetailsClick(order);
                }
            });

            btnCancelOrder.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelOrderClick(order);
                }
            });
        }

        private String getStatusText(String status) {
            switch (status) {
                case Constants.ORDER_STATUS_PENDING:
                    return "Chờ xử lý";
                case Constants.ORDER_STATUS_PROCESSING:
                    return "Đang xử lý";
                case Constants.ORDER_STATUS_SHIPPING:
                    return "Đang giao";
                case Constants.ORDER_STATUS_COMPLETED:
                    return "Hoàn thành";
                case Constants.ORDER_STATUS_CANCELLED:
                    return "Đã hủy";
                default:
                    return status;
            }
        }

        private int getStatusColor(String status) {
            switch (status) {
                case Constants.ORDER_STATUS_PENDING:
                case Constants.ORDER_STATUS_PROCESSING:
                    return R.color.colorSecondary;
                case Constants.ORDER_STATUS_SHIPPING:
                    return R.color.blue;
                case Constants.ORDER_STATUS_COMPLETED:
                    return R.color.colorPrimary;
                case Constants.ORDER_STATUS_CANCELLED:
                    return R.color.colorError;
                default:
                    return R.color.gray_dark;
            }
        }
        
        private int getStatusIcon(String status) {
            switch (status) {
                case Constants.ORDER_STATUS_PENDING:
                    return R.drawable.ic_notifications;
                case Constants.ORDER_STATUS_PROCESSING:
                    return R.drawable.ic_settings;
                case Constants.ORDER_STATUS_SHIPPING:
                    return R.drawable.ic_shopping_cart;
                case Constants.ORDER_STATUS_COMPLETED:
                    return R.drawable.ic_check;
                case Constants.ORDER_STATUS_CANCELLED:
                    return R.drawable.ic_delete;
                default:
                    return R.drawable.ic_list;
            }
        }
    }
}
