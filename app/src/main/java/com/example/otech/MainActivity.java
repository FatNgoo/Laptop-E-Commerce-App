package com.example.otech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.otech.activity.CartActivity;
import com.example.otech.activity.CategoriesActivity;
import com.example.otech.activity.FilterProductsActivity;
import com.example.otech.activity.ProductDetailActivity;
import com.example.otech.activity.ProfileActivity;
import com.example.otech.activity.WishlistActivity;
import com.example.otech.adapter.BannerAdapter;
import com.example.otech.adapter.CategoryAdapter;
import com.example.otech.adapter.ProductAdapter;
import com.example.otech.model.Banner;
import com.example.otech.model.Category;
import com.example.otech.model.Product;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener, CategoryAdapter.OnCategoryClickListener {

    private MaterialToolbar toolbar;
    private ViewPager2 viewPagerBanner;
    private RecyclerView rvCategories, rvPromotionProducts, rvBestSellerProducts, rvHotProducts;
    private ImageView ivNotifications;
    private TextView tvViewAllPromotion, tvViewAllBestSeller, tvViewAllHot;
    private BottomNavigationView bottomNavigation;
    private android.widget.LinearLayout layoutSearch;
    
    private DataRepository repository;
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter promotionProductsAdapter, bestSellerProductsAdapter, hotProductsAdapter;
    private ArrayList<Product> allProducts;
    private String currentUserId;
    
    // Banner auto-scroll
    private Handler bannerHandler;
    private Runnable bannerRunnable;
    private int currentBannerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = DataRepository.getInstance(this);
        
        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");
        
        initViews();
        setupBanner();
        setupCategories();
        setupPromotionProducts();
        setupBestSellerProducts();
        setupHotProducts();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        viewPagerBanner = findViewById(R.id.viewPagerBanner);
        rvCategories = findViewById(R.id.rvCategories);
        rvPromotionProducts = findViewById(R.id.rvPromotionProducts);
        rvBestSellerProducts = findViewById(R.id.rvBestSellerProducts);
        rvHotProducts = findViewById(R.id.rvHotProducts);
        layoutSearch = findViewById(R.id.layoutSearch);
        ivNotifications = findViewById(R.id.ivNotifications);
        tvViewAllPromotion = findViewById(R.id.tvViewAllPromotion);
        tvViewAllBestSeller = findViewById(R.id.tvViewAllBestSeller);
        tvViewAllHot = findViewById(R.id.tvViewAllHot);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        
        setSupportActionBar(toolbar);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    private void setupBanner() {
        // Load active banners from Room Database
        repository.getActiveBanners(new DataRepository.DataCallback<List<Banner>>() {
            @Override
            public void onSuccess(List<Banner> banners) {
                ArrayList<Banner> activeBanners = new ArrayList<>(banners);
        
        // If no banners, use default
        if (activeBanners.isEmpty()) {
            activeBanners = new ArrayList<>();
            activeBanners.add(new Banner("1", "banner1", "Default Banner", "", true, 1));
        }
        
        bannerAdapter = new BannerAdapter(MainActivity.this, activeBanners);
        viewPagerBanner.setAdapter(bannerAdapter);
        
        // Auto-scroll every 5 seconds
        final int bannerCount = activeBanners.size();
        bannerHandler = new Handler(Looper.getMainLooper());
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentBannerPosition == bannerCount) {
                    currentBannerPosition = 0;
                }
                viewPagerBanner.setCurrentItem(currentBannerPosition++, true);
                bannerHandler.postDelayed(this, 5000);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 5000);
            }

            @Override
            public void onError(Exception e) {
                // Use default banner on error
                ArrayList<Banner> defaultBanners = new ArrayList<>();
                defaultBanners.add(new Banner("1", "banner1", "Default Banner", "", true, 1));
                
                bannerAdapter = new BannerAdapter(MainActivity.this, defaultBanners);
                viewPagerBanner.setAdapter(bannerAdapter);
            }
        });
    }

    private void setupCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("office", "Văn phòng", R.drawable.catagory_vanphong));
        categories.add(new Category("gaming", "Gaming", R.drawable.catagory_gaming));
        categories.add(new Category("thin_light", "Mỏng nhẹ", R.drawable.catagory_mongnhe));
        categories.add(new Category("student", "Sinh viên", R.drawable.catagory_sinhvien));
        categories.add(new Category("touchscreen", "Cảm ứng", R.drawable.catagory_camung));
        categories.add(new Category("ai", "Laptop AI", R.drawable.catagory_ai));
        categories.add(new Category("graphics", "Đồ họa - Kỹ thuật", R.drawable.catagory_dohoa_kythuat));
        categories.add(new Category("macbook", "MacBook CTO", R.drawable.catagory_macbook));
        
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvCategories.setLayoutManager(layoutManager);
        
        categoryAdapter = new CategoryAdapter(this, categories, this);
        rvCategories.setAdapter(categoryAdapter);
    }

    private void setupPromotionProducts() {
        // Load products from Room Database
        repository.getPromotionProducts(10, new DataRepository.DataCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                ArrayList<Product> promotionProducts = new ArrayList<>(products);
                
                // Display as horizontal scrolling list (1 row)
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                rvPromotionProducts.setLayoutManager(layoutManager);
                
                promotionProductsAdapter = new ProductAdapter(MainActivity.this, promotionProducts, MainActivity.this);
                rvPromotionProducts.setAdapter(promotionProductsAdapter);
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "Lỗi load sản phẩm khuyến mãi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupBestSellerProducts() {
        // Load best seller products from Room Database
        repository.getBestSellers(10, new DataRepository.DataCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                ArrayList<Product> bestSellerProducts = new ArrayList<>(products);
                
                // Display as grid 2 columns
                GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                rvBestSellerProducts.setLayoutManager(layoutManager);
                
                bestSellerProductsAdapter = new ProductAdapter(MainActivity.this, bestSellerProducts, MainActivity.this);
                rvBestSellerProducts.setAdapter(bestSellerProductsAdapter);
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "Lỗi load sản phẩm bán chạy: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupHotProducts() {
        // Load all products and sort by rating (temporarily - can optimize with DAO query later)
        repository.getAllProducts(new DataRepository.DataCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                ArrayList<Product> sortedByRating = new ArrayList<>(products);
                sortedByRating.sort((p1, p2) -> Float.compare(p2.getRating(), p1.getRating()));
                
                int limit = Math.min(10, sortedByRating.size());
                ArrayList<Product> hotProducts = new ArrayList<>(sortedByRating.subList(0, limit));
                
                // Display as grid 2 columns
                GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                rvHotProducts.setLayoutManager(layoutManager);
                
                hotProductsAdapter = new ProductAdapter(MainActivity.this, hotProducts, MainActivity.this);
                rvHotProducts.setAdapter(hotProductsAdapter);
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "Lỗi load sản phẩm hot: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        // Search bar click - open CategoriesActivity with search ready
        layoutSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoriesActivity.class);
            intent.putExtra("open_search", true);
            startActivity(intent);
        });
        
        // Notification icon click
        ivNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.otech.activity.NotificationsActivity.class);
            startActivity(intent);
        });
        
        // View all promotion products
        tvViewAllPromotion.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoriesActivity.class);
            startActivity(intent);
        });
        
        // View all best seller products
        tvViewAllBestSeller.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoriesActivity.class);
            startActivity(intent);
        });
        
        // View all hot products
        tvViewAllHot.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoriesActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(this, FilterProductsActivity.class));
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(this, WishlistActivity.class));
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

    @Override
    public void onCategoryClick(Category category) {
        // Go directly to CategoriesActivity with usage category filter applied
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("usage_category", getCategoryDisplayName(category.getId()));
        startActivity(intent);
    }
    
    private String getCategoryDisplayName(String categoryId) {
        switch (categoryId) {
            case "office": return "Văn phòng";
            case "gaming": return "Gaming";
            case "thin_light": return "Mỏng nhẹ";
            case "student": return "Sinh viên";
            case "touchscreen": return "Cảm ứng";
            case "ai": return "Laptop AI";
            case "graphics": return "Đồ họa - Kỹ thuật";
            case "macbook": return "MacBook CTO";
            default: return "Văn phòng";
        }
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PRODUCT, product);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Product product, int position) {
        if (currentUserId.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        
        repository.isInWishlist(currentUserId, product.getId(), new DataRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isInWishlist) {
                if (isInWishlist) {
                    repository.removeFromWishlist(currentUserId, product.getId(), new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                            refreshAdapters();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(MainActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    repository.addToWishlist(currentUserId, product.getId(), new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                            refreshAdapters();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(MainActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void refreshAdapters() {
        // Refresh all adapters
        if (promotionProductsAdapter != null) {
            promotionProductsAdapter.notifyDataSetChanged();
        }
        if (bestSellerProductsAdapter != null) {
            bestSellerProductsAdapter.notifyDataSetChanged();
        }
        if (hotProductsAdapter != null) {
            hotProductsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset bottom nav selection
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        
        // Restart banner auto-scroll
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.postDelayed(bannerRunnable, 5000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop banner auto-scroll
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up handler
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }
}
