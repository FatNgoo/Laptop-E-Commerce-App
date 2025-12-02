package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.MainActivity;
import com.example.otech.R;
import com.example.otech.adapter.ProductAdapter;
import com.example.otech.model.Product;
import com.example.otech.repository.MockDataStore;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CategoriesActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private MaterialToolbar toolbar;
    private SearchView searchView;
    private MaterialButton btnFilter, btnSort;
    private ChipGroup chipGroupCategories;
    private TextView tvProductCount;
    private RecyclerView rvProducts;
    private BottomNavigationView bottomNavigation;

    private MockDataStore dataStore;
    private ProductAdapter productAdapter;
    private ArrayList<Product> allProducts;
    private ArrayList<Product> filteredProducts;
    private String currentUserId;

    // Filter state
    private String selectedCategory = "";
    private String selectedUsageCategory = "";
    private ArrayList<String> selectedBrands = new ArrayList<>();
    private String selectedPriceRange = "";
    private ArrayList<String> selectedProcessors = new ArrayList<>();
    private String selectedScreenSize = "";
    private double minPrice = 0;
    private double maxPrice = Double.MAX_VALUE;
    private String sortBy = "default"; // default, price_asc, price_desc, rating, name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        dataStore = MockDataStore.getInstance();

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        initViews();
        setupRecyclerView();
        applyFilterDataFromIntent();
        applySearchQueryFromIntent();
        checkOpenSearchIntent();
        loadProducts();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        btnFilter = findViewById(R.id.btnFilter);
        btnSort = findViewById(R.id.btnSort);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        tvProductCount = findViewById(R.id.tvProductCount);
        rvProducts = findViewById(R.id.rvProducts);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        setSupportActionBar(toolbar);
        bottomNavigation.setSelectedItemId(R.id.nav_categories);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);

        allProducts = new ArrayList<>();
        filteredProducts = new ArrayList<>();
        productAdapter = new ProductAdapter(this, filteredProducts, this);
        rvProducts.setAdapter(productAdapter);
    }

    private void applyFilterDataFromIntent() {
        Intent intent = getIntent();
        
        // Check for FilterData from FilterProductsActivity
        if (intent != null && intent.hasExtra("filter_data")) {
            FilterProductsActivity.FilterData filterData = 
                (FilterProductsActivity.FilterData) intent.getSerializableExtra("filter_data");
            
            if (filterData != null) {
                selectedUsageCategory = filterData.getUsageCategory();
                selectedBrands = filterData.getBrands();
                selectedPriceRange = filterData.getPriceRange();
                selectedProcessors = filterData.getProcessors();
                selectedScreenSize = filterData.getScreenSize();
                
                // Parse price range
                parsePriceRange(selectedPriceRange);
            }
        }
        
        // Check for usage_category from MainActivity category click
        if (intent != null && intent.hasExtra("usage_category")) {
            selectedUsageCategory = intent.getStringExtra("usage_category");
            preselectCategoryChip(selectedUsageCategory);
        }
    }
    
    private void preselectCategoryChip(String usageCategory) {
        if (usageCategory == null || usageCategory.isEmpty()) return;
        
        int chipId = 0;
        switch (usageCategory) {
            case "Văn phòng": chipId = R.id.chipOffice; break;
            case "Gaming": chipId = R.id.chipGaming; break;
            case "Mỏng nhẹ": chipId = R.id.chipThinLight; break;
            case "Sinh viên": chipId = R.id.chipStudent; break;
            case "Cảm ứng": chipId = R.id.chipTouchscreen; break;
            case "Laptop AI": chipId = R.id.chipAI; break;
            case "Đồ họa - Kỹ thuật": chipId = R.id.chipGraphics; break;
            case "MacBook CTO": chipId = R.id.chipMacBook; break;
        }
        
        if (chipId != 0) {
            chipGroupCategories.check(chipId);
        }
    }
    
    private void parsePriceRange(String priceRange) {
        if (priceRange == null || priceRange.isEmpty()) {
            minPrice = 0;
            maxPrice = Double.MAX_VALUE;
            return;
        }
        
        // Parse price range string to min/max values
        if (priceRange.contains("Dưới")) {
            minPrice = 0;
            maxPrice = 10000000;
        } else if (priceRange.contains("Trên")) {
            minPrice = 30000000;
            maxPrice = Double.MAX_VALUE;
        } else {
            // Format: "10 - 15 triệu"
            String[] parts = priceRange.split("-");
            if (parts.length == 2) {
                try {
                    minPrice = Double.parseDouble(parts[0].trim()) * 1000000;
                    maxPrice = Double.parseDouble(parts[1].replace("triệu", "").trim()) * 1000000;
                } catch (NumberFormatException e) {
                    minPrice = 0;
                    maxPrice = Double.MAX_VALUE;
                }
            }
        }
    }

    private void applySearchQueryFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("search_query")) {
            String query = intent.getStringExtra("search_query");
            searchView.setQuery(query, false);
        }
    }
    
    private void checkOpenSearchIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("open_search", false)) {
            // Focus on search view and open keyboard
            searchView.setIconified(false);
            searchView.requestFocus();
            searchView.setFocusable(true);
        }
    }

    private void loadProducts() {
        allProducts = dataStore.getAllProducts();
        applyFiltersAndSort();
    }

    private void applyFiltersAndSort() {
        filteredProducts.clear();

        // Apply filters
        for (Product product : allProducts) {
            boolean matches = true;

            // Usage category filter - match exactly with product category
            if (!selectedUsageCategory.isEmpty()) {
                // Map usage categories to product categories
                String productCategory = product.getCategory();
                
                if (selectedUsageCategory.equals("Văn phòng") && !productCategory.equals("Văn phòng")) {
                    matches = false;
                } else if (selectedUsageCategory.equals("Gaming") && !productCategory.equals("Gaming")) {
                    matches = false;
                } else if (selectedUsageCategory.equals("Mỏng nhẹ") && !productCategory.equals("Mỏng nhẹ")) {
                    matches = false;
                } else if (selectedUsageCategory.equals("Sinh viên") && !productCategory.equals("Sinh viên")) {
                    matches = false;
                } else if (selectedUsageCategory.equals("Cảm ứng") && !productCategory.equals("Cảm ứng")) {
                    matches = false;
                } else if (selectedUsageCategory.equals("Laptop AI") && !productCategory.equals("Laptop AI")) {
                    matches = false;
                } else if (selectedUsageCategory.equals("Đồ họa - Kỹ thuật") && !productCategory.equals("Đồ họa- Kỹ thuật")) {
                    matches = false;
                } else if (selectedUsageCategory.equals("MacBook CTO") && !productCategory.equals("Macbook CTO")) {
                    matches = false;
                }
            }
            
            // Category filter (additional category filter)
            if (!selectedCategory.isEmpty() && !product.getCategory().equals(selectedCategory)) {
                matches = false;
            }

            // Brand filter (from FilterProductsActivity - multiple brands)
            if (!selectedBrands.isEmpty()) {
                boolean brandMatch = false;
                for (String brand : selectedBrands) {
                    if (product.getBrand().equalsIgnoreCase(brand)) {
                        brandMatch = true;
                        break;
                    }
                }
                if (!brandMatch) {
                    matches = false;
                }
            }

            // Price filter
            if (product.getPrice() < minPrice || product.getPrice() > maxPrice) {
                matches = false;
            }

            // Processor filter (check if product specs contain any selected processor)
            if (!selectedProcessors.isEmpty()) {
                boolean processorMatch = false;
                String productSpecs = product.getSpecs().toLowerCase();
                for (String processor : selectedProcessors) {
                    if (productSpecs.contains(processor.toLowerCase()) || 
                        productSpecs.contains(processor.replace(" ", "").toLowerCase())) {
                        processorMatch = true;
                        break;
                    }
                }
                if (!processorMatch) {
                    matches = false;
                }
            }

            // Screen size filter (check if product specs contain selected screen size)
            if (!selectedScreenSize.isEmpty()) {
                String screenSizeNumber = selectedScreenSize.replace(" inch", "").replace("\"", "").trim();
                if (!product.getSpecs().contains(screenSizeNumber)) {
                    matches = false;
                }
            }

            if (matches) {
                filteredProducts.add(product);
            }
        }

        // Apply sorting
        switch (sortBy) {
            case "price_asc":
                Collections.sort(filteredProducts, Comparator.comparingDouble(Product::getPrice));
                break;
            case "price_desc":
                Collections.sort(filteredProducts, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "rating":
                Collections.sort(filteredProducts, (p1, p2) -> Float.compare(p2.getRating(), p1.getRating()));
                break;
            case "name":
                Collections.sort(filteredProducts, Comparator.comparing(Product::getName));
                break;
        }

        productAdapter.notifyDataSetChanged();
        tvProductCount.setText("Hiển thị " + filteredProducts.size() + " sản phẩm");
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadProducts();
                } else {
                    searchProducts(newText);
                }
                return true;
            }
        });

        // Filter button - open FilterProductsActivity
        btnFilter.setOnClickListener(v -> {
            Intent intent = new Intent(this, FilterProductsActivity.class);
            startActivity(intent);
            finish(); // Close current CategoriesActivity to avoid back stack issues
        });

        // Sort button
        btnSort.setOnClickListener(v -> showSortDialog());

        // Category chips
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);
                
                // Map chip IDs to usage categories
                if (checkedId == R.id.chipAll) {
                    selectedUsageCategory = "";
                } else if (checkedId == R.id.chipOffice) {
                    selectedUsageCategory = "Văn phòng";
                } else if (checkedId == R.id.chipGaming) {
                    selectedUsageCategory = "Gaming";
                } else if (checkedId == R.id.chipThinLight) {
                    selectedUsageCategory = "Mỏng nhẹ";
                } else if (checkedId == R.id.chipStudent) {
                    selectedUsageCategory = "Sinh viên";
                } else if (checkedId == R.id.chipTouchscreen) {
                    selectedUsageCategory = "Cảm ứng";
                } else if (checkedId == R.id.chipAI) {
                    selectedUsageCategory = "Laptop AI";
                } else if (checkedId == R.id.chipGraphics) {
                    selectedUsageCategory = "Đồ họa - Kỹ thuật";
                } else if (checkedId == R.id.chipMacBook) {
                    selectedUsageCategory = "MacBook CTO";
                }

                applyFiltersAndSort();
            }
        });

        // Bottom Navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_categories) {
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

    private void searchProducts(String query) {
        filteredProducts.clear();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                product.getBrand().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        productAdapter.notifyDataSetChanged();
        tvProductCount.setText("Hiển thị " + filteredProducts.size() + " sản phẩm");
    }

    private void showFilterDialog() {
        String[] brands = {"Tất cả", "ASUS", "Dell", "HP", "Lenovo", "Acer", "MSI", "Apple"};
        String[] priceRanges = {"Tất cả", "Dưới 15 triệu", "15-25 triệu", "25-35 triệu", "35-50 triệu", "Trên 50 triệu"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bộ lọc");

        String[] filterOptions = {"Lọc theo thương hiệu", "Lọc theo giá", "Xóa bộ lọc"};
        builder.setItems(filterOptions, (dialog, which) -> {
            if (which == 0) {
                // Brand filter
                new AlertDialog.Builder(this)
                    .setTitle("Chọn thương hiệu")
                    .setItems(brands, (d, i) -> {
                        selectedBrands.clear();
                        if (i > 0) {
                            selectedBrands.add(brands[i]);
                        }
                        applyFiltersAndSort();
                        Toast.makeText(this, "Đã lọc theo: " + (i == 0 ? "Tất cả" : brands[i]), Toast.LENGTH_SHORT).show();
                    })
                    .show();
            } else if (which == 1) {
                // Price filter
                new AlertDialog.Builder(this)
                    .setTitle("Chọn khoảng giá")
                    .setItems(priceRanges, (d, i) -> {
                        switch (i) {
                            case 0: minPrice = 0; maxPrice = Double.MAX_VALUE; break;
                            case 1: minPrice = 0; maxPrice = 15000000; break;
                            case 2: minPrice = 15000000; maxPrice = 25000000; break;
                            case 3: minPrice = 25000000; maxPrice = 35000000; break;
                            case 4: minPrice = 35000000; maxPrice = 50000000; break;
                            case 5: minPrice = 50000000; maxPrice = Double.MAX_VALUE; break;
                        }
                        applyFiltersAndSort();
                        Toast.makeText(this, "Đã lọc theo: " + priceRanges[i], Toast.LENGTH_SHORT).show();
                    })
                    .show();
            } else if (which == 2) {
                // Reset filters
                selectedBrands.clear();
                selectedProcessors.clear();
                selectedPriceRange = "";
                selectedScreenSize = "";
                selectedUsageCategory = "";
                minPrice = 0;
                maxPrice = Double.MAX_VALUE;
                applyFiltersAndSort();
                Toast.makeText(this, "Đã xóa bộ lọc", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void showSortDialog() {
        String[] sortOptions = {"Mặc định", "Giá: Thấp đến cao", "Giá: Cao đến thấp", "Đánh giá cao nhất", "Tên A-Z"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sắp xếp theo");
        builder.setItems(sortOptions, (dialog, which) -> {
            switch (which) {
                case 0: sortBy = "default"; break;
                case 1: sortBy = "price_asc"; break;
                case 2: sortBy = "price_desc"; break;
                case 3: sortBy = "rating"; break;
                case 4: sortBy = "name"; break;
            }
            applyFiltersAndSort();
            Toast.makeText(this, "Sắp xếp: " + sortOptions[which], Toast.LENGTH_SHORT).show();
        });
        builder.show();
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

        boolean isInWishlist = dataStore.isInWishlist(currentUserId, product.getId());

        if (isInWishlist) {
            dataStore.removeFromWishlist(currentUserId, product.getId());
            Toast.makeText(this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            dataStore.addToWishlist(currentUserId, product);
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        }

        productAdapter.notifyItemChanged(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
        bottomNavigation.setSelectedItemId(R.id.nav_categories);
    }
}
