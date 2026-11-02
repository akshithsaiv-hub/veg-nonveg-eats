package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponValidateResponse {
    private boolean valid;
    private String message;
    private String code;
    private Double percentage;
    private Double discount;

    public CouponValidateResponse() {
    }

    public CouponValidateResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public CouponValidateResponse(boolean valid, String message, String code, Double percentage, Double discount) {
        this.valid = valid;
        this.message = message;
        this.code = code;
        this.percentage = percentage;
        this.discount = discount;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
}
