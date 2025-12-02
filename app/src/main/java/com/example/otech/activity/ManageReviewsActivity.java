package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.ReviewAdapter;
import com.example.otech.model.Review;
import com.example.otech.repository.MockDataStore;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class ManageReviewsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView rvReviews;
    private LinearLayout layoutEmptyState;
    private ReviewAdapter adapter;
    private MockDataStore dataStore;
    private ArrayList<Review> allReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reviews);

        dataStore = MockDataStore.getInstance();

        initViews();
        setupToolbar();
        loadReviews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvReviews = findViewById(R.id.rvReviews);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý đánh giá");
        }
    }

    private void loadReviews() {
        allReviews = dataStore.getAllReviews();

        if (allReviews.isEmpty()) {
            rvReviews.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvReviews.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);

            adapter = new ReviewAdapter(this, allReviews, true, new ReviewAdapter.OnReviewActionListener() {
                @Override
                public void onDeleteReview(Review review, int position) {
                    showDeleteConfirmation(review, position);
                }

                @Override
                public void onViewProduct(Review review) {
                    // Navigate to product detail
                    Intent intent = new Intent(ManageReviewsActivity.this, ProductDetailActivity.class);
                    intent.putExtra("productId", review.getProductId());
                    startActivity(intent);
                }
            });
            rvReviews.setAdapter(adapter);
        }
    }

    private void showDeleteConfirmation(Review review, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xóa đánh giá")
                .setMessage("Bạn có chắc muốn xóa đánh giá này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = dataStore.deleteReview(review.getId());
                    if (success) {
                        allReviews.remove(position);
                        adapter.notifyItemRemoved(position);
                        android.widget.Toast.makeText(this, "Đã xóa đánh giá", android.widget.Toast.LENGTH_SHORT).show();
                        
                        if (allReviews.isEmpty()) {
                            rvReviews.setVisibility(View.GONE);
                            layoutEmptyState.setVisibility(View.VISIBLE);
                        }
                    } else {
                        android.widget.Toast.makeText(this, "Lỗi xóa đánh giá", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
