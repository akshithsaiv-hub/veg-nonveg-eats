package com.foodease.service.implementation;

import com.foodease.dto.CategoryInfoDTO;
import com.foodease.dto.FeaturedMenuDTO;
import com.foodease.dto.MenuItemDTO;
import com.foodease.exception.ResourceNotFoundException;
import com.foodease.model.Category;
import com.foodease.model.MenuItem;
import com.foodease.repository.CategoryRepository;
import com.foodease.repository.MenuItemRepository;
import com.foodease.service.interfaces.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of MenuService demonstrating Method Overloading & Dependency Inversion.
 */
@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuServiceImpl(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    // Method Overloading #1 (Default: all items)
    @Override
    public List<MenuItemDTO> getMenuItems() {
        return menuItemRepository.findAllByOrderBySeqAsc()
                .stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());
    }

    // Method Overloading #2 (Filtered by Category)
    @Override
    public List<MenuItemDTO> getMenuItems(String categoryId) {
        return menuItemRepository.findByCategoryIdOrderBySeqAsc(categoryId)
                .stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());
    }

    // Method Overloading #3 (Filtered by Veg/Non-Veg)
    @Override
    public List<MenuItemDTO> getMenuItems(boolean vegOnly) {
        return menuItemRepository.findByVegOrderBySeqAsc(vegOnly)
                .stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryInfoDTO> getCategories() {
        List<Category> categories = categoryRepository.findAllByOrderBySeqAsc();
        return categories.stream().map(cat -> {
            int count = (int) menuItemRepository.findByCategoryIdOrderBySeqAsc(cat.getId()).size();
            return new CategoryInfoDTO(cat, count);
        }).collect(Collectors.toList());
    }

    @Override
    public FeaturedMenuDTO getFeaturedMenu() {
        List<MenuItemDTO> bestSellers = menuItemRepository.findByBestSellerTrueOrderBySeqAsc()
                .stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());

        List<MenuItemDTO> popular = menuItemRepository.findByPopularTrueOrderBySeqAsc()
                .stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());

        return new FeaturedMenuDTO(bestSellers, popular);
    }

    @Override
    public MenuItemDTO getMenuItemById(String id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));
        return new MenuItemDTO(item);
    }
}
