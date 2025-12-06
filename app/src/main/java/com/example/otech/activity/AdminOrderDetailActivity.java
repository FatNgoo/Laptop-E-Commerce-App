package com.example.otech.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
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
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.example.otech.util.FormatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

public class AdminOrderDetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvOrderId, tvOrderDate, tvCustomerName, tvCustomerPhone, tvDeliveryAddress;
    private TextView tvSubtotal, tvShippingFee, tvTotalAmount, tvCancelReason;
    private Chip chipOrderStatus;
    private RecyclerView rvOrderProducts;
    private MaterialButton btnProcessOrder, btnCancelOrder;
    private MaterialCardView cardCancelReason;
    private LinearLayout layoutActionButtons;

    private Order order;
    private DataRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        repository = DataRepository.getInstance(getApplicationContext());

        // Get order from intent
        order = (Order) getIntent().getSerializableExtra(Constants.EXTRA_ORDER);

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
        chipOrderStatus = findViewById(R.id.chipOrderStatus);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        rvOrderProducts = findViewById(R.id.rvOrderProducts);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        cardCancelReason = findViewById(R.id.cardCancelReason);
        tvCancelReason = findViewById(R.id.tvCancelReason);
        btnProcessOrder = findViewById(R.id.btnProcessOrder);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        layoutActionButtons = findViewById(R.id.layoutActionButtons);
        MaterialButton btnViewMapAdmin = findViewById(R.id.btnViewMapAdmin);

        rvOrderProducts.setLayoutManager(new LinearLayoutManager(this));
        
        // Setup map button
        btnViewMapAdmin.setOnClickListener(v -> openAddressInMap());
    }

    private void displayOrderInfo() {
        // Order basic info
        tvOrderId.setText("#" + order.getId().substring(0, 8));
        tvOrderDate.setText(FormatUtils.formatDate(order.getOrderDate()));

        // Status
        String status = order.getStatus();
        chipOrderStatus.setText(getStatusText(status));
        chipOrderStatus.setChipBackgroundColorResource(getStatusColor(status));

        // Customer Info - Load asynchronously
        repository.getUserById(order.getUserId(), new DataRepository.DataCallback<com.example.otech.model.User>() {
            @Override
            public void onSuccess(com.example.otech.model.User user) {
                tvCustomerName.setText(user != null ? user.getFullName() : "N/A");
            }
            @Override
            public void onError(Exception e) {
                tvCustomerName.setText("N/A");
            }
        });
        
        tvCustomerPhone.setText(order.getPhone());
        tvDeliveryAddress.setText(order.getDeliveryAddress());

        // Products
        OrderProductAdapter adapter = new OrderProductAdapter(this, order.getItems());
        rvOrderProducts.setAdapter(adapter);

        // Payment info
        double subtotal = order.getTotalAmount() - 30000; // Assuming shipping fee is 30000
        tvSubtotal.setText(FormatUtils.formatCurrency(subtotal));
        tvShippingFee.setText(FormatUtils.formatCurrency(30000));
        tvTotalAmount.setText(FormatUtils.formatCurrency(order.getTotalAmount()));

        // Cancel reason
        if (status.equals(Constants.ORDER_STATUS_CANCELLED) && order.getCancelReason() != null) {
            cardCancelReason.setVisibility(View.VISIBLE);
            tvCancelReason.setText(order.getCancelReason());
        } else {
            cardCancelReason.setVisibility(View.GONE);
        }

        // Update action buttons based on status
        updateActionButtons(status);
    }

    private void updateActionButtons(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                btnProcessOrder.setVisibility(View.VISIBLE);
                btnProcessOrder.setText("Xác nhận đơn hàng");
                btnCancelOrder.setVisibility(View.VISIBLE);
                break;

            case "processing":
                btnProcessOrder.setVisibility(View.VISIBLE);
                btnProcessOrder.setText("Chuyển sang giao hàng");
                btnCancelOrder.setVisibility(View.VISIBLE);
                break;

            case "shipping":
                btnProcessOrder.setVisibility(View.VISIBLE);
                btnProcessOrder.setText("Xác nhận đã giao");
                btnCancelOrder.setVisibility(View.GONE);
                break;

            case "completed":
            case "cancelled":
                layoutActionButtons.setVisibility(View.GONE);
                break;

            default:
                btnProcessOrder.setVisibility(View.GONE);
                btnCancelOrder.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());

        btnProcessOrder.setOnClickListener(v -> processNextStatus());

        btnCancelOrder.setOnClickListener(v -> showCancelOrderDialog());
    }

    private void processNextStatus() {
        String currentStatus = order.getStatus();
        String newStatus = "";
        String message = "";

        switch (currentStatus.toLowerCase()) {
            case "pending":
                newStatus = Constants.ORDER_STATUS_PROCESSING;
                message = "Đã xác nhận đơn hàng";
                break;
            case "processing":
                newStatus = Constants.ORDER_STATUS_SHIPPING;
                message = "Đã chuyển sang giao hàng";
                break;
            case "shipping":
                newStatus = Constants.ORDER_STATUS_COMPLETED;
                message = "Đã hoàn thành đơn hàng";
                break;
        }

        if (!newStatus.isEmpty()) {
            updateOrderStatus(newStatus, message);
        }
    }

    private void showCancelOrderDialog() {
        // Create EditText for cancel reason
        final EditText input = new EditText(this);
        input.setHint("Nhập lý do hủy đơn");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setMinLines(3);
        input.setMaxLines(5);
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 20, 50, 20);
        input.setLayoutParams(lp);

        new AlertDialog.Builder(this)
                .setTitle("Hủy đơn hàng")
                .setMessage("Vui lòng nhập lý do hủy đơn hàng:")
                .setView(input)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    String reason = input.getText().toString().trim();
                    if (reason.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập lý do hủy", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cancelOrder(reason);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void cancelOrder(String reason) {
        repository.cancelOrder(order.getId(), reason, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                // Create notification for user
                repository.createNotification(
                    order.getUserId(),
                    "Đơn hàng đã bị hủy",
                    "Đơn hàng #" + order.getId() + " đã bị hủy bởi cửa hàng. Lý do: " + reason,
                    "ORDER_CANCELLED",
                    order.getId(),
                    new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AdminOrderDetailActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                            // Refresh order data
                            repository.getOrderById(order.getId(), new DataRepository.DataCallback<Order>() {
                                @Override
                                public void onSuccess(Order updatedOrder) {
                                    order = updatedOrder;
                                    displayOrderInfo();
                                }
                                @Override
                                public void onError(Exception e) {}
                            });
                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(AdminOrderDetailActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(AdminOrderDetailActivity.this, "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(String newStatus, String message) {
        order.setStatus(newStatus);
        
        repository.updateOrder(order, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                // Create notification based on status
                String notificationTitle = "";
                String notificationMessage = "";
                String notificationType = "";
                
                switch (newStatus.toLowerCase()) {
                    case "processing":
                        notificationTitle = "Đơn hàng đã được xác nhận";
                        notificationMessage = "Đơn hàng #" + order.getId() + " đã được xác nhận và đang được xử lý";
                        notificationType = "ORDER_CONFIRMED";
                        break;
                    case "shipping":
                        notificationTitle = "Đơn hàng đang giao";
                        notificationMessage = "Đơn hàng #" + order.getId() + " đang trên đường giao đến bạn";
                        notificationType = "ORDER_SHIPPING";
                        break;
                    case "completed":
                        notificationTitle = "Đơn hàng đã hoàn thành";
                        notificationMessage = "Đơn hàng #" + order.getId() + " đã được giao thành công";
                        notificationType = "ORDER_DELIVERED";
                        break;
                }
                
                if (!notificationTitle.isEmpty()) {
                    repository.createNotification(
                        order.getUserId(),
                        notificationTitle,
                        notificationMessage,
                        notificationType,
                        order.getId(),
                        new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(AdminOrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                // Refresh order data
                                repository.getOrderById(order.getId(), new DataRepository.DataCallback<Order>() {
                                    @Override
                                    public void onSuccess(Order updatedOrder) {
                                        order = updatedOrder;
                                        displayOrderInfo();
                                    }
                                    @Override
                                    public void onError(Exception e) {}
                                });
                            }
                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(AdminOrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    );
                }
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(AdminOrderDetailActivity.this, "Không thể cập nhật trạng thái", Toast.LENGTH_SHORT).show();
            }
        });
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
                return R.color.colorSecondary;
            case "processing":
                return R.color.colorPrimary;
            case "shipping":
                return R.color.colorPrimary;
            case "completed":
                return R.color.colorSuccess;
            case "cancelled":
                return R.color.colorPriceRed;
            default:
                return R.color.text_secondary;
        }
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
