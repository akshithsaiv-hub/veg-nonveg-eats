package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CartViewDTO {

    @JsonProperty("cart_id")
    private String cartId;

    private List<CartItemViewDTO> items;
    private int count;
    private double subtotal;

    public CartViewDTO() {
    }

    public CartViewDTO(String cartId, List<CartItemViewDTO> items, int count, double subtotal) {
        this.cartId = cartId;
        this.items = items;
        this.count = count;
        this.subtotal = subtotal;
    }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public List<CartItemViewDTO> getItems() { return items; }
    public void setItems(List<CartItemViewDTO> items) { this.items = items; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
