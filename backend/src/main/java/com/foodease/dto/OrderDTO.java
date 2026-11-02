package com.foodease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodease.model.Order;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {

    private String id;
    private List<OrderItemViewDTO> items;
    private CustomerInfoDTO customer;
    private double subtotal;

    @JsonProperty("discount_percentage")
    private double discountPercentage;

    private double discount;

    @JsonProperty("gst_percentage")
    private double gstPercentage;

    private double gst;
    private double total;

    @JsonProperty("coupon_code")
    private String couponCode;

    private String status;

    @JsonProperty("created_at")
    private String createdAt;

    public OrderDTO() {
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.items = order.getItems().stream().map(OrderItemViewDTO::new).collect(Collectors.toList());
        this.customer = new CustomerInfoDTO(
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getCustomerAddress(),
                order.getPaymentMode() != null ? order.getPaymentMode().name().toLowerCase() : "cash"
        );
        this.subtotal = order.getSubtotal();
        this.discountPercentage = order.getDiscountPercentage();
        this.discount = order.getDiscount();
        this.gstPercentage = order.getGstPercentage();
        this.gst = order.getGst();
        this.total = order.getTotal();
        this.couponCode = order.getCouponCode();
        this.status = order.getStatus() != null ? order.getStatus().name().toLowerCase() : "received";
        this.createdAt = order.getCreatedAt() != null ?
                order.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public List<OrderItemViewDTO> getItems() { return items; }
    public void setItems(List<OrderItemViewDTO> items) { this.items = items; }
    public CustomerInfoDTO getCustomer() { return customer; }
    public void setCustomer(CustomerInfoDTO customer) { this.customer = customer; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public double getGstPercentage() { return gstPercentage; }
    public void setGstPercentage(double gstPercentage) { this.gstPercentage = gstPercentage; }
    public double getGst() { return gst; }
    public void setGst(double gst) { this.gst = gst; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
