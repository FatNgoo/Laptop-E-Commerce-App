package com.example.otech.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.R;
import com.example.otech.model.User;
import com.example.otech.repository.MockDataStore;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etNewPassword, etConfirmNewPassword;
    private MaterialButton btnResetPassword, btnBackToLogin;
    private MockDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        dataStore = MockDataStore.getInstance();
        
        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
    }

    private void setupListeners() {
        btnResetPassword.setOnClickListener(v -> handleResetPassword());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleResetPassword() {
        String username = etUsername.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập username");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            etConfirmNewPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmNewPassword.requestFocus();
            return;
        }

        // Check if username exists
        User user = dataStore.getUserByUsername(username);
        
        if (user == null) {
            Toast.makeText(this, "Username không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reset password
        boolean success = dataStore.resetPassword(username, newPassword);
        
        if (success) {
            Toast.makeText(this, "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}
