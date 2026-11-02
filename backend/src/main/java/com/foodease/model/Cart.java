package com.foodease.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OOP ENCAPSULATION & COMPOSITION:
 * Cart has a composite relationship with CartItem.
 * Cart encapsulates all item addition, quantity updates, and subtotal calculations.
 */
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    private String cartId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(String cartId) {
        this.cartId = cartId;
    }

    // Business Logic Methods (Encapsulation)
    public void addItem(MenuItem menuItem, int quantity) {
        if (quantity <= 0) return;
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getMenuItem().getId().equals(menuItem.getId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(this, menuItem, quantity);
            items.add(newItem);
        }
    }

    public void updateItemQuantity(String menuItemId, int quantity) {
        if (quantity <= 0) {
            removeItem(menuItemId);
        } else {
            items.stream()
                    .filter(i -> i.getMenuItem().getId().equals(menuItemId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
        }
    }

    public void removeItem(String menuItemId) {
        items.removeIf(i -> i.getMenuItem().getId().equals(menuItemId));
    }

    public void clear() {
        items.clear();
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(CartItem::getLineTotal).sum();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    // Getters and Setters
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
