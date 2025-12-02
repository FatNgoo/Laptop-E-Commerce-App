package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.ProductAdapter;
import com.example.otech.model.Product;
import com.example.otech.repository.MockDataStore;
import com.example.otech.MainActivity;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvWishlist;
    private LinearLayout layoutEmptyWishlist;
    private BottomNavigationView bottomNavigation;
    
    private MockDataStore dataStore;
    private ProductAdapter productAdapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        dataStore = MockDataStore.getInstance();
        
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        initViews();
        setupRecyclerView();
        loadWishlist();
        setupBottomNavigation();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvWishlist = findViewById(R.id.rvWishlist);
        layoutEmptyWishlist = findViewById(R.id.layoutEmptyWishlist);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        bottomNavigation.setSelectedItemId(R.id.nav_favorites);
    }
    
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(this, FilterProductsActivity.class));
                return true;
            } else if (itemId == R.id.nav_favorites) {
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            
            return false;
        });
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvWishlist.setLayoutManager(layoutManager);
        
        productAdapter = new ProductAdapter(this, new ArrayList<>(), this);
        rvWishlist.setAdapter(productAdapter);
    }

    private void loadWishlist() {
        ArrayList<Product> wishlist = dataStore.getWishlist(currentUserId);
        
        if (wishlist.isEmpty()) {
            layoutEmptyWishlist.setVisibility(View.VISIBLE);
            rvWishlist.setVisibility(View.GONE);
        } else {
            layoutEmptyWishlist.setVisibility(View.GONE);
            rvWishlist.setVisibility(View.VISIBLE);
            productAdapter.updateProducts(wishlist);
        }
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(WishlistActivity.this, ProductDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PRODUCT, product);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Product product, int position) {
        dataStore.removeFromWishlist(currentUserId, product.getId());
        Toast.makeText(this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
        loadWishlist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWishlist();
    }
}
