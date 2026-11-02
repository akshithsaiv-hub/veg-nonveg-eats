package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AddCartItemRequest {

    @NotBlank(message = "menu_item_id is required")
    @JsonProperty("menu_item_id")
    private String menuItemId;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity = 1;

    public AddCartItemRequest() {
    }

    public AddCartItemRequest(String menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
