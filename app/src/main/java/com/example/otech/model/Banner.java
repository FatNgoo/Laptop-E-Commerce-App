package com.example.otech.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "banners")
public class Banner implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String imageUrl;
    private String title;
    private String link; // Link to product/category when clicked
    private boolean isActive;
    private int order; // Display order (lower number = higher priority)

    public Banner() {
    }

    @Ignore
    public Banner(String id, String imageUrl, String title, String link, boolean isActive, int order) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.link = link;
        this.isActive = isActive;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
