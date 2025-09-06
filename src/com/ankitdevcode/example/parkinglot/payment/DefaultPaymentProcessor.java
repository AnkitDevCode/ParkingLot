package com.ankitdevcode.example.parkinglot.payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPaymentProcessor implements PaymentProcessor {

    private final Map<String, Payment> payments = new ConcurrentHashMap<>();

    @Override
    public boolean processPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        payments.put(payment.getPaymentId(), payment);
        // Simulate payment processing
        return Math.random() > 0.1;
    }

    @Override
    public PaymentStatus checkStatus(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("Payment ID cannot be null or blank");
        }
        Payment payment = payments.get(paymentId);

        if (payment == null) {
            throw new IllegalArgumentException("Payment not found: " + paymentId);
        }
        return payment.getStatus();
    }

    @Override
    public void refund(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("Payment ID cannot be null or blank");
        }
        Payment payment = payments.get(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found: " + paymentId);
        }
        // Implement refund logic here
    }
}