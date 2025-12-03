package com.example.otech.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.BannerAdminAdapter;
import com.example.otech.model.Banner;
import com.example.otech.repository.MockDataStore;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AdminBannersActivity extends AppCompatActivity implements BannerAdminAdapter.OnBannerActionListener {

    private MaterialToolbar toolbar;
    private SearchView searchView;
    private RecyclerView rvBanners;
    private FloatingActionButton fabAddBanner;
    private TextView tvTotalBanners, tvActiveBanners, tvInactiveBanners;
    private LinearLayout layoutEmpty;

    private MockDataStore dataStore;
    private BannerAdminAdapter adapter;
    private ArrayList<Banner> banners;
    
    // Image picker
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private ImageView currentImageView;
    private TextInputEditText currentImageUrlEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_banners);

        dataStore = MockDataStore.getInstance();

        setupImagePicker();
        setupPermissionLauncher();
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupSearchView();
        setupListeners();
        loadBanners();
    }
    
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null && currentImageView != null && currentImageUrlEdit != null) {
                        try {
                            // Take persistable URI permission
                            getContentResolver().takePersistableUriPermission(
                                imageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                        } catch (Exception e) {
                            // Some content providers don't support persistable permissions
                            // That's okay, we'll still try to use the URI
                        }
                        
                        // Display image in preview
                        currentImageView.setImageURI(imageUri);
                        currentImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        
                        // Save URI to edit text
                        currentImageUrlEdit.setText(imageUri.toString());
                        
                        Toast.makeText(this, "Đã chọn ảnh thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }
    
    private void setupPermissionLauncher() {
        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Cần cấp quyền truy cập ảnh để tiếp tục", Toast.LENGTH_LONG).show();
                }
            }
        );
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        rvBanners = findViewById(R.id.rvBanners);
        fabAddBanner = findViewById(R.id.fabAddBanner);
        tvTotalBanners = findViewById(R.id.tvTotalBanners);
        tvActiveBanners = findViewById(R.id.tvActiveBanners);
        tvInactiveBanners = findViewById(R.id.tvInactiveBanners);
        layoutEmpty = findViewById(R.id.layoutEmpty);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        banners = new ArrayList<>();
        adapter = new BannerAdminAdapter(this, banners, this);
        rvBanners.setLayoutManager(new LinearLayoutManager(this));
        rvBanners.setAdapter(adapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    private void setupListeners() {
        fabAddBanner.setOnClickListener(v -> showAddEditBannerDialog(null));
    }

    private void loadBanners() {
        banners = dataStore.getAllBanners();
        adapter.updateData(banners);
        updateStatistics();
        updateEmptyState();
    }

    private void updateStatistics() {
        int total = banners.size();
        int active = 0;
        int inactive = 0;

        for (Banner banner : banners) {
            if (banner.isActive()) {
                active++;
            } else {
                inactive++;
            }
        }

        tvTotalBanners.setText(String.valueOf(total));
        tvActiveBanners.setText(String.valueOf(active));
        tvInactiveBanners.setText(String.valueOf(inactive));
    }

    private void updateEmptyState() {
        if (banners.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvBanners.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvBanners.setVisibility(View.VISIBLE);
        }
    }

    private void showAddEditBannerDialog(Banner banner) {
        boolean isEdit = banner != null;
        
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_banner, null);
        
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        ImageView ivBannerPreview = dialogView.findViewById(R.id.ivBannerPreview);
        TextInputEditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        TextInputEditText edtLink = dialogView.findViewById(R.id.edtLink);
        TextInputEditText edtOrder = dialogView.findViewById(R.id.edtOrder);
        TextInputEditText edtImageUrl = dialogView.findViewById(R.id.edtImageUrl);
        SwitchMaterial switchActive = dialogView.findViewById(R.id.switchActive);
        
        // Set title
        tvDialogTitle.setText(isEdit ? "Chỉnh sửa banner" : "Thêm banner mới");
        
        // Fill data if editing
        if (isEdit) {
            edtTitle.setText(banner.getTitle());
            edtLink.setText(banner.getLink());
            edtOrder.setText(String.valueOf(banner.getOrder()));
            edtImageUrl.setText(banner.getImageUrl());
            switchActive.setChecked(banner.isActive());
            loadBannerPreview(ivBannerPreview, banner.getImageUrl());
        }
        
        // Select image button click listener
        dialogView.findViewById(R.id.btnSelectImage).setOnClickListener(v -> {
            currentImageView = ivBannerPreview;
            currentImageUrlEdit = edtImageUrl;
            openImagePicker();
        });
        
        // Image preview click listener - also open gallery
        ivBannerPreview.setOnClickListener(v -> {
            currentImageView = ivBannerPreview;
            currentImageUrlEdit = edtImageUrl;
            openImagePicker();
        });
        
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String link = edtLink.getText().toString().trim();
            String orderStr = edtOrder.getText().toString().trim();
            String imageUrl = edtImageUrl.getText().toString().trim();
            boolean isActive = switchActive.isChecked();
            
            // Validation
            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (link.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập link", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (orderStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập thứ tự", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (imageUrl.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ảnh banner", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int order;
            try {
                order = Integer.parseInt(orderStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Thứ tự phải là số", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (isEdit) {
                // Update existing banner
                banner.setTitle(title);
                banner.setLink(link);
                banner.setOrder(order);
                banner.setImageUrl(imageUrl);
                banner.setActive(isActive);
                
                boolean success = dataStore.updateBanner(banner);
                if (success) {
                    Toast.makeText(this, "Cập nhật banner thành công", Toast.LENGTH_SHORT).show();
                    loadBanners();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Cập nhật banner thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add new banner
                String id = dataStore.generateBannerId();
                Banner newBanner = new Banner(id, imageUrl, title, link, isActive, order);
                
                boolean success = dataStore.addBanner(newBanner);
                if (success) {
                    Toast.makeText(this, "Thêm banner thành công", Toast.LENGTH_SHORT).show();
                    loadBanners();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Thêm banner thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        dialog.show();
    }

    private void openImagePicker() {
        // Check permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                return;
            }
        } else {
            // For Android 12 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }
        }
        
        // Permission granted, open image picker
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    private void loadBannerPreview(ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_category);
            return;
        }

        // Check if it's a URI (uploaded image)
        if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
            try {
                imageView.setImageURI(Uri.parse(imageUrl));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_category);
            }
        } else {
            // Load from drawable resources
            try {
                int resId = getResources().getIdentifier(imageUrl, "drawable", getPackageName());
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView.setImageResource(R.drawable.ic_category);
                }
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_category);
            }
        }
    }

    @Override
    public void onEditClick(Banner banner) {
        showAddEditBannerDialog(banner);
    }

    @Override
    public void onDeleteClick(Banner banner) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa banner \"" + banner.getTitle() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = dataStore.deleteBanner(banner.getId());
                    if (success) {
                        Toast.makeText(this, "Xóa banner thành công", Toast.LENGTH_SHORT).show();
                        loadBanners();
                    } else {
                        Toast.makeText(this, "Xóa banner thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBanners();
    }
}
