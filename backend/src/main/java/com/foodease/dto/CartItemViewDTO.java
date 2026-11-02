package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodease.model.CartItem;

public class CartItemViewDTO {
    private String id;
    private String name;
    private double price;
    private boolean veg;
    private String image;
    private int quantity;

    @JsonProperty("line_total")
    private double lineTotal;

    public CartItemViewDTO() {
    }

    public CartItemViewDTO(CartItem item) {
        this.id = item.getMenuItem().getId();
        this.name = item.getMenuItem().getName();
        this.price = item.getMenuItem().getPrice();
        this.veg = item.getMenuItem().isVeg();
        this.image = item.getMenuItem().getImage();
        this.quantity = item.getQuantity();
        this.lineTotal = item.getLineTotal();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getLineTotal() { return lineTotal; }
    public void setLineTotal(double lineTotal) { this.lineTotal = lineTotal; }
}
