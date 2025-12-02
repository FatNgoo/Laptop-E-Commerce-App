package com.example.otech.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.R;
import com.example.otech.model.Address;
import com.example.otech.model.User;
import com.example.otech.repository.MockDataStore;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ProfileInfoActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText etFullName, etEmail, etPhone, etAddress;
    private MaterialButton btnSave;
    
    private MockDataStore dataStore;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        dataStore = MockDataStore.getInstance();
        
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUsername = prefs.getString(Constants.KEY_USERNAME, "");

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
        User user = dataStore.getUserByUsername(currentUsername);
        if (user != null) {
            etFullName.setText(user.getFullName());
            etEmail.setText(user.getEmail());
            etPhone.setText(user.getPhone());
            etAddress.setText(user.getAddress());
        }
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

        User user = dataStore.getUserByUsername(currentUsername);
        if (user != null) {
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setAddress(address);
            
            // Auto-add address to address book if address is provided
            if (!address.isEmpty()) {
                // Check if this address already exists
                boolean addressExists = false;
                ArrayList<Address> addresses = dataStore.getUserAddresses(user.getId());
                for (Address addr : addresses) {
                    if (addr.getAddressDetail().equals(address)) {
                        addressExists = true;
                        break;
                    }
                }
                
                // Add new address if it doesn't exist
                if (!addressExists) {
                    // Parse address or use simple format (city, district, ward, addressLine)
                    // For simplicity, use address as addressLine
                    dataStore.addAddress(user.getId(), fullName, phone, "", "", "", address, addresses.isEmpty());
                }
            }
            
            Toast.makeText(this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
