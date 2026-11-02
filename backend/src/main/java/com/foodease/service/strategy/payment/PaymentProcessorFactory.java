package com.foodease.service.strategy.payment;

import com.foodease.enums.PaymentMode;
import org.springframework.stereotype.Component;

/**
 * Factory pattern demonstrating runtime polymorphism for payment processing.
 */
@Component
public class PaymentProcessorFactory {

    private final UpiPaymentProcessor upiProcessor;
    private final CardPaymentProcessor cardProcessor;
    private final CashPaymentProcessor cashProcessor;

    public PaymentProcessorFactory(UpiPaymentProcessor upiProcessor,
                                   CardPaymentProcessor cardProcessor,
                                   CashPaymentProcessor cashProcessor) {
        this.upiProcessor = upiProcessor;
        this.cardProcessor = cardProcessor;
        this.cashProcessor = cashProcessor;
    }

    public PaymentProcessor getProcessor(PaymentMode mode) {
        if (mode == null) return cashProcessor;
        return switch (mode) {
            case UPI -> upiProcessor;
            case CARD -> cardProcessor;
            case CASH -> cashProcessor;
        };
    }
}
