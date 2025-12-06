package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.MainActivity;
import com.example.otech.R;
import com.example.otech.adapter.OrderProductAdapter;
import com.example.otech.model.Address;
import com.example.otech.model.CartItem;
import com.example.otech.model.Order;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.example.otech.util.FormatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    // Step 1 views
    private MaterialToolbar toolbar;
    private TextInputEditText etFullName, etPhone, etEmail, etAddress;
    private MaterialButton btnContinue, btnSelectAddress;
    
    // Step 2 views
    private TextView tvCustomerName, tvCustomerPhone, tvCustomerEmail, tvDeliveryAddress;
    private TextView tvSubtotal, tvShippingFee, tvTotalAmount;
    private RecyclerView rvCheckoutProducts;
    private RadioGroup rgPaymentMethod;
    private MaterialButton btnPlaceOrder;
    
    private DataRepository repository;
    private String currentUserId;
    private ArrayList<CartItem> selectedItems;
    private int currentStep = 1;
    
    // Customer data
    private String customerName, customerPhone, customerEmail, deliveryAddress;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        repository = DataRepository.getInstance(this);
        
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");
        
        // Get selected items from intent
        selectedItems = (ArrayList<CartItem>) getIntent().getSerializableExtra(Constants.EXTRA_SELECTED_ITEMS);
        if (selectedItems == null || selectedItems.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm nào được chọn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Check if returning from step 2
        currentStep = getIntent().getIntExtra(Constants.EXTRA_CHECKOUT_STEP, 1);
        
        if (currentStep == 1) {
            showStep1();
        } else {
            showStep2();
        }
    }

    private void showStep1() {
        setContentView(R.layout.activity_checkout_step1);
        currentStep = 1;
        
        // Init Step 1 views
        toolbar = findViewById(R.id.toolbar);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        btnContinue = findViewById(R.id.btnContinue);
        btnSelectAddress = findViewById(R.id.btnSelectAddress);
        
        toolbar.setNavigationOnClickListener(v -> finish());
        
        loadUserInfo();
        setupStep1Listeners();
    }
    
    private void showStep2() {
        setContentView(R.layout.activity_checkout_step2);
        currentStep = 2;
        
        // Init Step 2 views
        toolbar = findViewById(R.id.toolbar);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        
        toolbar.setNavigationOnClickListener(v -> {
            showStep1();
        });
        
        displayStep2Info();
        setupStep2Listeners();
    }

    private void loadUserInfo() {
        repository.getUserById(currentUserId, new DataRepository.DataCallback<User>() {
            @Override
            public void onSuccess(User currentUser) {
                if (currentUser != null) {
                    etFullName.setText(currentUser.getFullName());
                    etPhone.setText(currentUser.getPhone());
                    etEmail.setText(currentUser.getEmail());
                    // Restore previously entered data if available
                    if (deliveryAddress != null && !deliveryAddress.isEmpty()) {
                        etAddress.setText(deliveryAddress);
                    } else {
                        // Load default address from address book or user profile
                        repository.getDefaultAddress(currentUserId, new DataRepository.DataCallback<Address>() {
                            @Override
                            public void onSuccess(Address defaultAddress) {
                                if (defaultAddress != null) {
                                    etAddress.setText(defaultAddress.getAddressDetail());
                                } else if (currentUser.getAddress() != null && !currentUser.getAddress().isEmpty()) {
                                    etAddress.setText(currentUser.getAddress());
                                }
                            }
                            
                            @Override
                            public void onError(Exception e) {
                                // If no default address, check user profile
                                if (currentUser.getAddress() != null && !currentUser.getAddress().isEmpty()) {
                                    etAddress.setText(currentUser.getAddress());
                                }
                            }
                        });
                    }
                }
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(CheckoutActivity.this, "Không tải được thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupStep1Listeners() {
        btnSelectAddress.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, AddressBookActivity.class);
            intent.putExtra("select_mode", true);
            startActivityForResult(intent, 100);
        });
        
        btnContinue.setOnClickListener(v -> {
            if (validateStep1()) {
                // Save customer data
                customerName = etFullName.getText().toString().trim();
                customerPhone = etPhone.getText().toString().trim();
                customerEmail = etEmail.getText().toString().trim();
                deliveryAddress = etAddress.getText().toString().trim();
                
                // Move to Step 2
                showStep2();
            }
        });
    }
    
    private boolean validateStep1() {
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        
        if (name.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return false;
        }
        
        if (phone.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return false;
        }
        
        if (phone.length() < 10) {
            etPhone.setError("Số điện thoại không hợp lệ");
            etPhone.requestFocus();
            return false;
        }
        
        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return false;
        }
        
        if (!email.contains("@")) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return false;
        }
        
        if (address.isEmpty()) {
            etAddress.setError("Vui lòng nhập địa chỉ giao hàng");
            etAddress.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void displayStep2Info() {
        // Display selected products
        OrderProductAdapter adapter = new OrderProductAdapter(this, selectedItems);
        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutProducts.setAdapter(adapter);
        
        // Display customer info
        tvCustomerName.setText(customerName);
        tvCustomerPhone.setText(customerPhone);
        tvCustomerEmail.setText(customerEmail);
        tvDeliveryAddress.setText(deliveryAddress);
        
        // Calculate and display total
        calculateTotal();
    }

    private void calculateTotal() {
        double subtotal = 0;
        
        for (CartItem item : selectedItems) {
            subtotal += item.getProduct().getPrice() * item.getQuantity();
        }
        
        totalAmount = subtotal; // shipping is free
        
        tvSubtotal.setText(String.format("₫%,.0f", subtotal));
        tvShippingFee.setText("Miễn phí");
        tvTotalAmount.setText(String.format("₫%,.0f", totalAmount));
    }

    private void setupStep2Listeners() {
        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        // Get selected payment method
        int selectedPaymentId = rgPaymentMethod.getCheckedRadioButtonId();
        String paymentMethod = "Thanh toán khi nhận hàng (COD)"; // Default
        
        if (selectedPaymentId == R.id.rbBankTransfer) {
            paymentMethod = "Chuyển khoản ngân hàng";
        } else if (selectedPaymentId == R.id.rbCreditCard) {
            paymentMethod = "Thẻ tín dụng";
        } else if (selectedPaymentId == R.id.rbEWallet) {
            paymentMethod = "Ví điện tử";
        }

        // Disable button to prevent double submission
        btnPlaceOrder.setEnabled(false);
        
        // Create order using Room Database
        String finalPaymentMethod = paymentMethod;
        repository.checkoutSelectedItems(currentUserId, selectedItems, deliveryAddress, customerPhone, 
            new DataRepository.DataCallback<Order>() {
                @Override
                public void onSuccess(Order order) {
                    // Order created successfully
                    // Create notification for order placed
                    repository.createNotification(
                        currentUserId,
                        "Đơn hàng đã được đặt",
                        "Đơn hàng #" + order.getId() + " đã được đặt thành công",
                        "ORDER_PLACED",
                        order.getId(),
                        new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                // Navigate to Order Confirmation screen
                                Intent intent = new Intent(CheckoutActivity.this, OrderConfirmationActivity.class);
                                intent.putExtra(Constants.EXTRA_ORDER, order);
                                intent.putExtra("customer_name", customerName);
                                intent.putExtra("payment_method", finalPaymentMethod);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            
                            @Override
                            public void onError(Exception e) {
                                // Even if notification fails, still proceed to confirmation
                                Intent intent = new Intent(CheckoutActivity.this, OrderConfirmationActivity.class);
                                intent.putExtra(Constants.EXTRA_ORDER, order);
                                intent.putExtra("customer_name", customerName);
                                intent.putExtra("payment_method", finalPaymentMethod);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    );
                }
                
                @Override
                public void onError(Exception e) {
                    // Re-enable button on error
                    btnPlaceOrder.setEnabled(true);
                    
                    // Order failed - likely due to insufficient stock
                    new android.app.AlertDialog.Builder(CheckoutActivity.this)
                        .setTitle("Đặt hàng thất bại")
                        .setMessage("Một số sản phẩm trong đơn hàng đã hết hoặc không đủ số lượng. Vui lòng quay lại giỏ hàng và kiểm tra lại.")
                        .setPositiveButton("Quay lại giỏ hàng", (dialog, which) -> {
                            finish();
                        })
                        .setCancelable(false)
                        .show();
                }
            }
        );
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Address selectedAddress = (Address) data.getSerializableExtra("selected_address");
            if (selectedAddress != null) {
                etFullName.setText(selectedAddress.getRecipientName());
                etPhone.setText(selectedAddress.getPhone());
                etAddress.setText(selectedAddress.getAddressDetail());
            }
        }
    }
}
