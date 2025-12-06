package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.NotificationAdapter;
import com.example.otech.model.Notification;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;

import java.util.List;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity implements NotificationAdapter.OnNotificationClickListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvNotifications;
    private LinearLayout layoutEmpty;

    private DataRepository repository;
    private NotificationAdapter adapter;
    private ArrayList<Notification> notifications;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        repository = DataRepository.getInstance(this);
        
        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        initViews();
        loadNotifications();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvNotifications = findViewById(R.id.rvNotifications);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        setSupportActionBar(toolbar);
    }

    private void loadNotifications() {
        repository.getUserNotifications(currentUserId, new DataRepository.DataCallback<List<Notification>>() {
            @Override
            public void onSuccess(List<Notification> notificationList) {
                notifications = new ArrayList<>(notificationList);
                
                if (notifications.isEmpty()) {
                    rvNotifications.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    rvNotifications.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);
                    
                    LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationsActivity.this);
                    rvNotifications.setLayoutManager(layoutManager);
                    
                    adapter = new NotificationAdapter(NotificationsActivity.this, notifications, NotificationsActivity.this);
                    rvNotifications.setAdapter(adapter);
                }
            }
            
            @Override
            public void onError(Exception e) {
                rvNotifications.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onNotificationClick(Notification notification, int position) {
        // Mark as read
        if (!notification.isRead()) {
            notification.setRead(true);
            repository.markNotificationAsRead(notification.getId(), new DataRepository.VoidCallback() {
                @Override
                public void onSuccess() {
                    adapter.notifyItemChanged(position);
                }
                
                @Override
                public void onError(Exception e) {
                    // Silent fail
                }
            });
        }
        
        // Navigate to order detail if it has orderId
        if (notification.getOrderId() != null && !notification.getOrderId().isEmpty()) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(Constants.EXTRA_ORDER_ID, notification.getOrderId());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload notifications in case status changed
        loadNotifications();
    }
}
