package com.foodease.dto;

public class UpdateCartItemRequest {
    private int quantity;

    public UpdateCartItemRequest() {
    }

    public UpdateCartItemRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
