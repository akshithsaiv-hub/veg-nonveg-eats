package com.foodease.service.strategy;

/**
 * OOP INHERITANCE & METHOD OVERRIDING (POLYMORPHISM):
 * Derived discount strategy that enforces a minimum order cart value.
 */
public class ThresholdDiscountStrategy extends PercentageDiscountStrategy {

    private final double minOrderAmount;

    public ThresholdDiscountStrategy(String code, double percentage, double minOrderAmount) {
        super(code, percentage);
        this.minOrderAmount = minOrderAmount;
    }

    public double getMinOrderAmount() {
        return minOrderAmount;
    }

    @Override
    public boolean isApplicable(double subtotal) {
        return subtotal >= minOrderAmount;
    }

    @Override
    public double calculateDiscount(double subtotal) {
        if (!isApplicable(subtotal)) {
            return 0.0;
        }
        return super.calculateDiscount(subtotal);
    }
}
