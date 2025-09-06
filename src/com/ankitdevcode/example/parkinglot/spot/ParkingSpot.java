package com.ankitdevcode.example.parkinglot.spot;

import com.ankitdevcode.example.parkinglot.vehicles.Vehicle;
import lombok.Getter;

import java.util.concurrent.locks.ReentrantLock;

@Getter
public class ParkingSpot {
    private final String spotId;
    private final ParkingSpotType type;
    private final int floor;
    private final int row;
    private final int number;
    private ParkingSpotStatus status;
    private Vehicle parkedVehicle;
    private final ReentrantLock lock = new ReentrantLock();

    public ParkingSpot(String spotId, ParkingSpotType type, int floor, int row, int number) {
        this.spotId = spotId;
        this.type = type;
        this.floor = floor;
        this.row = row;
        this.number = number;
        this.status = ParkingSpotStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        lock.lock();
        try {
            return status == ParkingSpotStatus.AVAILABLE && parkedVehicle == null;
        } finally {
            lock.unlock();
        }
    }

    public boolean parkVehicle(Vehicle vehicle) {
        lock.lock();
        try {
            if (!isAvailable() || !vehicle.canFitInSpot(this)) {
                return false;
            }
            this.parkedVehicle = vehicle;
            this.status = ParkingSpotStatus.OCCUPIED;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Vehicle removeVehicle() {
        lock.lock();
        try {
            Vehicle vehicle = this.parkedVehicle;
            this.parkedVehicle = null;
            this.status = ParkingSpotStatus.AVAILABLE;
            return vehicle;
        } finally {
            lock.unlock();
        }
    }

    public void setStatus(ParkingSpotStatus status) {
        lock.lock();
        try {
            this.status = status;
        } finally {
            lock.unlock();
        }
    }
}

