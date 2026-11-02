package com.foodease.model;

import com.foodease.enums.OrderStatus;
import com.foodease.enums.PaymentMode;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OOP COMPOSITION & ASSOCIATION:
 * Order contains multiple OrderItem objects (Composition).
 * Order is associated with a Customer.
 * Encapsulates full financial calculation (Subtotal, Discount, GST, Total).
 */
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    private String id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Snapshot of delivery details at order placement
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @Column(name = "customer_address", length = 500, nullable = false)
    private String customerAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;

    @Column(nullable = false)
    private double subtotal;

    @Column(name = "discount_percentage")
    private double discountPercentage;

    private double discount;

    @Column(name = "gst_percentage")
    private double gstPercentage;

    private double gst;

    @Column(nullable = false)
    private double total;

    @Column(name = "coupon_code")
    private String couponCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.RECEIVED;

    public Order() {
    }

    public Order(String id, Customer customer, PaymentMode paymentMode) {
        this.id = id;
        this.customer = customer;
        if (customer != null) {
            this.customerName = customer.getName();
            this.customerPhone = customer.getPhone();
            this.customerAddress = customer.getDeliveryAddress();
        }
        this.paymentMode = paymentMode;
        this.status = OrderStatus.RECEIVED;
    }

    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    // Encapsulated financial calculation domain method
    public void calculateFinancials(double discountRate, double gstRate) {
        this.subtotal = items.stream().mapToDouble(OrderItem::getLineTotal).sum();
        this.discountPercentage = discountRate;
        this.discount = Math.round((subtotal * (discountRate / 100.0)) * 100.0) / 100.0;
        double taxableAmount = Math.max(0.0, subtotal - discount);
        this.gstPercentage = gstRate;
        this.gst = Math.round((taxableAmount * (gstRate / 100.0)) * 100.0) / 100.0;
        this.total = Math.round((taxableAmount + gst) * 100.0) / 100.0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerName = customer.getName();
            this.customerPhone = customer.getPhone();
            this.customerAddress = customer.getDeliveryAddress();
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getGstPercentage() {
        return gstPercentage;
    }

    public void setGstPercentage(double gstPercentage) {
        this.gstPercentage = gstPercentage;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
