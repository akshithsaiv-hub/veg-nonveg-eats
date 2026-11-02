package com.foodease.enums;

public enum PaymentMode {
    UPI("Unified Payments Interface (UPI)"),
    CARD("Credit/Debit Card"),
    CASH("Cash on Delivery");

    private final String description;

    PaymentMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMode fromString(String text) {
        if (text == null) return CASH;
        for (PaymentMode m : PaymentMode.values()) {
            if (m.name().equalsIgnoreCase(text.trim())) {
                return m;
            }
        }
        return CASH;
    }
}
