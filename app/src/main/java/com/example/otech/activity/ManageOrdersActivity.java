package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.OrderAdapter;
import com.example.otech.model.Order;
import com.example.otech.repository.MockDataStore;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class ManageOrdersActivity extends AppCompatActivity implements OrderAdapter.OnOrderActionListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvOrders;
    private LinearLayout layoutEmptyOrders;
    private ChipGroup chipGroupStatus;
    private OrderAdapter adapter;
    private MockDataStore dataStore;
    private ArrayList<Order> allOrders;
    private ArrayList<Order> filteredOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        dataStore = MockDataStore.getInstance();

        initViews();
        setupRecyclerView();
        loadOrders();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvOrders = findViewById(R.id.rvOrders);
        layoutEmptyOrders = findViewById(R.id.layoutEmptyOrders);
        chipGroupStatus = findViewById(R.id.chipGroupStatus);

        toolbar.setNavigationOnClickListener(v -> finish());
        
        setupChipListeners();
    }

    private void setupRecyclerView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupChipListeners() {
        chipGroupStatus.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String statusFilter = "all";
            
            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chipPending) {
                    statusFilter = Constants.ORDER_STATUS_PENDING;
                } else if (checkedId == R.id.chipShipping) {
                    statusFilter = Constants.ORDER_STATUS_SHIPPING;
                } else if (checkedId == R.id.chipCompleted) {
                    statusFilter = Constants.ORDER_STATUS_COMPLETED;
                } else if (checkedId == R.id.chipCancelled) {
                    statusFilter = Constants.ORDER_STATUS_CANCELLED;
                }
            }
            
            filterOrders(statusFilter);
        });
    }

    private void loadOrders() {
        allOrders = dataStore.getAllOrders();
        filteredOrders = new ArrayList<>(allOrders);
        updateOrdersList();
    }

    private void filterOrders(String status) {
        filteredOrders.clear();
        
        if (status.equals("all")) {
            filteredOrders.addAll(allOrders);
        } else {
            for (Order order : allOrders) {
                if (order.getStatus().equals(status)) {
                    filteredOrders.add(order);
                }
            }
        }
        
        updateOrdersList();
    }

    private void updateOrdersList() {
        if (filteredOrders.isEmpty()) {
            layoutEmptyOrders.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.GONE);
        } else {
            layoutEmptyOrders.setVisibility(View.GONE);
            rvOrders.setVisibility(View.VISIBLE);

            if (adapter == null) {
                adapter = new OrderAdapter(filteredOrders, this);
                rvOrders.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onViewDetailsClick(Order order) {
        // Open AdminOrderDetailActivity for a better visual interface
        Intent intent = new Intent(this, AdminOrderDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ORDER, order);
        startActivity(intent);
    }

    @Override
    public void onCancelOrderClick(Order order) {
        // Admin can't cancel, but can change status
        showOrderStatusDialog(order);
    }

    private void showOrderStatusDialog(Order order) {
        String[] statuses = {
                "Chờ xử lý",
                "Đang xử lý",
                "Đang giao",
                "Hoàn thành",
                "Hủy đơn"
        };

        String[] statusValues = {
                Constants.ORDER_STATUS_PENDING,
                Constants.ORDER_STATUS_PROCESSING,
                Constants.ORDER_STATUS_SHIPPING,
                Constants.ORDER_STATUS_COMPLETED,
                Constants.ORDER_STATUS_CANCELLED
        };

        new AlertDialog.Builder(this)
                .setTitle("Cập nhật trạng thái đơn hàng")
                .setItems(statuses, (dialog, which) -> {
                    String newStatus = statusValues[which];
                    updateOrderStatus(order, newStatus);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateOrderStatus(Order order, String newStatus) {
        if (newStatus.equals(Constants.ORDER_STATUS_CANCELLED)) {
            // If cancelling, ask for reason
            new AlertDialog.Builder(this)
                    .setTitle("Lý do hủy đơn")
                    .setMessage("Nhập lý do hủy đơn hàng")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        boolean success = dataStore.cancelOrder(order.getId(), "Admin hủy đơn");
                        if (success) {
                            Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                            loadOrders();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        } else {
            boolean success = dataStore.updateOrderStatus(order.getId(), newStatus);
            if (success) {
                Toast.makeText(this, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                loadOrders();
            } else {
                Toast.makeText(this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
