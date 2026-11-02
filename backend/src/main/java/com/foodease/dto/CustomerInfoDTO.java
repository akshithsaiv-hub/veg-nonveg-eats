package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CustomerInfoDTO {

    @NotBlank(message = "Customer name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Delivery address is required")
    private String address;

    @JsonProperty("payment_mode")
    private String paymentMode;

    public CustomerInfoDTO() {
    }

    public CustomerInfoDTO(String name, String phone, String address, String paymentMode) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentMode = paymentMode;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
}
