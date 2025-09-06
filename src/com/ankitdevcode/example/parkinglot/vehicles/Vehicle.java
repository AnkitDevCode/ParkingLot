package com.ankitdevcode.example.parkinglot.vehicles;

import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import lombok.Getter;

import java.util.Objects;

@Getter
public abstract sealed class Vehicle permits Motorcycle, Car, ElectricCar, Truck {
    protected final String licensePlate;
    protected final VehicleType type;
    protected final String color;

    public Vehicle(String licensePlate, VehicleType type, String color) {
        this.licensePlate = Objects.requireNonNull(licensePlate);
        this.type = Objects.requireNonNull(type);
        this.color = Objects.requireNonNull(color);
    }

    public abstract boolean canFitInSpot(ParkingSpot spot);
}

