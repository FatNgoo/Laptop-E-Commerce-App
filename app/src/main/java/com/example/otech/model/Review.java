package com.example.otech.model;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private String id;
    private String productId;
    private String userId;
    private String userName;
    private float rating;
    private String comment;
    private Date reviewDate;

    public Review(String id, String productId, String userId, String userName, float rating, String comment) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = new Date();
    }

    // Getters
    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public float getRating() { return rating; }
    public String getComment() { return comment; }
    public Date getReviewDate() { return reviewDate; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setRating(float rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }
}
