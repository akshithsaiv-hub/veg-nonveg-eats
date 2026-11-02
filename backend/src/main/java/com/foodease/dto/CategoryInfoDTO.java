package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodease.model.Category;

public class CategoryInfoDTO {
    private String id;
    private String name;
    private String emoji;
    private boolean veg;
    private int seq;

    @JsonProperty("item_count")
    private int itemCount;

    public CategoryInfoDTO() {
    }

    public CategoryInfoDTO(Category cat) {
        this.id = cat.getId();
        this.name = cat.getName();
        this.emoji = cat.getEmoji();
        this.veg = cat.isVeg();
        this.seq = cat.getSeq();
        this.itemCount = cat.getItemCount();
    }

    public CategoryInfoDTO(Category cat, int count) {
        this.id = cat.getId();
        this.name = cat.getName();
        this.emoji = cat.getEmoji();
        this.veg = cat.isVeg();
        this.seq = cat.getSeq();
        this.itemCount = count;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }
    public int getSeq() { return seq; }
    public void setSeq(int seq) { this.seq = seq; }
    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
}
