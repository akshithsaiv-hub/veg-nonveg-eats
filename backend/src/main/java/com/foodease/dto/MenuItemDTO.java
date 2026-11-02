package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodease.model.MenuItem;

public class MenuItemDTO {
    private String id;
    private String name;
    private String description;
    private double price;
    private boolean veg;
    private String category;
    private String image;

    @JsonProperty("best_seller")
    private boolean bestSeller;

    @JsonProperty("popular")
    private boolean popular;

    private int seq;

    public MenuItemDTO() {
    }

    public MenuItemDTO(MenuItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.veg = item.isVeg();
        this.category = item.getCategory() != null ? item.getCategory().getId() : "";
        this.image = item.getImage();
        this.bestSeller = item.isBestSeller();
        this.popular = item.isPopular();
        this.seq = item.getSeq();
    }

    // Encapsulation
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public boolean isBestSeller() { return bestSeller; }
    public void setBestSeller(boolean bestSeller) { this.bestSeller = bestSeller; }
    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }
    public int getSeq() { return seq; }
    public void setSeq(int seq) { this.seq = seq; }
}
