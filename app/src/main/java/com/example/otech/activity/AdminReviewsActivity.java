package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.ProductReviewAdapter;
import com.example.otech.model.Product;
import com.example.otech.model.Review;
import com.example.otech.repository.DataRepository;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Locale;

public class AdminReviewsActivity extends AppCompatActivity implements ProductReviewAdapter.OnProductClickListener {

    private MaterialToolbar toolbar;
    private SearchView searchView;
    private ChipGroup chipGroupRating, chipGroupBrand;
    private RecyclerView rvProducts;
    private TextView tvTotalProducts, tvTotalReviews, tvAverageRating;
    private View layoutEmpty;
    
    private ProductReviewAdapter adapter;
    private DataRepository repository;
    private ArrayList<Product> allProducts;
    private String currentSearchQuery = "";
    private float currentMinRating = 0;
    private String currentBrand = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reviews);

        repository = DataRepository.getInstance(getApplicationContext());
        
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupSearch();
        setupFilters();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        chipGroupRating = findViewById(R.id.chipGroupRating);
        chipGroupBrand = findViewById(R.id.chipGroupBrand);
        rvProducts = findViewById(R.id.rvProducts);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalReviews = findViewById(R.id.tvTotalReviews);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        layoutEmpty = findViewById(R.id.layoutEmpty);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        repository.getAllProducts(new DataRepository.DataCallback<java.util.List<Product>>() {
            @Override
            public void onSuccess(java.util.List<Product> products) {
                allProducts = new ArrayList<>(products);
                adapter = new ProductReviewAdapter(AdminReviewsActivity.this, allProducts, AdminReviewsActivity.this);
                rvProducts.setLayoutManager(new LinearLayoutManager(AdminReviewsActivity.this));
                rvProducts.setAdapter(adapter);
                updateEmptyState();
                updateStatistics();
            }
            @Override
            public void onError(Exception e) {}
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                applyFilters();
                return true;
            }
        });
    }

    private void setupFilters() {
        // Brand filter
        chipGroupBrand.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentBrand = "";
            } else {
                int checkedId = checkedIds.get(0);
                
                if (checkedId == R.id.chipAllBrands) {
                    currentBrand = "";
                } else if (checkedId == R.id.chipApple) {
                    currentBrand = "Apple";
                } else if (checkedId == R.id.chipDell) {
                    currentBrand = "Dell";
                } else if (checkedId == R.id.chipAsus) {
                    currentBrand = "Asus";
                } else if (checkedId == R.id.chipHP) {
                    currentBrand = "HP";
                } else if (checkedId == R.id.chipLenovo) {
                    currentBrand = "Lenovo";
                } else if (checkedId == R.id.chipMSI) {
                    currentBrand = "MSI";
                } else if (checkedId == R.id.chipAcer) {
                    currentBrand = "Acer";
                } else if (checkedId == R.id.chipLG) {
                    currentBrand = "LG";
                } else if (checkedId == R.id.chipGigabyte) {
                    currentBrand = "Gigabyte";
                } else if (checkedId == R.id.chipRazer) {
                    currentBrand = "Razer";
                }
            }
            
            applyFilters();
        });
        
        // Rating filter
        chipGroupRating.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentMinRating = 0;
            } else {
                int checkedId = checkedIds.get(0);
                
                if (checkedId == R.id.chipAll) {
                    currentMinRating = 0;
                } else if (checkedId == R.id.chip5Star) {
                    currentMinRating = 5.0f;
                } else if (checkedId == R.id.chip4Star) {
                    currentMinRating = 4.0f;
                } else if (checkedId == R.id.chip3Star) {
                    currentMinRating = 3.0f;
                } else if (checkedId == R.id.chipLow) {
                    currentMinRating = -1.0f; // Special flag for < 3
                }
            }
            
            applyFilters();
        });
    }

    private void applyFilters() {
        if (adapter == null || allProducts == null) {
            return;
        }
        // Special case for "< 3 stars"
        if (currentMinRating == -1.0f) {
            filterLowRating();
        } else {
            // Reset to full dataset before filtering
            adapter.updateData(allProducts);
            adapter.filter(currentSearchQuery, currentMinRating, currentBrand);
            updateEmptyState();
        }
    }

    private void filterLowRating() {
        if (adapter == null || allProducts == null) {
            return;
        }
        // Reset to full dataset first
        adapter.updateData(allProducts);
        
        // Apply custom filter for rating < 3
        String lowerCaseQuery = currentSearchQuery.toLowerCase().trim();
        ArrayList<Product> filtered = new ArrayList<>();
        
        for (Product product : allProducts) {
            boolean matchesSearch = currentSearchQuery.isEmpty() || 
                product.getName().toLowerCase().contains(lowerCaseQuery) ||
                product.getBrand().toLowerCase().contains(lowerCaseQuery);
            
            boolean matchesRating = product.getRating() < 3.0f;
            
            boolean matchesBrand = (currentBrand == null || currentBrand.isEmpty()) ||
                product.getBrand().equalsIgnoreCase(currentBrand);
            
            if (matchesSearch && matchesRating && matchesBrand) {
                filtered.add(product);
            }
        }
        
        adapter.updateData(filtered);
        updateEmptyState();
    }

    private void loadData() {
        repository.getAllProducts(new DataRepository.DataCallback<java.util.List<Product>>() {
            @Override
            public void onSuccess(java.util.List<Product> products) {
                allProducts = new ArrayList<>(products);
                if (adapter != null) {
                    adapter.updateData(allProducts);
                    updateEmptyState();
                }
            }
            @Override
            public void onError(Exception e) {}
        });
    }

    private void updateStatistics() {
        // Total products
        repository.getAllProducts(new DataRepository.DataCallback<java.util.List<Product>>() {
            @Override
            public void onSuccess(java.util.List<Product> products) {
                tvTotalProducts.setText(String.valueOf(products.size()));
            }
            @Override
            public void onError(Exception e) {}
        });
        
        // Total reviews
        repository.getAllReviews(new DataRepository.DataCallback<java.util.List<Review>>() {
            @Override
            public void onSuccess(java.util.List<Review> reviews) {
                tvTotalReviews.setText(String.valueOf(reviews.size()));
            }
            @Override
            public void onError(Exception e) {}
        });
        
        // Average rating (only products with reviews)
        repository.getAllProducts(new DataRepository.DataCallback<java.util.List<Product>>() {
            @Override
            public void onSuccess(java.util.List<Product> products) {
                float totalRating = 0;
                int count = 0;
                for (Product product : products) {
                    if (product.getRating() > 0) {
                        totalRating += product.getRating();
                        count++;
                    }
                }
                float avgRating = count > 0 ? totalRating / count : 0;
                tvAverageRating.setText(String.format(Locale.getDefault(), "%.1f", avgRating));
            }
            @Override
            public void onError(Exception e) {}
        });
    }

    private void updateEmptyState() {
        if (adapter == null) {
            return;
        }
        if (adapter.getItemCount() == 0) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvProducts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, AdminProductReviewsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning from detail page
        loadData();
        updateStatistics();
    }
}
