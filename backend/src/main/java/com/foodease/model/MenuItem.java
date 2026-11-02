package com.foodease.model;

import jakarta.persistence.*;

/**
 * OOP ENCAPSULATION & ASSOCIATION:
 * Represents a Food Item on the FoodEase menu.
 * Associated with Category via @ManyToOne.
 */
@Entity
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean veg;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "image_url")
    private String image;

    @Column(name = "is_best_seller")
    private boolean bestSeller;

    @Column(name = "is_popular")
    private boolean popular;

    private int seq;

    public MenuItem() {
    }

    public MenuItem(String id, String name, String description, double price, boolean veg,
                    Category category, String image, boolean bestSeller, boolean popular, int seq) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.veg = veg;
        this.category = category;
        this.image = image;
        this.bestSeller = bestSeller;
        this.popular = popular;
        this.seq = seq;
    }

    // Business domain method: Encapsulation
    public boolean isEligibleForOffer() {
        return this.price > 100 && this.popular;
    }

    // Encapsulated getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Menu item price cannot be negative");
        }
        this.price = price;
    }

    public boolean isVeg() {
        return veg;
    }

    public void setVeg(boolean veg) {
        this.veg = veg;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
