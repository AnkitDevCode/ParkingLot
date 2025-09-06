package com.ankitdevcode.example.parkinglot.vehicles;

import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;


public final class ElectricCar extends Vehicle {
    public ElectricCar(String licensePlate, String color) {
        super(licensePlate, VehicleType.ELECTRIC, color);
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getType() == ParkingSpotType.ELECTRIC ||
                spot.getType() == ParkingSpotType.COMPACT ||
                spot.getType() == ParkingSpotType.LARGE;
    }
}
