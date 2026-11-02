package com.foodease.service.interfaces;

import com.foodease.dto.OrderCreateRequest;
import com.foodease.dto.OrderDTO;
import java.util.List;

/**
 * OOP ABSTRACTION:
 * Interface defining Order lifecycle contract.
 */
public interface OrderService {
    OrderDTO placeOrder(String cartId, OrderCreateRequest request);
    OrderDTO getOrderById(String orderId);
    List<OrderDTO> getAllOrders();

    // Method Overloading (OOP Polymorphism for financial calculations)
    double calculateTotal(double subtotal);
    double calculateTotal(double subtotal, double discountAmount);
    double calculateTotal(double subtotal, double discountAmount, double gstRate);
}
