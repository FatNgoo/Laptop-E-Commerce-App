package com.example.otech.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.example.otech.database.Converters;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "user_preferences")
@TypeConverters(Converters.class)
public class UserPreference {
    @PrimaryKey
    @NonNull
    private String userId;
    
    // Category view counts: {"gaming": 15, "office": 5, "student": 3}
    private Map<String, Integer> categoryViewCounts;
    
    // Last viewed product ID
    private String lastViewedProductId;
    
    // Last viewed timestamp
    private long lastViewedTimestamp;

    public UserPreference() {
        this.categoryViewCounts = new HashMap<>();
    }

    public UserPreference(@NonNull String userId) {
        this.userId = userId;
        this.categoryViewCounts = new HashMap<>();
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public Map<String, Integer> getCategoryViewCounts() {
        return categoryViewCounts;
    }

    public void setCategoryViewCounts(Map<String, Integer> categoryViewCounts) {
        this.categoryViewCounts = categoryViewCounts;
    }

    public String getLastViewedProductId() {
        return lastViewedProductId;
    }

    public void setLastViewedProductId(String lastViewedProductId) {
        this.lastViewedProductId = lastViewedProductId;
    }

    public long getLastViewedTimestamp() {
        return lastViewedTimestamp;
    }

    public void setLastViewedTimestamp(long lastViewedTimestamp) {
        this.lastViewedTimestamp = lastViewedTimestamp;
    }

    // Helper method to increment category view count
    public void incrementCategoryView(String category) {
        if (categoryViewCounts == null) {
            categoryViewCounts = new HashMap<>();
        }
        int count = categoryViewCounts.getOrDefault(category, 0);
        categoryViewCounts.put(category, count + 1);
    }

    // Get most viewed category
    public String getMostViewedCategory() {
        if (categoryViewCounts == null || categoryViewCounts.isEmpty()) {
            return null;
        }
        
        String mostViewed = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : categoryViewCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostViewed = entry.getKey();
            }
        }
        return mostViewed;
    }
}
