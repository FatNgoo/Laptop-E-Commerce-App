package com.example.otech.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "cart_items")
public class CartItem implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String userId;
    
    @Embedded(prefix = "product_")
    private Product product;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String id, String userId, Product product, int quantity) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
