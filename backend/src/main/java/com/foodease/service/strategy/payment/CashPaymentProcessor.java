package com.foodease.service.strategy.payment;

import com.foodease.model.Order;
import org.springframework.stereotype.Component;

@Component
public class CashPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Order order) {
        // Cash on delivery - pending till physical delivery
        return true;
    }

    @Override
    public String getProcessorName() {
        return "CASH_ON_DELIVERY";
    }
}
