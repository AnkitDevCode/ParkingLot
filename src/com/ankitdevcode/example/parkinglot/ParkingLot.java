package com.ankitdevcode.example.parkinglot;

import com.ankitdevcode.example.parkinglot.floor.ParkingFloor;
import com.ankitdevcode.example.parkinglot.observer.ParkingObserver;
import com.ankitdevcode.example.parkinglot.payment.DefaultPaymentProcessor;
import com.ankitdevcode.example.parkinglot.payment.Payment;
import com.ankitdevcode.example.parkinglot.payment.PaymentMethod;
import com.ankitdevcode.example.parkinglot.payment.PaymentProcessor;
import com.ankitdevcode.example.parkinglot.payment.pricing.HourlyPricingStrategy;
import com.ankitdevcode.example.parkinglot.payment.pricing.PricingStrategy;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpot;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;
import com.ankitdevcode.example.parkinglot.ticket.ParkingTicket;
import com.ankitdevcode.example.parkinglot.vehicles.Vehicle;
import com.ankitdevcode.example.parkinglot.vehicles.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Getter
class ParkingLot {
    private static ParkingLot instance; // Singleton Pattern
    private static final ReentrantLock instanceLock = new ReentrantLock();
    private final String name;
    private final String address;
    private final List<ParkingFloor> floors;
    private final Map<String, ParkingTicket> activeTickets;
    private final Map<String, ParkingTicket> ticketHistory;
    private final List<ParkingObserver> observers;
    @Setter
    private PricingStrategy pricingStrategy;
    private final PaymentProcessor paymentProcessor;
    private final ReentrantLock parkingLock = new ReentrantLock();

    private ParkingLot(String name, String address) {
        this.name = name;
        this.address = address;
        this.floors = new ArrayList<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.ticketHistory = new ConcurrentHashMap<>();
        this.observers = new ArrayList<>();
        this.pricingStrategy = new HourlyPricingStrategy();
        this.paymentProcessor = new DefaultPaymentProcessor();
    }

    // Singleton Pattern Implementation
    public static ParkingLot getInstance(String name, String address) {
        if (instance == null) {
            instanceLock.lock();
            try {
                if (instance == null) {
                    instance = new ParkingLot(name, address);
                }
            } finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void addObserver(ParkingObserver observer) {
        observers.add(observer);
    }

    // Core parking functionality
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        parkingLock.lock();
        try {
            ParkingSpot spot = findAvailableSpot(vehicle.getType());
            if (spot == null) {
                notifyObservers(ParkingObserver::onParkingLotFull);
                return null;
            }

            if (!spot.parkVehicle(vehicle)) {
                return null;
            }

            String ticketId = generateTicketId();
            ParkingTicket ticket = new ParkingTicket(ticketId, vehicle.getLicensePlate(), spot);
            activeTickets.put(ticketId, ticket);

            // Update floor statistics
            ParkingFloor floor = floors.get(spot.getFloor());
            floor.updateAvailableSpots(spot.getType(), true);

            // Notify observers
            notifyObservers(obs -> obs.onSpotOccupied(spot, vehicle));

            return ticket;
        } finally {
            parkingLock.unlock();
        }
    }

    public boolean unparkVehicle(String ticketId, PaymentMethod paymentMethod) {
        parkingLock.lock();
        try {
            ParkingTicket ticket = activeTickets.get(ticketId);
            if (ticket == null || !ticket.isActive()) {
                return false;
            }

            // Calculate payment
            double amount = pricingStrategy.calculatePrice(
                    ticket.getEntryTime(),
                    LocalDateTime.now(),
                    ticket.getAssignedSpot().getParkedVehicle().getType()
            );

            Payment payment = new Payment(
                    generatePaymentId(),
                    amount,
                    paymentMethod
            );

            if (paymentProcessor.processPayment(payment)) {
                return false;
            }

            // Complete parking process
            ParkingSpot spot = ticket.getAssignedSpot();
            spot.removeVehicle();
            ticket.completeParking(payment);

            // Update records
            activeTickets.remove(ticketId);
            ticketHistory.put(ticketId, ticket);

            // Update floor statistics
            ParkingFloor floor = floors.get(spot.getFloor());
            floor.updateAvailableSpots(spot.getType(), false);

            // Notify observers
            notifyObservers(obs -> obs.onSpotFreed(spot));

            return true;
        } finally {
            parkingLock.unlock();
        }
    }

    private ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(vehicleType);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }

    private void notifyObservers(java.util.function.Consumer<ParkingObserver> action) {
        for (ParkingObserver observer : observers) {
            action.accept(observer);
        }
    }

    // Utility methods
    private String generateTicketId() {
        return "TKT" + System.currentTimeMillis() + "_" + new Random().nextInt(1000);
    }

    private String generatePaymentId() {
        return "PAY" + System.currentTimeMillis() + "_" + new Random().nextInt(1000);
    }

    // Administrative methods
    public ParkingTicket getTicket(String ticketId) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        return ticket != null ? ticket : ticketHistory.get(ticketId);
    }

    public Map<ParkingSpotType, Integer> getAvailabilityByType() {
        Map<ParkingSpotType, Integer> totalAvailable = new HashMap<>();
        for (ParkingSpotType type : ParkingSpotType.values()) {
            totalAvailable.put(type, 0);
        }

        for (ParkingFloor floor : floors) {
            for (ParkingSpotType type : ParkingSpotType.values()) {
                totalAvailable.put(type,
                        totalAvailable.get(type) + floor.getAvailableSpots(type));
            }
        }
        return totalAvailable;
    }

    public double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return ticketHistory.values().stream()
                .filter(ticket -> ticket.getExitTime() != null)
                .filter(ticket -> ticket.getExitTime().isAfter(startDate) &&
                        ticket.getExitTime().isBefore(endDate))
                .mapToDouble(ticket -> ticket.getPayment().getAmount())
                .sum();
    }
}
