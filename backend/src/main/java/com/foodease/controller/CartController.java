package com.foodease.controller;

import com.foodease.dto.AddCartItemRequest;
import com.foodease.dto.CartViewDTO;
import com.foodease.dto.UpdateCartItemRequest;
import com.foodease.service.interfaces.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private String getOrCreateCartId(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("cart_id".equals(c.getName()) && c.getValue() != null && !c.getValue().isEmpty()) {
                    return c.getValue();
                }
            }
        }
        String newCartId = UUID.randomUUID().toString().replace("-", "");
        Cookie cookie = new Cookie("cart_id", newCartId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 3600);
        response.addCookie(cookie);
        return newCartId;
    }

    private String getExistingCartId(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> "cart_id".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @GetMapping
    public CartViewDTO getCart(HttpServletRequest request) {
        String cartId = getExistingCartId(request);
        return cartService.getCart(cartId);
    }

    @PostMapping("/items")
    public CartViewDTO addItem(@Valid @RequestBody AddCartItemRequest payload,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        String cartId = getOrCreateCartId(request, response);
        return cartService.addItem(cartId, payload.getMenuItemId(), payload.getQuantity());
    }

    @PatchMapping("/items/{itemId}")
    public CartViewDTO updateItem(@PathVariable String itemId,
                                  @RequestBody UpdateCartItemRequest payload,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        String cartId = getOrCreateCartId(request, response);
        return cartService.updateItemQuantity(cartId, itemId, payload.getQuantity());
    }

    @DeleteMapping("/items/{itemId}")
    public CartViewDTO removeItem(@PathVariable String itemId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        String cartId = getOrCreateCartId(request, response);
        return cartService.removeItem(cartId, itemId);
    }
}
