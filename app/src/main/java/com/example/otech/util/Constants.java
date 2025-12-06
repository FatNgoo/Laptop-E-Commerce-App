package com.example.otech.util;

public class Constants {
    // SharedPreferences Keys
    public static final String PREFS_NAME = "OTechPrefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_USER_ROLE = "user_role";
    
    // Intent Extra Keys
    public static final String EXTRA_PRODUCT = "product";
    public static final String EXTRA_ORDER = "order";
    public static final String EXTRA_ORDER_ID = "order_id";
    public static final String EXTRA_USER = "user";
    public static final String EXTRA_CART_ITEM = "cart_item";
    public static final String EXTRA_SELECTED_ITEMS = "selected_items";
    public static final String EXTRA_CHECKOUT_STEP = "checkout_step";
    
    // Order Status
    public static final String ORDER_STATUS_PENDING = "pending";
    public static final String ORDER_STATUS_PROCESSING = "processing";
    public static final String ORDER_STATUS_SHIPPING = "shipping";
    public static final String ORDER_STATUS_COMPLETED = "completed";
    public static final String ORDER_STATUS_CANCELLED = "cancelled";
    public static final String ORDER_STATUS_OUT_OF_STOCK = "out_of_stock";
    
    // User Roles
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    
    // Categories
    public static final String CATEGORY_GAMING = "Gaming";
    public static final String CATEGORY_WORKSTATION = "Workstation";
    public static final String CATEGORY_ULTRABOOK = "Ultrabook";
    public static final String CATEGORY_OFFICE = "Văn phòng";
    
    // Brands
    public static final String[] BRANDS = {"ASUS", "MSI", "Acer", "Dell", "HP", "Lenovo", "Apple"};
}
