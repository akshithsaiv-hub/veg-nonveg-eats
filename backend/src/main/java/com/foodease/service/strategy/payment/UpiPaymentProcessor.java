package com.foodease.service.strategy.payment;

import com.foodease.model.Order;
import org.springframework.stereotype.Component;

@Component
public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Order order) {
        // Simulates UPI QR / VPA transaction settlement
        return true;
    }

    @Override
    public String getProcessorName() {
        return "UPI_INSTANT_GATEWAY";
    }
}
