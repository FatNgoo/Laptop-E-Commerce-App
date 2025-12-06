package com.example.otech.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.R;
import com.example.otech.model.Address;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProfileInfoActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText etFullName, etEmail, etPhone, etAddress;
    private MaterialButton btnSave;
    
    private DataRepository repository;
    private String currentUsername;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        repository = DataRepository.getInstance(getApplicationContext());
        
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUsername = prefs.getString(Constants.KEY_USERNAME, "");
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        initViews();
        loadUserInfo();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);
        
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadUserInfo() {
        repository.getUserById(currentUserId, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    etFullName.setText(user.getFullName());
                    etEmail.setText(user.getEmail());
                    etPhone.setText(user.getPhone());
                    etAddress.setText(user.getAddress());
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ProfileInfoActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    private void saveUserInfo() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }

        repository.getUserById(currentUserId, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    user.setFullName(fullName);
                    user.setPhone(phone);
                    user.setAddress(address);
                    
                    repository.updateUser(user, new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            // Auto-add address to address book if address is provided
                            if (!address.isEmpty()) {
                                repository.getUserAddresses(currentUserId, new DataRepository.DataCallback<List<Address>>() {
                                    @Override
                                    public void onSuccess(List<Address> addresses) {
                                        // Check if this address already exists
                                        boolean addressExists = false;
                                        for (Address addr : addresses) {
                                            if (addr.getAddressDetail().equals(address)) {
                                                addressExists = true;
                                                break;
                                            }
                                        }
                                        
                                        // Add new address if it doesn't exist
                                        if (!addressExists) {
                                            Address newAddress = new Address(
                                                java.util.UUID.randomUUID().toString(),
                                                currentUserId,
                                                fullName,
                                                phone,
                                                address, // addressDetail
                                                addresses.isEmpty() // isDefault
                                            );
                                            repository.insertAddress(newAddress, new DataRepository.VoidCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    Toast.makeText(ProfileInfoActivity.this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    Toast.makeText(ProfileInfoActivity.this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(ProfileInfoActivity.this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Toast.makeText(ProfileInfoActivity.this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(ProfileInfoActivity.this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ProfileInfoActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ProfileInfoActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
