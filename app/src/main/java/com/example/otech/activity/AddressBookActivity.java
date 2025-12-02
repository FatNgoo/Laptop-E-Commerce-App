package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.AddressAdapter;
import com.example.otech.model.Address;
import com.example.otech.repository.MockDataStore;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddressBookActivity extends AppCompatActivity implements AddressAdapter.OnAddressActionListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvAddresses;
    private TextView tvEmptyAddresses;
    private MaterialButton btnAddAddress;
    
    private MockDataStore dataStore;
    private String currentUserId;
    private AddressAdapter adapter;
    private ArrayList<Address> addresses;
    private boolean isSelectMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        dataStore = MockDataStore.getInstance();
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");
        
        // Check if in select mode (called from checkout)
        isSelectMode = getIntent().getBooleanExtra("select_mode", false);

        initViews();
        loadAddresses();
        setupRecyclerView();
        setupListeners();
        checkEmptyState();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvAddresses = findViewById(R.id.rvAddresses);
        tvEmptyAddresses = findViewById(R.id.tvEmptyAddresses);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        
        if (isSelectMode) {
            toolbar.setTitle("Chọn địa chỉ");
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadAddresses() {
        addresses = dataStore.getUserAddresses(currentUserId);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAddresses.setLayoutManager(layoutManager);
        
        adapter = new AddressAdapter(this, addresses, this);
        rvAddresses.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddAddress.setOnClickListener(v -> showAddEditAddressDialog(null));
    }

    private void checkEmptyState() {
        if (addresses.isEmpty()) {
            tvEmptyAddresses.setVisibility(View.VISIBLE);
            rvAddresses.setVisibility(View.GONE);
        } else {
            tvEmptyAddresses.setVisibility(View.GONE);
            rvAddresses.setVisibility(View.VISIBLE);
        }
    }

    private void showAddEditAddressDialog(Address existingAddress) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null);
        
        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextInputEditText edtName = dialogView.findViewById(R.id.edt_name);
        TextInputEditText edtPhone = dialogView.findViewById(R.id.edt_phone);
        TextInputEditText edtCity = dialogView.findViewById(R.id.edt_city);
        TextInputEditText edtDistrict = dialogView.findViewById(R.id.edt_district);
        TextInputEditText edtWard = dialogView.findViewById(R.id.edt_ward);
        TextInputEditText edtAddressLine = dialogView.findViewById(R.id.edt_address_line);
        MaterialCheckBox checkboxDefault = dialogView.findViewById(R.id.checkbox_default);
        
        // Fill data if editing
        if (existingAddress != null) {
            tvTitle.setText("Chỉnh sửa địa chỉ");
            edtName.setText(existingAddress.getRecipientName());
            edtPhone.setText(existingAddress.getPhone());
            
            // Parse address (simple parsing)
            String[] parts = existingAddress.getAddressDetail().split(", ");
            if (parts.length >= 4) {
                edtAddressLine.setText(parts[0]);
                edtWard.setText(parts[1]);
                edtDistrict.setText(parts[2]);
                edtCity.setText(parts[3]);
            }
            
            checkboxDefault.setChecked(existingAddress.isDefault());
        }
        
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String city = edtCity.getText().toString().trim();
            String district = edtDistrict.getText().toString().trim();
            String ward = edtWard.getText().toString().trim();
            String addressLine = edtAddressLine.getText().toString().trim();
            boolean isDefault = checkboxDefault.isChecked();
            
            // Validation
            if (name.isEmpty() || phone.isEmpty() || city.isEmpty() || 
                district.isEmpty() || ward.isEmpty() || addressLine.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (existingAddress == null) {
                // Add new
                dataStore.addAddress(currentUserId, name, phone, city, district, ward, addressLine, isDefault);
                Toast.makeText(this, "Đã thêm địa chỉ mới", Toast.LENGTH_SHORT).show();
            } else {
                // Update existing
                String fullAddress = addressLine + ", " + ward + ", " + district + ", " + city;
                existingAddress.setRecipientName(name);
                existingAddress.setPhone(phone);
                existingAddress.setAddressDetail(fullAddress);
                existingAddress.setDefault(isDefault);
                dataStore.updateAddress(existingAddress);
                Toast.makeText(this, "Đã cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
            }
            
            loadAddresses();
            adapter.updateAddresses(addresses);
            checkEmptyState();
            dialog.dismiss();
        });
        
        dialog.show();
    }

    @Override
    public void onSetDefault(Address address) {
        dataStore.setDefaultAddress(currentUserId, address.getId());
        loadAddresses();
        adapter.updateAddresses(addresses);
        Toast.makeText(this, "Đã đặt làm địa chỉ mặc định", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEdit(Address address) {
        showAddEditAddressDialog(address);
    }

    @Override
    public void onDelete(Address address) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá địa chỉ")
                .setMessage("Bạn có chắc muốn xoá địa chỉ này?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    dataStore.deleteAddress(currentUserId, address.getId());
                    loadAddresses();
                    adapter.updateAddresses(addresses);
                    checkEmptyState();
                    Toast.makeText(this, "Đã xoá địa chỉ", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    @Override
    public void onSelect(Address address) {
        if (isSelectMode) {
            // Return selected address to calling activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_address", address);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
