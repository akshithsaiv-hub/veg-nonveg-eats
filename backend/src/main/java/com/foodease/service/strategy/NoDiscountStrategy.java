package com.foodease.service.strategy;

/**
 * OOP POLYMORPHISM (Null Object Pattern):
 * Default strategy when no discount coupon is applied.
 */
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscount(double subtotal) {
        return 0.0;
    }

    @Override
    public String getStrategyName() {
        return "NO_DISCOUNT";
    }

    @Override
    public boolean isApplicable(double subtotal) {
        return false;
    }
}
