package com.ankitdevcode.example.parkinglot.observer;

import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.vehicles.Vehicle;

public interface ParkingObserver {

    void onSpotOccupied(ParkingSpot spot, Vehicle vehicle);

    void onSpotFreed(ParkingSpot spot);

    void onParkingLotFull();
}