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
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity implements OrderAdapter.OnOrderActionListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvOrders;
    private LinearLayout layoutEmptyOrders;
    private ChipGroup chipGroupStatus;
    private OrderAdapter adapter;
    private DataRepository repository;
    private ArrayList<Order> allOrders;
    private ArrayList<Order> filteredOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        repository = DataRepository.getInstance(getApplicationContext());

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
                } else if (checkedId == R.id.chipProcessing) {
                    statusFilter = Constants.ORDER_STATUS_PROCESSING;
                } else if (checkedId == R.id.chipShipping) {
                    statusFilter = Constants.ORDER_STATUS_SHIPPING;
                } else if (checkedId == R.id.chipCompleted) {
                    statusFilter = Constants.ORDER_STATUS_COMPLETED;
                } else if (checkedId == R.id.chipCancelled) {
                    statusFilter = Constants.ORDER_STATUS_CANCELLED;
                } else if (checkedId == R.id.chipOutOfStock) {
                    statusFilter = Constants.ORDER_STATUS_OUT_OF_STOCK;
                }
            }
            
            filterOrders(statusFilter);
        });
    }

    private void loadOrders() {
        allOrders = new ArrayList<>();
        repository.getAllOrders(new DataRepository.DataCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> orders) {
                allOrders.clear();
                allOrders.addAll(orders);
                filteredOrders = new ArrayList<>(allOrders);
                updateOrdersList();
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(ManageOrdersActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
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
                adapter.updateOrders(filteredOrders);
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
                "Hủy đơn",
                "Hết hàng"
        };

        String[] statusValues = {
                Constants.ORDER_STATUS_PENDING,
                Constants.ORDER_STATUS_PROCESSING,
                Constants.ORDER_STATUS_SHIPPING,
                Constants.ORDER_STATUS_COMPLETED,
                Constants.ORDER_STATUS_CANCELLED,
                Constants.ORDER_STATUS_OUT_OF_STOCK
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
        if (Constants.ORDER_STATUS_CANCELLED.equals(newStatus)) {
            // If cancelling, use repository.cancelOrder
            new AlertDialog.Builder(this)
                    .setTitle("Lý do hủy đơn")
                    .setMessage("Nhập lý do hủy đơn hàng")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        repository.cancelOrder(order.getId(), "Admin hủy đơn", new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ManageOrdersActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                                loadOrders();
                            }
                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ManageOrdersActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        } else {
            order.setStatus(newStatus);
            repository.updateOrder(order, new DataRepository.VoidCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ManageOrdersActivity.this, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                    loadOrders();
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(ManageOrdersActivity.this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                }
            });
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
            case Constants.ORDER_STATUS_OUT_OF_STOCK:
                return "Hết hàng";
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
