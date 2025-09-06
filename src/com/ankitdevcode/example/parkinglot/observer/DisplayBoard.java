package com.ankitdevcode.example.parkinglot.observer;

import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.vehicles.Vehicle;

public record DisplayBoard(int floorNumber) implements ParkingObserver {

    @Override
    public void onSpotOccupied(ParkingSpot spot, Vehicle vehicle) {
        if (spot.getFloor() == floorNumber) {
            System.out.println("Display Board Floor " + floorNumber + ": Spot " +
                    spot.getSpotId() + " occupied by " + vehicle.getLicensePlate());
        }
    }

    @Override
    public void onSpotFreed(ParkingSpot spot) {
        if (spot.getFloor() == floorNumber) {
            System.out.println("Display Board Floor " + floorNumber + ": Spot " +
                    spot.getSpotId() + " is now available");
        }
    }

    @Override
    public void onParkingLotFull() {
        System.out.println("Display Board Floor " + floorNumber + ": PARKING LOT FULL");
    }
}