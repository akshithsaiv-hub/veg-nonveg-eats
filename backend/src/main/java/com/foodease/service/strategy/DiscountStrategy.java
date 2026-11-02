package com.foodease.service.strategy;

/**
 * OOP ABSTRACTION & POLYMORPHISM:
 * Common contract for promotional discount calculations.
 * Open/Closed Principle: New discount types can be introduced without modifying order services.
 */
public interface DiscountStrategy {
    double calculateDiscount(double subtotal);
    String getStrategyName();
    boolean isApplicable(double subtotal);
}
