package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity implements OrderAdapter.OnOrderActionListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvOrders;
    private LinearLayout layoutEmptyOrders;
    private OrderAdapter adapter;
    private MockDataStore dataStore;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, null);

        if (currentUserId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dataStore = MockDataStore.getInstance();

        initViews();
        setupToolbar();
        loadOrders();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvOrders = findViewById(R.id.rvOrders);
        layoutEmptyOrders = findViewById(R.id.layoutEmptyOrders);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadOrders() {
        ArrayList<Order> orders = dataStore.getUserOrders(currentUserId);

        if (orders.isEmpty()) {
            layoutEmptyOrders.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.GONE);
        } else {
            layoutEmptyOrders.setVisibility(View.GONE);
            rvOrders.setVisibility(View.VISIBLE);

            adapter = new OrderAdapter(orders, this);
            rvOrders.setAdapter(adapter);
        }
    }

    @Override
    public void onViewDetailsClick(Order order) {
        // Navigate to OrderDetailActivity
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ORDER, order);
        startActivity(intent);
    }

    @Override
    public void onCancelOrderClick(Order order) {
        // Show cancel confirmation dialog
        showCancelConfirmationDialog(order);
    }

    private void showCancelConfirmationDialog(Order order) {
        new AlertDialog.Builder(this)
                .setTitle("Hủy đơn hàng")
                .setMessage("Bạn có chắc muốn hủy đơn hàng này?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    boolean success = dataStore.cancelOrder(order.getId(), "Khách hàng yêu cầu hủy");
                    if (success) {
                        Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                        loadOrders(); // Reload orders
                    } else {
                        Toast.makeText(this, "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
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
        // Reload orders when returning to this activity
        loadOrders();
    }
}
