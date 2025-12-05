package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.CartAdapter;
import com.example.otech.model.CartItem;
import com.example.otech.model.Product;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.example.otech.util.FormatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvCartItems;
    private LinearLayout layoutEmptyCart, layoutCartContent;
    private TextView tvTotal;
    private MaterialButton btnCheckout;
    private CheckBox cbSelectAll;
    
    private DataRepository repository;
    private CartAdapter cartAdapter;
    private String currentUserId;
    private ArrayList<CartItem> selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        repository = DataRepository.getInstance(this);
        
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        initViews();
        setupRecyclerView();
        loadCart();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvCartItems = findViewById(R.id.rvCartItems);
        layoutEmptyCart = findViewById(R.id.layoutEmptyCart);
        layoutCartContent = findViewById(R.id.layoutCartContent);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        selectedItems = new ArrayList<>();
        
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, new ArrayList<>(), this);
        rvCartItems.setAdapter(cartAdapter);
    }

    private void loadCart() {
        // Load cart from Room Database on background thread
        repository.getCart(currentUserId, new DataRepository.DataCallback<java.util.List<CartItem>>() {
            @Override
            public void onSuccess(java.util.List<CartItem> cartItems) {
                ArrayList<CartItem> items = new ArrayList<>(cartItems);
                
                if (items.isEmpty()) {
                    layoutEmptyCart.setVisibility(View.VISIBLE);
                    layoutCartContent.setVisibility(View.GONE);
                } else {
                    layoutEmptyCart.setVisibility(View.GONE);
                    layoutCartContent.setVisibility(View.VISIBLE);
                    cartAdapter.updateCart(items);
                    updateTotal();
                }
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotal() {
        double total = 0;
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (CartItem item : selectedItems) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        tvTotal.setText(FormatUtils.formatCurrency(total));
    }

    private void setupListeners() {
        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cartAdapter != null) {
                cartAdapter.selectAll(isChecked);
            }
        });
        
        btnCheckout.setOnClickListener(v -> {
            if (selectedItems == null || selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check stock availability for all selected items
            StringBuilder unavailableProducts = new StringBuilder();
            for (CartItem item : selectedItems) {
                Product product = item.getProduct();
                if (product.getStock() < item.getQuantity()) {
                    if (unavailableProducts.length() > 0) {
                        unavailableProducts.append("\n");
                    }
                    if (product.getStock() == 0) {
                        unavailableProducts.append("• ")
                            .append(product.getName())
                            .append(": Đã hết hàng");
                    } else {
                        unavailableProducts.append("• ")
                            .append(product.getName())
                            .append(": Chỉ còn ")
                            .append(product.getStock())
                            .append(" sản phẩm");
                    }
                }
            }
            
            if (unavailableProducts.length() > 0) {
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Không đủ hàng")
                    .setMessage("Các sản phẩm sau không đủ số lượng:\n\n" + unavailableProducts.toString() + 
                               "\n\nVui lòng cập nhật số lượng hoặc xóa khỏi giỏ hàng.")
                    .setPositiveButton("Đồng ý", null)
                    .show();
                return;
            }
            
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra(Constants.EXTRA_SELECTED_ITEMS, selectedItems);
            startActivity(intent);
        });
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        // Update quantity in Room Database
        item.setQuantity(newQuantity);
        repository.updateCartItem(item, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                loadCart();
                Toast.makeText(CartActivity.this, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(CartActivity.this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemDeleted(CartItem item) {
        // Delete from Room Database
        repository.removeFromCart(item, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                selectedItems.remove(item);
                loadCart();
                Toast.makeText(CartActivity.this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(CartActivity.this, "Lỗi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onItemSelectionChanged(ArrayList<CartItem> selectedItems) {
        this.selectedItems = selectedItems;
        updateTotal();
        
        // Update "Select All" checkbox state
        if (cartAdapter != null) {
            cbSelectAll.setOnCheckedChangeListener(null);
            cbSelectAll.setChecked(cartAdapter.isAllSelected());
            cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
                cartAdapter.selectAll(isChecked);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }
}
