package com.example.otech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.otech.R;
import com.example.otech.adapter.ProductImagePreviewAdapter;
import com.example.otech.adapter.ProductThumbnailAdapter;
import com.example.otech.adapter.RelatedProductAdapter;
import com.example.otech.adapter.ReviewAdapter;
import com.example.otech.model.Product;
import com.example.otech.model.Review;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;
import com.example.otech.util.Constants;
import com.example.otech.util.FormatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import android.os.Handler;
import android.os.Looper;

public class ProductDetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ViewPager2 vpProductImages;
    private LinearLayout layoutIndicators;
    private FloatingActionButton fabFavorite;
    private TextView tvProductName, tvBrand, tvRating, tvPrice, tvOldPrice, tvDiscount, tvSoldCount, tvStock;
    private TextView tvSpecCpu, tvSpecRam, tvSpecStorage, tvSpecGpu, tvSpecDisplay;
    private TextView tvDescription, tvAverageRating, tvTotalReviews;
    private TextView tvRating5Count, tvRating4Count, tvRating3Count, tvRating2Count, tvRating1Count;
    private android.widget.ProgressBar pbRating5, pbRating4, pbRating3, pbRating2, pbRating1;
    private RatingBar ratingBar, rbAverageRating;
    private MaterialButton btnAddToCart, btnBuyNow, btnCompare, btnWriteReview, btnToggleDescription;
    private RecyclerView rvReviews, rvRelatedProducts, rvThumbnails;
    private LinearLayout layoutAvailableButtons;
    private MaterialCardView cardOutOfStock;
    private com.example.otech.adapter.ProductThumbnailAdapter thumbnailAdapter;

    private Product product;
    private DataRepository repository;
    private String currentUserId;
    private int quantity = 1;
    private boolean isDescriptionExpanded = false;
    
    // Auto-scroll variables
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;
    private static final long AUTO_SCROLL_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        repository = DataRepository.getInstance(getApplicationContext());
        
        // Get current user
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getString(Constants.KEY_USER_ID, "");

        // Get product from intent
        product = (Product) getIntent().getSerializableExtra(Constants.EXTRA_PRODUCT);
        
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayProductInfo();
        setupListeners();
        trackProductView();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        vpProductImages = findViewById(R.id.vpProductImages);
        layoutIndicators = findViewById(R.id.layoutIndicators);
        fabFavorite = findViewById(R.id.fabFavorite);
        tvProductName = findViewById(R.id.tvProductName);
        tvBrand = findViewById(R.id.tvBrand);
        tvRating = findViewById(R.id.tvRating);
        tvSoldCount = findViewById(R.id.tvSoldCount);
        tvStock = findViewById(R.id.tvStock);
        tvPrice = findViewById(R.id.tvPrice);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvSpecCpu = findViewById(R.id.tvSpecCpu);
        tvSpecRam = findViewById(R.id.tvSpecRam);
        tvSpecStorage = findViewById(R.id.tvSpecStorage);
        tvSpecGpu = findViewById(R.id.tvSpecGpu);
        tvSpecDisplay = findViewById(R.id.tvSpecDisplay);
        tvDescription = findViewById(R.id.tvDescription);
        ratingBar = findViewById(R.id.ratingBar);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnCompare = findViewById(R.id.btnCompare);
        btnToggleDescription = findViewById(R.id.btnToggleDescription);
        
        // Reviews section
        tvAverageRating = findViewById(R.id.tvAverageRating);
        tvTotalReviews = findViewById(R.id.tvTotalReviews);
        rbAverageRating = findViewById(R.id.rbAverageRating);
        btnWriteReview = findViewById(R.id.btnWriteReview);
        rvReviews = findViewById(R.id.rvReviews);
        rvRelatedProducts = findViewById(R.id.rvRelatedProducts);
        
        // Rating breakdown
        pbRating5 = findViewById(R.id.pbRating5);
        pbRating4 = findViewById(R.id.pbRating4);
        pbRating3 = findViewById(R.id.pbRating3);
        pbRating2 = findViewById(R.id.pbRating2);
        pbRating1 = findViewById(R.id.pbRating1);
        tvRating5Count = findViewById(R.id.tvRating5Count);
        tvRating4Count = findViewById(R.id.tvRating4Count);
        tvRating3Count = findViewById(R.id.tvRating3Count);
        tvRating2Count = findViewById(R.id.tvRating2Count);
        tvRating1Count = findViewById(R.id.tvRating1Count);
        
        // Out of stock views
        layoutAvailableButtons = findViewById(R.id.layoutAvailableButtons);
        cardOutOfStock = findViewById(R.id.cardOutOfStock);
        
        // Thumbnails
        rvThumbnails = findViewById(R.id.rvThumbnails);
        
        setupReviewsSection();
        setupRelatedProducts();
        setupImageGallery();
    }
    
    private void setupImageGallery() {
        // Get images from Product model
        ArrayList<String> imageUrls = product.getImageUrls();
        if (imageUrls == null || imageUrls.isEmpty()) {
            // Fallback to default images
            imageUrls = new ArrayList<>();
            imageUrls.add("laptop1");
            imageUrls.add("laptop2");
            imageUrls.add("laptop3");
        }
        
        // Use new adapter that supports both URI and drawable names
        ProductImagePreviewAdapter adapter = new ProductImagePreviewAdapter(this, imageUrls);
        vpProductImages.setAdapter(adapter);
        
        // Setup thumbnails
        setupThumbnails(imageUrls);
        
        // Setup indicators
        setupIndicators(imageUrls.size());
        vpProductImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);
                // Update thumbnail selection
                if (thumbnailAdapter != null) {
                    thumbnailAdapter.setSelectedPosition(position);
                    // Scroll thumbnail to center
                    rvThumbnails.smoothScrollToPosition(position);
                }
            }
        });
        
        // Setup auto-scroll
        setupAutoScroll(imageUrls.size());
    }
    
    private void setupThumbnails(ArrayList<String> imageUrls) {
        if (imageUrls == null || imageUrls.size() <= 1) {
            rvThumbnails.setVisibility(View.GONE);
            return;
        }
        
        rvThumbnails.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvThumbnails.setLayoutManager(layoutManager);
        
        thumbnailAdapter = new ProductThumbnailAdapter(this, imageUrls, position -> {
            // When thumbnail clicked, update ViewPager
            vpProductImages.setCurrentItem(position, true);
        });
        rvThumbnails.setAdapter(thumbnailAdapter);
    }
    
    private void setupAutoScroll(final int imageCount) {
        if (imageCount <= 1) return; // No need to auto-scroll if only 1 image
        
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = vpProductImages.getCurrentItem();
                int nextItem = (currentItem + 1) % imageCount;
                vpProductImages.setCurrentItem(nextItem, true);
                autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
        
        // Start auto-scroll
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }
    
    private void setupIndicators(int count) {
        layoutIndicators.removeAllViews();
        ImageView[] indicators = new ImageView[count];
        
        for (int i = 0; i < count; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageResource(R.drawable.ic_indicator_inactive);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            
            layoutIndicators.addView(indicators[i], params);
        }
        
        if (count > 0) {
            indicators[0].setImageResource(R.drawable.ic_indicator_active);
        }
    }
    
    private void updateIndicators(int position) {
        int count = layoutIndicators.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView indicator = (ImageView) layoutIndicators.getChildAt(i);
            if (i == position) {
                indicator.setImageResource(R.drawable.ic_indicator_active);
            } else {
                indicator.setImageResource(R.drawable.ic_indicator_inactive);
            }
        }
    }
    
    private void setupReviewsSection() {
        // Setup RecyclerView for reviews
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.setNestedScrollingEnabled(false);
        
        loadReviews();
    }
    
    private void loadReviews() {
        repository.getProductReviews(product.getId(), new DataRepository.DataCallback<java.util.List<Review>>() {
            @Override
            public void onSuccess(java.util.List<Review> reviews) {
                ArrayList<Review> allReviews = new ArrayList<>(reviews);
        
        if (!allReviews.isEmpty()) {
            // Show only first 5 reviews initially
            int maxReviews = Math.min(5, allReviews.size());
            ArrayList<Review> displayedReviews = new ArrayList<>(allReviews.subList(0, maxReviews));
            
            ReviewAdapter adapter = new ReviewAdapter(ProductDetailActivity.this, displayedReviews);
            rvReviews.setAdapter(adapter);
            rvReviews.setVisibility(View.VISIBLE);
            
            // Show "View More" button if there are more than 5 reviews
            MaterialButton btnViewMore = findViewById(R.id.btnViewMoreReviews);
            if (allReviews.size() > 5) {
                btnViewMore.setVisibility(View.VISIBLE);
                btnViewMore.setOnClickListener(v -> {
                    // Show all reviews
                    ReviewAdapter fullAdapter = new ReviewAdapter(ProductDetailActivity.this, allReviews);
                    rvReviews.setAdapter(fullAdapter);
                    btnViewMore.setVisibility(View.GONE);
                });
            } else {
                btnViewMore.setVisibility(View.GONE);
            }
            
            // Calculate rating statistics
            int[] ratingCounts = new int[6]; // Index 0 unused, 1-5 for star ratings
            int totalReviews = allReviews.size();
            
            for (Review review : allReviews) {
                int rating = (int) review.getRating();
                if (rating >= 1 && rating <= 5) {
                    ratingCounts[rating]++;
                }
            }
            
            // Update rating breakdown UI
            updateRatingBreakdown(ratingCounts, totalReviews);
            
            tvAverageRating.setText(String.format("%.1f", product.getRating()));
            rbAverageRating.setRating(product.getRating());
            tvTotalReviews.setText(totalReviews + " đánh giá");
        } else {
            rvReviews.setVisibility(View.GONE);
            MaterialButton btnViewMore = findViewById(R.id.btnViewMoreReviews);
            btnViewMore.setVisibility(View.GONE);
            tvAverageRating.setText("Chưa có");
            rbAverageRating.setRating(0);
            tvTotalReviews.setText("Chưa có đánh giá");
            
            // Reset rating breakdown
            updateRatingBreakdown(new int[6], 0);
        }
            }

            @Override
            public void onError(Exception e) {
                // Handle error - show empty state
                rvReviews.setVisibility(View.GONE);
                MaterialButton btnViewMore = findViewById(R.id.btnViewMoreReviews);
                btnViewMore.setVisibility(View.GONE);
                tvAverageRating.setText("Chưa có");
                rbAverageRating.setRating(0);
                tvTotalReviews.setText("Chưa có đánh giá");
                updateRatingBreakdown(new int[6], 0);
            }
        });
    }
    
    private void updateRatingBreakdown(int[] ratingCounts, int totalReviews) {
        if (totalReviews == 0) {
            // No reviews, set all to 0
            pbRating5.setProgress(0);
            pbRating4.setProgress(0);
            pbRating3.setProgress(0);
            pbRating2.setProgress(0);
            pbRating1.setProgress(0);
            tvRating5Count.setText("0");
            tvRating4Count.setText("0");
            tvRating3Count.setText("0");
            tvRating2Count.setText("0");
            tvRating1Count.setText("0");
            return;
        }
        
        // Calculate percentages and update UI
        int count5 = ratingCounts[5];
        int count4 = ratingCounts[4];
        int count3 = ratingCounts[3];
        int count2 = ratingCounts[2];
        int count1 = ratingCounts[1];
        
        int percent5 = (count5 * 100) / totalReviews;
        int percent4 = (count4 * 100) / totalReviews;
        int percent3 = (count3 * 100) / totalReviews;
        int percent2 = (count2 * 100) / totalReviews;
        int percent1 = (count1 * 100) / totalReviews;
        
        pbRating5.setProgress(percent5);
        pbRating4.setProgress(percent4);
        pbRating3.setProgress(percent3);
        pbRating2.setProgress(percent2);
        pbRating1.setProgress(percent1);
        
        tvRating5Count.setText(String.valueOf(count5));
        tvRating4Count.setText(String.valueOf(count4));
        tvRating3Count.setText(String.valueOf(count3));
        tvRating2Count.setText(String.valueOf(count2));
        tvRating1Count.setText(String.valueOf(count1));
    }

    private void displayProductInfo() {
        tvProductName.setText(product.getName());
        tvBrand.setText(product.getBrand());
        tvPrice.setText(FormatUtils.formatCurrency(product.getPrice()));
        tvRating.setText(String.format("%.1f", product.getRating()));
        ratingBar.setRating(product.getRating());
        tvSoldCount.setText("Đã bán: " + product.getSoldCount());
        tvStock.setText("Còn lại: " + product.getStock());
        tvDescription.setText(product.getDescription());

        // Parse specs and display
        parseAndDisplaySpecs(product.getSpecs());

        // Old price and discount
        if (product.getOldPrice() > product.getPrice()) {
            tvOldPrice.setVisibility(View.VISIBLE);
            tvOldPrice.setText(FormatUtils.formatCurrency(product.getOldPrice()));
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            
            tvDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(FormatUtils.formatDiscount(product.getDiscountPercent()));
        } else {
            tvOldPrice.setVisibility(View.GONE);
            tvDiscount.setVisibility(View.GONE);
        }

        // Update favorite icon
        updateFavoriteIcon();
        
        // Check stock and update button visibility
        checkStockAvailability();
    }

    private void parseAndDisplaySpecs(String specs) {
        // Parse specs string like "CPU: i7, RAM: 16GB, Storage: 512GB, GPU: RTX 4060, Display: 16\" FHD"
        String[] specLines = specs.split(",");
        for (String spec : specLines) {
            String[] parts = spec.trim().split(":");
            if (parts.length == 2) {
                String key = parts[0].trim().toUpperCase();
                String value = parts[1].trim();
                
                switch (key) {
                    case "CPU":
                        tvSpecCpu.setText(value);
                        break;
                    case "RAM":
                        tvSpecRam.setText(value);
                        break;
                    case "STORAGE":
                    case "Ổ CỨNG":
                        tvSpecStorage.setText(value);
                        break;
                    case "GPU":
                    case "VGA":
                    case "CARD ĐỒ HỌA":
                        tvSpecGpu.setText(value);
                        break;
                    case "DISPLAY":
                    case "MÀN HÌNH":
                    case "SCREEN":
                        tvSpecDisplay.setText(value);
                        break;
                }
            }
        }
    }

    private void updateFavoriteIcon() {
        if (currentUserId.isEmpty()) {
            fabFavorite.setImageResource(R.drawable.ic_favorite_border);
            return;
        }
        
        repository.isInWishlist(currentUserId, product.getId(), new DataRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isInWishlist) {
                if (isInWishlist) {
                    fabFavorite.setImageResource(R.drawable.ic_favorite_filled);
                } else {
                    fabFavorite.setImageResource(R.drawable.ic_favorite_border);
                }
            }

            @Override
            public void onError(Exception e) {
                fabFavorite.setImageResource(R.drawable.ic_favorite_border);
            }
        });
    }
    
    private void checkStockAvailability() {
        if (product.getStock() <= 0) {
            // Out of stock - show out of stock card, hide buttons
            layoutAvailableButtons.setVisibility(View.GONE);
            cardOutOfStock.setVisibility(View.VISIBLE);
        } else {
            // In stock - show buttons, hide out of stock card
            layoutAvailableButtons.setVisibility(View.VISIBLE);
            cardOutOfStock.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());

        fabFavorite.setOnClickListener(v -> toggleFavorite());
        
        btnWriteReview.setOnClickListener(v -> {
            if (currentUserId.isEmpty()) {
                Toast.makeText(this, "Vui lòng đăng nhập để đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check if user has already reviewed (load reviews and check userId)
            repository.getProductReviews(product.getId(), new DataRepository.DataCallback<java.util.List<Review>>() {
                @Override
                public void onSuccess(java.util.List<Review> reviews) {
                    boolean hasReviewed = false;
                    for (Review r : reviews) {
                        if (r.getUserId().equals(currentUserId)) {
                            hasReviewed = true;
                            break;
                        }
                    }
                    
                    if (hasReviewed) {
                        Toast.makeText(ProductDetailActivity.this, "Bạn đã đánh giá sản phẩm này rồi", Toast.LENGTH_SHORT).show();
                    } else {
                        showWriteReviewDialog();
                    }
                }

                @Override
                public void onError(Exception e) {
                    // On error, allow review
                    showWriteReviewDialog();
                }
            });
        });

        btnAddToCart.setOnClickListener(v -> {
            if (currentUserId.isEmpty()) {
                Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if product already exists in cart
            repository.getCartItemByUserAndProduct(currentUserId, product.getId(), new DataRepository.DataCallback<com.example.otech.model.CartItem>() {
                @Override
                public void onSuccess(com.example.otech.model.CartItem existingCartItem) {
                    if (existingCartItem != null) {
                        // Product already in cart, increase quantity
                        existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                        repository.updateCartItem(existingCartItem, new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ProductDetailActivity.this, "Đã thêm 1 sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Product not in cart, add new item
                        com.example.otech.model.CartItem newCartItem = new com.example.otech.model.CartItem(
                            java.util.UUID.randomUUID().toString(),
                            currentUserId,
                            product,
                            1
                        );
                        
                        repository.addToCart(newCartItem, new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ProductDetailActivity.this, "Đã thêm 1 sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBuyNow.setOnClickListener(v -> {
            if (currentUserId.isEmpty()) {
                Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check if product already exists in cart
            repository.getCartItemByUserAndProduct(currentUserId, product.getId(), new DataRepository.DataCallback<com.example.otech.model.CartItem>() {
                @Override
                public void onSuccess(com.example.otech.model.CartItem existingCartItem) {
                    if (existingCartItem != null) {
                        // Product already in cart, increase quantity
                        existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                        repository.updateCartItem(existingCartItem, new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                // Navigate to cart for checkout
                                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Product not in cart, add new item
                        com.example.otech.model.CartItem newCartItem = new com.example.otech.model.CartItem(
                            java.util.UUID.randomUUID().toString(),
                            currentUserId,
                            product,
                            1
                        );
                        
                        repository.addToCart(newCartItem, new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                // Navigate to cart for checkout
                                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnToggleDescription.setOnClickListener(v -> {
            toggleDescription();
        });

        btnCompare.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CompareActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
    }

    private void toggleDescription() {
        if (isDescriptionExpanded) {
            // Collapse description
            tvDescription.setMaxLines(4);
            btnToggleDescription.setText("Xem thêm");
            isDescriptionExpanded = false;
        } else {
            // Expand description
            tvDescription.setMaxLines(Integer.MAX_VALUE);
            btnToggleDescription.setText("Xem bớt");
            isDescriptionExpanded = true;
        }
    }

    private void toggleFavorite() {
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
                            Toast.makeText(ProductDetailActivity.this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                            updateFavoriteIcon();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    repository.addToWishlist(currentUserId, product.getId(), new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ProductDetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                            updateFavoriteIcon();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupRelatedProducts() {
        repository.getAllProducts(new DataRepository.DataCallback<java.util.List<Product>>() {
            @Override
            public void onSuccess(java.util.List<Product> products) {
                // Filter related products by category, exclude current product
                ArrayList<Product> relatedProducts = new ArrayList<>();
                for (Product p : products) {
                    if (!p.getId().equals(product.getId()) && 
                        p.getCategory().equals(product.getCategory())) {
                        relatedProducts.add(p);
                        if (relatedProducts.size() >= 10) break;
                    }
                }
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvRelatedProducts.setLayoutManager(layoutManager);
        
        RelatedProductAdapter adapter = new RelatedProductAdapter(relatedProducts, product -> {
            // Reload current activity with new product
            Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
            finish();
        });
        
        rvRelatedProducts.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                // Hide related products section on error
                rvRelatedProducts.setVisibility(View.GONE);
            }
        });
    }
    
    private void showWriteReviewDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_write_review, null);
        
        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        TextInputEditText edtComment = dialogView.findViewById(R.id.edt_comment);
        
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btn_submit).setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = edtComment.getText().toString().trim();
            
            if (rating == 0) {
                Toast.makeText(this, "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (comment.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
                return;
            }
            
            repository.getUserById(currentUserId, new DataRepository.DataCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    String userName = user != null ? user.getFullName() : "Người dùng";
                    
                    // Create and insert review
                    Review review = new Review(
                        java.util.UUID.randomUUID().toString(),
                        product.getId(),
                        currentUserId,
                        userName,
                        rating,
                        comment
                    );
                    
                    repository.insertReview(review, new DataRepository.VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ProductDetailActivity.this, "Đã gữi đánh giá thành công", Toast.LENGTH_SHORT).show();
                            
                            // Reload product from database to get updated rating
                            repository.getProductById(product.getId(), new DataRepository.DataCallback<Product>() {
                                @Override
                                public void onSuccess(Product updatedProduct) {
                                    if (updatedProduct != null) {
                                        product = updatedProduct;
                                        
                                        // Update UI with new rating
                                        tvRating.setText(String.format("%.1f", product.getRating()));
                                        ProductDetailActivity.this.ratingBar.setRating(product.getRating());
                                        
                                        // Reload reviews section
                                        loadReviews();
                                    }
                                }
                                
                                @Override
                                public void onError(Exception e) {
                                    // Still reload reviews even if product reload fails
                                    loadReviews();
                                }
                            });
                            
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ProductDetailActivity.this, "Có lỗi khi gửi đánh giá", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            });
        });
        
        dialog.show();
    }
    
    private void trackProductView() {
        if (currentUserId == null || currentUserId.isEmpty()) {
            return; // User not logged in
        }
        
        repository.trackProductView(currentUserId, product.getId(), product.getCategory(), new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                // View tracked successfully
            }
            
            @Override
            public void onError(Exception e) {
                // Silent fail - tracking is not critical
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop auto-scroll to prevent memory leaks
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }
    
}
