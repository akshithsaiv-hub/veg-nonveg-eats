package com.foodease.service.strategy;

/**
 * OOP ABSTRACTION & INHERITANCE:
 * Abstract base class capturing common discount parameters and encapsulation.
 */
public abstract class AbstractDiscountStrategy implements DiscountStrategy {

    private final String code;
    private final double percentage;

    protected AbstractDiscountStrategy(String code, double percentage) {
        this.code = code != null ? code.toUpperCase() : "STANDARD";
        this.percentage = percentage;
    }

    public String getCode() {
        return code;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public String getStrategyName() {
        return getCode() + " (" + percentage + "% off)";
    }
}
