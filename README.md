# Parking Lot System Design

## What is Low-Level Design (LLD)?

**Low-Level Design (LLD)**  is the process of translating High-Level Design (HLD) into concrete implementation (like class diagrams, interfaces, object relationships, and design patterns) that can be directly implemented in code.

## [](https://dev.to/ankitdevcode/parking-lot-system-design-lld-in-action-part-1-6f0#core-components-of-lld)Core Components of LLD

You can find a more in-depth information of this concept in my earlier post  [Object-oriented programming System(OOPs)](https://dev.to/jungledev/object-oriented-programming-systemoops-3eip)

-   Classes and Objects
-   Interfaces and Abstractions
-   Relationships Between Classes

## [](https://dev.to/ankitdevcode/parking-lot-system-design-lld-in-action-part-1-6f0#key-considerations-for-lld)Key Considerations for LLD

-   Scope & Requirements
-   Clear Data Models
-   Core Design Principles (SOLID & Beyond)
-   System Robustness & Scalability
-   Design Patterns & Reusability

## [](https://dev.to/ankitdevcode/parking-lot-system-design-lld-in-action-part-1-6f0#common-pitfalls-to-avoid)Common Pitfalls to Avoid

-   God classes that handle too many responsibilities.
-   Inconsistent Data Models
-   Overuse of inheritance, leading to fragile hierarchies.
-   Ignoring non-functional requirements until it’s too late.
-   Overengineering: Don’t use complex patterns unless justified—keep it lean.

## [](https://dev.to/ankitdevcode/parking-lot-system-design-lld-in-action-part-1-6f0#parking-lot-system)Parking Lot System

**Problem Statement**

Design a parking lot management system for a multi-story parking garage that can handle different types of vehicles, dynamic pricing, real-time availability tracking.

**Focus:**  Low-Level Design (LLD), Object-Oriented Programming, System Architecture


### Essential Questions to Ask:

-   What types of vehicles should the system support?
-   How many floors will the parking lot have?
-   What are the different parking spot sizes?
-   How should pricing work - hourly, flat rate, or both?
-   Do we need real-time availability tracking?
-   Should the system handle reservations?
-   What payment methods are supported?
-   Do we need administrative features?

**❌ Red Flags - Poor Requirements Gathering**

-   Jumping straight to code without understanding requirements
-   Making assumptions without asking questions
-   Not clarifying scope - building too much or too little
-   Ignoring edge cases early in the discussion

----------

## [](https://dev.to/ankitdevcode/parking-lot-system-design-lld-in-action-part-1-6f0#how-to-approach-the-problem-statement-)How to approach the problem statement ?

When approaching system design, there are two common strategies:

**Top-down approach**  → Start with the big picture. Break the larger problem into smaller, manageable parts, and then continue refining each part step by step.

-   Recommended during the analysis and design phase.
-   Starts with the big picture of the system.
-   Breaks the system into smaller, manageable sub-systems step by step.
-   Focus is on defining responsibilities and interactions at higher levels before going into details.
-   Useful for requirements gathering and system architecture design.

**Bottom-up approach**  → Start small. Focus first on solving the smaller, individual problems, and then integrate them together to form the complete solution.

-   Recommended during the implementation phase.
-   Uses structure diagrams (e.g., class diagrams) to represent system components.
-   Focuses on breaking the problem statement into smaller components without deep details initially.
-   Behavior diagrams (e.g., sequence diagrams) are used alongside to show how the system functions overall.
-   Helps in understanding both the structure and the functionality of the system in its early stages.



### Good Approach - Identify Core Components

```
+---------------------+       1..* +---------------------+       1..* +---------------------+
|      ParkingLot     |<>----------|    ParkingFloor     |<>----------|    ParkingSpot      |
+---------------------+            +---------------------+            +---------------------+
| - id: String        |            | - floorNumber: int  |            | - spotId: String    |
| - levels: List      |            | - spots: Map        |            | - isOccupied: bool  |
| - gates: Map        |            | - freeSpots: Map    |            | - vehicle: Vehicle  |
| - tickets: Map      |            +---------------------+            | - type: SpotType    |
+---------------------+            | + findSpot(vType)   |            +---------------------+
| + parkVehicle()     |            | + parkVehicle(v)    |                  |
| + unparkVehicle()   |            | + unparkVehicle(t)  |                  | 1
| + processPayment()  |            | + getFreeSpots()    |                  |
+---------------------+            | + notifyObserver()  |                  |
                                   +---------------------+                  |
                                                                            | 1..*
+-----------------+     +-----------------+     +-----------------+         |
|      Vehicle    |     |   ParkingTicket |     |      Payment    |         |
+-----------------+     +-----------------+     +-----------------+         |
| - license: String |   | - ticketId: int |     | - amount: double|         |
| - type: VehicleType | | - entryTime: Date |   | - method: String|         |
+-----------------+     | - spot: ParkingSpot|  | - status: String|         |
| + getType()     |     +-----------------+     +-----------------+         |
+-----------------+     | + getDuration() |                                 |
                        +-----------------+                                 |
                                                                            |
                                                                            |
                                                                            |
+------------------+     +-----------------------+                          |
|   PaymentProcessor|<-----|  DefaultPaymentProcessor|                      |
+------------------+     +-----------------------+                          |
| + process() |                                                             |
+------------------+                                                        |
                                                                            |
+------------------+     +-----------------------+                          |
|   PricingStrategy|<-----|  HourlyPricingStrategy|                         |
+------------------+     +-----------------------+                          |
| + calculateFee() |                                                        |
+------------------+                                                        |
                                                                            |
+------------------+                                                        |
|  ParkingObserver |<------------------------------------------------------+
+------------------+
| + update()       |
+------------------+ 

```

----------

**Parking Lot LLD – Component Summary**

**1. ParkingLot**

The central manager of the entire parking system.

**Responsibilities:**

-   Keeps track of multiple floors.
-   Handles parking and unparking.
-   Interacts with Payment and PricingStrategy.

Key Methods:

`parkVehicle(vehicle):`  Finds an available spot and issues a ParkingTicket  
`unparkVehicle(ticket):`  Frees the spot and processes payment  
`processPayment(ticket, paymentMethod):`  Calculates fee and completes payment

**2. ParkingFloor**

Manages spots on a single floor.

Responsibilities:

-   Keeps track of all parking spots on the floor.
-   Notifies observers when a spot becomes occupied or free.

Key Methods:

`notifyObserver():`  Updates DisplayBoard or other systems  
`getAvailableSpot(vehicleType):`  Returns a free spot suitable for the vehicle

**3. ParkingSpot**

Represents a single parking slot.

Attributes:

`spotNumber, spotType (COMPACT, LARGE, ELECTRIC, HANDICAPPED), isOccupied, vehicle`

Methods:

`assignVehicle(vehicle)`  
`removeVehicle()`  
`isOccupied()`

**4. ParkingTicket**

Tracks a vehicle’s parking session.

Attributes:

`ticketId, vehicle, spot, entryTime, exitTime`

Methods:

`getDuration(): Time elapsed since entry`

**5. Payment & Processor**

Handles payment processing.

Attributes:

`amount, method (CASH, CARD), status (PENDING, COMPLETED)`

`PaymentProcessor`

Methods:

`process(): Completes the payment`

**6. PricingStrategy**

Defines how fees are calculated.

`calculateFee(duration): returns fee`

Example Implementation:

`HourlyPricingStrategy, FlatRatePricingStrategy`

**7. ParkingObserver**

Used to update displays or other systems when a spot changes.

`update(): Called when spot becomes free/occupied`

**Example**: DisplayBoard implements ParkingObserver to show available spots.

----------

### [](https://dev.to/ankitdevcode/parking-lot-system-design-lld-in-action-part-1-6f0#uml-diagram-analysis-parking-lot-system)UML Diagram Analysis - Parking Lot System

The UML class diagram illustrates a comprehensive parking lot management system that demonstrates multiple design patterns and object-oriented principles. Let me break down each section:

Please find full diagram on  [kroki](https://kroki.io/mermaid/svg/eNrNWdtu4zYQfe9XCCkWkDd2gL4awQKKbG8MyLFhaxfYp4CRaEeILAqUnNbdNt_eIXUbXmQb2yLbPBjQcGZ4OHM4HDJRSopikpAdJ_tfHPj78MGZZod9IT8iMep8pc9JlNLwmFPnu5SLv9tbCnqUkzJh2adPrXyxDJdr_5sfTFuRv1ysPD9svwNv_bkbnQZTP1zPfSn4W_6iyVeEvyTZbpOz8l0AiL9772Ey973VajppZdc7Wm6SP6k7cJKsPIt1U5LyUFyA1vvqzQPvDoFd-v6X1RxNvZ5upuuvSLD8Ej4uZ4_L9WS67kNy3NPschSrKaz44bMSsGAaojlnAFPBNPvyMKkFfdMvaPnM4gum973Nffexnk7m4aPvrbvpJtM7XbRY3gGix5X3bTF9CHUcQGPvqSg5icqGv44vsJm8VvCR2giB-zVNIpoVdJWSko6dTckhx91oCawc4z3SDUUsZdywuK51XZvfoWP4GzqKn4FCyQD5AGrqU4GGcAEjNoBi2Be-LZYRyWZJOc8Emd0CfsaY3oOPzhNjKSWZJfA-yyJOS6oGnuLQL1jJeHRUo3_dSXti0xeIM2D7sFZYfMIxCPh8z9lDfohe8PxS8J4IpimNwGGkxQGJ3wcNMKdWc4Seva4ihCPheh4b-2tU7SDt4OiGtykTsJsqLmWc_a5JoE49UV2tkPV0bBb6TiWHIRrXzG_3cTeesuhl7KwplEdOsjKAzy5yyK-rrq6pC9qqhk63mqHTrGLoIPQoMUnhvZIkJU-pqAg4DXJYQG9q06u2AIs6p3v2ShuDgbFUeWLKRZwsTH15kuYyuKqSFvDrolXrzc7AeWVJfIJvMxFEk3BSjBkng_1gZQZMVtwdQ5mkBcnfjEQFSVFi6dubYS3SLWyblGPtTpk0ORTyome2eVbSHeVvGvbgAvbJVbv6UhGNSBzrRNV2eBtuqb9NstjDoBt2hfpRpyS6c3DIYyg8iovC7dkPSVGLxg1jdTjAq0tcWfs8yRnZ2zibY1HSvdn0YL7klchWpMieHTKIW8wOT7g-7GXPNFZbKFsFQg0eKn3JnoLKPh87kFuSTiBuIchwkqWha0AbOiqkoWOFgniQcxbRomg8WkoEhHrVzGMvAp6cE8a0OGi7X1-sfRuHSfRCLQdHJceJKaXElpeTzd4InCa7jMYbnfOdithaRxHzvhSM6B9JeVKhzk0b-W4EKnhUJq90bMZaWaurL3Do2M9wc_Oic5zt8xT6uHrU1WFZNlZYT2tJdgPdPdUAgBHA2x1hirKkPHO2UIBX0IU0ruq8VpJWG3fwsGsp35KIohYeWpI0OoiVC0vq9uVo6PTkZuj0lyzEXKO3umcHnh774Y6epcIa5HUlVxr_iXTdFfFrqz938PMXOoM51_W0PUvd1ipGzfsJoIFqy6eC8lfKG6qZZaPVOEsvJnvdZRQd8oTG5onYAkT9lLp3Kg8zTm3mpnI9GIDJIU1de4NTLWaSFHlKjneM8PiiXuYaW5zqA_6fq4bULkiStYdCYLtKBMpN4va2ABmUOuVRYpRkcJRmUdcaBLjOZ2RvOyDimMOZaA7IOIIct4Gy08KdnSyQVQ0trK1gNfamH2T34JTx42UmrKa1BqZhO9LM1c081uuueuuRATrRXsLxMK8j6uLgQeOhBm1gi_eZ24myWNQ5ZtjMPBRz3NronY55xgJQ1B2PlXuCTlfQbULqNiEf66VFN4LLjF7ci77o99J_BjwCNlgKWx2IRgHvgEOZpEl5VA5NTmGiNnjmu9QlzwLKtVDFeXdI0vhUAYb8Nzrf__XGM69rK8pnVR4vu0KJ7DzA5Ap7ByZYxcCrcLknOG4zk8AKF4E_aQHeBXCfZdtkd6jeV_tuSFHV5Z9z-SQErrEXtX4NWAwkcAJypNyawEbju6VYlNbKem0Yu3aLwcniEP4gX3sqiWg9_rM60nXK2ZYZ7noxdDdXuVchOeeJq72Jx3s4GhckIzva3iz0lFVKP5QwaXo-XTuaiX8C0DXNGS9dvQruwUa2FodyuV1yoKX2GtZb-tY0ldwvnpO8uik2b9C3f41G6NnZHPMJN4XyMdYUo7fRbvpGYzT6ZLy2yx_8jCmUbC9fJ3TQ9VfXaqYeO8rzozF19aB19duV8xFWcbUn2fHKuMViA9En2dW7J7OzqojGSFeuztJPaO6Y4a45Ow2w9TVfC1uvRne1rlWq5xs0iB5fLKMoH5WKdvW5_evmxn4B7FfvuUYZS22vJtIKN-wGH5vD_uYG86Q62wst4s1pK3RRHsaOPAwKGz2a-o6iHjA92VVFUVX-AWMARAs)

**Class Structure:**  

```
Vehicle (Abstract)
├── Motorcycle
├── Car  
├── Truck
└── ElectricCar

```

**UML Relationships Explained:**

Inheritance (Generalization) - Solid Line with Empty Triangle:  

```
Vehicle <|-- Motorcycle
Vehicle <|-- Car
Vehicle <|-- Truck
Vehicle <|-- ElectricCar

```

[![ ](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Ffeqanpwk8drvk998p77h.png)](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Ffeqanpwk8drvk998p77h.png)

## [](https://dev.to/ankitdevcode/parking-lot-system-design-lld-in-action-part-1-6f0#composition-and-aggregation-relationships)**Composition and Aggregation Relationships**

**Strong Composition (Filled Diamond):**  

```
ParkingLot "1" *-- "many" ParkingFloor
ParkingFloor "1" *-- "many" ParkingSpot

```

[![ ](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fwhlntnx9jr9yez7vk73p.png)](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fwhlntnx9jr9yez7vk73p.png)

**Meaning:**

-   Parking floors cannot exist without the parking lot
-   Parking spots cannot exist without their floor
-   Lifecycle dependency: Child objects destroyed when parent is destroyed

----------

**Weak Aggregation (Empty Diamond):**  

```
ParkingLot "1" o-- "many" ParkingObserver

```

[![ ](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2F6dc49bo2h4zt7mpa834v.png)](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2F6dc49bo2h4zt7mpa834v.png)

**Meaning:**

-   Observers can exist independently of the parking lot
-   Loose coupling: Observers can be shared across multiple parking lots

----------

**Simple Association (Solid Line):**  

```
ParkingSpot --> Vehicle : parkedVehicle
ParkingTicket --> ParkingSpot
ParkingTicket --> Payment

```

[![ ](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2F4fn0vapyauoowhfxaaub.png)](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2F4fn0vapyauoowhfxaaub.png)

**Meaning:**

-   Objects reference each other but are independent
-   Temporary relationships: Vehicle can move to different spots

----------

**Design Pattern Implementations**

> **Strategy Pattern:**  

```
PricingStrategy (Interface)
├── HourlyPricingStrategy
└── FlatRatePricingStrategy


```

UML Notation  

```
PricingStrategy <|.. HourlyPricingStrategy
PricingStrategy <|.. FlatRatePricingStrategy

```

[![ ](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fgb0sk2mnvtevmtv7u8h2.png)](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fgb0sk2mnvtevmtv7u8h2.png)

-   Interface implementation relationship
-   Enables runtime strategy switching
-   Open/Closed Principle: Add new strategies without modifying existing code

> **Observer Pattern:**  

```
Relationship: ParkingLot "1" o-- "many" ParkingObserver

```

-   Loose coupling: ParkingLot doesn't know concrete observer types
-   Event-driven: Automatic notifications on state changes

> **Factory Pattern:**

-   Factory creates Vehicle instances but doesn't store references
-   Encapsulates creation logic: Hides vehicle instantiation complexity

> **Singleton Pattern:**

-   Single instance guarantee: Only one parking lot can exist
-   Global access point: Available throughout the application

> **Builder Pattern:**

Purpose: Constructs complex ParkingLot objects step by step

-   Fluent interface: Method chaining for easy configuration
-   Complex construction: Handles multi-floor setup with different spot types

----------

**Key Relationships Analysis**

**1.  `Vehicle ↔ ParkingSpot`  Interaction**

-   Bidirectional relationship: Each knows about the other when parked
-   Validation logic: Vehicle determines if it can fit in spot

**2. ParkingLot as Central Coordinator**

[![ ](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fqneo6ndaihtz45xsl43a.png)](https://media2.dev.to/dynamic/image/width=800%2Cheight=%2Cfit=scale-down%2Cgravity=auto%2Cformat=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fqneo6ndaihtz45xsl43a.png)

-   Hub pattern: Central point for all parking operations
-   Dependency injection: Strategy and observers injected at runtime

----------

**Interface vs Abstract Class Decisions**

> Interfaces Used:

-   PricingStrategy: Behavior-only contract (no shared data)
-   ParkingObserver: Event handling contract (no shared implementation)

> Abstract Classes Used:

-   Vehicle: Shared data (licensePlate, color) + abstract behavior

**Decision Criteria:**

Interface when: Pure behavior contract, no shared data  
Abstract class when: Shared data + some abstract behaviors

----------

**Encapsulation and Access Modifiers**

> Field Visibility Patterns:

**Private Fields (-):**

-   All internal state in ParkingSpot, Payment, ParkingTicket
-   Information hiding: Implementation details not exposed

**Protected Fields (#):**

-   Vehicle class fields accessible to subclasses
-   Inheritance support: Subclasses can access parent data

**Public Methods (+):**

-   All service methods and getters
-   Clear API: Well-defined public interface

----------

**Thread Safety Considerations**

> ReentrantLock Usage:

-   ParkingSpot has individual locks for fine-grained control
-   ParkingFloor has floor-level locks for coordination
-   ParkingLot has system-wide locks for complex operations

> Thread-Safe Collections:

-   Maps shown as ConcurrentHashMap implementations
-   Concurrent access: Multiple threads can safely access shared data

## Summary: Good vs Bad Approaches

**✅ What Makes This Solution Strong:**

-   Clear Separation of Concerns: Each class has a single responsibility
-   Proper Abstraction: Abstract classes and interfaces used appropriately
-   Thread Safety: Comprehensive concurrency handling
-   Design Patterns: Multiple patterns used correctly (Strategy, Factory, Singleton, Observer)
-   Extensibility: Easy to add new features without breaking existing code
-   Error Handling: Graceful handling of edge cases
-   Type Safety: Enums prevent invalid states and types

**🚩 Common Red Flags to Avoid:**

-   God Class Anti-pattern: Single class doing everything
-   Primitive Obsession: Using strings/ints instead of proper types
-   No Thread Safety: Ignoring concurrent access issues
-   Tight Coupling: Classes knowing too much about each other
-   No Error Handling: Not handling edge cases
-   Hard-coded Values: No flexibility for configuration
-   Missing Abstractions: Not using inheritance where appropriate
-   Poor Naming: Unclear or misleading class/method names
