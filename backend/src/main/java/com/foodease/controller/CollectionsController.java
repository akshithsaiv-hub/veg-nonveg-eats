package com.foodease.controller;

import com.foodease.dto.CollectionsStatsDTO;
import com.foodease.dto.ConfigDTO;
import com.foodease.model.Category;
import com.foodease.repository.CartRepository;
import com.foodease.repository.CategoryRepository;
import com.foodease.repository.MenuItemRepository;
import com.foodease.repository.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller maintaining exact contracts for:
 * - /api/collections/config
 * - /api/collections/stats (Academic OOP & Collections demonstration)
 */
@RestController
@RequestMapping("/api/collections")
public class CollectionsController {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CollectionsController(MenuItemRepository menuItemRepository,
                                 CategoryRepository categoryRepository,
                                 CartRepository cartRepository,
                                 OrderRepository orderRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/config")
    public ConfigDTO getConfig() {
        return new ConfigDTO(5.0, Arrays.asList("COMBO15", "FESTIVE20", "WELCOME10"));
    }

    @GetMapping("/stats")
    public CollectionsStatsDTO getStats() {
        int menuCount = (int) menuItemRepository.count();
        int activeCarts = (int) cartRepository.count();
        int ordersCount = (int) orderRepository.count();

        List<String> uniqueCategories = categoryRepository.findAllByOrderBySeqAsc()
                .stream()
                .map(Category::getId)
                .sorted()
                .collect(Collectors.toList());

        return new CollectionsStatsDTO(menuCount, activeCarts, uniqueCategories, ordersCount);
    }
}
