package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.UserAdapter;
import com.example.otech.model.Order;
import com.example.otech.model.User;
import com.example.otech.repository.MockDataStore;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvUsers;
    private LinearLayout layoutEmptyState;
    private TextView tvTotalUsers, tvActiveUsers, tvTotalOrders, tvTotalRevenue;
    private UserAdapter adapter;
    private MockDataStore dataStore;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        dataStore = MockDataStore.getInstance();

        initViews();
        setupToolbar();
        loadUsers();
        loadStatistics();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvUsers = findViewById(R.id.rvUsers);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvActiveUsers = findViewById(R.id.tvActiveUsers);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý người dùng");
        }
    }

    private void loadUsers() {
        users = new ArrayList<>();
        for (User user : dataStore.getAllUsers()) {
            if (!user.isAdmin()) {
                users.add(user);
            }
        }

        if (users.isEmpty()) {
            rvUsers.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvUsers.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);

            adapter = new UserAdapter(users, this);
            rvUsers.setAdapter(adapter);
        }
    }

    private void loadStatistics() {
        // Total users
        tvTotalUsers.setText(String.valueOf(users.size()));

        // Active users (có ít nhất 1 đơn hàng)
        int activeCount = 0;
        int totalOrders = 0;
        long totalRevenue = 0;

        for (User user : users) {
            ArrayList<Order> userOrders = dataStore.getUserOrders(user.getId());
            if (!userOrders.isEmpty()) {
                activeCount++;
                totalOrders += userOrders.size();

                for (Order order : userOrders) {
                    if (order.getStatus().equals("delivered")) {
                        totalRevenue += order.getTotalAmount();
                    }
                }
            }
        }

        tvActiveUsers.setText(String.valueOf(activeCount));
        tvTotalOrders.setText(String.valueOf(totalOrders));
        tvTotalRevenue.setText(formatCurrency(totalRevenue));
    }

    private String formatCurrency(long amount) {
        return String.format("%,dđ", amount);
    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
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
