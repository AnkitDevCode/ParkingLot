package com.ankitdevcode.example.parkinglot;

import com.ankitdevcode.example.parkinglot.floor.ParkingFloor;
import com.ankitdevcode.example.parkinglot.observer.DisplayBoard;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;

import java.util.HashMap;
import java.util.Map;

class ParkingLotBuilder {
    private String name;
    private String address;
    private int floors;
    private final Map<ParkingSpotType, Integer> spotsPerFloor;

    public ParkingLotBuilder() {
        spotsPerFloor = new HashMap<>();
    }

    public ParkingLotBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ParkingLotBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public ParkingLotBuilder setFloors(int floors) {
        this.floors = floors;
        return this;
    }

    public ParkingLotBuilder addSpotConfiguration(ParkingSpotType type, int count) {
        spotsPerFloor.put(type, count);
        return this;
    }

    public ParkingLot build() {
        ParkingLot parkingLot = ParkingLot.getInstance(name, address);

        for (int floor = 0; floor < floors; floor++) {
            ParkingFloor parkingFloor = new ParkingFloor(floor);

            int spotCounter = 1;
            for (Map.Entry<ParkingSpotType, Integer> entry : spotsPerFloor.entrySet()) {
                ParkingSpotType type = entry.getKey();
                int count = entry.getValue();

                for (int i = 0; i < count; i++) {
                    String spotId = "F" + floor + "R" + (spotCounter / 10) + "S" + spotCounter;
                    ParkingSpot spot = new ParkingSpot(spotId, type, floor, spotCounter / 10, spotCounter);
                    parkingFloor.addParkingSpot(spot);
                    spotCounter++;
                }
            }

            parkingLot.addFloor(parkingFloor);
            parkingLot.addObserver(new DisplayBoard(floor));
        }

        return parkingLot;
    }
}