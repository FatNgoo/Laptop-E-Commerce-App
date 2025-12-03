package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.R;
import com.example.otech.model.Order;
import com.example.otech.model.User;
import com.example.otech.repository.MockDataStore;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialButton btnLogout;
    private TextView tvAdminName, tvTotalRevenue, tvTotalOrders, tvTotalProducts, tvTotalUsers;
    private TextView tvPendingOrders, tvShippingOrders, tvDeliveredOrders;
    private MaterialCardView cardManageProducts, cardManageOrders, cardManageUsers, cardManageReviews, cardManageBanners;
    private MockDataStore dataStore;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dataStore = MockDataStore.getInstance();

        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, null);

        // Verify admin access
        if (currentUserId == null || !isAdmin()) {
            finish();
            return;
        }

        initViews();
        loadDashboardData();
        setupListeners();
    }

    private boolean isAdmin() {
        User user = dataStore.getUserById(currentUserId);
        return user != null && user.isAdmin();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        btnLogout = findViewById(R.id.btnLogout);
        tvAdminName = findViewById(R.id.tvAdminName);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);
        tvShippingOrders = findViewById(R.id.tvShippingOrders);
        tvDeliveredOrders = findViewById(R.id.tvDeliveredOrders);
        cardManageProducts = findViewById(R.id.cardManageProducts);
        cardManageOrders = findViewById(R.id.cardManageOrders);
        cardManageUsers = findViewById(R.id.cardManageUsers);
        cardManageReviews = findViewById(R.id.cardManageReviews);
        cardManageBanners = findViewById(R.id.cardManageBanners);

        setSupportActionBar(toolbar);
    }

    private void loadDashboardData() {
        // Load admin name
        User admin = dataStore.getUserById(currentUserId);
        if (admin != null) {
            tvAdminName.setText(admin.getFullName());
        }

        // Calculate total revenue (from delivered and completed orders)
        long totalRevenue = 0;
        ArrayList<Order> orders = dataStore.getAllOrders();
        for (Order order : orders) {
            String status = order.getStatus();
            if (status.equals("delivered") || status.equals("completed")) {
                totalRevenue += order.getTotalAmount();
            }
        }
        tvTotalRevenue.setText(formatCurrency(totalRevenue));

        // Total orders
        tvTotalOrders.setText(String.valueOf(orders.size()));

        // Total products
        int productCount = dataStore.getAllProducts().size();
        tvTotalProducts.setText(String.valueOf(productCount));

        // Total users (excluding admins)
        int userCount = 0;
        for (User user : dataStore.getAllUsers()) {
            if (!user.isAdmin()) {
                userCount++;
            }
        }
        tvTotalUsers.setText(String.valueOf(userCount));

        // Order status counts
        int pendingCount = 0;
        int shippingCount = 0;
        int deliveredCount = 0;

        for (Order order : orders) {
            String status = order.getStatus();
            switch (status) {
                case "pending":
                    pendingCount++;
                    break;
                case "shipping":
                    shippingCount++;
                    break;
                case "delivered":
                case "completed":
                    deliveredCount++;
                    break;
            }
        }

        tvPendingOrders.setText(String.valueOf(pendingCount));
        tvShippingOrders.setText(String.valueOf(shippingCount));
        tvDeliveredOrders.setText(String.valueOf(deliveredCount));
    }

    private String formatCurrency(long amount) {
        return String.format("%,dđ", amount);
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());

        cardManageProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManageProductsActivity.class);
            startActivity(intent);
        });

        cardManageOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManageOrdersActivity.class);
            startActivity(intent);
        });

        cardManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });

        cardManageReviews.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManageReviewsActivity.class);
            startActivity(intent);
        });

        cardManageBanners.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AdminBannersActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning from other screens
        loadDashboardData();
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    // Clear login session
                    SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
                    prefs.edit().clear().apply();

                    // Go to login
                    Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        // Prevent back button to avoid going back to login
        new AlertDialog.Builder(this)
                .setTitle("Thoát ứng dụng")
                .setMessage("Bạn có muốn thoát?")
                .setPositiveButton("Thoát", (dialog, which) -> finishAffinity())
                .setNegativeButton("Hủy", null)
                .show();
    }
}
