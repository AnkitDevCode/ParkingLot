package com.ankitdevcode.example.parkinglot.vehicles;

import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;

public final class Car extends Vehicle {

    public Car(String licensePlate, String color) {
        super(licensePlate, VehicleType.COMPACT, color);
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getType() == ParkingSpotType.COMPACT ||
                spot.getType() == ParkingSpotType.LARGE ||
                spot.getType() == ParkingSpotType.ELECTRIC;
    }
}