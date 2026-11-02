package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ConfigDTO {

    @JsonProperty("gst_rate")
    private double gstRate;

    private List<String> coupons;

    public ConfigDTO() {
    }

    public ConfigDTO(double gstRate, List<String> coupons) {
        this.gstRate = gstRate;
        this.coupons = coupons;
    }

    public double getGstRate() { return gstRate; }
    public void setGstRate(double gstRate) { this.gstRate = gstRate; }
    public List<String> getCoupons() { return coupons; }
    public void setCoupons(List<String> coupons) { this.coupons = coupons; }
}
