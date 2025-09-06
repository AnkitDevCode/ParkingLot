package com.ankitdevcode.example.parkinglot;

import com.ankitdevcode.example.parkinglot.payment.PaymentMethod;
import com.ankitdevcode.example.parkinglot.payment.pricing.FlatRatePricingStrategy;
import com.ankitdevcode.example.parkinglot.payment.pricing.HourlyPricingStrategy;
import com.ankitdevcode.example.parkinglot.spot.ParkingSpotType;
import com.ankitdevcode.example.parkinglot.ticket.ParkingTicket;
import com.ankitdevcode.example.parkinglot.vehicles.VehicleType;

import java.time.LocalDateTime;
import java.util.Map;

public class ParkingLotSystemDemo {
    public static void main(String[] args) {

        ParkingLot parkingLot = setupParkingLot();

        ParkingLotService service = new ParkingLotService(parkingLot);

        // Demonstrate different scenarios
        demonstrateBasicParking(service);
        demonstrateFullCapacity(service);
        demonstratePricingStrategies(service);
        demonstrateRevenueReport(service);
    }


    private static ParkingLot setupParkingLot() {
        return new ParkingLotBuilder()
                .setName("Smart Parking Complex")
                .setAddress("123 Main Street, Delhi")
                .setFloors(2)
                .addSpotConfiguration(ParkingSpotType.MOTORCYCLE, 5)  // Smaller numbers for demo
                .addSpotConfiguration(ParkingSpotType.COMPACT, 5)
                .addSpotConfiguration(ParkingSpotType.LARGE, 3)
                .addSpotConfiguration(ParkingSpotType.ELECTRIC, 2)
                .addSpotConfiguration(ParkingSpotType.HANDICAPPED, 2)
                .build();
    }

    private static void demonstrateBasicParking(ParkingLotService service) {
        System.out.println("\n=== Basic Parking Demonstration ===");
        printAvailability(service, "Initial availability");

        // Park different types of vehicles
        ParkingTicket motoTicket = service.parkVehicle(VehicleType.MOTORCYCLE, "BIKE001", "Black");
        ParkingTicket carTicket = service.parkVehicle(VehicleType.COMPACT, "CAR001", "Red");
        ParkingTicket teslaTicket = service.parkVehicle(VehicleType.ELECTRIC, "TESLA01", "White");

        printAvailability(service, "After parking 3 vehicles");

        // Exit one vehicle
        simulateTimePass(2000);
        if (carTicket != null) {
            System.out.println("\nExiting vehicle: " + carTicket.getLicensePlate());
            boolean exitSuccess = service.exitVehicle(carTicket.getTicketId(), PaymentMethod.CREDIT_CARD);
            System.out.println("Exit successful: " + exitSuccess);
            ParkingTicket completedTicket = service.getTicketInfo(carTicket.getTicketId());
            if (completedTicket.getPayment() != null) {
                System.out.printf("Payment Amount: $%.2f%n", completedTicket.getPayment().getAmount());
            }
        }
    }

    private static void demonstratePricingStrategies(ParkingLotService service) {
        System.out.println("\n=== Pricing Strategy Demonstration ===");

        // Test with different pricing strategies
        System.out.println("\nHourly Pricing:");
        service.switchPricingStrategy(new HourlyPricingStrategy());
        ParkingTicket ticket1 = service.parkVehicle(VehicleType.COMPACT, "HOUR001", "Blue");
        simulateTimePass(3000);
        service.exitVehicle(ticket1.getTicketId(), PaymentMethod.CREDIT_CARD);

        System.out.println("\nFlat Rate Pricing:");
        service.switchPricingStrategy(new FlatRatePricingStrategy());
        ParkingTicket ticket2 = service.parkVehicle(VehicleType.COMPACT, "FLAT001", "Green");
        simulateTimePass(3000);
        service.exitVehicle(ticket2.getTicketId(), PaymentMethod.MOBILE_PAYMENT);

        System.out.println("\nHourly Pricing (Peak Hours):");
        service.switchPricingStrategy(new HourlyPricingStrategy());
        ParkingTicket ticket3 = service.parkVehicle(VehicleType.COMPACT, "DYN001", "Yellow");
        simulateTimePass(3000);
        service.exitVehicle(ticket3.getTicketId(), PaymentMethod.CASH);
    }

    private static void demonstrateFullCapacity(ParkingLotService service) {
        System.out.println("\n=== Full Capacity Demonstration ===");
        // Try to park more vehicles than capacity
        for (int i = 1; i <= 7; i++) {
            ParkingTicket ticket = service.parkVehicle(VehicleType.MOTORCYCLE,
                    "MOTO" + i, "Black");
            if (ticket == null) {
                System.out.println("Parking full after " + (i - 1) + " motorcycles");
                break;
            }
        }
    }

    private static void demonstrateRevenueReport(ParkingLotService service) {
        System.out.println("\n=== Revenue Report Demonstration ===");
        LocalDateTime now = LocalDateTime.now();
        double todayRevenue = service.getRevenue(now.withHour(0), now);
        System.out.printf("Today's Revenue: $%.2f%n", todayRevenue);
    }

    private static void printAvailability(ParkingLotService service, String message) {
        System.out.println("\n" + message + ":");
        Map<ParkingSpotType, Integer> availability = service.getAvailability();
        availability.forEach((type, count) ->
                System.out.println(type + ": " + count + " spots available"));
    }

    private static void simulateTimePass(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}