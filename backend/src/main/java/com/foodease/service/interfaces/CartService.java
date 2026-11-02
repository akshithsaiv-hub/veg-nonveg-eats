package com.foodease.service.interfaces;

import com.foodease.dto.CartViewDTO;

/**
 * OOP ABSTRACTION:
 * Interface defining Cart management contract.
 */
public interface CartService {
    CartViewDTO getCart(String cartId);

    // Method Overloading (OOP Polymorphism)
    CartViewDTO addItem(String cartId, String menuItemId);
    CartViewDTO addItem(String cartId, String menuItemId, int quantity);

    CartViewDTO updateItemQuantity(String cartId, String menuItemId, int quantity);
    CartViewDTO removeItem(String cartId, String menuItemId);
    void clearCart(String cartId);
}
