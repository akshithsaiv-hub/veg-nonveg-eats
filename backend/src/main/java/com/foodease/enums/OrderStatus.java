package com.foodease.enums;

public enum OrderStatus {
    RECEIVED("Order Received"),
    CONFIRMED("Order Confirmed"),
    PREPARING("Preparing Food"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayStatus;

    OrderStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
