package com.foodease.service.strategy.payment;

import com.foodease.model.Order;

/**
 * OOP ABSTRACTION:
 * Contract for processing multiple payment options (UPI, Card, Cash).
 */
public interface PaymentProcessor {
    boolean processPayment(Order order);
    String getProcessorName();
}
