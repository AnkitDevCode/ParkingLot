package com.ankitdevcode.example.parkinglot.payment.pricing;

import com.ankitdevcode.example.parkinglot.vehicles.VehicleType;

import java.time.LocalDateTime;

public interface PricingStrategy {

    double calculatePrice(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType vehicleType);
}