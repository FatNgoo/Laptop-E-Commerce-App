package com.example.otech.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.otech.database.AppDatabase;
import com.example.otech.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * DataRepository - Repository pattern wrap Room Database operations
 * Sử dụng ExecutorService để xử lý database operations trên background thread
 * Callback về main thread để update UI
 */
public class DataRepository {
    private static DataRepository instance;
    
    private AppDatabase database;
    private ExecutorService executor;
    private Handler mainHandler;
    
    private DataRepository(Context context) {
        database = AppDatabase.getInstance(context);
        executor = AppDatabase.databaseWriteExecutor;
        mainHandler = new Handler(Looper.getMainLooper());
    }
    
    public static synchronized DataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DataRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    // ==================== CALLBACK INTERFACES ====================
    
    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(Exception e);
    }
    
    public interface VoidCallback {
        void onSuccess();
        void onError(Exception e);
    }
    
    // ==================== USER OPERATIONS ====================
    
    public void login(String username, String password, DataCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = database.userDao().login(username, password);
                mainHandler.post(() -> callback.onSuccess(user));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getAllUsers(DataCallback<List<User>> callback) {
        executor.execute(() -> {
            try {
                List<User> users = database.userDao().getAll();
                mainHandler.post(() -> callback.onSuccess(users));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getUserByUsername(String username, DataCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = database.userDao().getByUsername(username);
                mainHandler.post(() -> callback.onSuccess(user));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getUserById(String userId, DataCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = database.userDao().getById(userId);
                mainHandler.post(() -> callback.onSuccess(user));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void register(User user, DataCallback<User> callback) {
        executor.execute(() -> {
            try {
                // Check if username already exists
                User existingUser = database.userDao().getByUsername(user.getUsername());
                if (existingUser != null) {
                    mainHandler.post(() -> callback.onError(new Exception("Username already exists")));
                    return;
                }
                
                // Generate user ID if not set
                if (user.getId() == null || user.getId().isEmpty()) {
                    user.setId(java.util.UUID.randomUUID().toString());
                }
                
                database.userDao().insert(user);
                mainHandler.post(() -> callback.onSuccess(user));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertUser(User user, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.userDao().insert(user);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void updateUser(User user, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.userDao().update(user);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== PRODUCT OPERATIONS ====================
    
    public void getAllProducts(DataCallback<List<Product>> callback) {
        executor.execute(() -> {
            try {
                List<Product> products = database.productDao().getAll();
                mainHandler.post(() -> callback.onSuccess(products));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getProductById(String productId, DataCallback<Product> callback) {
        executor.execute(() -> {
            try {
                Product product = database.productDao().getById(productId);
                mainHandler.post(() -> callback.onSuccess(product));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void searchProducts(String query, DataCallback<List<Product>> callback) {
        executor.execute(() -> {
            try {
                List<Product> products = database.productDao().search(query);
                mainHandler.post(() -> callback.onSuccess(products));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getProductsByCategory(String category, DataCallback<List<Product>> callback) {
        executor.execute(() -> {
            try {
                List<Product> products = database.productDao().getByCategory(category);
                mainHandler.post(() -> callback.onSuccess(products));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getBestSellers(int limit, DataCallback<List<Product>> callback) {
        executor.execute(() -> {
            try {
                List<Product> products = database.productDao().getBestSellers(limit);
                mainHandler.post(() -> callback.onSuccess(products));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getPromotionProducts(int limit, DataCallback<List<Product>> callback) {
        executor.execute(() -> {
            try {
                List<Product> products = database.productDao().getPromotionProducts(limit);
                mainHandler.post(() -> callback.onSuccess(products));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertProduct(Product product, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.productDao().insert(product);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void updateProduct(Product product, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.productDao().update(product);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void deleteProduct(Product product, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.productDao().delete(product);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== CART OPERATIONS ====================
    
    public void getCart(String userId, DataCallback<List<CartItem>> callback) {
        executor.execute(() -> {
            try {
                List<CartItem> cartItems = database.cartItemDao().getCartByUserId(userId);
                mainHandler.post(() -> callback.onSuccess(cartItems));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getCartItemByUserAndProduct(String userId, String productId, DataCallback<CartItem> callback) {
        executor.execute(() -> {
            try {
                CartItem cartItem = database.cartItemDao().getCartItemByUserAndProduct(userId, productId);
                mainHandler.post(() -> callback.onSuccess(cartItem));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void addToCart(CartItem cartItem, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.cartItemDao().insert(cartItem);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void updateCartItem(CartItem cartItem, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.cartItemDao().update(cartItem);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void removeFromCart(CartItem cartItem, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.cartItemDao().delete(cartItem);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void clearCart(String userId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.cartItemDao().clearCart(userId);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== ORDER OPERATIONS ====================
    
    public void getAllOrders(DataCallback<List<Order>> callback) {
        executor.execute(() -> {
            try {
                List<Order> orders = database.orderDao().getAll();
                mainHandler.post(() -> callback.onSuccess(orders));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getUserOrders(String userId, DataCallback<List<Order>> callback) {
        executor.execute(() -> {
            try {
                List<Order> orders = database.orderDao().getByUserId(userId);
                mainHandler.post(() -> callback.onSuccess(orders));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getOrderById(String orderId, DataCallback<Order> callback) {
        executor.execute(() -> {
            try {
                Order order = database.orderDao().getById(orderId);
                mainHandler.post(() -> callback.onSuccess(order));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void updateOrder(Order order, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.orderDao().update(order);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void cancelOrder(String orderId, String reason, VoidCallback callback) {
        executor.execute(() -> {
            try {
                Order order = database.orderDao().getById(orderId);
                if (order != null && order.canBeCancelled()) {
                    order.setStatus("cancelled");
                    order.setCancelReason(reason);
                    database.orderDao().update(order);
                    
                    // Return stock to products
                    for (CartItem item : order.getItems()) {
                        Product product = database.productDao().getById(item.getProduct().getId());
                        if (product != null) {
                            product.setStock(product.getStock() + item.getQuantity());
                            product.setSoldCount(Math.max(0, product.getSoldCount() - item.getQuantity()));
                            database.productDao().update(product);
                        }
                    }
                    
                    mainHandler.post(callback::onSuccess);
                } else {
                    mainHandler.post(() -> callback.onError(new Exception("Cannot cancel this order")));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void deleteOrder(Order order, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.orderDao().update(order);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== ADDRESS OPERATIONS ====================
    
    public void getUserAddresses(String userId, DataCallback<List<Address>> callback) {
        executor.execute(() -> {
            try {
                List<Address> addresses = database.addressDao().getByUserId(userId);
                mainHandler.post(() -> callback.onSuccess(addresses));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getDefaultAddress(String userId, DataCallback<Address> callback) {
        executor.execute(() -> {
            try {
                Address address = database.addressDao().getDefaultAddress(userId);
                mainHandler.post(() -> callback.onSuccess(address));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertAddress(Address address, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.addressDao().insert(address);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void updateAddress(Address address, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.addressDao().update(address);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void deleteAddress(Address address, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.addressDao().delete(address);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void setDefaultAddress(String userId, String addressId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                // First, unset all default addresses for this user
                List<Address> userAddresses = database.addressDao().getByUserId(userId);
                for (Address addr : userAddresses) {
                    if (addr.isDefault()) {
                        addr.setDefault(false);
                        database.addressDao().update(addr);
                    }
                }
                
                // Then set the selected address as default
                for (Address addr : userAddresses) {
                    if (addr.getId().equals(addressId)) {
                        addr.setDefault(true);
                        database.addressDao().update(addr);
                        break;
                    }
                }
                
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== REVIEW OPERATIONS ====================
    
    public void getAllReviews(DataCallback<List<Review>> callback) {
        executor.execute(() -> {
            try {
                List<Review> reviews = database.reviewDao().getAll();
                mainHandler.post(() -> callback.onSuccess(reviews));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getProductReviews(String productId, DataCallback<List<Review>> callback) {
        executor.execute(() -> {
            try {
                List<Review> reviews = database.reviewDao().getByProductId(productId);
                mainHandler.post(() -> callback.onSuccess(reviews));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertReview(Review review, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.reviewDao().insert(review);
                
                // Update product's average rating
                float avgRating = database.reviewDao().getAverageRating(review.getProductId());
                Product product = database.productDao().getById(review.getProductId());
                if (product != null) {
                    product.setRating(avgRating);
                    database.productDao().update(product);
                }
                
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void deleteReview(Review review, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.reviewDao().delete(review);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== CHECKOUT OPERATION ====================
    
    public void checkoutSelectedItems(String userId, ArrayList<CartItem> selectedItems, 
                                      String deliveryAddress, String phone, 
                                      DataCallback<Order> callback) {
        executor.execute(() -> {
            try {
                if (selectedItems == null || selectedItems.isEmpty()) {
                    mainHandler.post(() -> callback.onError(new Exception("No items selected")));
                    return;
                }
                
                // Check stock availability
                for (CartItem item : selectedItems) {
                    Product product = database.productDao().getById(item.getProduct().getId());
                    if (product == null || product.getStock() < item.getQuantity()) {
                        mainHandler.post(() -> callback.onError(new Exception("Insufficient stock")));
                        return;
                    }
                }
                
                // Calculate total
                double total = 0;
                for (CartItem item : selectedItems) {
                    total += item.getProduct().getPrice() * item.getQuantity();
                }
                
                // Create order
                String orderId = java.util.UUID.randomUUID().toString();
                Order order = new Order(orderId, userId, new ArrayList<>(selectedItems), 
                                      total, deliveryAddress, phone);
                database.orderDao().insert(order);
                
                // Update product stock and soldCount
                for (CartItem item : selectedItems) {
                    Product product = database.productDao().getById(item.getProduct().getId());
                    if (product != null) {
                        product.setStock(product.getStock() - item.getQuantity());
                        product.setSoldCount(product.getSoldCount() + item.getQuantity());
                        database.productDao().update(product);
                    }
                }
                
                // Remove selected items from cart
                for (CartItem item : selectedItems) {
                    database.cartItemDao().delete(item);
                }
                
                mainHandler.post(() -> callback.onSuccess(order));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void createNotification(String userId, String title, String message, 
                                   String type, String relatedId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                String notificationId = java.util.UUID.randomUUID().toString();
                long timestamp = System.currentTimeMillis();
                Notification notification = new Notification(notificationId, userId, 
                                                            title, message, type, relatedId, 
                                                            timestamp, false);
                database.notificationDao().insert(notification);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== NOTIFICATION OPERATIONS ====================
    
    public void getUserNotifications(String userId, DataCallback<List<Notification>> callback) {
        executor.execute(() -> {
            try {
                List<Notification> notifications = database.notificationDao().getByUserId(userId);
                mainHandler.post(() -> callback.onSuccess(notifications));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertNotification(Notification notification, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.notificationDao().insert(notification);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void updateNotification(Notification notification, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.notificationDao().update(notification);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void markNotificationAsRead(String notificationId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                Notification notification = database.notificationDao().getById(notificationId);
                if (notification != null) {
                    notification.setRead(true);
                    database.notificationDao().update(notification);
                }
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void markAllNotificationsAsRead(String userId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.notificationDao().markAllAsRead(userId);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== WISHLIST OPERATIONS ====================
    
    public void getWishlist(String userId, DataCallback<ArrayList<Product>> callback) {
        executor.execute(() -> {
            try {
                List<String> productIds = database.wishlistDao().getProductIdsByUserId(userId);
                ArrayList<Product> wishlist = new ArrayList<>();
                for (String productId : productIds) {
                    Product product = database.productDao().getById(productId);
                    if (product != null) {
                        wishlist.add(product);
                    }
                }
                mainHandler.post(() -> callback.onSuccess(wishlist));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void addToWishlist(String userId, String productId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                com.example.otech.model.Wishlist wishlist = new com.example.otech.model.Wishlist(userId, productId);
                database.wishlistDao().insert(wishlist);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void removeFromWishlist(String userId, String productId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.wishlistDao().deleteByUserAndProduct(userId, productId);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void isInWishlist(String userId, String productId, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                int count = database.wishlistDao().isInWishlist(userId, productId);
                boolean inWishlist = count > 0;
                mainHandler.post(() -> callback.onSuccess(inWishlist));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== DATABASE CHECK ====================
    
    public void isDatabaseEmpty(DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                int productCount = database.productDao().getCount();
                int userCount = database.userDao().getCount();
                boolean isEmpty = (productCount == 0 || userCount == 0);
                mainHandler.post(() -> callback.onSuccess(isEmpty));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== BULK INSERT FOR INITIALIZATION ====================
    
    public void insertAllUsers(List<User> users, VoidCallback callback) {
        executor.execute(() -> {
            try {
                for (User user : users) {
                    database.userDao().insert(user);
                }
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertAllProducts(List<Product> products, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.productDao().insertAll(products);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertAllReviews(List<Review> reviews, VoidCallback callback) {
        executor.execute(() -> {
            try {
                for (Review review : reviews) {
                    database.reviewDao().insert(review);
                }
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== BANNER OPERATIONS ====================
    
    public void getAllBanners(DataCallback<List<Banner>> callback) {
        executor.execute(() -> {
            try {
                List<Banner> banners = database.bannerDao().getAllBanners();
                mainHandler.post(() -> callback.onSuccess(banners));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void getActiveBanners(DataCallback<List<Banner>> callback) {
        executor.execute(() -> {
            try {
                List<Banner> banners = database.bannerDao().getAllActiveBanners();
                mainHandler.post(() -> callback.onSuccess(banners));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertBanner(Banner banner, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.bannerDao().insert(banner);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void insertAllBanners(java.util.ArrayList<Banner> banners, VoidCallback callback) {
        executor.execute(() -> {
            try {
                for (Banner banner : banners) {
                    database.bannerDao().insert(banner);
                }
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void updateBanner(Banner banner, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.bannerDao().update(banner);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    public void deleteBanner(String bannerId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.bannerDao().deleteById(bannerId);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    // ==================== VIEW HISTORY & PERSONALIZATION ====================
    
    /**
     * Track product view - Increment view count and update timestamp
     */
    public void trackProductView(String userId, String productId, String category, VoidCallback callback) {
        executor.execute(() -> {
            try {
                // Update view history
                ViewHistory existing = database.viewHistoryDao().getViewHistory(userId, productId);
                if (existing != null) {
                    existing.setViewCount(existing.getViewCount() + 1);
                    existing.setTimestamp(System.currentTimeMillis());
                    database.viewHistoryDao().update(existing);
                } else {
                    String id = java.util.UUID.randomUUID().toString();
                    ViewHistory newHistory = new ViewHistory(id, userId, productId, System.currentTimeMillis(), 1);
                    database.viewHistoryDao().insert(newHistory);
                }
                
                // Update user preferences
                UserPreference preference = database.userPreferenceDao().getUserPreference(userId);
                if (preference == null) {
                    preference = new UserPreference(userId);
                }
                preference.incrementCategoryView(category);
                preference.setLastViewedProductId(productId);
                preference.setLastViewedTimestamp(System.currentTimeMillis());
                
                if (database.userPreferenceDao().getUserPreference(userId) == null) {
                    database.userPreferenceDao().insert(preference);
                } else {
                    database.userPreferenceDao().update(preference);
                }
                
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    /**
     * Get recently viewed products
     */
    public void getRecentlyViewed(String userId, int limit, DataCallback<List<Product>> callback) {
        executor.execute(() -> {
            try {
                List<ViewHistory> histories = database.viewHistoryDao().getRecentlyViewed(userId, limit);
                List<Product> products = new ArrayList<>();
                
                for (ViewHistory history : histories) {
                    Product product = database.productDao().getById(history.getProductId());
                    if (product != null) {
                        products.add(product);
                    }
                }
                
                mainHandler.post(() -> callback.onSuccess(products));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    /**
     * Get recommended products based on user preferences
     */
    public void getRecommendedProducts(String userId, int limit, DataCallback<List<Product>> callback) {
        executor.execute(() -> {
            try {
                UserPreference preference = database.userPreferenceDao().getUserPreference(userId);
                List<Product> recommended = new ArrayList<>();
                
                if (preference != null) {
                    String mostViewedCategory = preference.getMostViewedCategory();
                    
                    if (mostViewedCategory != null) {
                        // Get products from most viewed category
                        List<Product> categoryProducts = database.productDao().getByCategory(mostViewedCategory);
                        
                        // Sort by rating and sold count
                        categoryProducts.sort((p1, p2) -> {
                            int ratingCompare = Float.compare(p2.getRating(), p1.getRating());
                            if (ratingCompare != 0) return ratingCompare;
                            return Integer.compare(p2.getSoldCount(), p1.getSoldCount());
                        });
                        
                        // Add top products
                        for (int i = 0; i < Math.min(limit, categoryProducts.size()); i++) {
                            recommended.add(categoryProducts.get(i));
                        }
                    }
                }
                
                // If not enough recommendations, fill with best sellers
                if (recommended.size() < limit) {
                    int remaining = limit - recommended.size();
                    List<Product> bestSellers = database.productDao().getBestSellers(remaining);
                    for (Product product : bestSellers) {
                        if (!recommended.contains(product) && recommended.size() < limit) {
                            recommended.add(product);
                        }
                    }
                }
                
                mainHandler.post(() -> callback.onSuccess(recommended));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    /**
     * Get user preference (for resume browsing)
     */
    public void getUserPreference(String userId, DataCallback<UserPreference> callback) {
        executor.execute(() -> {
            try {
                UserPreference preference = database.userPreferenceDao().getUserPreference(userId);
                mainHandler.post(() -> callback.onSuccess(preference));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
    
    /**
     * Clear user view history
     */
    public void clearViewHistory(String userId, VoidCallback callback) {
        executor.execute(() -> {
            try {
                database.viewHistoryDao().deleteAllForUser(userId);
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
}
