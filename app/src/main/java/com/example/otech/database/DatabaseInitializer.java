package com.example.otech.database;

import android.content.Context;
import android.util.Log;

import com.example.otech.model.Product;
import com.example.otech.model.Review;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;

import java.util.ArrayList;
import java.util.Date;

/**
 * DatabaseInitializer - Seed initial mock data vào Room Database
 * Chạy lần đầu tiên khi database trống
 */
public class DatabaseInitializer {
    private static final String TAG = "DatabaseInitializer";
    
    public interface InitCallback {
        void onComplete();
        void onError(Exception e);
    }
    
    public static void initializeDatabase(Context context, InitCallback callback) {
        DataRepository repository = DataRepository.getInstance(context);
        
        // Check xem database đã có data chưa
        repository.isDatabaseEmpty(new DataRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isEmpty) {
                if (isEmpty) {
                    Log.d(TAG, "Database is empty, initializing with mock data...");
                    seedInitialData(repository, callback);
                } else {
                    Log.d(TAG, "Database already has data, skipping initialization");
                    callback.onComplete();
                }
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error checking database: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static void seedInitialData(DataRepository repository, InitCallback callback) {
        // Seed users trước
        seedUsers(repository, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                // Sau đó seed products
                seedProducts(repository, new DataRepository.VoidCallback() {
                    @Override
                    public void onSuccess() {
                        // Cuối cùng seed reviews
                        seedReviews(repository, callback);
                    }
                    
                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Error seeding products: " + e.getMessage());
                        callback.onError(e);
                    }
                });
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding users: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static void seedUsers(DataRepository repository, DataRepository.VoidCallback callback) {
        ArrayList<User> users = new ArrayList<>();
        
        users.add(new User("1", "admin", "admin", "Admin User",
                "0123456789", "123 Admin St", "admin@otech.com", "admin"));
        users.add(new User("2", "user", "user", "Test User",
                "0987654321", "456 User Ave", "user@otech.com", "user"));
        
        // Use repository method to insert
        repository.insertAllUsers(users, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Seeded " + users.size() + " users");
                callback.onSuccess();
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding users: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static void seedProducts(DataRepository repository, DataRepository.VoidCallback callback) {
        ArrayList<Product> products = new ArrayList<>();
        
        // Văn phòng - 7 products
        products.add(createProduct("1", "Dell Inspiron 15 3530", 16990000, 20000000, "Dell", "Văn phòng",
                "Dell Inspiron 15 3530 là lựa chọn hoàn hảo cho môi trường văn phòng hiện đại...",
                "CPU: Intel Core i5-1335U, RAM: 16GB DDR4 3200MHz, Storage: 512GB SSD NVMe, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD 120Hz", 4.2f, 20));
        
        products.add(createProduct("2", "HP Pavilion 15-eg3000", 17500000, 21000000, "HP", "Văn phòng",
                "HP Pavilion 15-eg3000 mang đến sự cân bằng hoàn hảo...",
                "CPU: Intel Core i5-13500H, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD IPS", 4.3f, 20));
        
        products.add(createProduct("3", "Asus Vivobook Go 15", 12990000, 14500000, "Asus", "Văn phòng",
                "Asus Vivobook Go 15 là người bạn đồng hành lý tưởng...",
                "CPU: AMD Ryzen 5 7520U, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD OLED", 4.1f, 20));
        
        products.add(createProduct("4", "Acer Aspire 5 Slim", 13990000, 17000000, "Acer", "Văn phòng",
                "Acer Aspire 5 Slim kết hợp hoàn hảo...",
                "CPU: Intel Core i5-13420H, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS", 4.2f, 20));
        
        products.add(createProduct("5", "Lenovo IdeaPad Slim 5", 18990000, 20500000, "Lenovo", "Văn phòng",
                "Lenovo IdeaPad Slim 5 là biểu tượng của sự tinh tế...",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 16 inch WUXGA IPS", 4.4f, 20));
        
        products.add(createProduct("6", "MSI Modern 14 C13M", 11990000, 15000000, "MSI", "Văn phòng",
                "MSI Modern 14 C13M là lựa chọn hoàn hảo...",
                "CPU: Intel Core i3-1315U, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS", 4.0f, 20));
        
        products.add(createProduct("7", "LG Gram 14 2023", 24990000, 28900000, "LG", "Văn phòng",
                "LG Gram 14 2023 là chiếc laptop siêu nhẹ nhất thế giới...",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WUXGA IPS", 4.5f, 20));
        
        // Gaming - 8 products
        products.add(createProduct("8", "Asus ROG Strix G16", 39990000, 45000000, "Asus", "Gaming",
                "Asus ROG Strix G16 là chiến binh gaming đỉnh cao...",
                "CPU: Intel Core i9-13980HX, RAM: 16GB DDR5, Storage: 1TB SSD NVMe, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch QHD+ 240Hz", 4.6f, 20));
        
        products.add(createProduct("9", "MSI Katana 15", 28500000, 38000000, "MSI", "Gaming",
                "MSI Katana 15 là laptop gaming giá rẻ...",
                "CPU: Intel Core i7-13620H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 15.6 inch FHD 144Hz", 4.5f, 20));
        
        products.add(createProduct("10", "Acer Predator Helios Neo 16", 35990000, 38900000, "Acer", "Gaming",
                "Acer Predator Helios Neo 16 sở hữu công nghệ tản nhiệt...",
                "CPU: Intel Core i7-13700HX, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch WQXGA 165Hz", 4.6f, 20));
        
        // Use repository method to insert products
        repository.insertAllProducts(products, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Seeded " + products.size() + " products");
                callback.onSuccess();
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding products: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static Product createProduct(String id, String name, double price, double oldPrice,
                                        String brand, String category, String description,
                                        String specs, float rating, int stock) {
        Product product = new Product(id, name, price, oldPrice, description, 
                "placeholder_" + id, brand, category, specs, rating, stock);
        product.setImageUrls(new ArrayList<>());
        return product;
    }
    
    private static void seedReviews(DataRepository repository, InitCallback callback) {
        ArrayList<Review> reviews = new ArrayList<>();
        
        // Sample reviews cho products
        reviews.add(new Review("r1", "1", "2", "Test User", 4.5f, 
                "Laptop rất tốt cho văn phòng, pin trâu!"));
        reviews.add(new Review("r2", "8", "2", "Test User", 5.0f, 
                "Chơi game siêu mượt, RTX 4060 quá đỉnh!"));
        
        // Use repository method to insert reviews
        repository.insertAllReviews(reviews, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Seeded " + reviews.size() + " reviews");
                Log.d(TAG, "Database initialization complete!");
                callback.onComplete();
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding reviews: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
}
