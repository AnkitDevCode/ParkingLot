package com.ankitdevcode.example.parkinglot.payment.pricing;

import com.ankitdevcode.example.parkinglot.vehicles.VehicleType;

import java.time.LocalDateTime;

public class FlatRatePricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType vehicleType) {
        return 15.0;
    }
}