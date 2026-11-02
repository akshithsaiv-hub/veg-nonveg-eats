package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Academic Collections & OOP Demonstration DTO.
 */
public class CollectionsStatsDTO {

    @JsonProperty("menu_items_structure")
    private String menuItemsStructure;

    @JsonProperty("menu_items_count")
    private int menuItemsCount;

    @JsonProperty("cart_structure")
    private String cartStructure;

    @JsonProperty("active_carts_count")
    private int activeCartsCount;

    @JsonProperty("categories_structure")
    private String categoriesStructure;

    @JsonProperty("unique_categories_count")
    private int uniqueCategoriesCount;

    @JsonProperty("unique_categories")
    private List<String> uniqueCategories;

    @JsonProperty("orders_structure")
    private String ordersStructure;

    @JsonProperty("orders_history_count")
    private int ordersHistoryCount;

    public CollectionsStatsDTO() {
    }

    public CollectionsStatsDTO(int menuCount, int activeCarts, List<String> uniqueCats, int ordersCount) {
        this.menuItemsStructure = "ArrayList<FoodItem> -> Spring Data JPA List<MenuItem>";
        this.menuItemsCount = menuCount;
        this.cartStructure = "HashMap<String, Cart> -> Spring Data JPA Session Cart Entity";
        this.activeCartsCount = activeCarts;
        this.categoriesStructure = "HashSet<String> -> Set<String> unique categories";
        this.uniqueCategoriesCount = uniqueCats.size();
        this.uniqueCategories = uniqueCats;
        this.ordersStructure = "ArrayList<Order> -> Spring Data JPA List<Order>";
        this.ordersHistoryCount = ordersCount;
    }

    public String getMenuItemsStructure() { return menuItemsStructure; }
    public int getMenuItemsCount() { return menuItemsCount; }
    public String getCartStructure() { return cartStructure; }
    public int getActiveCartsCount() { return activeCartsCount; }
    public String getCategoriesStructure() { return categoriesStructure; }
    public int getUniqueCategoriesCount() { return uniqueCategoriesCount; }
    public List<String> getUniqueCategories() { return uniqueCategories; }
    public String getOrdersStructure() { return ordersStructure; }
    public int getOrdersHistoryCount() { return ordersHistoryCount; }
}
