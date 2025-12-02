package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.MainActivity;
import com.example.otech.R;
import com.example.otech.model.Order;
import com.example.otech.util.Constants;
import com.example.otech.util.FormatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvOrderId, tvOrderDate, tvOrderStatus, tvTotalAmount;
    private TextView tvCustomerName, tvCustomerPhone, tvDeliveryAddress;
    private TextView tvPaymentMethod;
    private MaterialButton btnViewOrder, btnBackHome;
    
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Get order from intent
        order = (Order) getIntent().getSerializableExtra(Constants.EXTRA_ORDER);
        if (order == null) {
            finish();
            return;
        }

        initViews();
        displayOrderInfo();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        btnViewOrder = findViewById(R.id.btnViewOrder);
        btnBackHome = findViewById(R.id.btnBackHome);

        toolbar.setNavigationOnClickListener(v -> {
            navigateToHome();
        });
    }

    private void displayOrderInfo() {
        // Order info
        tvOrderId.setText(order.getId());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvOrderDate.setText(sdf.format(order.getOrderDate()));
        
        // Status with color
        String status = getStatusText(order.getStatus());
        tvOrderStatus.setText(status);
        
        tvTotalAmount.setText(FormatUtils.formatCurrency(order.getTotalAmount()));

        // Delivery info - get from order or intent extras
        String customerName = getIntent().getStringExtra("customer_name");
        String customerPhone = order.getPhone();
        String deliveryAddress = order.getDeliveryAddress();
        String paymentMethod = getIntent().getStringExtra("payment_method");

        if (customerName != null) {
            tvCustomerName.setText(customerName);
        }
        
        if (customerPhone != null) {
            tvCustomerPhone.setText(customerPhone);
        }
        
        if (deliveryAddress != null) {
            tvDeliveryAddress.setText(deliveryAddress);
        }
        
        if (paymentMethod != null) {
            tvPaymentMethod.setText(paymentMethod);
        } else {
            tvPaymentMethod.setText("Thanh toán khi nhận hàng (COD)");
        }
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending":
                return "Chờ xác nhận";
            case "confirmed":
                return "Đã xác nhận";
            case "shipping":
                return "Đang giao hàng";
            case "delivered":
                return "Đã giao hàng";
            case "cancelled":
                return "Đã hủy";
            default:
                return "Chờ xác nhận";
        }
    }

    private void setupListeners() {
        btnViewOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, OrderDetailActivity.class);
            intent.putExtra(Constants.EXTRA_ORDER, order);
            startActivity(intent);
            finish();
        });

        btnBackHome.setOnClickListener(v -> {
            navigateToHome();
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(OrderConfirmationActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateToHome();
    }
}
