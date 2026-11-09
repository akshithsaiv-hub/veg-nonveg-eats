package com.foodease.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodease.model.Admin;
import com.foodease.model.Category;
import com.foodease.model.Coupon;
import com.foodease.model.MenuItem;
import com.foodease.repository.CategoryRepository;
import com.foodease.repository.CouponRepository;
import com.foodease.repository.MenuItemRepository;
import com.foodease.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Startup Data Seeder:
 * Reads data catalogs from classpath (menu.json, categories.json, coupons.json)
 * and populates the database idempotently so that subsequent restarts on persistent
 * databases like MySQL do not duplicate data or trigger constraint violations.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public DataInitializer(CategoryRepository categoryRepository,
                           MenuItemRepository menuItemRepository,
                           CouponRepository couponRepository,
                           UserRepository userRepository,
                           ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking FoodEase Database Initialization status...");

        Map<String, Category> categoryMap = new HashMap<>();

        // 1. Seed Categories if empty
        if (categoryRepository.count() == 0) {
            log.info("Seeding categories into database...");
            try (InputStream is = new ClassPathResource("data/categories.json").getInputStream()) {
                JsonNode root = objectMapper.readTree(is);
                if (root.isArray()) {
                    for (JsonNode n : root) {
                        Category cat = new Category(
                                n.get("id").asText(),
                                n.get("name").asText(),
                                n.has("emoji") ? n.get("emoji").asText() : "",
                                n.has("veg") && n.get("veg").asBoolean(),
                                n.has("seq") ? n.get("seq").asInt() : 0
                        );
                        categoryRepository.save(cat);
                        categoryMap.put(cat.getId(), cat);
                    }
                }
            }
            log.info("Loaded {} categories into database.", categoryMap.size());
        } else {
            categoryRepository.findAll().forEach(cat -> categoryMap.put(cat.getId(), cat));
            log.info("Categories already exist in database ({} found).", categoryMap.size());
        }

        // 2. Seed Menu Items if empty
        if (menuItemRepository.count() == 0) {
            log.info("Seeding menu items into database...");
            int menuCount = 0;
            try (InputStream is = new ClassPathResource("data/menu.json").getInputStream()) {
                JsonNode root = objectMapper.readTree(is);
                if (root.isArray()) {
                    for (JsonNode n : root) {
                        String catId = n.has("category") ? n.get("category").asText() : "";
                        Category cat = categoryMap.get(catId);

                        MenuItem item = new MenuItem(
                                n.get("id").asText(),
                                n.get("name").asText(),
                                n.has("description") ? n.get("description").asText() : "",
                                n.get("price").asDouble(),
                                n.get("veg").asBoolean(),
                                cat,
                                n.has("image") ? n.get("image").asText() : "",
                                n.has("best_seller") && n.get("best_seller").asBoolean(),
                                n.has("popular") && n.get("popular").asBoolean(),
                                n.has("seq") ? n.get("seq").asInt() : 0
                        );
                        menuItemRepository.save(item);
                        menuCount++;
                    }
                }
            }
            log.info("Loaded {} menu items into database.", menuCount);
        } else {
            log.info("Menu items already exist in database ({} found).", menuItemRepository.count());
        }

        // 3. Seed Coupons if empty
        if (couponRepository.count() == 0) {
            log.info("Seeding coupons into database...");
            int couponCount = 0;
            try (InputStream is = new ClassPathResource("data/coupons.json").getInputStream()) {
                JsonNode root = objectMapper.readTree(is);
                if (root.isArray()) {
                    for (JsonNode n : root) {
                        Coupon coupon = new Coupon(
                                n.get("code").asText(),
                                n.get("percentage").asDouble(),
                                n.has("min_order") ? n.get("min_order").asDouble() : 0.0,
                                n.has("label") ? n.get("label").asText() : ""
                        );
                        couponRepository.save(coupon);
                        couponCount++;
                    }
                }
            }
            log.info("Loaded {} discount coupons into database.", couponCount);
        } else {
            log.info("Coupons already exist in database ({} found).", couponRepository.count());
        }

        // 4. Seed Default Admin User if empty
        if (userRepository.count() == 0) {
            Admin admin = new Admin("System Administrator", "admin@foodease.com", "Operations", 10);
            userRepository.save(admin);
            log.info("Initialized default administrator user.");
        }

        log.info("Database Initialization check completed successfully!");
    }
}
