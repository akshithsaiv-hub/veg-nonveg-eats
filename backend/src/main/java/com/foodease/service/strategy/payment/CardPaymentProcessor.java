package com.foodease.service.strategy.payment;

import com.foodease.model.Order;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Order order) {
        // Simulates PCI-compliant Credit/Debit card transaction
        return true;
    }

    @Override
    public String getProcessorName() {
        return "CARD_MERCHANT_GATEWAY";
    }
}
