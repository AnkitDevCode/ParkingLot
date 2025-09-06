package com.ankitdevcode.example.parkinglot.ticket;

import com.ankitdevcode.example.parkinglot.payment.Payment;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ParkingTicket {
    private final String ticketId;
    private final String licensePlate;
    private final ParkingSpot assignedSpot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Payment payment;
    private boolean isActive;

    public ParkingTicket(String ticketId, String licensePlate, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.licensePlate = licensePlate;
        this.assignedSpot = spot;
        this.entryTime = LocalDateTime.now();
        this.isActive = true;
    }

    public void completeParking(Payment payment) {
        this.exitTime = LocalDateTime.now();
        this.payment = payment;
        this.isActive = false;
    }
}