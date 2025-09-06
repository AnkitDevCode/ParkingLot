package com.ankitdevcode.example.parkinglot.payment.pricing;

import com.ankitdevcode.example.parkinglot.vehicles.VehicleType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy {

    private final Map<VehicleType, Double> hourlyRates;

    public HourlyPricingStrategy() {
        hourlyRates = new HashMap<>();
        hourlyRates.put(VehicleType.MOTORCYCLE, 2.0);
        hourlyRates.put(VehicleType.COMPACT, 5.0);
        hourlyRates.put(VehicleType.LARGE, 8.0);
        hourlyRates.put(VehicleType.ELECTRIC, 4.0);
    }

    @Override
    public double calculatePrice(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType vehicleType) {
        long minutes = java.time.Duration.between(entryTime, exitTime).toMinutes();
        double hours = Math.ceil(minutes / 60.0);
        return hours * hourlyRates.get(vehicleType);
    }
}