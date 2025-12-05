package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.ProductAdapter;
import com.example.otech.model.Product;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;

import java.util.List;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ManageProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvProducts;
    private FloatingActionButton fabAddProduct;
    private TextInputEditText etSearch;
    private MaterialButton btnFilter;
    private ChipGroup chipGroup;
    private LinearLayout emptyState;
    
    // Filter views
    private LinearLayout layoutFilters;
    private MaterialAutoCompleteTextView actvFilterCategory;
    private MaterialAutoCompleteTextView actvFilterBrand;
    private TextInputEditText etPriceMin;
    private TextInputEditText etPriceMax;
    private MaterialButton btnApplyFilter;

    private ProductAdapter adapter;
    private DataRepository repository;
    private ArrayList<Product> allProducts;
    private ArrayList<Product> filteredProducts;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        repository = DataRepository.getInstance(this);

        initViews();
        setupRecyclerView();
        loadProducts();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvProducts = findViewById(R.id.rvProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        etSearch = findViewById(R.id.etSearch);
        btnFilter = findViewById(R.id.btnFilter);
        chipGroup = findViewById(R.id.chipGroup);
        emptyState = findViewById(R.id.emptyState);

        // Filter views
        layoutFilters = findViewById(R.id.layoutFilters);
        actvFilterCategory = findViewById(R.id.actvFilterCategory);
        actvFilterBrand = findViewById(R.id.actvFilterBrand);
        etPriceMin = findViewById(R.id.etPriceMin);
        etPriceMax = findViewById(R.id.etPriceMax);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
    }

    private void loadProducts() {
        repository.getAllProducts(new DataRepository.DataCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                allProducts = new ArrayList<>(products);
                filteredProducts = new ArrayList<>(allProducts);
                adapter = new ProductAdapter(ManageProductsActivity.this, filteredProducts, ManageProductsActivity.this);
                rvProducts.setAdapter(adapter);
                updateEmptyState();
            }
            
            @Override
            public void onError(Exception e) {
                Toast.makeText(ManageProductsActivity.this, "Lỗi tải sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        fabAddProduct.setOnClickListener(v -> showAddProductDialog());

        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter chips
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentFilter = "all";
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chipAll) {
                    currentFilter = "all";
                } else if (checkedId == R.id.chipLowStock) {
                    currentFilter = "lowstock";
                }
            }
            filterProducts();
        });

        // Toggle filter panel
        btnFilter.setOnClickListener(v -> {
            if (layoutFilters.getVisibility() == View.VISIBLE) {
                layoutFilters.setVisibility(View.GONE);
            } else {
                layoutFilters.setVisibility(View.VISIBLE);
            }
        });

        // Setup Filter Spinners
        String[] categories = {"Tất cả", "Văn phòng", "Gaming", "Mỏng nhẹ", "Sinh viên", "Cảm ứng", "Laptop AI", "Đồ hoạ – kỹ thuật", "MacBook CTO"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actvFilterCategory.setAdapter(categoryAdapter);
        actvFilterCategory.setText("Tất cả", false);

        String[] brands = {"Tất cả", "ASUS", "MSI", "Acer", "Dell", "HP", "Apple", "Lenovo", "LG", "Gigabyte", "Razer"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, brands);
        actvFilterBrand.setAdapter(brandAdapter);
        actvFilterBrand.setText("Tất cả", false);

        // Apply Filter Button
        btnApplyFilter.setOnClickListener(v -> filterProducts());
    }

    private void filterProducts() {
        String query = etSearch.getText().toString();
        filteredProducts.clear();

        String selectedCategory = actvFilterCategory.getText().toString();
        String selectedBrand = actvFilterBrand.getText().toString();
        String minPriceStr = etPriceMin.getText().toString();
        String maxPriceStr = etPriceMax.getText().toString();

        double minPrice = minPriceStr.isEmpty() ? 0 : Double.parseDouble(minPriceStr);
        double maxPrice = maxPriceStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceStr);

        for (Product product : allProducts) {
            boolean matchesSearch = query.isEmpty() || 
                product.getName().toLowerCase().contains(query.toLowerCase()) ||
                product.getBrand().toLowerCase().contains(query.toLowerCase());

            boolean matchesFilter = currentFilter.equals("all") ||
                (currentFilter.equals("lowstock") && product.getStock() < 10);

            boolean matchesCategory = selectedCategory.equals("Tất cả") || product.getCategory().equals(selectedCategory);
            boolean matchesBrand = selectedBrand.equals("Tất cả") || product.getBrand().equals(selectedBrand);
            boolean matchesPrice = product.getPrice() >= minPrice && product.getPrice() <= maxPrice;

            if (matchesSearch && matchesFilter && matchesCategory && matchesBrand && matchesPrice) {
                filteredProducts.add(product);
            }
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredProducts.isEmpty()) {
            rvProducts.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvProducts.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    private void showSortOptions() {
        String[] sortOptions = {"Tên A-Z", "Tên Z-A", "Giá thấp đến cao", "Giá cao đến thấp", "Tồn kho thấp đến cao"};
        
        new AlertDialog.Builder(this)
                .setTitle("Sắp xếp theo")
                .setItems(sortOptions, (dialog, which) -> {
                    switch (which) {
                        case 0: // A-Z
                            filteredProducts.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                            break;
                        case 1: // Z-A
                            filteredProducts.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
                            break;
                        case 2: // Price low to high
                            filteredProducts.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
                            break;
                        case 3: // Price high to low
                            filteredProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                            break;
                        case 4: // Stock low to high
                            filteredProducts.sort((p1, p2) -> Integer.compare(p1.getStock(), p2.getStock()));
                            break;
                    }
                    adapter.notifyDataSetChanged();
                })
                .show();
    }

    private void applyFilters() {
        String query = etSearch.getText().toString();
        String filter = currentFilter;

        // Get filter values
        String selectedCategory = actvFilterCategory.getText().toString().trim();
        String selectedBrand = actvFilterBrand.getText().toString().trim();
        String priceMinStr = etPriceMin.getText().toString().trim();
        String priceMaxStr = etPriceMax.getText().toString().trim();

        double priceMin = priceMinStr.isEmpty() ? 0 : Double.parseDouble(priceMinStr);
        double priceMax = priceMaxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(priceMaxStr);

        filteredProducts.clear();

        for (Product product : allProducts) {
            boolean matchesSearch = query.isEmpty() || 
                product.getName().toLowerCase().contains(query.toLowerCase()) ||
                product.getBrand().toLowerCase().contains(query.toLowerCase());

            boolean matchesFilter = filter.equals("all") ||
                (filter.equals("lowstock") && product.getStock() < 10);

            boolean matchesCategory = selectedCategory.isEmpty() || product.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesBrand = selectedBrand.isEmpty() || product.getBrand().equalsIgnoreCase(selectedBrand);
            boolean matchesPrice = product.getPrice() >= priceMin && product.getPrice() <= priceMax;

            if (matchesSearch && matchesFilter && matchesCategory && matchesBrand && matchesPrice) {
                filteredProducts.add(product);
            }
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    @Override
    public void onProductClick(Product product) {
        // Show edit/delete options
        showProductOptions(product);
    }

    @Override
    public void onFavoriteClick(Product product, int position) {
        // Admin view - no favorite functionality
    }

    private void showProductOptions(Product product) {
        String[] options = {"Chỉnh sửa", "Xóa sản phẩm"};

        new AlertDialog.Builder(this)
                .setTitle(product.getName())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            editProduct(product);
                            break;
                        case 1:
                            confirmDeleteProduct(product);
                            break;
                    }
                })
                .show();
    }

    private void showAddProductDialog() {
        // Navigate to AddEditProductActivity
        Intent intent = new Intent(this, AddEditProductActivity.class);
        startActivity(intent);
    }

    private void editProduct(Product product) {
        // Navigate to AddEditProductActivity with product data
        Intent intent = new Intent(this, AddEditProductActivity.class);
        intent.putExtra(Constants.EXTRA_PRODUCT, product);
        startActivity(intent);
    }

    private void showProductDialog(Product existingProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_product, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Initialize views
        TextInputEditText etProductName = dialogView.findViewById(R.id.etProductName);
        MaterialAutoCompleteTextView actvBrand = dialogView.findViewById(R.id.actvBrand);
        MaterialAutoCompleteTextView actvCategory = dialogView.findViewById(R.id.actvCategory);
        TextInputEditText etOldPrice = dialogView.findViewById(R.id.etOldPrice);
        TextInputEditText etPrice = dialogView.findViewById(R.id.etPrice);
        TextInputEditText etStock = dialogView.findViewById(R.id.etStock);
        TextInputEditText etDescription = dialogView.findViewById(R.id.etDescription);
        TextInputEditText etCpu = dialogView.findViewById(R.id.etCpu);
        TextInputEditText etRam = dialogView.findViewById(R.id.etRam);
        TextInputEditText etStorage = dialogView.findViewById(R.id.etStorage);
        TextInputEditText etGpu = dialogView.findViewById(R.id.etGpu);
        TextInputEditText etScreen = dialogView.findViewById(R.id.etScreen);
        TextInputEditText etImageUrl = dialogView.findViewById(R.id.etImageUrl);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancel);
        MaterialButton btnSave = dialogView.findViewById(R.id.btnSave);

        // Setup brand dropdown
        String[] brands = {"ASUS", "MSI", "Acer", "Dell", "HP", "Apple", "Lenovo", "LG", "Gigabyte", "Razer"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, brands);
        actvBrand.setAdapter(brandAdapter);

        // Setup category dropdown
        String[] categories = {"Văn phòng", "Gaming", "Mỏng nhẹ", "Sinh viên", "Cảm ứng", "Laptop AI", "Đồ hoạ – kỹ thuật", "MacBook CTO"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(categoryAdapter);

        // Fill existing data if editing
        if (existingProduct != null) {
            etProductName.setText(existingProduct.getName());
            actvBrand.setText(existingProduct.getBrand(), false);
            actvCategory.setText(existingProduct.getCategory(), false);
            etOldPrice.setText(String.valueOf(existingProduct.getOldPrice()));
            etPrice.setText(String.valueOf(existingProduct.getPrice()));
            etStock.setText(String.valueOf(existingProduct.getStock()));
            etDescription.setText(existingProduct.getDescription());
            
            // Parse specs string: "CPU: xxx, RAM: xxx, Storage: xxx, GPU: xxx, Screen: xxx"
            if (existingProduct.getSpecs() != null && !existingProduct.getSpecs().isEmpty()) {
                String[] specParts = existingProduct.getSpecs().split(",");
                for (String part : specParts) {
                    String[] keyValue = part.trim().split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        
                        switch (key) {
                            case "CPU":
                                etCpu.setText(value);
                                break;
                            case "RAM":
                                etRam.setText(value);
                                break;
                            case "Storage":
                                etStorage.setText(value);
                                break;
                            case "GPU":
                                etGpu.setText(value);
                                break;
                            case "Screen":
                                etScreen.setText(value);
                                break;
                        }
                    }
                }
            }
            
            if (existingProduct.getImageUrl() != null && !existingProduct.getImageUrl().isEmpty()) {
                etImageUrl.setText(existingProduct.getImageUrl());
            }
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnSave.setOnClickListener(v -> {
            String name = etProductName.getText().toString().trim();
            String brand = actvBrand.getText().toString().trim();
            String category = actvCategory.getText().toString().trim();
            String oldPriceStr = etOldPrice.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String stockStr = etStock.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            // Validation
            if (name.isEmpty() || brand.isEmpty() || category.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc (*)", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                double oldPrice = oldPriceStr.isEmpty() ? price : Double.parseDouble(oldPriceStr);
                int stock = Integer.parseInt(stockStr);

                // Build specs string (format: "CPU: xxx, RAM: xxx, Storage: xxx, GPU: xxx, Screen: xxx")
                StringBuilder specsBuilder = new StringBuilder();
                String cpu = etCpu.getText().toString().trim();
                String ram = etRam.getText().toString().trim();
                String storage = etStorage.getText().toString().trim();
                String gpu = etGpu.getText().toString().trim();
                String screen = etScreen.getText().toString().trim();
                
                if (!cpu.isEmpty()) specsBuilder.append("CPU: ").append(cpu);
                if (!ram.isEmpty()) {
                    if (specsBuilder.length() > 0) specsBuilder.append(", ");
                    specsBuilder.append("RAM: ").append(ram);
                }
                if (!storage.isEmpty()) {
                    if (specsBuilder.length() > 0) specsBuilder.append(", ");
                    specsBuilder.append("Storage: ").append(storage);
                }
                if (!gpu.isEmpty()) {
                    if (specsBuilder.length() > 0) specsBuilder.append(", ");
                    specsBuilder.append("GPU: ").append(gpu);
                }
                if (!screen.isEmpty()) {
                    if (specsBuilder.length() > 0) specsBuilder.append(", ");
                    specsBuilder.append("Screen: ").append(screen);
                }
                
                String specs = specsBuilder.toString();
                String imageUrl = etImageUrl.getText().toString().trim();

                if (existingProduct != null) {
                    // Update existing product
                    existingProduct.setName(name);
                    existingProduct.setBrand(brand);
                    existingProduct.setCategory(category);
                    existingProduct.setPrice(price);
                    existingProduct.setOldPrice(oldPrice);
                    existingProduct.setStock(stock);
                    existingProduct.setDescription(description);
                    existingProduct.setSpecs(specs);
                    
                    if (!imageUrl.isEmpty()) {
                        existingProduct.setImageUrl(imageUrl);
                    }

                    repository.updateProduct(existingProduct, new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ManageProductsActivity.this, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                            loadProducts();
                            dialog.dismiss();
                        }
                        
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ManageProductsActivity.this, "Lỗi cập nhật sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Add new product
                    Product newProduct = new Product(
                        "P" + System.currentTimeMillis(),
                        name,
                        price,
                        oldPrice,
                        description,
                        imageUrl.isEmpty() ? "https://via.placeholder.com/400" : imageUrl,
                        brand,
                        category,
                        specs,
                        0f, // rating = 0
                        stock
                    );

                    repository.insertProduct(newProduct, new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ManageProductsActivity.this, "Đã thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
                            loadProducts();
                            dialog.dismiss();
                        }
                        
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ManageProductsActivity.this, "Lỗi thêm sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá và số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void confirmDeleteProduct(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa " + product.getName() + "?")
                .setPositiveButton("埠a", (dialog, which) -> {
                    repository.deleteProduct(product, new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ManageProductsActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            loadProducts();
                        }
                        
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ManageProductsActivity.this, "Không thể xóa sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}
