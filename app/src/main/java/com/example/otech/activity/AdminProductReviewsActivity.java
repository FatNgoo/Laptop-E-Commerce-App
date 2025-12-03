package com.example.otech.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.ReviewAdminAdapter;
import com.example.otech.model.Product;
import com.example.otech.model.Review;
import com.example.otech.repository.MockDataStore;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Locale;

public class AdminProductReviewsActivity extends AppCompatActivity implements ReviewAdminAdapter.OnReviewDeletedListener {

    private MaterialToolbar toolbar;
    private ImageView ivProductImage;
    private TextView tvProductName, tvRating, tvReviewCount;
    private ProgressBar progressBar5Star, progressBar4Star, progressBar3Star, progressBar2Star, progressBar1Star;
    private TextView tvCount5Star, tvCount4Star, tvCount3Star, tvCount2Star, tvCount1Star;
    private ChipGroup chipGroupRating;
    private RecyclerView rvReviews;
    private View layoutEmpty;
    
    private ReviewAdminAdapter adapter;
    private MockDataStore dataStore;
    private Product product;
    private ArrayList<Review> allReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_reviews);

        dataStore = MockDataStore.getInstance();
        product = (Product) getIntent().getSerializableExtra("product");
        
        if (product == null) {
            finish();
            return;
        }
        
        initViews();
        setupToolbar();
        setupProductInfo();
        setupRecyclerView();
        setupFilters();
        updateRatingStatistics();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvRating = findViewById(R.id.tvRating);
        tvReviewCount = findViewById(R.id.tvReviewCount);
        
        progressBar5Star = findViewById(R.id.progressBar5Star);
        progressBar4Star = findViewById(R.id.progressBar4Star);
        progressBar3Star = findViewById(R.id.progressBar3Star);
        progressBar2Star = findViewById(R.id.progressBar2Star);
        progressBar1Star = findViewById(R.id.progressBar1Star);
        
        tvCount5Star = findViewById(R.id.tvCount5Star);
        tvCount4Star = findViewById(R.id.tvCount4Star);
        tvCount3Star = findViewById(R.id.tvCount3Star);
        tvCount2Star = findViewById(R.id.tvCount2Star);
        tvCount1Star = findViewById(R.id.tvCount1Star);
        
        chipGroupRating = findViewById(R.id.chipGroupRating);
        rvReviews = findViewById(R.id.rvReviews);
        layoutEmpty = findViewById(R.id.layoutEmpty);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupProductInfo() {
        tvProductName.setText(product.getName());
        tvRating.setText(String.format(Locale.getDefault(), "%.1f", product.getRating()));
        
        int reviewCount = dataStore.getReviewCountForProduct(product.getId());
        tvReviewCount.setText(reviewCount + " đánh giá");
        
        // Load product image
        loadProductImage(product.getImageUrl());
    }

    private void loadProductImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            ivProductImage.setImageResource(R.drawable.ic_category);
            return;
        }

        // Check if it's a URI (uploaded image)
        if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
            try {
                ivProductImage.setImageURI(android.net.Uri.parse(imageUrl));
                ivProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                ivProductImage.setImageResource(R.drawable.ic_category);
            }
        } else {
            // Try to load from drawable resources
            try {
                int resId = getResources().getIdentifier(
                    imageUrl, "drawable", getPackageName()
                );
                
                if (resId != 0) {
                    ivProductImage.setImageResource(resId);
                } else {
                    ivProductImage.setImageResource(R.drawable.ic_category);
                }
            } catch (Exception e) {
                ivProductImage.setImageResource(R.drawable.ic_category);
            }
        }
    }

    private void setupRecyclerView() {
        allReviews = dataStore.getProductReviews(product.getId());
        adapter = new ReviewAdminAdapter(this, allReviews, this);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(adapter);
        
        updateEmptyState();
    }

    private void setupFilters() {
        chipGroupRating.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                adapter.filter(0, 0);
            } else {
                int checkedId = checkedIds.get(0);
                
                if (checkedId == R.id.chipAll) {
                    adapter.filter(0, 0);
                } else if (checkedId == R.id.chip5Star) {
                    adapter.filter(5.0f, 5.0f);
                } else if (checkedId == R.id.chip4Star) {
                    adapter.filter(4.0f, 4.99f);
                } else if (checkedId == R.id.chip3Star) {
                    adapter.filter(3.0f, 3.99f);
                } else if (checkedId == R.id.chip2Star) {
                    adapter.filter(2.0f, 2.99f);
                } else if (checkedId == R.id.chip1Star) {
                    adapter.filter(1.0f, 1.99f);
                }
            }
            
            updateEmptyState();
        });
    }

    private void updateRatingStatistics() {
        allReviews = dataStore.getProductReviews(product.getId());
        int totalReviews = allReviews.size();
        
        if (totalReviews == 0) {
            return;
        }
        
        // Count reviews by rating
        int count5 = 0, count4 = 0, count3 = 0, count2 = 0, count1 = 0;
        
        for (Review review : allReviews) {
            float rating = review.getRating();
            if (rating >= 5.0f) count5++;
            else if (rating >= 4.0f) count4++;
            else if (rating >= 3.0f) count3++;
            else if (rating >= 2.0f) count2++;
            else count1++;
        }
        
        // Update UI
        tvCount5Star.setText(String.valueOf(count5));
        tvCount4Star.setText(String.valueOf(count4));
        tvCount3Star.setText(String.valueOf(count3));
        tvCount2Star.setText(String.valueOf(count2));
        tvCount1Star.setText(String.valueOf(count1));
        
        // Update progress bars
        progressBar5Star.setMax(totalReviews);
        progressBar5Star.setProgress(count5);
        
        progressBar4Star.setMax(totalReviews);
        progressBar4Star.setProgress(count4);
        
        progressBar3Star.setMax(totalReviews);
        progressBar3Star.setProgress(count3);
        
        progressBar2Star.setMax(totalReviews);
        progressBar2Star.setProgress(count2);
        
        progressBar1Star.setMax(totalReviews);
        progressBar1Star.setProgress(count1);
    }

    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvReviews.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvReviews.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReviewDeleted() {
        // Refresh data after deleting review
        allReviews = dataStore.getProductReviews(product.getId());
        adapter.updateData(allReviews);
        updateRatingStatistics();
        updateEmptyState();
        
        // Update product info
        int reviewCount = dataStore.getReviewCountForProduct(product.getId());
        tvReviewCount.setText(reviewCount + " đánh giá");
        tvRating.setText(String.format(Locale.getDefault(), "%.1f", product.getRating()));
    }
}
