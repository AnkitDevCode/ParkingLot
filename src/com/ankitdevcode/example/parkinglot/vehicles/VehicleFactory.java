package com.ankitdevcode.example.parkinglot.vehicles;

public final class VehicleFactory {

    private VehicleFactory() {
        // Prevent instantiation
    }

    public static Vehicle createVehicle(VehicleType type, String licensePlate, String color) {
        return switch (type) {
            case MOTORCYCLE -> new Motorcycle(licensePlate, color);
            case COMPACT -> new Car(licensePlate, color);
            case LARGE -> new Truck(licensePlate, color);
            case ELECTRIC -> new ElectricCar(licensePlate, color);
        };
    }
}