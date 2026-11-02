package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodease.model.Coupon;

public class CouponInfoDTO {
    private String code;
    private double percentage;

    @JsonProperty("min_order")
    private double minOrder;

    private String label;

    public CouponInfoDTO() {
    }

    public CouponInfoDTO(Coupon c) {
        this.code = c.getCode();
        this.percentage = c.getPercentage();
        this.minOrder = c.getMinOrder();
        this.label = c.getLabel();
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    public double getMinOrder() { return minOrder; }
    public void setMinOrder(double minOrder) { this.minOrder = minOrder; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
