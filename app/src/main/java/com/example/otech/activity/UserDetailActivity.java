package com.example.otech.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.OrderAdapter;
import com.example.otech.model.Order;
import com.example.otech.model.User;
import com.example.otech.repository.MockDataStore;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class UserDetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvFullName, tvUsername, tvEmail, tvPhone;
    private Chip chipRole;
    private TextView tvTotalOrders, tvTotalSpent;
    private RecyclerView rvUserOrders;
    private TextView tvNoOrders;
    
    private User user;
    private MockDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        dataStore = MockDataStore.getInstance();
        user = (User) getIntent().getSerializableExtra("user");

        initViews();
        loadUserData();
        loadUserOrders();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        chipRole = findViewById(R.id.chipRole);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);
        rvUserOrders = findViewById(R.id.rvUserOrders);
        tvNoOrders = findViewById(R.id.tvNoOrders);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(user != null ? user.getFullName() : "Chi tiết người dùng");
        }

        rvUserOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadUserData() {
        if (user == null) return;

        tvFullName.setText(user.getFullName());
        tvUsername.setText("@" + user.getUsername());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        
        if (user.isAdmin()) {
            chipRole.setText("Quản trị viên");
            chipRole.setChipBackgroundColorResource(android.R.color.holo_red_light);
        } else {
            chipRole.setText("Khách hàng");
            chipRole.setChipBackgroundColorResource(android.R.color.holo_green_light);
        }
    }

    private void loadUserOrders() {
        if (user == null) return;

        ArrayList<Order> userOrders = dataStore.getOrdersByUserId(user.getId());
        
        tvTotalOrders.setText(String.valueOf(userOrders.size()));
        
        // Calculate total spent
        double totalSpent = 0;
        for (Order order : userOrders) {
            if (!order.getStatus().equals("cancelled")) {
                totalSpent += order.getTotalAmount();
            }
        }
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = formatter.format(totalSpent);
        // Simplify display: convert to millions
        if (totalSpent >= 1000000) {
            tvTotalSpent.setText(String.format("%.1fM", totalSpent / 1000000));
        } else {
            tvTotalSpent.setText(formattedAmount);
        }
        
        // Display orders
        if (userOrders.isEmpty()) {
            tvNoOrders.setVisibility(View.VISIBLE);
            rvUserOrders.setVisibility(View.GONE);
        } else {
            tvNoOrders.setVisibility(View.GONE);
            rvUserOrders.setVisibility(View.VISIBLE);
            
            OrderAdapter adapter = new OrderAdapter(userOrders, new OrderAdapter.OnOrderActionListener() {
                @Override
                public void onViewDetailsClick(Order order) {
                    // Could open order detail if needed
                }

                @Override
                public void onCancelOrderClick(Order order) {
                    // Admin view - no cancel action
                }
            });
            rvUserOrders.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
