package com.foodease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class CouponValidateRequest {

    @NotBlank(message = "Coupon code is required")
    private String code;

    @PositiveOrZero
    private double subtotal;

    public CouponValidateRequest() {
    }

    public CouponValidateRequest(String code, double subtotal) {
        this.code = code;
        this.subtotal = subtotal;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
