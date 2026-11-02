package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodease.model.OrderItem;

public class OrderItemViewDTO {

    @JsonProperty("item_id")
    private String itemId;

    private String name;
    private double price;
    private boolean veg;
    private int quantity;

    @JsonProperty("line_total")
    private double lineTotal;

    public OrderItemViewDTO() {
    }

    public OrderItemViewDTO(OrderItem item) {
        this.itemId = item.getMenuItem() != null ? item.getMenuItem().getId() : "";
        this.name = item.getName();
        this.price = item.getPrice();
        this.veg = item.isVeg();
        this.quantity = item.getQuantity();
        this.lineTotal = item.getLineTotal();
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getLineTotal() { return lineTotal; }
    public void setLineTotal(double lineTotal) { this.lineTotal = lineTotal; }
}
