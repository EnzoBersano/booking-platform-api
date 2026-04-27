# Booking Platform API

Un backend system inspirado en plataformas de tecnología de viajes como Despegar y Airbnb, enfocado en modelar sistemas de reservas del mundo real con un fuerte énfasis en diseño de dominio, arquitectura limpia y extensibilidad.

Este proyecto no es solo CRUD — explora cómo construir un sistema de reservas con reglas de negocio reales, resolución de conflictos y patrones de arquitectura escalables.

## Objetivo

El objetivo es diseñar un sistema de reservas realista que maneje:

- Reservas de recursos con conflictos de tiempo
- Reglas de validación de disponibilidad (políticas de dominio)
- Autenticación sin estado (JWT)
- Separación limpia de la lógica de negocio de las preocupaciones del framework
- Arquitectura extensible para escalado futuro (motor de disponibilidad, despliegue distribuido)

## Arquitectura

El sistema sigue una Arquitectura Hexagonal (Puertos y Adaptadores) combinada con un enfoque pragmático de DDD.

````
com.enzobersano.booking_platform_api
├── booking
├── resource
├── auth
├── availability (en progreso)
└── shared
````


Cada módulo está estructurado como:

- **api** → Controladores REST, mapeo de peticiones/respuestas
- **application** → casos de uso (orquestación de negocio)
- **domain** → reglas de negocio centrales y modelos
- **infrastructure** → persistencia, seguridad, sistemas externos

## Decisiones de Diseño Clave

### Arquitectura Hexagonal

La lógica de negocio está completamente aislada de Spring y la infraestructura a través de puertos y adaptadores.

### Reglas de reserva guiadas por dominio

La validación de reservas está centralizada en un `BookingPolicy`, asegurando:

- Sin superposición de horarios
- Restricciones de duración mínima
- Límites de reserva anticipada
- Reglas de disponibilidad de recursos
- Detección de conflictos por usuario

### Manejo de errores basado en Resultados

En lugar de excepciones, el sistema utiliza un modelo `Result<T, Failure>` para un flujo de control explícito.

### Abstracción de autenticación

El contexto del usuario se resuelve a través de un `CurrentUserPort`, desacoplando la seguridad de la lógica de negocio.

### Arquitectura testeable

Los casos de uso y la lógica de dominio están completamente probados con unit tests usando Mockito y JUnit.

## Stack Tecnológico

- Java 21
- Spring Boot
- PostgreSQL
- Docker
- Autenticación JWT
- Mockito + JUnit 5
- OpenApi/Swagger
- AWS (planeado: EC2 + ALB + ASG)

## Características Actuales

- Creación de reservas con detección de conflictos
- Gestión de recursos
- Validación de rangos de tiempo
- Aislamiento de reservas por usuario
- Paginación y filtrado
- Seguridad basada en roles (JWT)
- Manejo estructurado de errores

## En Progreso

- Motor de disponibilidad (capa de resolución de conflictos en tiempo real)
- Despliegue en AWS (EC2 + balanceo de carga)
- Diagrama de arquitectura (nube + módulos)
- Reglas de reserva avanzadas (modelo preparado para capacidad y precios dinámicos)

## Estado

Este proyecto se encuentra en desarrollo arquitectónico activo, enfocado en el diseño de sistemas backend, patrones de escalabilidad y una estructura lista para producción, en lugar de una simple implementación CRUD.