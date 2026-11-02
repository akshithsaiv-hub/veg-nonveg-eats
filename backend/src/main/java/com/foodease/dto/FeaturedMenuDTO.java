package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FeaturedMenuDTO {

    @JsonProperty("best_sellers")
    private List<MenuItemDTO> bestSellers;

    @JsonProperty("popular")
    private List<MenuItemDTO> popular;

    public FeaturedMenuDTO() {
    }

    public FeaturedMenuDTO(List<MenuItemDTO> bestSellers, List<MenuItemDTO> popular) {
        this.bestSellers = bestSellers;
        this.popular = popular;
    }

    public List<MenuItemDTO> getBestSellers() { return bestSellers; }
    public void setBestSellers(List<MenuItemDTO> bestSellers) { this.bestSellers = bestSellers; }
    public List<MenuItemDTO> getPopular() { return popular; }
    public void setPopular(List<MenuItemDTO> popular) { this.popular = popular; }
}
