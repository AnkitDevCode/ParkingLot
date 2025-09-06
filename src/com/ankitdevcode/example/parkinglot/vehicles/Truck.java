package com.ankitdevcode.example.parkinglot.vehicles;

import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;


public final class Truck extends Vehicle {
    public Truck(String licensePlate, String color) {
        super(licensePlate, VehicleType.LARGE, color);
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getType() == ParkingSpotType.LARGE;
    }
}