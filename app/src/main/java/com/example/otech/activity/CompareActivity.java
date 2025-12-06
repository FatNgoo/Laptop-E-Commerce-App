package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.CompareProductAdapter;
import com.example.otech.model.Product;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import android.text.Editable;
import android.text.TextWatcher;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompareActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialCardView cardSelectProduct;
    private RecyclerView rvSelectProduct;
    private LinearLayout layoutCompareView;
    private MaterialButton btnSwapProducts;
    
    // Product 1 views (Original product)
    private ImageView ivProduct1;
    private TextView tvProduct1Name, tvProduct1Brand, tvProduct1Price;
    private TextView tvProduct1Cpu, tvProduct1Ram, tvProduct1Storage;
    private TextView tvProduct1Screen, tvProduct1Graphics, tvProduct1Battery;
    private TextView tvProduct1Weight, tvProduct1Os;
    
    // Product 2 views (Selected product)
    private ImageView ivProduct2;
    private TextView tvProduct2Name, tvProduct2Brand, tvProduct2Price;
    private TextView tvProduct2Cpu, tvProduct2Ram, tvProduct2Storage;
    private TextView tvProduct2Screen, tvProduct2Graphics, tvProduct2Battery;
    private TextView tvProduct2Weight, tvProduct2Os;
    
    // Action buttons removed for cleaner comparison UI
    
    private Product product1, product2;
    private DataRepository repository;
    private CompareProductAdapter adapter;
    private String currentUserId;
    
    // Search and filter views
    private TextInputEditText etSearch;
    private ChipGroup chipGroupFilter;
    private Chip chipAll, chipSameCategory, chipGaming, chipOffice, chipThin;
    private Chip chipStudent, chipTouch, chipAI, chipWorkstation, chipMacbook;
    private TextView tvProductCount;
    
    // All products list for filtering
    private ArrayList<Product> allProducts = new ArrayList<>();
    private ArrayList<Product> filteredProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        repository = DataRepository.getInstance(this);
        
        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        initViews();
        setupToolbar();
        
        // Get product from intent
        product1 = (Product) getIntent().getSerializableExtra("product");
        if (product1 != null) {
            displayProduct1(product1);
            loadProductsForSelection();
        } else {
            Toast.makeText(this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        cardSelectProduct = findViewById(R.id.cardSelectProduct);
        rvSelectProduct = findViewById(R.id.rvSelectProduct);
        layoutCompareView = findViewById(R.id.layoutCompareView);
        btnSwapProducts = findViewById(R.id.btnSwapProducts);
        
        // Search and filter
        etSearch = findViewById(R.id.etSearch);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);
        tvProductCount = findViewById(R.id.tvProductCount);
        chipAll = findViewById(R.id.chipAll);
        chipSameCategory = findViewById(R.id.chipSameCategory);
        chipGaming = findViewById(R.id.chipGaming);
        chipOffice = findViewById(R.id.chipOffice);
        chipThin = findViewById(R.id.chipThin);
        chipStudent = findViewById(R.id.chipStudent);
        chipTouch = findViewById(R.id.chipTouch);
        chipAI = findViewById(R.id.chipAI);
        chipWorkstation = findViewById(R.id.chipWorkstation);
        chipMacbook = findViewById(R.id.chipMacbook);
        
        // Product 1 views
        ivProduct1 = findViewById(R.id.ivProduct1);
        tvProduct1Name = findViewById(R.id.tvProduct1Name);
        tvProduct1Brand = findViewById(R.id.tvProduct1Brand);
        tvProduct1Price = findViewById(R.id.tvProduct1Price);
        tvProduct1Cpu = findViewById(R.id.tvProduct1Cpu);
        tvProduct1Ram = findViewById(R.id.tvProduct1Ram);
        tvProduct1Storage = findViewById(R.id.tvProduct1Storage);
        tvProduct1Screen = findViewById(R.id.tvProduct1Screen);
        tvProduct1Graphics = findViewById(R.id.tvProduct1Graphics);
        tvProduct1Battery = findViewById(R.id.tvProduct1Battery);
        tvProduct1Weight = findViewById(R.id.tvProduct1Weight);
        tvProduct1Os = findViewById(R.id.tvProduct1Os);
        
        // Product 2 views
        ivProduct2 = findViewById(R.id.ivProduct2);
        tvProduct2Name = findViewById(R.id.tvProduct2Name);
        tvProduct2Brand = findViewById(R.id.tvProduct2Brand);
        tvProduct2Price = findViewById(R.id.tvProduct2Price);
        tvProduct2Cpu = findViewById(R.id.tvProduct2Cpu);
        tvProduct2Ram = findViewById(R.id.tvProduct2Ram);
        tvProduct2Storage = findViewById(R.id.tvProduct2Storage);
        tvProduct2Screen = findViewById(R.id.tvProduct2Screen);
        tvProduct2Graphics = findViewById(R.id.tvProduct2Graphics);
        tvProduct2Battery = findViewById(R.id.tvProduct2Battery);
        tvProduct2Weight = findViewById(R.id.tvProduct2Weight);
        tvProduct2Os = findViewById(R.id.tvProduct2Os);
        
        // Action buttons removed for cleaner comparison UI
        
        setupButtons();
        // Don't setup search and filter here - will be called after data is loaded
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupButtons() {
        btnSwapProducts.setOnClickListener(v -> swapProducts());
        // Product action buttons removed for cleaner comparison UI
    }

    private void setupSearchAndFilter() {
        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!allProducts.isEmpty()) {
                    filterProducts();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter chips
        chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!allProducts.isEmpty()) {
                filterProducts();
            }
        });
    }

    private void filterProducts() {
        String searchQuery = etSearch.getText().toString().toLowerCase().trim();
        filteredProducts.clear();
        
        android.util.Log.d("CompareActivity", "filterProducts: allProducts size = " + allProducts.size());
        
        for (Product p : allProducts) {
            // Skip current product
            if (p.getId().equals(product1.getId())) continue;
            
            // REMOVED: Don't filter by stock, show all products
            // if (p.getStock() <= 0) continue;
            
            // Apply category filter
            boolean categoryMatch = false;
            if (chipAll.isChecked()) {
                categoryMatch = true;
            } else if (chipSameCategory.isChecked()) {
                categoryMatch = p.getCategory().equals(product1.getCategory());
            } else if (chipGaming.isChecked()) {
                // Match "Gaming" category
                categoryMatch = p.getCategory().equalsIgnoreCase("gaming");
            } else if (chipOffice.isChecked()) {
                // Match "Văn phòng" category (with or without diacritics)
                categoryMatch = p.getCategory().equalsIgnoreCase("văn phòng") || 
                               p.getCategory().equalsIgnoreCase("van phong") ||
                               p.getCategory().equalsIgnoreCase("office");
            } else if (chipThin.isChecked()) {
                // Match "Mỏng nhẹ" category
                categoryMatch = p.getCategory().equalsIgnoreCase("mỏng nhẹ") ||
                               p.getCategory().equalsIgnoreCase("mong nhe") ||
                               p.getCategory().equalsIgnoreCase("thin");
            } else if (chipStudent.isChecked()) {
                // Match "Sinh viên" category
                categoryMatch = p.getCategory().equalsIgnoreCase("sinh viên") ||
                               p.getCategory().equalsIgnoreCase("sinh vien") ||
                               p.getCategory().equalsIgnoreCase("student");
            } else if (chipTouch.isChecked()) {
                // Match "Cảm ứng" category
                categoryMatch = p.getCategory().equalsIgnoreCase("cảm ứng") ||
                               p.getCategory().equalsIgnoreCase("cam ung") ||
                               p.getCategory().equalsIgnoreCase("touch");
            } else if (chipAI.isChecked()) {
                // Match "Laptop AI" category
                categoryMatch = p.getCategory().equalsIgnoreCase("laptop ai") ||
                               p.getCategory().equalsIgnoreCase("ai");
            } else if (chipWorkstation.isChecked()) {
                // Match "Đồ họa- Kỹ thuật" category
                categoryMatch = p.getCategory().equalsIgnoreCase("đồ họa- kỹ thuật") ||
                               p.getCategory().equalsIgnoreCase("do hoa- ky thuat") ||
                               p.getCategory().equalsIgnoreCase("workstation");
            } else if (chipMacbook.isChecked()) {
                // Match "Macbook CTO" category
                categoryMatch = p.getCategory().equalsIgnoreCase("macbook cto") ||
                               p.getCategory().equalsIgnoreCase("macbook");
            } else {
                // Default: if no chip is checked (shouldn't happen with selectionRequired), show all
                categoryMatch = true;
            }
            
            if (!categoryMatch) continue;
            
            // Apply search filter
            if (!searchQuery.isEmpty()) {
                String productName = p.getName().toLowerCase();
                String productBrand = p.getBrand().toLowerCase();
                String productCategory = p.getCategory().toLowerCase();
                if (!productName.contains(searchQuery) && 
                    !productBrand.contains(searchQuery) && 
                    !productCategory.contains(searchQuery)) {
                    continue;
                }
            }
            
            filteredProducts.add(p);
        }
        
        android.util.Log.d("CompareActivity", "filterProducts: filteredProducts size = " + filteredProducts.size());
        
        // Update product count display
        if (tvProductCount != null) {
            tvProductCount.setText("Tìm thấy " + filteredProducts.size() + " sản phẩm");
        }
        
        if (adapter != null) {
            adapter.updateProducts(filteredProducts);
            android.util.Log.d("CompareActivity", "filterProducts: adapter updated");
        } else {
            android.util.Log.e("CompareActivity", "filterProducts: adapter is null!");
        }
    }

    private void loadProductsForSelection() {
        repository.getAllProducts(new DataRepository.DataCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                android.util.Log.d("CompareActivity", "loadProducts: received " + products.size() + " products");
                allProducts.clear();
                allProducts.addAll(products);
                
                // Ensure card is visible
                cardSelectProduct.setVisibility(View.VISIBLE);
                layoutCompareView.setVisibility(View.GONE);
                
                setupRecyclerView(); // Setup adapter first (only once)
                setupSearchAndFilter(); // Setup listeners after data is loaded
                filterProducts(); // Then filter and update adapter
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CompareActivity.this, "Lỗi tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        // Only create adapter if it doesn't exist yet
        if (adapter == null) {
            adapter = new CompareProductAdapter(this, filteredProducts, product -> {
                product2 = product;
                displayProduct2(product2);
                cardSelectProduct.setVisibility(View.GONE);
                layoutCompareView.setVisibility(View.VISIBLE);
            });
            
            // Custom LinearLayoutManager that wraps content properly in ScrollView
            LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return false; // Disable RecyclerView scroll, let parent ScrollView handle it
                }
            };
            
            rvSelectProduct.setLayoutManager(layoutManager);
            rvSelectProduct.setAdapter(adapter);
            rvSelectProduct.setHasFixedSize(false);
            
            // Force RecyclerView to measure properly in nested ScrollView
            rvSelectProduct.post(() -> {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void displayProduct1(Product product) {
        // Use first image from imageUrls list instead of placeholder imageUrl
        String imageUrl = null;
        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            imageUrl = product.getImageUrls().get(0);
        }
        loadProductImage(ivProduct1, imageUrl);
        
        tvProduct1Name.setText(product.getName());
        tvProduct1Brand.setText(product.getBrand());
        tvProduct1Price.setText(formatPrice(product.getPrice()));
        
        // Parse specs string and display individual specs
        String specs = product.getSpecs() != null ? product.getSpecs() : "";
        displaySpecsForProduct1(specs);
    }

    private void displayProduct2(Product product) {
        // Use first image from imageUrls list instead of placeholder imageUrl
        String imageUrl = null;
        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            imageUrl = product.getImageUrls().get(0);
        }
        loadProductImage(ivProduct2, imageUrl);
        
        tvProduct2Name.setText(product.getName());
        tvProduct2Brand.setText(product.getBrand());
        tvProduct2Price.setText(formatPrice(product.getPrice()));
        
        // Parse specs string and display individual specs
        String specs = product.getSpecs() != null ? product.getSpecs() : "";
        displaySpecsForProduct2(specs);
        
        // Highlight differences
        highlightDifferences();
    }

    private void displaySpecsForProduct1(String specs) {
        // Parse specs like ProductDetailActivity does (comma-separated format)
        if (specs == null || specs.isEmpty()) {
            tvProduct1Cpu.setText("N/A");
            tvProduct1Ram.setText("N/A");
            tvProduct1Storage.setText("N/A");
            tvProduct1Screen.setText("N/A");
            tvProduct1Graphics.setText("N/A");
            tvProduct1Battery.setText("N/A");
            tvProduct1Weight.setText("N/A");
            tvProduct1Os.setText("N/A");
            return;
        }
        
        String[] specLines = specs.split(",");
        for (String spec : specLines) {
            String[] parts = spec.trim().split(":");
            if (parts.length == 2) {
                String key = parts[0].trim().toUpperCase();
                String value = parts[1].trim();
                
                switch (key) {
                    case "CPU":
                        tvProduct1Cpu.setText(value);
                        break;
                    case "RAM":
                        tvProduct1Ram.setText(value);
                        break;
                    case "STORAGE":
                    case "Ổ CỨNG":
                        tvProduct1Storage.setText(value);
                        break;
                    case "GPU":
                    case "VGA":
                    case "CARD ĐỒ HỌA":
                        tvProduct1Graphics.setText(value);
                        break;
                    case "DISPLAY":
                    case "MÀN HÌNH":
                    case "SCREEN":
                        tvProduct1Screen.setText(value);
                        break;
                    case "PIN":
                    case "BATTERY":
                        tvProduct1Battery.setText(value);
                        break;
                    case "TRỌNG LƯỢNG":
                    case "WEIGHT":
                        tvProduct1Weight.setText(value);
                        break;
                    case "HỆ ĐIỀU HÀNH":
                    case "OS":
                        tvProduct1Os.setText(value);
                        break;
                }
            }
        }
    }

    private void displaySpecsForProduct2(String specs) {
        // Parse specs like ProductDetailActivity does (comma-separated format)
        if (specs == null || specs.isEmpty()) {
            tvProduct2Cpu.setText("N/A");
            tvProduct2Ram.setText("N/A");
            tvProduct2Storage.setText("N/A");
            tvProduct2Screen.setText("N/A");
            tvProduct2Graphics.setText("N/A");
            tvProduct2Battery.setText("N/A");
            tvProduct2Weight.setText("N/A");
            tvProduct2Os.setText("N/A");
            return;
        }
        
        String[] specLines = specs.split(",");
        for (String spec : specLines) {
            String[] parts = spec.trim().split(":");
            if (parts.length == 2) {
                String key = parts[0].trim().toUpperCase();
                String value = parts[1].trim();
                
                switch (key) {
                    case "CPU":
                        tvProduct2Cpu.setText(value);
                        break;
                    case "RAM":
                        tvProduct2Ram.setText(value);
                        break;
                    case "STORAGE":
                    case "Ổ CỨNG":
                        tvProduct2Storage.setText(value);
                        break;
                    case "GPU":
                    case "VGA":
                    case "CARD ĐỒ HỌA":
                        tvProduct2Graphics.setText(value);
                        break;
                    case "DISPLAY":
                    case "MÀN HÌNH":
                    case "SCREEN":
                        tvProduct2Screen.setText(value);
                        break;
                    case "PIN":
                    case "BATTERY":
                        tvProduct2Battery.setText(value);
                        break;
                    case "TRỌNG LƯỢNG":
                    case "WEIGHT":
                        tvProduct2Weight.setText(value);
                        break;
                    case "HỆ ĐIỀU HÀNH":
                    case "OS":
                        tvProduct2Os.setText(value);
                        break;
                }
            }
        }
    }

    private void highlightDifferences() {
        // Highlight price difference
        if (product1.getPrice() < product2.getPrice()) {
            tvProduct1Price.setTextColor(getResources().getColor(R.color.colorSecondary));
            tvProduct2Price.setTextColor(getResources().getColor(R.color.text_primary));
        } else if (product1.getPrice() > product2.getPrice()) {
            tvProduct1Price.setTextColor(getResources().getColor(R.color.text_primary));
            tvProduct2Price.setTextColor(getResources().getColor(R.color.colorSecondary));
        }
        
        // Spec highlighting can be added later based on actual display values
    }

    private void compareAndHighlight(TextView tv1, TextView tv2, String val1, String val2) {
        if (val1 == null) val1 = "";
        if (val2 == null) val2 = "";
        
        if (!val1.equals(val2)) {
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorSecondary));
        } else {
            tv1.setTextColor(getResources().getColor(R.color.text_primary));
            tv2.setTextColor(getResources().getColor(R.color.text_primary));
        }
    }

    private void swapProducts() {
        if (product2 == null) return;
        
        Product temp = product1;
        product1 = product2;
        product2 = temp;
        
        displayProduct1(product1);
        displayProduct2(product2);
    }

    // Action button methods removed (buttons removed for cleaner UI)

    private void loadProductImage(ImageView imageView, String imageUrl) {
        if (imageView == null) return;
        
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return;
        }

        try {
            if (imageUrl.startsWith("file://")) {
                android.net.Uri uri = android.net.Uri.parse(imageUrl);
                java.io.File file = new java.io.File(uri.getPath());
                if (file.exists()) {
                    imageView.setImageURI(null); // Clear cache
                    imageView.setImageURI(uri);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return;
                }
            } else if (imageUrl.startsWith("content://")) {
                android.net.Uri uri = android.net.Uri.parse(imageUrl);
                imageView.setImageURI(null); // Clear cache
                imageView.setImageURI(uri);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return;
            } else if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                // Ignore placeholder URLs from mock data
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return;
            } else {
                // Try as drawable resource name
                int resId = getResources().getIdentifier(imageUrl, "drawable", getPackageName());
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Fallback to placeholder
        imageView.setImageResource(R.drawable.ic_launcher_foreground);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private String formatPrice(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
}
