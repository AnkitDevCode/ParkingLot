package com.ankitdevcode.example.parkinglot.floor;

import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;
import com.ankitdevcode.example.parkinglot.vehicles.VehicleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingFloor {
    private final int floorNumber;
    private final Map<ParkingSpotType, List<ParkingSpot>> spotsByType;
    private final Map<String, ParkingSpot> spotsById;
    private final Map<ParkingSpotType, Integer> availableSpots;
    private final ReentrantLock floorLock = new ReentrantLock();

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spotsByType = new ConcurrentHashMap<>();
        this.spotsById = new ConcurrentHashMap<>();
        this.availableSpots = new ConcurrentHashMap<>();

        // Initialize maps
        for (ParkingSpotType type : ParkingSpotType.values()) {
            spotsByType.put(type, new ArrayList<>());
            availableSpots.put(type, 0);
        }
    }

    public void addParkingSpot(ParkingSpot spot) {
        floorLock.lock();
        try {
            spotsByType.get(spot.getType()).add(spot);
            spotsById.put(spot.getSpotId(), spot);
            if (spot.isAvailable()) {
                availableSpots.put(spot.getType(), availableSpots.get(spot.getType()) + 1);
            }
        } finally {
            floorLock.unlock();
        }
    }

    public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        List<ParkingSpotType> compatibleSpots = getCompatibleSpotTypes(vehicleType);

        for (ParkingSpotType spotType : compatibleSpots) {
            List<ParkingSpot> spots = spotsByType.get(spotType);
            for (ParkingSpot spot : spots) {
                if (spot.isAvailable()) {
                    return spot;
                }
            }
        }
        return null;
    }

    private List<ParkingSpotType> getCompatibleSpotTypes(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE ->
                    Arrays.asList(ParkingSpotType.MOTORCYCLE, ParkingSpotType.COMPACT, ParkingSpotType.LARGE);
            case COMPACT -> Arrays.asList(ParkingSpotType.COMPACT, ParkingSpotType.LARGE);
            case LARGE -> List.of(ParkingSpotType.LARGE);
            case ELECTRIC -> Arrays.asList(ParkingSpotType.ELECTRIC, ParkingSpotType.COMPACT, ParkingSpotType.LARGE);
        };
    }

    public void updateAvailableSpots(ParkingSpotType type, boolean isParking) {
        floorLock.lock();
        try {
            availableSpots.compute(type, (k, current) -> isParking ? current - 1 : current + 1);
        } finally {
            floorLock.unlock();
        }
    }

    public int getAvailableSpots(ParkingSpotType type) {
        return availableSpots.get(type);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public ParkingSpot getSpotById(String spotId) {
        return spotsById.get(spotId);
    }
}
