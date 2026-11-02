package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull(message = "Customer information is required")
    @Valid
    private CustomerInfoDTO customer;

    @JsonProperty("coupon_code")
    private String couponCode;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(CustomerInfoDTO customer, String couponCode) {
        this.customer = customer;
        this.couponCode = couponCode;
    }

    public CustomerInfoDTO getCustomer() { return customer; }
    public void setCustomer(CustomerInfoDTO customer) { this.customer = customer; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
}
