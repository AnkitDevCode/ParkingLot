package com.ankitdevcode.example.parkinglot.payment;

public interface PaymentProcessor {
    boolean processPayment(Payment payment);

    PaymentStatus checkStatus(String paymentId);

    void refund(String paymentId);
}