package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.MainActivity;
import com.example.otech.R;
import com.example.otech.database.DatabaseInitializer;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private com.google.android.material.card.MaterialCardView btnGoogleLogin, btnFacebookLogin;
    private DataRepository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if already logged in
        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false)) {
            navigateToHome();
            return;
        }
        
        setContentView(R.layout.activity_login);

        repository = DataRepository.getInstance(this);
        
        // Initialize database với mock data nếu chưa có
        DatabaseInitializer.initializeDatabase(this, new DatabaseInitializer.InitCallback() {
            @Override
            public void onComplete() {
                // Database ready
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(LoginActivity.this, "Lỗi khởi tạo database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        
        btnGoogleLogin.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đăng nhập Google đang phát triển", Toast.LENGTH_SHORT).show();
        });
        
        btnFacebookLogin.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đăng nhập Facebook đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập username");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        // Disable button và hiển thị loading
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");
        
        // Attempt login - sử dụng background thread qua DataRepository
        repository.login(username, password, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");
                
                if (user != null) {
                    // Save login state
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
                    editor.putString(Constants.KEY_USER_ID, user.getId());
                    editor.putString(Constants.KEY_USERNAME, user.getUsername());
                    editor.putString(Constants.KEY_USER_ROLE, user.getRole());
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    
                    // Navigate based on role
                    if (user.isAdmin()) {
                        navigateToAdmin();
                    } else {
                        navigateToHome();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Username hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onError(Exception e) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");
                Toast.makeText(LoginActivity.this, "Lỗi đăng nhập: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToAdmin() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
