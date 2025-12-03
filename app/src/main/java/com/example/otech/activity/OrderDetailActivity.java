package com.example.otech.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.OrderProductAdapter;
import com.example.otech.model.Order;
import com.example.otech.repository.MockDataStore;
import com.example.otech.util.Constants;
import com.example.otech.util.FormatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class OrderDetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvOrderId, tvOrderDate, tvOrderStatus;
    private TextView tvReceiverName, tvReceiverPhone, tvDeliveryAddress;
    private TextView tvSubtotal, tvShippingFee, tvTotalAmount;
    private TextView tvCancelReason;
    private RecyclerView rvOrderProducts;
    private MaterialButton btnCancelOrder;
    private com.google.android.material.card.MaterialCardView cardCancelReason;
    
    // Admin controls
    private LinearLayout layoutAdminActions;
    private MaterialButton btnAdminReject, btnAdminAction;
    
    // Status icons
    private ImageView ivStatusPending, ivStatusProcessing, ivStatusShipping, ivStatusCompleted;

    private Order order;
    private MockDataStore dataStore;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        dataStore = MockDataStore.getInstance();

        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        // Get order from intent
        order = (Order) getIntent().getSerializableExtra(Constants.EXTRA_ORDER);
        
        // If order not passed directly, try to get by ID
        if (order == null) {
            String orderId = getIntent().getStringExtra(Constants.EXTRA_ORDER_ID);
            if (orderId != null) {
                order = dataStore.getOrderById(orderId);
            }
        }

        if (order == null) {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
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
        tvReceiverName = findViewById(R.id.tvReceiverName);
        tvReceiverPhone = findViewById(R.id.tvReceiverPhone);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvCancelReason = findViewById(R.id.tvCancelReason);
        cardCancelReason = findViewById(R.id.cardCancelReason);
        rvOrderProducts = findViewById(R.id.rvOrderProducts);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        MaterialButton btnViewMap = findViewById(R.id.btnViewMap);
        
        layoutAdminActions = findViewById(R.id.layoutAdminActions);
        btnAdminReject = findViewById(R.id.btnAdminReject);
        btnAdminAction = findViewById(R.id.btnAdminAction);
        
        ivStatusPending = findViewById(R.id.ivStatusPending);
        ivStatusProcessing = findViewById(R.id.ivStatusProcessing);
        ivStatusShipping = findViewById(R.id.ivStatusShipping);
        ivStatusCompleted = findViewById(R.id.ivStatusCompleted);
        
        // Setup map button
        btnViewMap.setOnClickListener(v -> openAddressInMap());
    }

    private void displayOrderInfo() {
        // Order basic info
        tvOrderId.setText("Mã đơn: #" + order.getId());
        tvOrderDate.setText("Ngày đặt: " + android.text.format.DateFormat.format("dd/MM/yyyy", order.getOrderDate()));
        
        // Status with color
        String status = order.getStatus();
        tvOrderStatus.setText(getStatusText(status));
        tvOrderStatus.setTextColor(getStatusColor(status));
        
        // Update status progress
        updateStatusProgress(status);

        // Delivery address (using User info or order data)
        tvReceiverName.setText("Khách hàng"); // Can get from User model if needed
        tvReceiverPhone.setText(order.getPhone());
        tvDeliveryAddress.setText(order.getDeliveryAddress());

        // Price summary
        tvSubtotal.setText(FormatUtils.formatCurrency(order.getTotalAmount()));
        tvShippingFee.setText("Miễn phí"); // Free shipping
        tvTotalAmount.setText(FormatUtils.formatCurrency(order.getTotalAmount()));

        // Products list
        OrderProductAdapter adapter = new OrderProductAdapter(this, order.getItems());
        rvOrderProducts.setLayoutManager(new LinearLayoutManager(this));
        rvOrderProducts.setAdapter(adapter);

        // Show cancel reason if order is cancelled
        if ("cancelled".equalsIgnoreCase(status) && order.getCancelReason() != null && !order.getCancelReason().isEmpty()) {
            cardCancelReason.setVisibility(View.VISIBLE);
            tvCancelReason.setText(order.getCancelReason());
        } else {
            cardCancelReason.setVisibility(View.GONE);
        }

        // Check user role
        com.example.otech.model.User user = dataStore.getUserById(currentUserId);
        boolean isAdmin = user != null && "admin".equals(user.getRole());

        if (isAdmin) {
            btnCancelOrder.setVisibility(View.GONE);
            layoutAdminActions.setVisibility(View.VISIBLE);
            
            if ("pending".equalsIgnoreCase(status)) {
                btnAdminReject.setVisibility(View.VISIBLE);
                btnAdminAction.setVisibility(View.VISIBLE);
                btnAdminAction.setText("Duyệt đơn");
            } else if ("processing".equalsIgnoreCase(status)) {
                btnAdminReject.setVisibility(View.GONE);
                btnAdminAction.setVisibility(View.VISIBLE);
                btnAdminAction.setText("Giao hàng");
            } else if ("shipping".equalsIgnoreCase(status)) {
                btnAdminReject.setVisibility(View.GONE);
                btnAdminAction.setVisibility(View.VISIBLE);
                btnAdminAction.setText("Hoàn thành");
            } else {
                layoutAdminActions.setVisibility(View.GONE);
            }
        } else {
            layoutAdminActions.setVisibility(View.GONE);
            // Show/hide cancel button based on status
            if ("pending".equalsIgnoreCase(order.getStatus())) {
                btnCancelOrder.setVisibility(View.VISIBLE);
            } else {
                btnCancelOrder.setVisibility(View.GONE);
            }
        }

        // Admin controls visibility
        if (isUserAdmin()) {
            layoutAdminActions.setVisibility(View.VISIBLE);
            btnAdminAction.setText(status.equals("completed") ? "Xem lại" : "Tiếp tục");
        } else {
            layoutAdminActions.setVisibility(View.GONE);
        }
    }

    private boolean isUserAdmin() {
        // Check if the current user is an admin
        return "admin".equalsIgnoreCase(currentUserId);
    }

    private String getStatusText(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return "Chờ xử lý";
            case "processing":
                return "Đang xử lý";
            case "shipping":
                return "Đang giao hàng";
            case "completed":
                return "Hoàn thành";
            case "cancelled":
                return "Đã hủy";
            default:
                return status;
        }
    }

    private int getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return getResources().getColor(R.color.colorSecondary, null);
            case "processing":
                return getResources().getColor(R.color.colorPrimary, null);
            case "shipping":
                return getResources().getColor(R.color.colorPrimary, null);
            case "completed":
                return getResources().getColor(R.color.colorSuccess, null);
            case "cancelled":
                return getResources().getColor(R.color.colorPriceRed, null);
            default:
                return getResources().getColor(R.color.text_secondary, null);
        }
    }

    private void updateStatusProgress(String status) {
        // Reset all to inactive state
        resetStatusIcons();

        // Activate icons based on status
        switch (status.toLowerCase()) {
            case "pending":
                activateStatusIcon(ivStatusPending);
                break;
            case "processing":
                activateStatusIcon(ivStatusPending);
                activateStatusIcon(ivStatusProcessing);
                break;
            case "shipping":
                activateStatusIcon(ivStatusPending);
                activateStatusIcon(ivStatusProcessing);
                activateStatusIcon(ivStatusShipping);
                break;
            case "completed":
                activateStatusIcon(ivStatusPending);
                activateStatusIcon(ivStatusProcessing);
                activateStatusIcon(ivStatusShipping);
                activateStatusIcon(ivStatusCompleted);
                break;
        }
    }

    private void resetStatusIcons() {
        ivStatusPending.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light, null));
        ivStatusProcessing.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light, null));
        ivStatusShipping.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light, null));
        ivStatusCompleted.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light, null));
    }

    private void activateStatusIcon(ImageView imageView) {
        imageView.setBackgroundTintList(getResources().getColorStateList(R.color.colorSecondary, null));
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());

        btnCancelOrder.setOnClickListener(v -> showCancelOrderDialog());

        btnAdminReject.setOnClickListener(v -> showRejectOrderDialog());

        btnAdminAction.setOnClickListener(v -> {
            String status = order.getStatus();
            String newStatus = "";
            String message = "";
            
            if ("pending".equalsIgnoreCase(status)) {
                newStatus = "processing";
                message = "Đã duyệt đơn hàng";
            } else if ("processing".equalsIgnoreCase(status)) {
                newStatus = "shipping";
                message = "Đã chuyển sang giao hàng";
            } else if ("shipping".equalsIgnoreCase(status)) {
                newStatus = "completed";
                message = "Đã hoàn thành đơn hàng";
            }
            
            if (!newStatus.isEmpty()) {
                updateOrderStatus(newStatus, message);
            }
        });
    }

    private void updateOrderStatus(String newStatus, String message) {
        order.setStatus(newStatus);
        dataStore.updateOrderStatus(order.getId(), newStatus);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        displayOrderInfo();
    }

    private void showCancelOrderDialog() {
        // Create EditText for cancel reason
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Nhập lý do hủy đơn");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setMinLines(3);
        input.setMaxLines(5);
        
        android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 20, 50, 20);
        input.setLayoutParams(lp);

        new AlertDialog.Builder(this)
                .setTitle("Hủy đơn hàng")
                .setMessage("Vui lòng nhập lý do hủy đơn hàng:")
                .setView(input)
                .setPositiveButton("Xác nhận hủy", (dialog, which) -> {
                    String reason = input.getText().toString().trim();
                    if (reason.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập lý do hủy", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    // Update order status to cancelled with reason
                    boolean success = dataStore.cancelOrder(order.getId(), reason);
                    if (success) {
                        order.setStatus("cancelled");
                        order.setCancelReason(reason);
                        
                        // Create notification for order cancelled
                        dataStore.createNotification(
                            order.getUserId(),
                            "Đơn hàng đã bị hủy",
                            "Đơn hàng #" + order.getId() + " đã được hủy. Lý do: " + reason,
                            "ORDER_CANCELLED",
                            order.getId()
                        );
                        
                        Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                        
                        // Refresh display
                        displayOrderInfo();
                    } else {
                        Toast.makeText(this, "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void showRejectOrderDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Từ chối đơn hàng")
                .setMessage("Bạn có chắc chắn muốn từ chối đơn hàng này?")
                .setPositiveButton("Từ chối", (dialog, which) -> {
                    // Update order status to rejected
                    order.setStatus("rejected");
                    dataStore.updateOrderStatus(order.getId(), "rejected");
                    
                    Toast.makeText(this, "Đã từ chối đơn hàng", Toast.LENGTH_SHORT).show();
                    
                    // Refresh display
                    displayOrderInfo();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void openReviewActivity() {
        // TODO: Implement opening review activity for the completed order
        Toast.makeText(this, "Mở hoạt động đánh giá đơn hàng", Toast.LENGTH_SHORT).show();
    }

    private void continueOrderProcessing() {
        // TODO: Implement logic to continue processing the order
        Toast.makeText(this, "Tiếp tục xử lý đơn hàng", Toast.LENGTH_SHORT).show();
    }

    private void openAddressInMap() {
        String address = order.getDeliveryAddress();
        if (address == null || address.trim().isEmpty()) {
            Toast.makeText(this, "Địa chỉ không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Open MapViewActivity to show address
        android.content.Intent intent = new android.content.Intent(this, MapViewActivity.class);
        intent.putExtra("address", address);
        
        // Mock coordinates for demonstration (in real app, use Geocoding API)
        // For now, use center of Vietnam as default
        intent.putExtra("latitude", 21.0285);
        intent.putExtra("longitude", 105.8542);
        
        startActivity(intent);
    }
}
