package com.foodease.controller;

import com.foodease.dto.CategoryInfoDTO;
import com.foodease.dto.FeaturedMenuDTO;
import com.foodease.dto.MenuItemDTO;
import com.foodease.service.interfaces.MenuService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<MenuItemDTO> getMenu(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean veg) {
        if (category != null && !category.trim().isEmpty()) {
            return menuService.getMenuItems(category.trim());
        }
        if (veg != null) {
            return menuService.getMenuItems(veg);
        }
        return menuService.getMenuItems();
    }

    @GetMapping("/categories")
    public List<CategoryInfoDTO> getCategories() {
        return menuService.getCategories();
    }

    @GetMapping("/featured")
    public FeaturedMenuDTO getFeatured() {
        return menuService.getFeaturedMenu();
    }

    @GetMapping("/{id}")
    public MenuItemDTO getItemById(@PathVariable String id) {
        return menuService.getMenuItemById(id);
    }
}
