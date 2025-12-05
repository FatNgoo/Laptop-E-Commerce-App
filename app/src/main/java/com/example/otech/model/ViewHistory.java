package com.example.otech.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "view_history")
public class ViewHistory {
    @PrimaryKey
    @NonNull
    private String id;
    private String userId;
    private String productId;
    private long timestamp;
    private int viewCount;

    public ViewHistory() {
    }

    public ViewHistory(@NonNull String id, String userId, String productId, long timestamp, int viewCount) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.timestamp = timestamp;
        this.viewCount = viewCount;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
