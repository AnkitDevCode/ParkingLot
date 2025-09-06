package com.ankitdevcode.example.parkinglot.payment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Payment {
    private final String paymentId;
    private final double amount;
    private final PaymentMethod method;
    private final PaymentStatus status;
    private final LocalDateTime timestamp;

    public Payment(String paymentId, double amount, PaymentMethod method) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.timestamp = LocalDateTime.now();
    }
}


