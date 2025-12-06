package com.example.otech.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.ProductImageAdapter;
import com.example.otech.model.Product;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddEditProductActivity extends AppCompatActivity implements ProductImageAdapter.OnImageRemoveListener {

    private static final int MAX_IMAGES = 8;

    private MaterialToolbar toolbar;
    private RecyclerView rvImages;
    private MaterialButton btnAddImages, btnSave;
    private TextInputEditText etProductName, etDescription, etOldPrice, etPrice, etStock;
    private TextInputEditText etCpu, etRam, etStorage, etGpu, etScreen;
    private MaterialAutoCompleteTextView actvBrand, actvCategory;

    private ProductImageAdapter imageAdapter;
    private ArrayList<Uri> selectedImages;
    private DataRepository repository;
    private Product existingProduct;
    private boolean isEditMode = false;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        repository = DataRepository.getInstance(getApplicationContext());
        selectedImages = new ArrayList<>();

        // Check if editing existing product
        if (getIntent().hasExtra(Constants.EXTRA_PRODUCT)) {
            existingProduct = (Product) getIntent().getSerializableExtra(Constants.EXTRA_PRODUCT);
            isEditMode = true;
        }

        initViews();
        setupImagePicker();
        setupRecyclerView();
        setupDropdowns();
        loadProductData();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvImages = findViewById(R.id.rvImages);
        btnAddImages = findViewById(R.id.btnAddImages);
        btnSave = findViewById(R.id.btnSave);
        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etOldPrice = findViewById(R.id.etOldPrice);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        etCpu = findViewById(R.id.etCpu);
        etRam = findViewById(R.id.etRam);
        etStorage = findViewById(R.id.etStorage);
        etGpu = findViewById(R.id.etGpu);
        etScreen = findViewById(R.id.etScreen);
        actvBrand = findViewById(R.id.actvBrand);
        actvCategory = findViewById(R.id.actvCategory);

        // Set toolbar title
        if (isEditMode) {
            toolbar.setTitle("Chỉnh sửa sản phẩm");
        } else {
            toolbar.setTitle("Thêm sản phẩm");
        }

        toolbar.setNavigationOnClickListener(v -> {
            if (hasUnsavedChanges()) {
                showDiscardChangesDialog();
            } else {
                finish();
            }
        });
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        
                        if (data.getClipData() != null) {
                            // Multiple images selected
                            int count = data.getClipData().getItemCount();
                            int available = MAX_IMAGES - selectedImages.size();
                            int toAdd = Math.min(count, available);
                            
                            for (int i = 0; i < toAdd; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                // Grant persistable URI permission
                                try {
                                    getContentResolver().takePersistableUriPermission(
                                            imageUri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                selectedImages.add(imageUri);
                            }
                            
                            if (count > available) {
                                Toast.makeText(this, "Chỉ thêm được " + toAdd + " ảnh (tối đa 8 ảnh)", Toast.LENGTH_SHORT).show();
                            }
                        } else if (data.getData() != null) {
                            // Single image selected
                            if (selectedImages.size() < MAX_IMAGES) {
                                Uri imageUri = data.getData();
                                try {
                                    getContentResolver().takePersistableUriPermission(
                                            imageUri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                selectedImages.add(imageUri);
                            } else {
                                Toast.makeText(this, "Đã đạt giới hạn 8 ảnh", Toast.LENGTH_SHORT).show();
                            }
                        }
                        
                        updateImagesList();
                    }
                }
        );
    }

    private void setupRecyclerView() {
        imageAdapter = new ProductImageAdapter(this, selectedImages, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvImages.setLayoutManager(layoutManager);
        rvImages.setAdapter(imageAdapter);
    }

    private void setupDropdowns() {
        // Brand dropdown
        String[] brands = {"ASUS", "MSI", "Acer", "Dell", "HP", "Apple", "Lenovo", "LG", "Gigabyte", "Razer"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, brands);
        actvBrand.setAdapter(brandAdapter);

        // Category dropdown
        String[] categories = {"Văn phòng", "Gaming", "Mỏng nhẹ", "Sinh viên", "Cảm ứng", "Laptop AI", "Đồ hoạ – kỹ thuật", "MacBook CTO"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(categoryAdapter);
    }

    private void loadProductData() {
        if (!isEditMode || existingProduct == null) {
            return;
        }

        // Load basic info
        etProductName.setText(existingProduct.getName());
        actvBrand.setText(existingProduct.getBrand(), false);
        actvCategory.setText(existingProduct.getCategory(), false);
        etDescription.setText(existingProduct.getDescription());
        etOldPrice.setText(String.valueOf(existingProduct.getOldPrice()));
        etPrice.setText(String.valueOf(existingProduct.getPrice()));
        etStock.setText(String.valueOf(existingProduct.getStock()));

        // Parse and load specs
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

        // Load existing images (if any)
        ArrayList<String> existingImageUrls = existingProduct.getImageUrls();
        if (existingImageUrls != null && !existingImageUrls.isEmpty()) {
            // Convert string URLs to URIs and add to selectedImages
            for (String imageUrl : existingImageUrls) {
                try {
                    Uri uri = Uri.parse(imageUrl);
                    selectedImages.add(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            updateImagesList();
        } else if (existingProduct.getImageUrl() != null && !existingProduct.getImageUrl().isEmpty()) {
            // Fallback: If no imageUrls but has single imageUrl
            try {
                Uri uri = Uri.parse(existingProduct.getImageUrl());
                selectedImages.add(uri);
                updateImagesList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupListeners() {
        btnAddImages.setOnClickListener(v -> openImagePicker());
        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void openImagePicker() {
        if (selectedImages.size() >= MAX_IMAGES) {
            Toast.makeText(this, "Đã đạt giới hạn " + MAX_IMAGES + " ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn hình ảnh"));
    }

    private void updateImagesList() {
        imageAdapter.notifyDataSetChanged();
        btnAddImages.setEnabled(selectedImages.size() < MAX_IMAGES);
        
        if (selectedImages.size() >= MAX_IMAGES) {
            btnAddImages.setText("Đã đạt giới hạn 8 ảnh");
        } else {
            btnAddImages.setText("Thêm hình ảnh (" + selectedImages.size() + "/8)");
        }
    }

    @Override
    public void onRemoveImage(int position) {
        if (position >= 0 && position < selectedImages.size()) {
            selectedImages.remove(position);
            updateImagesList();
            Toast.makeText(this, "Đã xóa ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProduct() {
        // Validate required fields
        String name = etProductName.getText().toString().trim();
        String brand = actvBrand.getText().toString().trim();
        String category = actvCategory.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            etProductName.requestFocus();
            return;
        }

        if (brand.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn thương hiệu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập giá bán", Toast.LENGTH_SHORT).show();
            etPrice.requestFocus();
            return;
        }

        if (stockStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số lượng tồn kho", Toast.LENGTH_SHORT).show();
            etStock.requestFocus();
            return;
        }

        if (!isEditMode && selectedImages.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm ít nhất 1 hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            String oldPriceStr = etOldPrice.getText().toString().trim();
            double oldPrice = oldPriceStr.isEmpty() ? price : Double.parseDouble(oldPriceStr);
            int stock = Integer.parseInt(stockStr);

            if (price <= 0) {
                Toast.makeText(this, "Giá bán phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (stock < 0) {
                Toast.makeText(this, "Số lượng tồn kho không được âm", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build specs string
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
            String description = etDescription.getText().toString().trim();

            // Convert URIs to strings for storage
            ArrayList<String> imageUrls = new ArrayList<>();
            for (Uri uri : selectedImages) {
                imageUrls.add(uri.toString());
            }

            boolean success;
            if (isEditMode && existingProduct != null) {
                // Update existing product
                existingProduct.setName(name);
                existingProduct.setBrand(brand);
                existingProduct.setCategory(category);
                existingProduct.setPrice(price);
                existingProduct.setOldPrice(oldPrice);
                existingProduct.setStock(stock);
                existingProduct.setDescription(description);
                existingProduct.setSpecs(specs);
                
                // Always update images if there are new ones, otherwise keep existing
                if (!imageUrls.isEmpty()) {
                    existingProduct.setImageUrls(imageUrls);
                    existingProduct.setImageUrl(imageUrls.get(0)); // Set first image as main
                } else if (existingProduct.getImageUrl() == null || existingProduct.getImageUrl().isEmpty()) {
                    // If no images at all, set placeholder
                    existingProduct.setImageUrl("https://via.placeholder.com/400");
                }

                repository.updateProduct(existingProduct, new DataRepository.VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddEditProductActivity.this, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AddEditProductActivity.this, "Lỗi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
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
                        imageUrls.isEmpty() ? "https://via.placeholder.com/400" : imageUrls.get(0),
                        brand,
                        category,
                        specs,
                        0f,
                        stock
                );
                
                if (!imageUrls.isEmpty()) {
                    newProduct.setImageUrls(imageUrls);
                }

                repository.insertProduct(newProduct, new DataRepository.VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddEditProductActivity.this, "Đã thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AddEditProductActivity.this, "Lỗi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá và số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasUnsavedChanges() {
        if (!selectedImages.isEmpty()) return true;
        if (etProductName.getText() != null && !etProductName.getText().toString().trim().isEmpty()) return true;
        if (etPrice.getText() != null && !etPrice.getText().toString().trim().isEmpty()) return true;
        return false;
    }

    private void showDiscardChangesDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hủy thay đổi?")
                .setMessage("Các thay đổi chưa lưu sẽ bị mất")
                .setPositiveButton("Hủy bỏ", (dialog, which) -> finish())
                .setNegativeButton("Tiếp tục chỉnh sửa", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (hasUnsavedChanges()) {
            showDiscardChangesDialog();
        } else {
            super.onBackPressed();
        }
    }
}
