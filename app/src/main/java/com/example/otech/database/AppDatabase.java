package com.example.otech.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.otech.database.dao.AddressDao;
import com.example.otech.database.dao.BannerDao;
import com.example.otech.database.dao.CartItemDao;
import com.example.otech.database.dao.NotificationDao;
import com.example.otech.database.dao.OrderDao;
import com.example.otech.database.dao.ProductDao;
import com.example.otech.database.dao.ReviewDao;
import com.example.otech.database.dao.UserDao;
import com.example.otech.database.dao.UserPreferenceDao;
import com.example.otech.database.dao.ViewHistoryDao;
import com.example.otech.database.dao.WishlistDao;
import com.example.otech.model.Address;
import com.example.otech.model.Banner;
import com.example.otech.model.CartItem;
import com.example.otech.model.Notification;
import com.example.otech.model.Order;
import com.example.otech.model.Product;
import com.example.otech.model.Review;
import com.example.otech.model.User;
import com.example.otech.model.UserPreference;
import com.example.otech.model.ViewHistory;
import com.example.otech.model.Wishlist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
    Product.class,
    User.class,
    CartItem.class,
    Order.class,
    Address.class,
    Review.class,
    Notification.class,
    Wishlist.class,
    Banner.class,
    ViewHistory.class,
    UserPreference.class
}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    // DAOs
    public abstract ProductDao productDao();
    public abstract UserDao userDao();
    public abstract CartItemDao cartItemDao();
    public abstract OrderDao orderDao();
    public abstract AddressDao addressDao();
    public abstract ReviewDao reviewDao();
    public abstract NotificationDao notificationDao();
    public abstract WishlistDao wishlistDao();
    public abstract BannerDao bannerDao();
    public abstract ViewHistoryDao viewHistoryDao();
    public abstract UserPreferenceDao userPreferenceDao();
    
    // Singleton instance
    private static volatile AppDatabase INSTANCE;
    
    // ExecutorService for background operations (4 threads)
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    /**
     * Get singleton instance of AppDatabase
     * Thread-safe implementation với double-checked locking
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "otech_database"
                    )
                    .fallbackToDestructiveMigration() // Xóa DB cũ khi upgrade version
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * Đóng database connection (dùng khi cần cleanup)
     */
    public static void destroyInstance() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }
}
