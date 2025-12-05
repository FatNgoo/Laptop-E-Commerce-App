package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.MainActivity;
import com.example.otech.R;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvUserName, tvUserEmail;
    private LinearLayout layoutNotifications, layoutOrderHistory, layoutProfileInfo, layoutAddressBook, 
                         layoutChangePassword, layoutSupport, layoutTerms, layoutLogout;
    private BottomNavigationView bottomNavigation;
    
    private DataRepository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        repository = DataRepository.getInstance(this);
        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);

        initViews();
        loadUserInfo();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        layoutNotifications = findViewById(R.id.layoutNotifications);
        layoutOrderHistory = findViewById(R.id.layoutOrderHistory);
        layoutProfileInfo = findViewById(R.id.layoutProfileInfo);
        layoutAddressBook = findViewById(R.id.layoutAddressBook);
        layoutChangePassword = findViewById(R.id.layoutChangePassword);
        layoutSupport = findViewById(R.id.layoutSupport);
        layoutTerms = findViewById(R.id.layoutTerms);
        layoutLogout = findViewById(R.id.layoutLogout);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
        
        setupBottomNavigation();
    }
    
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(this, FilterProductsActivity.class));
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(this, WishlistActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            
            return false;
        });
    }

    private void loadUserInfo() {
        String username = sharedPreferences.getString(Constants.KEY_USERNAME, "");
        repository.getUserByUsername(username, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    tvUserName.setText(user.getFullName());
                    tvUserEmail.setText(user.getEmail());
                }
            }
            
            @Override
            public void onError(Exception e) {
                // Silent fail for profile load
            }
        });
    }

    private void setupListeners() {
        layoutNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });

        layoutOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        layoutProfileInfo.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileInfoActivity.class);
            startActivity(intent);
        });

        layoutAddressBook.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddressBookActivity.class);
            startActivity(intent);
        });

        layoutChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        layoutSupport.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SupportActivity.class);
            startActivity(intent);
        });

        layoutTerms.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, TermsActivity.class);
            startActivity(intent);
        });

        layoutLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
            .setPositiveButton("Đăng xuất", (dialog, which) -> logout())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
