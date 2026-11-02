package com.foodease.model;

import com.foodease.enums.PaymentMode;
import com.foodease.enums.UserRole;
import jakarta.persistence.*;

/**
 * OOP INHERITANCE & POLYMORPHISM:
 * Derived class extending User (IS-A relationship).
 * Encapsulates customer-specific delivery attributes and overrides dashboard access.
 */
@Entity
@Table(name = "customers")
public class Customer extends User {

    @Column(nullable = false)
    private String phone;

    @Column(length = 500)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private PaymentMode preferredPaymentMode;

    public Customer() {
        super();
        setRole(UserRole.CUSTOMER);
    }

    // Overloaded Constructor (OOP Polymorphism)
    public Customer(String name, String phone, String deliveryAddress) {
        super(name, null, UserRole.CUSTOMER);
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.preferredPaymentMode = PaymentMode.UPI;
    }

    public Customer(String name, String email, String phone, String deliveryAddress, PaymentMode preferredPaymentMode) {
        super(name, email, UserRole.CUSTOMER);
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.preferredPaymentMode = preferredPaymentMode;
    }

    // Method Overriding (OOP Polymorphism)
    @Override
    public String getDashboardAccess() {
        return "CUSTOMER_PORTAL_VIEW";
    }

    // Encapsulated Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone number is required");
        }
        this.phone = phone.trim();
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PaymentMode getPreferredPaymentMode() {
        return preferredPaymentMode;
    }

    public void setPreferredPaymentMode(PaymentMode preferredPaymentMode) {
        this.preferredPaymentMode = preferredPaymentMode;
    }
}
