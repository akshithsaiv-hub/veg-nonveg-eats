package com.foodease.service.implementation;

import com.foodease.dto.CartItemViewDTO;
import com.foodease.dto.CartViewDTO;
import com.foodease.exception.ResourceNotFoundException;
import com.foodease.model.Cart;
import com.foodease.model.MenuItem;
import com.foodease.repository.CartRepository;
import com.foodease.repository.MenuItemRepository;
import com.foodease.service.interfaces.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cart Service demonstrating Method Overloading and Encapsulation.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;

    public CartServiceImpl(CartRepository cartRepository, MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
    }

    private Cart getOrCreateCart(String cartId) {
        if (cartId == null || cartId.trim().isEmpty()) {
            return null;
        }
        return cartRepository.findById(cartId).orElseGet(() -> {
            Cart newCart = new Cart(cartId);
            return cartRepository.save(newCart);
        });
    }

    @Override
    public CartViewDTO getCart(String cartId) {
        if (cartId == null || !cartRepository.existsById(cartId)) {
            return new CartViewDTO(null, Collections.emptyList(), 0, 0.0);
        }
        Cart cart = cartRepository.findById(cartId).get();
        return toCartViewDTO(cart);
    }

    // Method Overloading #1: default quantity 1
    @Override
    public CartViewDTO addItem(String cartId, String menuItemId) {
        return addItem(cartId, menuItemId, 1);
    }

    // Method Overloading #2: custom quantity
    @Override
    public CartViewDTO addItem(String cartId, String menuItemId, int quantity) {
        Cart cart = getOrCreateCart(cartId);
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + menuItemId));

        cart.addItem(menuItem, quantity);
        Cart saved = cartRepository.save(cart);
        return toCartViewDTO(saved);
    }

    @Override
    public CartViewDTO updateItemQuantity(String cartId, String menuItemId, int quantity) {
        Cart cart = getOrCreateCart(cartId);
        cart.updateItemQuantity(menuItemId, quantity);
        Cart saved = cartRepository.save(cart);
        return toCartViewDTO(saved);
    }

    @Override
    public CartViewDTO removeItem(String cartId, String menuItemId) {
        Cart cart = getOrCreateCart(cartId);
        cart.removeItem(menuItemId);
        Cart saved = cartRepository.save(cart);
        return toCartViewDTO(saved);
    }

    @Override
    public void clearCart(String cartId) {
        if (cartId != null && cartRepository.existsById(cartId)) {
            Cart cart = cartRepository.findById(cartId).get();
            cart.clear();
            cartRepository.save(cart);
        }
    }

    private CartViewDTO toCartViewDTO(Cart cart) {
        List<CartItemViewDTO> items = cart.getItems().stream()
                .map(CartItemViewDTO::new)
                .collect(Collectors.toList());
        return new CartViewDTO(cart.getCartId(), items, cart.getTotalQuantity(), cart.getSubtotal());
    }
}
