# 🎬 Cinema Backend - Microservices Architecture

A scalable, modular backend system for a modern cinema booking platform, built with Java Spring Boot and microservices principles.

---

## 🧱 Architecture Overview

This project is designed using the **Microservices Architecture** pattern. Each service is independent, loosely coupled, and can scale individually.

### 🗂️ Services

| Service               | Description                                |
|-----------------------|--------------------------------------------|
| `api-gateway`         | Routes external traffic to internal services via Spring Cloud Gateway |
| `service-discovery`   | Eureka server for service registration and discovery |
| `auth-service`        | Handles authentication (JWT), login, registration |
| `user-service`        | Manages user profiles, roles, preferences |
| `movie-service`       | Manages movie info, genres, schedules      |
| `room-service`        | Manages cinema rooms and seat layouts      |
| `booking-service`     | Handles seat booking and ticket management |
| `payment-service`     | Processes payments via MoMo or VNPAY       |
| `notification-service`| Sends emails and push notifications (via Firebase FCM) |

---

## 🚀 Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3
- **Communication**: REST, Kafka (optionally for async events)
- **Security**: Spring Security, JWT
- **Discovery**: Eureka
- **API Gateway**: Spring Cloud Gateway
- **Data**: Postgres, Redis (for caching & locking)
- **Messaging**: Kafka 
- **Notifications**: Firebase Cloud Messaging, JavaMail, SMS
- **Build Tool**: Maven

---

## 🧪 Local Development Setup

### 🔧 Prerequisites

- Java 21
- Docker & Docker Compose
- Maven
- Postgres, Redis

### 📦 Run All Services (with Docker)

```bash
docker-compose up --build
