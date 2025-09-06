package com.ankitdevcode.example.parkinglot.spot;

import lombok.Getter;

@Getter
public enum ParkingSpotType {

    MOTORCYCLE(1), COMPACT(1), LARGE(2), ELECTRIC(1), HANDICAPPED(1);

    private final int size;

    ParkingSpotType(int size) {
        this.size = size;
    }

}