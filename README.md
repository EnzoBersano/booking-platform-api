
# Booking Platform API

Backend system inspired by travel platforms like Despegar, designed to explore reservation systems, availability engines and clean architecture principles.

---

## Goal

This project aims to model a simplified but realistic booking system with:

- Reservation management
- Resource hierarchy (Composite pattern)
- Availability engine for conflict detection
- Domain-driven design (lightweight, practical)
- Stateless authentication (JWT)

---

## Architecture

The system follows a **DDD-inspired modular architecture**:
```
com.enzobersano.booking_platform_api
reservation/
resource/
availability/
auth/
shared/
```
---

Each module contains its own:

- API layer: REST controllers and DTOs 
- Application layer: use cases and orchestration
- Domain layer: core business model (framework independent)
- Infrastructure layer: persistence and external systems

---

##  Design Principles

- Domain-first design
- Separation of concerns
- No framework leakage into domain layer
- Explicit boundaries between modules
- Result-oriented error handling 

---

##  Tech Stack

- Java 17
- Spring Boot
- PostgreSQL
- Docker
- AWS (EC2 + ALB + ASG deployment)

---

##  Status

Early architecture phase – domain modeling in progress.