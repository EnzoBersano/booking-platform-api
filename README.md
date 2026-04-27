#  Booking Platform API

A backend system inspired by travel tech platforms like Despegar and Airbnb, focused on modeling real-world booking systems with a strong emphasis on domain design, clean architecture, and extensibility.

This project is not just CRUD — it explores how to build a reservation system with real business rules, conflict resolution, and scalable architecture patterns.
##  Goal

The goal is to design a realistic booking system that handles:

- Resource reservations with time conflicts
- Availability validation rules (domain policies)
- Stateless authentication (JWT)
- Clean separation of business logic from framework concerns
- Extensible architecture for future scaling (availability engine, distributed deployment)

##  Architecture

The system follows a Hexagonal Architecture (Ports & Adapters) combined with a pragmatic DDD approach.

````
com.enzobersano.booking_platform_api
├── booking
├── resource
├── auth
├── availability (in progress)
└── shared
````

Each module is structured as:

- **api** → REST controllers, request/response mapping
- **application** → use cases (business orchestration)
- **domain** → core business rules and models
- **infrastructure** → persistence, security, external systems

##  Key Design Decisions

### 🔹 Hexagonal Architecture

Business logic is completely isolated from Spring and infrastructure through ports and adapters.

### 🔹 Domain-driven booking rules

Booking validation is centralized in a `BookingPolicy`, ensuring:

- No time overlaps
- Minimum duration constraints
- Advance booking limits
- Resource availability rules
- User conflict detection

### 🔹 Result-based error handling

Instead of exceptions, the system uses a `Result<T, Failure>` model for explicit control flow.

### 🔹 Authentication abstraction

User context is resolved through a `CurrentUserPort`, decoupling security from business logic.

### 🔹 Testable architecture

Use cases and domain logic are fully unit-tested with Mockito and JUnit.

##  Tech Stack

- Java 21
- Spring Boot
- PostgreSQL
- Docker
- JWT Authentication
- Mockito + JUnit 5
- OpenApi/Swagger
- AWS (planned: EC2 + ALB + ASG)

## Current Features

- Booking creation with conflict detection
- Resource management
- Time-range validation
- User-based booking isolation
- Pagination and filtering
- Role-based security (JWT)
- Structured error handling

## In Progress

- Availability engine (real-time conflict resolution layer)
- AWS deployment (EC2 + load balancing)
- Architecture diagram (cloud + modules)
- Advanced booking rules (capacity, dynamic pricing-ready model)

##  Status

This project is in active architectural development, focusing on backend system design, scalability patterns, 
and production-ready structure rather than simple CRUD implementation.