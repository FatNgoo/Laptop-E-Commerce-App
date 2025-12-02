package com.example.otech.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private String id;
    private String name;
    private double price;
    private double oldPrice;
    private String description;
    private String imageUrl;
    private ArrayList<String> imageUrls; // Multiple images
    private String brand;
    private String category;
    private String specs;
    private float rating;
    private boolean isFavorite;
    private int stock;
    private int soldCount;

    public Product() {
    }

    public Product(String id, String name, double price, double oldPrice, 
                   String description, String imageUrl, String brand, 
                   String category, String specs, float rating, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.description = description;
        this.imageUrl = imageUrl;
        this.brand = brand;
        this.category = category;
        this.specs = specs;
        this.rating = rating;
        this.isFavorite = false;
        this.stock = stock;
        this.soldCount = 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getOldPrice() { return oldPrice; }
    public void setOldPrice(double oldPrice) { this.oldPrice = oldPrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public ArrayList<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(ArrayList<String> imageUrls) { this.imageUrls = imageUrls; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSpecs() { return specs; }
    public void setSpecs(String specs) { this.specs = specs; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getSoldCount() { return soldCount; }
    public void setSoldCount(int soldCount) { this.soldCount = soldCount; }

    public double getDiscountPercent() {
        if (oldPrice > 0) {
            return ((oldPrice - price) / oldPrice) * 100;
        }
        return 0;
    }
}
