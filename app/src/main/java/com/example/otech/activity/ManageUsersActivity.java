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
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvUsers;
    private LinearLayout layoutEmptyState;
    private TextView tvTotalUsers;
    private com.google.android.material.textfield.TextInputEditText etSearchUsers;
    private UserAdapter adapter;
    private DataRepository repository;
    private ArrayList<User> users;
    private ArrayList<User> filteredUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        repository = DataRepository.getInstance(getApplicationContext());

        initViews();
        setupToolbar();
        loadUsers();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvUsers = findViewById(R.id.rvUsers);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        etSearchUsers = findViewById(R.id.etSearchUsers);

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        setupSearch();
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
        filteredUsers = new ArrayList<>();
        repository.getAllUsers(new DataRepository.DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> allUsers) {
                users.clear();
                for (User user : allUsers) {
                    if (!user.isAdmin()) {
                        users.add(user);
                    }
                }
                filteredUsers = new ArrayList<>(users);
                tvTotalUsers.setText(String.valueOf(users.size()));
                
                if (filteredUsers.isEmpty()) {
                    rvUsers.setVisibility(View.GONE);
                    layoutEmptyState.setVisibility(View.VISIBLE);
                } else {
                    rvUsers.setVisibility(View.VISIBLE);
                    layoutEmptyState.setVisibility(View.GONE);
                    adapter = new UserAdapter(filteredUsers, ManageUsersActivity.this);
                    rvUsers.setAdapter(adapter);
                }
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(ManageUsersActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        etSearchUsers.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterUsers(String query) {
        filteredUsers.clear();
        if (query.isEmpty()) {
            filteredUsers.addAll(users);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (User user : users) {
                if (user.getFullName().toLowerCase().contains(lowerQuery) ||
                    user.getEmail().toLowerCase().contains(lowerQuery)) {
                    filteredUsers.add(user);
                }
            }
        }
        
        if (filteredUsers.isEmpty()) {
            rvUsers.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvUsers.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
        }
        
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
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
