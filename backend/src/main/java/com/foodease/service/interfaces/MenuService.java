package com.foodease.service.interfaces;

import com.foodease.dto.CategoryInfoDTO;
import com.foodease.dto.FeaturedMenuDTO;
import com.foodease.dto.MenuItemDTO;
import java.util.List;

/**
 * OOP ABSTRACTION:
 * Interface defining contract for Menu queries and management.
 */
public interface MenuService {
    // Method Overloading (OOP Polymorphism)
    List<MenuItemDTO> getMenuItems();
    List<MenuItemDTO> getMenuItems(String categoryId);
    List<MenuItemDTO> getMenuItems(boolean vegOnly);

    List<CategoryInfoDTO> getCategories();
    FeaturedMenuDTO getFeaturedMenu();
    MenuItemDTO getMenuItemById(String id);
}
