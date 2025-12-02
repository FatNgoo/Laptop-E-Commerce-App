package com.example.otech.model;

import java.io.Serializable;

public class Address implements Serializable {
    private String id;
    private String userId;
    private String recipientName;
    private String phone;
    private String addressDetail;
    private boolean isDefault;

    public Address(String id, String userId, String recipientName, String phone, String addressDetail, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.recipientName = recipientName;
        this.phone = phone;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getRecipientName() { return recipientName; }
    public String getPhone() { return phone; }
    public String getAddressDetail() { return addressDetail; }
    public boolean isDefault() { return isDefault; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddressDetail(String addressDetail) { this.addressDetail = addressDetail; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    @Override
    public String toString() {
        return recipientName + " - " + phone + "\n" + addressDetail;
    }
}
