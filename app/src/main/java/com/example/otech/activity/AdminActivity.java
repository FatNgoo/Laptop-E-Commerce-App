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
import com.example.otech.model.Product;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialButton btnLogout;
    private TextView tvAdminName, tvTotalRevenue, tvTotalOrders, tvTotalProducts, tvTotalUsers;
    private TextView tvPendingOrders, tvShippingOrders, tvDeliveredOrders;
    private MaterialCardView cardManageProducts, cardManageOrders, cardManageUsers, cardManageReviews, cardManageBanners;
    private DataRepository repository;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        repository = DataRepository.getInstance(getApplicationContext());

        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, null);

        // Verify admin access
        if (currentUserId == null) {
            finish();
            return;
        }
        
        verifyAdmin();
    }

    private void verifyAdmin() {
        repository.getUserById(currentUserId, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null && user.isAdmin()) {
                    initViews();
                    loadDashboardData();
                    setupListeners();
                } else {
                    finish();
                }
            }
            @Override
            public void onError(Exception e) {
                finish();
            }
        });
    }
    
    private void loadDashboardData() {
        // Load admin name
        repository.getUserById(currentUserId, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User admin) {
                if (admin != null) {
                    tvAdminName.setText(admin.getFullName());
                }
            }
            @Override
            public void onError(Exception e) {}
        });

        // Load all orders
        repository.getAllOrders(new DataRepository.DataCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> orders) {
                long totalRevenue = 0;
                int pendingCount = 0, shippingCount = 0, deliveredCount = 0;
                
                for (Order order : orders) {
                    String status = order.getStatus();
                    if ("delivered".equals(status) || "completed".equals(status)) {
                        totalRevenue += (long) order.getTotalAmount();
                        deliveredCount++;
                    } else if ("pending".equals(status)) {
                        pendingCount++;
                    } else if ("shipping".equals(status)) {
                        shippingCount++;
                    }
                }
                
                tvTotalRevenue.setText(formatCurrency(totalRevenue));
                tvTotalOrders.setText(String.valueOf(orders.size()));
                tvPendingOrders.setText(String.valueOf(pendingCount));
                tvShippingOrders.setText(String.valueOf(shippingCount));
                tvDeliveredOrders.setText(String.valueOf(deliveredCount));
            }
            @Override
            public void onError(Exception e) {}
        });

        // Load products count
        repository.getAllProducts(new DataRepository.DataCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                tvTotalProducts.setText(String.valueOf(products.size()));
            }
            @Override
            public void onError(Exception e) {}
        });

        // Load users count
        repository.getAllUsers(new DataRepository.DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                int userCount = 0;
                for (User user : users) {
                    if (!user.isAdmin()) {
                        userCount++;
                    }
                }
                tvTotalUsers.setText(String.valueOf(userCount));
            }
            @Override
            public void onError(Exception e) {}
        });
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
            Intent intent = new Intent(AdminActivity.this, AdminReviewsActivity.class);
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
