package com.foodease.service.strategy;

/**
 * OOP INHERITANCE & METHOD OVERRIDING (POLYMORPHISM):
 * Calculates straight percentage discount on eligible orders.
 */
public class PercentageDiscountStrategy extends AbstractDiscountStrategy {

    public PercentageDiscountStrategy(String code, double percentage) {
        super(code, percentage);
    }

    @Override
    public boolean isApplicable(double subtotal) {
        return subtotal > 0;
    }

    @Override
    public double calculateDiscount(double subtotal) {
        if (!isApplicable(subtotal)) return 0.0;
        return Math.round((subtotal * (getPercentage() / 100.0)) * 100.0) / 100.0;
    }
}
