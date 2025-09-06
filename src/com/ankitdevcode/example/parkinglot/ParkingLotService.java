package com.ankitdevcode.example.parkinglot;

import com.ankitdevcode.example.parkinglot.payment.PaymentMethod;
import com.ankitdevcode.example.parkinglot.payment.pricing.PricingStrategy;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;
import com.ankitdevcode.example.parkinglot.ticket.ParkingTicket;
import com.ankitdevcode.example.parkinglot.vehicles.Vehicle;
import com.ankitdevcode.example.parkinglot.vehicles.VehicleFactory;
import com.ankitdevcode.example.parkinglot.vehicles.VehicleType;

import java.time.LocalDateTime;
import java.util.Map;

record ParkingLotService(ParkingLot parkingLot) {

    public ParkingTicket parkVehicle(VehicleType vehicleType, String licensePlate, String color) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("VehicleType cannot be null");
        }
        Vehicle vehicle = VehicleFactory.createVehicle(vehicleType, licensePlate, color);
        return parkingLot.parkVehicle(vehicle);
    }

    public boolean exitVehicle(String ticketId, PaymentMethod paymentMethod) {
        return parkingLot.unparkVehicle(ticketId, paymentMethod);
    }

    public ParkingTicket getTicketInfo(String ticketId) {
        return parkingLot.getTicket(ticketId);
    }

    public Map<ParkingSpotType, Integer> getAvailability() {
        return parkingLot.getAvailabilityByType();
    }

    public double getRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return parkingLot.calculateRevenue(startDate, endDate);
    }

    public void switchPricingStrategy(PricingStrategy newStrategy) {
        parkingLot.setPricingStrategy(newStrategy);
    }
}