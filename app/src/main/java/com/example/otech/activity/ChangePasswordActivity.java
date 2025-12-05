package com.example.otech.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.R;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private MaterialButton btnChangePassword;
    
    private DataRepository repository;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        repository = DataRepository.getInstance(this);
        
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUsername = prefs.getString(Constants.KEY_USERNAME, "");

        initViews();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            etCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            etCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        // Verify current password
        btnChangePassword.setEnabled(false);
        repository.getUserByUsername(currentUsername, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null && user.getPassword().equals(currentPassword)) {
                    user.setPassword(newPassword);
                    repository.updateUser(user, new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        
                        @Override
                        public void onError(Exception e) {
                            btnChangePassword.setEnabled(true);
                            Toast.makeText(ChangePasswordActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    btnChangePassword.setEnabled(true);
                    etCurrentPassword.setError("Mật khẩu hiện tại không đúng");
                    etCurrentPassword.requestFocus();
                }
            }
            
            @Override
            public void onError(Exception e) {
                btnChangePassword.setEnabled(true);
                Toast.makeText(ChangePasswordActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
