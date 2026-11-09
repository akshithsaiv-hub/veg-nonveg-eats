-- ============================================================
-- FoodEase MySQL Database Schema & Initial Data Dump
-- Target Database: MySQL 8.0+ / 9.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS `foodeasedb` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `foodeasedb`;

-- 1. Categories Table
CREATE TABLE IF NOT EXISTS `categories` (
    `id` VARCHAR(100) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `emoji` VARCHAR(50),
    `veg` BOOLEAN NOT NULL,
    `seq` INT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Menu Items Table
CREATE TABLE IF NOT EXISTS `menu_items` (
    `id` VARCHAR(100) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `price` DOUBLE NOT NULL,
    `veg` BOOLEAN NOT NULL,
    `category_id` VARCHAR(100),
    `image_url` VARCHAR(500),
    `is_best_seller` BOOLEAN NOT NULL DEFAULT FALSE,
    `is_popular` BOOLEAN NOT NULL DEFAULT FALSE,
    `seq` INT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_menu_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Coupons Table
CREATE TABLE IF NOT EXISTS `coupons` (
    `code` VARCHAR(50) NOT NULL,
    `percentage` DOUBLE NOT NULL,
    `min_order` DOUBLE NOT NULL,
    `label` VARCHAR(255),
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Users Table (Inheritance Root)
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) UNIQUE,
    `role` VARCHAR(50) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Customers Table (Inherits Users)
CREATE TABLE IF NOT EXISTS `customers` (
    `id` BIGINT NOT NULL,
    `phone` VARCHAR(50) NOT NULL,
    `delivery_address` VARCHAR(500),
    `preferred_payment_mode` VARCHAR(50),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_customer_user` FOREIGN KEY (`id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Admins Table (Inherits Users)
CREATE TABLE IF NOT EXISTS `admins` (
    `id` BIGINT NOT NULL,
    `department` VARCHAR(255) NOT NULL,
    `permission_level` INT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_admin_user` FOREIGN KEY (`id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Carts Table
CREATE TABLE IF NOT EXISTS `carts` (
    `cart_id` VARCHAR(100) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`cart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Cart Items Table (Composition with Cart)
CREATE TABLE IF NOT EXISTS `cart_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `cart_id` VARCHAR(100) NOT NULL,
    `menu_item_id` VARCHAR(100) NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_cart_items_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`cart_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cart_items_menu` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Orders Table
CREATE TABLE IF NOT EXISTS `orders` (
    `id` VARCHAR(50) NOT NULL,
    `customer_id` BIGINT,
    `customer_name` VARCHAR(255) NOT NULL,
    `customer_phone` VARCHAR(50) NOT NULL,
    `customer_address` VARCHAR(500) NOT NULL,
    `payment_mode` VARCHAR(50) NOT NULL,
    `subtotal` DOUBLE NOT NULL,
    `discount_percentage` DOUBLE DEFAULT 0.0,
    `discount` DOUBLE DEFAULT 0.0,
    `gst_percentage` DOUBLE DEFAULT 5.0,
    `gst` DOUBLE DEFAULT 0.0,
    `total` DOUBLE NOT NULL,
    `coupon_code` VARCHAR(50),
    `status` VARCHAR(50) NOT NULL DEFAULT 'RECEIVED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Order Items Table (Composition with Order)
CREATE TABLE IF NOT EXISTS `order_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` VARCHAR(50) NOT NULL,
    `menu_item_id` VARCHAR(100),
    `item_name` VARCHAR(255) NOT NULL,
    `price` DOUBLE NOT NULL,
    `veg` BOOLEAN NOT NULL,
    `quantity` INT NOT NULL,
    `line_total` DOUBLE NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_order_items_menu` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Data Insertion Statements
-- ============================================================

-- Seed Categories
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('best-sellers', 'Best Sellers', '⭐', 1, 1) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('breakfast-tiffins', 'Breakfast / Tiffins', '🍽️', 1, 2) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('dosa', 'Dosa', '🥞', 1, 3) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('combo-breakfast', 'Combo Breakfast', '👨‍👩‍👧‍👦', 1, 4) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('rice-items', 'Rice Items', '🍚', 1, 5) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('thali', 'Thali', '🍛', 1, 6) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('chinese-starters', 'Chinese Starters', '🥢', 1, 7) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('rice-noodles', 'Rice & Noodles', '🍜', 1, 8) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('veg-curries', 'Indian Veg Curries', '🍲', 1, 9) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('roti-naan', 'Roti & Naan', '🫓', 1, 10) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('chicken-starters', 'Chicken Starters', '🍗', 0, 11) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('chicken-curries', 'Chicken Curries', '🍛', 0, 12) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('chicken-biryani', 'Chicken Biryani', '🍚', 0, 13) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('mutton-specials', 'Mutton Specials', '🥘', 0, 14) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('fish-seafood', 'Fish & Seafood', '🐟', 0, 15) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('egg-dishes', 'Egg Dishes', '🥚', 0, 16) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('nonveg-rice-noodles', 'Non-Veg Fried Rice & Noodles', '🍜', 0, 17) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
INSERT INTO `categories` (`id`, `name`, `emoji`, `veg`, `seq`) VALUES ('nonveg-combos', 'Non-Veg Combos & Meals', '🍽️', 0, 18) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

-- Seed Menu Items
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('idly', 'Idly', '2 soft, fluffy idlis served with sambar, chutney & our special allam chutney.', 80, 1, 'best-sellers', '/images/idly.jpg', 1, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('2-idly-1-vada', '2 Idly + 1 Vada', 'Soft idlis and crispy vada served with sambar, chutney & special allam chutney.', 120, 1, 'best-sellers', '/images/2-idly-1-vada.jpg', 1, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('vada', 'Vada', 'Crispy, golden South Indian vadas served with sambar, chutney & special allam chutney.', 110, 1, 'best-sellers', '/images/vada.jpg', 1, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('poori', 'Poori', '3 fluffy, puffed pooris served with flavourful green aloo curry and special allam chutney.', 120, 1, 'best-sellers', '/images/poori.jpg', 1, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('masala-dosa', 'Masala Dosa', 'Crispy dosa filled with seasoned potato masala, served with sambar, chutney & allam chutney.', 120, 1, 'best-sellers', '/images/masala-dosa.jpg', 1, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('rava-dosa', 'Rava Dosa', 'Thin, crispy and crunchy rava dosa served with sambar and chutney.', 120, 1, 'best-sellers', '/images/rava-dosa.jpg', 1, 0, 6) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('parota', 'Parota', 'Flaky, layered South Indian parotas served with hot veg curry and chutney.', 120, 1, 'best-sellers', '/images/parota.jpg', 1, 0, 7) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('andhra-meals', 'Andhra Meals', 'Traditional Andhra-style meal with rice, flavoured rice, curries, fry, dal, sambar, rasam, curd, pickle & sweet.', 239, 1, 'best-sellers', '/images/andhra-meals.jpg', 1, 0, 8) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('sambar-idly', 'Sambar Idly', 'Soft idlis gently simmered in hot, flavourful sambar with a tempering of mustard & curry leaves.', 110, 1, 'breakfast-tiffins', '/images/sambar-idly.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('ghee-karam-idly', 'Ghee Karam Idly', 'Soft idlis layered with ghee & our signature karam podi, served with chutney.', 90, 1, 'breakfast-tiffins', '/images/ghee-karam-idly.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('small-vada', 'Small Vada', 'A plate of small, crispy medu vadas served with sambar, chutney & allam chutney.', 110, 1, 'breakfast-tiffins', '/images/small-vada.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('sambar-vada', 'Sambar Vada', 'Crispy golden vadas soaked in hot sambar, served with chutney.', 120, 1, 'breakfast-tiffins', '/images/sambar-vada.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('dahi-vada', 'Dahi Vada', 'Soft vadas dipped in chilled fresh curd, topped with karam podi & coriander.', 120, 1, 'breakfast-tiffins', '/images/dahi-vada.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('idly-vada', 'Idly + Vada', '1 soft idli & 1 crispy vada served with sambar, chutney & special allam chutney.', 100, 1, 'breakfast-tiffins', '/images/idly-vada.jpg', 0, 0, 6) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('idly-vada-sambar', 'Idly + Vada with Sambar', '1 soft idli & 1 crispy vada served with hot sambar, chutney & allam chutney.', 110, 1, 'breakfast-tiffins', '/images/idly-vada-sambar.jpg', 0, 0, 7) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mysore-bajji', 'Mysore Bajji', 'Golden-fried Mysore bajjis served with coconut chutney & sambar.', 90, 1, 'breakfast-tiffins', '/images/mysore-bajji.jpg', 0, 0, 8) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('upma', 'Upma', 'Hot, fluffy rava upma tempered with mustard, curry leaves, cashews & ghee.', 90, 1, 'breakfast-tiffins', '/images/placeholder.jpg', 0, 0, 9) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('pongal', 'Pongal', 'Creamy ven pongal cooked with ghee, pepper, cumin & cashews, served with sambar & chutney.', 90, 1, 'breakfast-tiffins', '/images/pongal.jpg', 0, 0, 10) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chapathi', 'Chapathi', '2 soft whole-wheat chapathis served with a flavourful mixed veg curry.', 110, 1, 'breakfast-tiffins', '/images/chapathi.jpg', 0, 0, 11) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('plain-dosa', 'Plain Dosa', 'Thin & crispy golden dosa served with sambar, chutney & allam chutney.', 100, 1, 'dosa', '/images/plain-dosa.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('onion-dosa', 'Onion Dosa', 'Crispy dosa sprinkled with chopped onions, served with sambar, chutney & allam chutney.', 120, 1, 'dosa', '/images/onion-dosa.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('onion-masala-dosa', 'Onion Masala Dosa', 'Crispy dosa loaded with spiced onion & potato masala, served with sambar, chutney & allam chutney.', 130, 1, 'dosa', '/images/onion-masala-dosa.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('ghee-karam-dosa', 'Ghee Karam Dosa', 'Dosa roasted in ghee & coated with our signature karam podi, served with sambar & chutney.', 130, 1, 'dosa', '/images/ghee-karam-dosa.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('ghee-masala-dosa', 'Ghee Masala Dosa', 'Ghee-roasted crispy dosa filled with seasoned potato masala, served with sambar, chutney & allam chutney.', 140, 1, 'dosa', '/images/ghee-masala-dosa.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('paneer-butter-masala-dosa', 'Paneer Butter Masala Dosa', 'Crispy dosa rolled with creamy paneer butter masala, served with sambar, chutney & allam chutney.', 150, 1, 'dosa', '/images/paneer-butter-masala-dosa.jpg', 0, 1, 6) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('70mm-dosa', '70MM Dosa', 'Thick, soft 70MM dosa roasted till golden, served with sambar, chutney & allam chutney.', 160, 1, 'dosa', '/images/70mm-dosa.jpg', 0, 0, 7) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('onion-rava-dosa', 'Onion Rava Dosa', 'Thin, lacy rava dosa with crunchy onions, served with sambar & chutney.', 130, 1, 'dosa', '/images/onion-rava-dosa.jpg', 0, 0, 8) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('masala-rava-dosa', 'Masala Rava Dosa', 'Lacy, crispy rava dosa with spiced potato masala, served with sambar & chutney.', 130, 1, 'dosa', '/images/masala-rava-dosa.jpg', 0, 0, 9) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('set-dosa', 'Set Dosa', 'A set of 3 soft, thick mini dosas served with sambar, chutney & allam chutney.', 140, 1, 'dosa', '/images/placeholder.jpg', 0, 0, 10) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('onion-uttapam', 'Onion Uttapam', 'Thick, soft uttapam topped with onions, served with sambar & chutney.', 140, 1, 'dosa', '/images/onion-uttapam.jpg', 0, 0, 11) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('pesarattu', 'Pesarattu', 'Protein-rich green gram pesarattu served with allam (ginger) chutney & sambar.', 120, 1, 'dosa', '/images/pesarattu.jpg', 0, 0, 12) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('upma-pesarattu', 'Upma Pesarattu', 'Green gram pesarattu layered with hot rava upma, served with allam chutney & sambar.', 140, 1, 'dosa', '/images/upma-pesarattu.jpg', 0, 1, 13) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('south-indian-combo', 'South Indian Combo', '1 Idly, 1 Vada & 1 Masala Dosa served with sambar, chutney & allam chutney — a complete South Indian breakfast combo.', 279, 1, 'combo-breakfast', '/images/south-indian-combo.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('family-breakfast', 'Family Breakfast', '4 Idly, 4 Vada, 2 Masala Dosa, 2 Poori & 1 Pongal served with sambar, chutney & allam chutney. Serves 3-4.', 599, 1, 'combo-breakfast', '/images/family-breakfast.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('jumbo-breakfast', 'Jumbo Breakfast', '6 Idly, 4 Vada, 2 Masala Dosa, 2 Poori, 2 Pongal & 1 Upma served with sambar, chutney & allam chutney. Serves 4-6.', 799, 1, 'combo-breakfast', '/images/jumbo-breakfast.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('veg-biryani', 'Veg Biryani', 'Fragrant basmati rice dum-cooked with garden vegetables, whole spices & herbs, served with raita & salan.', 169, 1, 'rice-items', '/images/veg-biryani.jpg', 0, 1, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('kaju-biryani', 'Kaju Biryani', 'Aromatic dum biryani loaded with roasted cashews & whole spices, served with raita & salan.', 239, 1, 'rice-items', '/images/kaju-biryani.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('paneer-biryani', 'Paneer Biryani', 'Dum-cooked basmati layered with marinated paneer & spices, served with raita & salan.', 229, 1, 'rice-items', '/images/paneer-biryani.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('tomato-rice', 'Tomato Rice', 'Tangy rice tossed with ripe tomatoes, curry leaves & roasted peanuts.', 139, 1, 'rice-items', '/images/tomato-rice.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('lemon-rice', 'Lemon Rice', 'Zesty rice tempered with lemon, mustard, chana dal & cashews.', 139, 1, 'rice-items', '/images/lemon-rice.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('jeera-rice', 'Jeera Rice', 'Fluffy basmati rice tossed in ghee with toasted cumin seeds.', 179, 1, 'rice-items', '/images/jeera-rice.jpg', 0, 0, 6) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('curd-rice', 'Curd Rice', 'Cool, comforting rice folded in fresh curd with a mustard-curry leaf tempering.', 129, 1, 'rice-items', '/images/curd-rice.jpg', 0, 0, 7) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('south-indian-andhra-thali', 'South Indian Andhra Thali', 'Authentic Andhra thali with rice, dal, sambar, rasam, 2 curries, fry, curd, pickle, papad & sweet.', 239, 1, 'thali', '/images/south-indian-andhra-thali.jpg', 0, 1, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('north-indian-thali', 'North Indian Thali', 'North Indian thali with 2 rotis, rice, dal, paneer curry, veg sabzi, salad, papad & sweet.', 279, 1, 'thali', '/images/north-indian-thali.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('gobi-manchurian', 'Gobi Manchurian', 'Crisp cauliflower florets tossed in a tangy manchurian sauce with spring onions.', 240, 1, 'chinese-starters', '/images/gobi-manchurian.jpg', 0, 1, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('veg-manchurian', 'Veg Manchurian', 'Fried veggie dumplings wok-tossed in spicy manchurian gravy with garlic & spring onion.', 240, 1, 'chinese-starters', '/images/veg-manchurian.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('paneer-65', 'Paneer 65', 'Spicy fried paneer cubes tossed with curry leaves, chillies, served with a cooling dip.', 270, 1, 'chinese-starters', '/images/paneer-65.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mushroom-chilli', 'Mushroom Chilli', 'Wok-tossed mushrooms with peppers, onions & fiery chilli sauce.', 260, 1, 'chinese-starters', '/images/mushroom-chilli.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('paneer-chilli', 'Paneer Chilli', 'Crisp paneer tossed with capsicum, onion & hot chilli-garlic sauce.', 260, 1, 'chinese-starters', '/images/paneer-chilli.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('veg-fried-rice', 'Veg Fried Rice', 'Classic wok-tossed rice with carrots, beans, cabbage & spring onions.', 240, 1, 'rice-noodles', '/images/veg-fried-rice.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mushroom-fried-rice', 'Mushroom Fried Rice', 'Fried rice tossed with sautéed mushrooms, peppers & spring onion.', 250, 1, 'rice-noodles', '/images/mushroom-fried-rice.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('paneer-fried-rice', 'Paneer Fried Rice', 'Fried rice tossed with paneer cubes, veggies & soy-pepper seasoning.', 250, 1, 'rice-noodles', '/images/paneer-fried-rice.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('gobi-fried-rice', 'Gobi Fried Rice', 'Fried rice tossed with crisp cauliflower, carrots & spring onion.', 260, 1, 'rice-noodles', '/images/gobi-fried-rice.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('avakaya-fried-rice', 'Avakaya Fried Rice', 'Spicy Andhra-style fried rice with tangy avakaya (raw mango) pickle & veggies.', 250, 1, 'rice-noodles', '/images/avakaya-fried-rice.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('pandu-mirchi-fried-rice', 'Pandu Mirchi Fried Rice', 'Fried rice wok-tossed with ripe red chillies & garlic for a fiery kick.', 250, 1, 'rice-noodles', '/images/pandu-mirchi-fried-rice.jpg', 0, 0, 6) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('veg-noodles', 'Veg Noodles', 'Hakka-style noodles stir-fried with julienned veggies & soy.', 230, 1, 'rice-noodles', '/images/veg-noodles.jpg', 0, 0, 7) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('avakaya-noodles', 'Avakaya Noodles', 'Noodles tossed with tangy-spicy avakaya pickle & crunchy veggies.', 250, 1, 'rice-noodles', '/images/avakaya-noodles.jpg', 0, 0, 8) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('pandu-mirchi-noodles', 'Pandu Mirchi Noodles', 'Noodles tossed with ripe red-chilli paste, garlic & veggies.', 250, 1, 'rice-noodles', '/images/pandu-mirchi-noodles.jpg', 0, 0, 9) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('paneer-butter-masala', 'Paneer Butter Masala', 'Soft paneer simmered in silky tomato-butter gravy finished with cream & kasuri methi.', 290, 1, 'veg-curries', '/images/paneer-butter-masala.jpg', 0, 1, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mushroom-butter-masala', 'Mushroom Butter Masala', 'Mushrooms in a rich makhani gravy with cream & toasted spices.', 290, 1, 'veg-curries', '/images/mushroom-butter-masala.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('kadai-paneer', 'Kadai Paneer', 'Paneer & capsicum tossed in a robust kadai masala with freshly ground spices.', 290, 1, 'veg-curries', '/images/kadai-paneer.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('dal-fry', 'Dal Fry', 'Yellow dal tempered with ghee, garlic, cumin & red chilli.', 220, 1, 'veg-curries', '/images/dal-fry.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('kaju-masala', 'Kaju Masala', 'Roasted cashews in a luxuriously rich cashew-tomato gravy with whole spices.', 340, 1, 'veg-curries', '/images/kaju-masala.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mixed-veg-curry', 'Mixed Veg Curry', 'Garden vegetables simmered in a homestyle onion-tomato gravy.', 270, 1, 'veg-curries', '/images/mixed-veg-curry.jpg', 0, 0, 6) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('roti', 'Roti', 'Soft whole-wheat phulka roti, freshly made — served with your meal.', 70, 1, 'roti-naan', '/images/roti.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('naan', 'Naan', 'Soft leavened naan, freshly baked in the tandoor.', 75, 1, 'roti-naan', '/images/naan.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('butter-roti', 'Butter Roti', 'Fluffy whole-wheat roti brushed with melting butter.', 80, 1, 'roti-naan', '/images/butter-roti.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('butter-naan', 'Butter Naan', 'Tandoor-fresh naan slathered with melting butter.', 90, 1, 'roti-naan', '/images/butter-naan.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('garlic-naan', 'Garlic Naan', 'Naan studded with garlic & coriander, brushed with butter.', 100, 1, 'roti-naan', '/images/garlic-naan.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('masala-kulcha', 'Masala Kulcha', 'Soft kulcha stuffed with spiced potato-onion masala, brushed with butter.', 110, 1, 'roti-naan', '/images/masala-kulcha.jpg', 0, 0, 6) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-65', 'Chicken 65', 'Fiery boneless chicken 65 flash-fried and tossed with curry leaves, garlic & green chillies, served with a mint dip.', 290, 0, 'chicken-starters', '/images/chicken-65.jpg', 0, 1, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-lollipop', 'Chicken Lollipop', 'Frenched chicken wings marinated in red chilli paste, fried crisp & served with schezwan dip.', 300, 0, 'chicken-starters', '/images/chicken-lollipop.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chilli-chicken', 'Chilli Chicken', 'Indo-Chinese favourite — wok-tossed chicken with onions, capsicum, chillies & soy.', 290, 0, 'chicken-starters', '/images/chilli-chicken.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-manchurian', 'Chicken Manchurian', 'Crisp chicken dumplings in a tangy manchurian sauce with garlic & spring onions.', 280, 0, 'chicken-starters', '/images/chicken-manchurian.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('butter-chicken', 'Butter Chicken', 'Tandoor-charred chicken simmered in silky tomato-butter gravy finished with cream.', 340, 0, 'chicken-curries', '/images/butter-chicken.jpg', 0, 1, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-tikka-masala', 'Chicken Tikka Masala', 'Char-grilled chicken tikka pieces in a rich onion-tomato masala with toasted spices.', 330, 0, 'chicken-curries', '/images/chicken-tikka-masala.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('andhra-chicken-curry', 'Andhra Kodi Kura', 'Country-style Andhra chicken curry cooked with coconut, curry leaves & hand-pounded masala.', 320, 0, 'chicken-curries', '/images/andhra-chicken-curry.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('pepper-chicken', 'Pepper Chicken Masala', 'Black-pepper spiked chicken with onion & curry-leaf tempering — a South Indian classic.', 330, 0, 'chicken-curries', '/images/pepper-chicken.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-biryani', 'Chicken Biryani', 'Fragrant basmati layered with marinated chicken, fried onions, mint & saffron, sealed dum style. Served with raita & salan.', 279, 0, 'chicken-biryani', '/images/chicken-biryani.jpg', 0, 1, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-dum-biryani', 'Chicken Dum Biryani (Boneless)', 'Boneless chicken dum-cooked with long-grain basmati, whole spices & herbs. Served with raita & salan.', 329, 0, 'chicken-biryani', '/images/chicken-dum-biryani.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-fry-piece-biryani', 'Chicken Fry Piece Biryani', 'Our dum biryani topped with crispy fried chicken pieces & fried onions. Served with raita & salan.', 299, 0, 'chicken-biryani', '/images/chicken-fry-piece-biryani.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-65-biryani', 'Chicken 65 Biryani', 'Dum biryani crowned with fiery chicken 65 pieces. Served with raita & salan.', 309, 0, 'chicken-biryani', '/images/chicken-65-biryani.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mutton-biryani', 'Mutton Biryani', 'Slow-cooked mutton dum-layered with long-grain basmati, saffron & fried onions. Served with raita & salan.', 349, 0, 'mutton-specials', '/images/mutton-biryani.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('andhra-mutton-curry', 'Andhra Mutton Curry', 'Tender mutton slow-simmered in a fiery Andhra masala with coconut & curry leaves.', 360, 0, 'mutton-specials', '/images/andhra-mutton-curry.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mutton-fry', 'Mutton Fry', 'Tender mutton pieces pan-fried with curry leaves, garlic & coarse pepper.', 350, 0, 'mutton-specials', '/images/mutton-fry.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mutton-rogan-josh', 'Mutton Rogan Josh', 'Kashmiri-style mutton in a deep red, aromatic rogan josh gravy.', 370, 0, 'mutton-specials', '/images/mutton-rogan-josh.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('apollo-fish', 'Apollo Fish', 'Boneless fish chunks tossed in chilli-garlic masala with curry leaves & yogurt.', 330, 0, 'fish-seafood', '/images/apollo-fish.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('andhra-fish-curry', 'Andhra Fish Curry', 'Fish simmered in tangy tamarind gravy with mustard, curry leaves & Andhra spices.', 340, 0, 'fish-seafood', '/images/andhra-fish-curry.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('fish-fry', 'Fish Fry (Andhra Style)', 'Fish marinated in Andhra masala, semolina-crusted & shallow-fried crisp.', 320, 0, 'fish-seafood', '/images/fish-fry.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('prawn-fry', 'Prawn Fry', 'Juicy prawns tossed with chilli, garlic & curry leaves in a spicy dry roast.', 350, 0, 'fish-seafood', '/images/prawn-fry.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('prawn-curry', 'Prawn Curry', 'Prawns simmered in a coconut-onion gravy with whole spices & tamarind.', 360, 0, 'fish-seafood', '/images/prawn-curry.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('egg-biryani', 'Egg Biryani', 'Dum-cooked basmati with boiled eggs, fried onions & whole spices. Served with raita & salan.', 219, 0, 'egg-dishes', '/images/egg-biryani.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('egg-curry', 'Egg Curry', 'Farm eggs in a golden onion-tomato gravy with whole spices & coriander.', 190, 0, 'egg-dishes', '/images/egg-curry.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('egg-bhurji', 'Egg Bhurji', 'Spiced scrambled eggs with onion, tomato, green chilli & pepper.', 170, 0, 'egg-dishes', '/images/egg-bhurji.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('boiled-egg-fry', 'Boiled Egg Fry', 'Boiled eggs flash-fried with chilli, garlic & curry-leaf masala.', 180, 0, 'egg-dishes', '/images/boiled-egg-fry.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-fried-rice', 'Chicken Fried Rice', 'Wok-tossed rice with shredded chicken, veggies, egg & soy-pepper.', 260, 0, 'nonveg-rice-noodles', '/images/chicken-fried-rice.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('egg-fried-rice', 'Egg Fried Rice', 'Classic wok-tossed rice with scrambled egg, spring onion & pepper.', 220, 0, 'nonveg-rice-noodles', '/images/egg-fried-rice.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mixed-fried-rice', 'Mixed Fried Rice', 'Fried rice loaded with chicken, egg & prawns with veggies & soy.', 290, 0, 'nonveg-rice-noodles', '/images/mixed-fried-rice.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-noodles', 'Chicken Noodles', 'Hakka noodles stir-fried with shredded chicken, veggies & soy.', 260, 0, 'nonveg-rice-noodles', '/images/chicken-noodles.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('egg-noodles', 'Egg Noodles', 'Hakka noodles tossed with egg, spring onion & pepper.', 220, 0, 'nonveg-rice-noodles', '/images/egg-noodles.jpg', 0, 0, 5) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-thali', 'Chicken Thali', 'Chicken curry, 2 rotis, rice, dal, veg sabzi, salad, papad & sweet — a full non-veg thali.', 349, 0, 'nonveg-combos', '/images/chicken-thali.jpg', 0, 0, 1) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('chicken-combo-meal', 'Chicken Combo Meal', 'Chicken Biryani + Chicken 65 + raita & salad — a hearty combo for one.', 379, 0, 'nonveg-combos', '/images/chicken-combo-meal.jpg', 0, 0, 2) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('mutton-special-meal', 'Mutton Special Meal', 'Mutton curry, 2 rotis, rice, dal, pickle, salad & papad — a mutton lover\'s feast.', 429, 0, 'nonveg-combos', '/images/mutton-special-meal.jpg', 0, 0, 3) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);
INSERT INTO `menu_items` (`id`, `name`, `description`, `price`, `veg`, `category_id`, `image_url`, `is_best_seller`, `is_popular`, `seq`) VALUES ('family-nonveg-feast', 'Family Non-Veg Feast', 'Chicken Biryani, Mutton Fry, Chilli Chicken & Egg Curry with rotis, raita & salad. Serves 3-4.', 999, 0, 'nonveg-combos', '/images/family-nonveg-feast.jpg', 0, 0, 4) ON DUPLICATE KEY UPDATE `price`=VALUES(`price`);

-- Seed Promotional Coupons
INSERT INTO `coupons` (`code`, `percentage`, `min_order`, `label`, `active`) VALUES ('WELCOME10', 10.0, 0, '10% off on all orders', 1) ON DUPLICATE KEY UPDATE `percentage`=VALUES(`percentage`);
INSERT INTO `coupons` (`code`, `percentage`, `min_order`, `label`, `active`) VALUES ('COMBO15', 15.0, 399, '15% off on orders above ₹399', 1) ON DUPLICATE KEY UPDATE `percentage`=VALUES(`percentage`);
INSERT INTO `coupons` (`code`, `percentage`, `min_order`, `label`, `active`) VALUES ('FESTIVE20', 20.0, 499, '20% off on orders above ₹499', 1) ON DUPLICATE KEY UPDATE `percentage`=VALUES(`percentage`);

-- Seed Default Admin User
INSERT IGNORE INTO `users` (`id`, `name`, `email`, `role`) VALUES (1, 'System Administrator', 'admin@foodease.com', 'ADMIN');
INSERT IGNORE INTO `admins` (`id`, `department`, `permission_level`) VALUES (1, 'Operations', 10);