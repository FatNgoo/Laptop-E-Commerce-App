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
import com.example.otech.repository.DataRepository;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class ManageReviewsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView rvReviews;
    private LinearLayout layoutEmptyState;
    private ReviewAdapter adapter;
    private DataRepository repository;
    private ArrayList<Review> allReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reviews);

        repository = DataRepository.getInstance(getApplicationContext());

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
        repository.getAllReviews(new DataRepository.DataCallback<java.util.List<Review>>() {
            @Override
            public void onSuccess(java.util.List<Review> reviews) {
                allReviews = new ArrayList<>(reviews);

                if (allReviews.isEmpty()) {
                    rvReviews.setVisibility(View.GONE);
                    layoutEmptyState.setVisibility(View.VISIBLE);
                } else {
                    rvReviews.setVisibility(View.VISIBLE);
                    layoutEmptyState.setVisibility(View.GONE);

                    adapter = new ReviewAdapter(ManageReviewsActivity.this, allReviews, true, new ReviewAdapter.OnReviewActionListener() {
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
            @Override
            public void onError(Exception e) {}
        });
    }

    private void showDeleteConfirmation(Review review, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xóa đánh giá")
                .setMessage("Bạn có chắc muốn xóa đánh giá này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    repository.deleteReview(review, new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            if (position >= 0 && position < allReviews.size()) {
                                allReviews.remove(position);
                                adapter.notifyItemRemoved(position);
                            }
                            android.widget.Toast.makeText(ManageReviewsActivity.this, "Đã xóa đánh giá", android.widget.Toast.LENGTH_SHORT).show();
                            
                            if (allReviews.isEmpty()) {
                                rvReviews.setVisibility(View.GONE);
                                layoutEmptyState.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            android.widget.Toast.makeText(ManageReviewsActivity.this, "Lỗi xóa đánh giá", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
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
