package com.foodease.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OOP COMPOSITION / AGGREGATION:
 * Category aggregates multiple MenuItem objects.
 */
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String emoji;

    @Column(nullable = false)
    private boolean veg;

    @Column(nullable = false)
    private int seq;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuItem> items = new ArrayList<>();

    public Category() {
    }

    public Category(String id, String name, String emoji, boolean veg, int seq) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.veg = veg;
        this.seq = seq;
    }

    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    // Encapsulation
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

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public boolean isVeg() {
        return veg;
    }

    public void setVeg(boolean veg) {
        this.veg = veg;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
