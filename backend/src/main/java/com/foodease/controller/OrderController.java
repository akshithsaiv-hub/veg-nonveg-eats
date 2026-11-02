package com.foodease.controller;

import com.foodease.dto.OrderCreateRequest;
import com.foodease.dto.OrderDTO;
import com.foodease.service.interfaces.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDTO placeOrder(@Valid @RequestBody OrderCreateRequest request, HttpServletRequest httpRequest) {
        String cartId = null;
        if (httpRequest.getCookies() != null) {
            cartId = Arrays.stream(httpRequest.getCookies())
                    .filter(c -> "cart_id".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return orderService.placeOrder(cartId, request);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }
}
