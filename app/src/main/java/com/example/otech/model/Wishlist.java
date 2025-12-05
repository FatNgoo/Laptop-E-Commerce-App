package com.example.otech.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.example.otech.database.Converters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "wishlist", primaryKeys = {"userId", "productId"})
@TypeConverters(Converters.class)
public class Wishlist implements Serializable {
    @NonNull
    private String userId;
    
    @NonNull
    private String productId;
    
    private Date addedDate;

    public Wishlist() {
    }

    public Wishlist(@NonNull String userId, @NonNull String productId) {
        this.userId = userId;
        this.productId = productId;
        this.addedDate = new Date();
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getProductId() {
        return productId;
    }

    public void setProductId(@NonNull String productId) {
        this.productId = productId;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
}
