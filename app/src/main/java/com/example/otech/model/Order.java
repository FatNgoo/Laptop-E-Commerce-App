package com.example.otech.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    private String id;
    private String userId;
    private ArrayList<CartItem> items;
    private double totalAmount;
    private String deliveryAddress;
    private String phone;
    private String status; // "pending", "processing", "shipping", "completed", "cancelled"
    private Date orderDate;
    private String cancelReason;

    public Order() {
        this.orderDate = new Date();
        this.items = new ArrayList<>();
    }

    public Order(String id, String userId, ArrayList<CartItem> items, 
                 double totalAmount, String deliveryAddress, String phone) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;
        this.status = "pending";
        this.orderDate = new Date();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public ArrayList<CartItem> getItems() { return items; }
    public void setItems(ArrayList<CartItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public boolean canBeCancelled() {
        return "pending".equals(status) || "processing".equals(status);
    }
}
