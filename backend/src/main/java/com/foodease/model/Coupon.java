package com.foodease.model;

import jakarta.persistence.*;

/**
 * OOP ENCAPSULATION:
 * Encapsulates promotion coupon rules and discount eligibility criteria.
 */
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @Id
    private String code;

    @Column(nullable = false)
    private double percentage;

    @Column(name = "min_order", nullable = false)
    private double minOrder;

    private String label;

    private boolean active = true;

    public Coupon() {
    }

    public Coupon(String code, double percentage, double minOrder, String label) {
        this.code = code != null ? code.toUpperCase() : null;
        this.percentage = percentage;
        this.minOrder = minOrder;
        this.label = label;
        this.active = true;
    }

    // Business Logic Method (Encapsulation)
    public boolean isValidForAmount(double subtotal) {
        return this.active && subtotal >= this.minOrder;
    }

    public double calculateDiscount(double subtotal) {
        if (!isValidForAmount(subtotal)) {
            return 0.0;
        }
        return Math.round((subtotal * (percentage / 100.0)) * 100.0) / 100.0;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code.toUpperCase() : null;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(double minOrder) {
        this.minOrder = minOrder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
