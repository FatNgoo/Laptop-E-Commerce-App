package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.UserAdapter;
import com.example.otech.model.Order;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvUsers;
    private LinearLayout layoutEmptyState;
    private TextView tvTotalUsers, tvActiveUsers, tvTotalOrders, tvTotalRevenue;
    private UserAdapter adapter;
    private DataRepository repository;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        repository = DataRepository.getInstance(getApplicationContext());

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
        repository.getAllUsers(new DataRepository.DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> allUsers) {
                users.clear();
                for (User user : allUsers) {
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
                    adapter = new UserAdapter(users, ManageUsersActivity.this);
                    rvUsers.setAdapter(adapter);
                }
                loadStatistics();
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(ManageUsersActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStatistics() {
        tvTotalUsers.setText(String.valueOf(users.size()));
        int[] activeCount = {0};
        int[] totalOrders = {0};
        long[] totalRevenue = {0};
        int[] completed = {0};
        
        repository.getAllOrders(new DataRepository.DataCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> allOrders) {
                for (Order order : allOrders) {
                    for (User user : users) {
                        if (order.getUserId().equals(user.getId())) {
                            if (activeCount[0] == 0 || !userExistsInList(activeCount[0], user.getId())) {
                                activeCount[0]++;
                            }
                            totalOrders[0]++;
                            if ("delivered".equals(order.getStatus())) {
                                totalRevenue[0] += (long) order.getTotalAmount();
                            }
                            break;
                        }
                    }
                }
                tvActiveUsers.setText(String.valueOf(activeCount[0]));
                tvTotalOrders.setText(String.valueOf(totalOrders[0]));
                tvTotalRevenue.setText(formatCurrency(totalRevenue[0]));
            }
            @Override
            public void onError(Exception e) {
                // Use default values
            }
        });
    }
    
    private boolean userExistsInList(int count, String userId) {
        if (count == 0) return false;
        for (User user : users) {
            if (user.getId().equals(userId)) return true;
        }
        return false;
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
