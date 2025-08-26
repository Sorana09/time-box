<div id="top">

<!-- HEADER STYLE: CLASSIC -->
<div align="center">

# TIME-BOX

<em>Master Time, Unlock Your Full Potential</em>

<!-- BADGES -->
<img src="https://img.shields.io/github/last-commit/Sorana09/time-box?style=flat&logo=git&logoColor=white&color=0080ff" alt="last-commit">
<img src="https://img.shields.io/github/languages/top/Sorana09/time-box?style=flat&color=0080ff" alt="repo-top-language">
<img src="https://img.shields.io/github/languages/count/Sorana09/time-box?style=flat&color=0080ff" alt="repo-language-count">

<em>Built with the tools and technologies:</em>

<img src="https://img.shields.io/badge/Spring-000000.svg?style=flat&logo=Spring&logoColor=white" alt="Spring">
<img src="https://img.shields.io/badge/Docker-2496ED.svg?style=flat&logo=Docker&logoColor=white" alt="Docker">
<img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=flat&logo=PostgreSQL&logoColor=white" alt="PostgreSQL">
<img src="https://img.shields.io/badge/Java-ED8B00.svg?style=flat&logo=openjdk&logoColor=white" alt="Java">
<img src="https://img.shields.io/badge/OpenTelemetry-000000.svg?style=flat&logo=opentelemetry&logoColor=white" alt="OpenTelemetry">

</div>
<br>

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Monitoring and Observability](#monitoring-and-observability)

---

## Overview

Time-Box is a comprehensive time management and productivity tracking system designed to help users monitor and optimize
their study and work sessions. The application allows users to track their time spent on different subjects, set goals,
monitor progress, and earn achievements for their productivity.

With Time-Box, users can:

- Track study/work sessions by subject
- Set daily and weekly goals
- Monitor productivity metrics
- Analyze performance trends

Whether you're a student trying to optimize your study schedule or a professional tracking work productivity, Time-Box
provides the tools to help you manage your time effectively and achieve your goals.

## Features

### User Management

- User registration and authentication
- Profile management

### Time Tracking

- Session tracking by subject
- Daily and weekly goal setting
- Productivity metrics:
    - Time studied
    - Days streak
    - Average session length
    - Longest session
    - Most productive subject

### Subject Management

- Create and manage subjects/topics
- Track time spent per subject
- Identify most productive subjects

### Achievement System

- Earn achievements for productivity milestones
- Track progress towards achievements

### Analytics

- View productivity trends
- Analyze time spent by subject
- Track goal completion

## Technology Stack

### Backend

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.4
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Security**: Spring Security
- **Build Tool**: Maven

### DevOps & Infrastructure

- **Containerization**: Docker
- **Container Orchestration**: Docker Compose
- **Monitoring**:
    - Spring Boot Actuator
    - Micrometer
    - Prometheus
    - OpenTelemetry
    - Grafana

## Project Structure

The application follows a standard Spring Boot project structure:

```
time-box/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/time/box/
│   │   │       ├── aspect/         # Aspect-oriented programming components
│   │   │       ├── controller/     # REST API controllers
│   │   │       ├── domain/         # Domain objects and DTOs
│   │   │       ├── entity/         # JPA entities
│   │   │       ├── exception/      # Exception handling
│   │   │       ├── metrics/        # Metrics and monitoring
│   │   │       ├── repository/     # Data access layer
│   │   │       ├── security/       # Security configuration
│   │   │       ├── service/        # Business logic
│   │   │       └── TimeBoxApplication.java  # Application entry point
│   │   └── resources/              # Configuration files
│   └── test/                       # Test classes
├── Dockerfile                      # Docker configuration
├── compose.yaml                    # Docker Compose configuration
├── mvnw                            # Maven wrapper script
├── mvnw.cmd                        # Maven wrapper script for Windows
└── pom.xml                         # Maven project configuration
```

---

## Getting Started

### Prerequisites

To run this application, you need:

- Java 21 or later
- Maven 3.9+ (or use the included Maven wrapper)
- Docker and Docker Compose (for containerized deployment)

### Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/Sorana09/time-box.git
    cd time-box
    ```

2. **Build the application:**

    ```sh
    ./mvnw clean package
    ```

### Running the Application

#### Using Docker Compose (Recommended)

The easiest way to run the application is using Docker Compose, which will set up the application, database, and
monitoring tools:

```sh
docker-compose up -d
```

This will start:

- PostgreSQL database
- Time-Box application
- OpenTelemetry with Grafana for monitoring

The application will be available at http://localhost:8100

#### Using Maven

You can also run the application directly using Maven:

```sh
./mvnw spring-boot:run
```

Note: This requires a PostgreSQL database to be running and configured in application properties.

## API Documentation

The application provides a RESTful API for managing users, subjects, sessions, and achievements.

## Security

The application uses Spring Security for authentication and authorization:

- Password hashing for secure storage
- Custom logout handling
- Session management

## Monitoring and Observability

Time-Box includes comprehensive monitoring and observability features:

- **Spring Boot Actuator**: Provides health checks and application metrics
- **Micrometer**: Collects application metrics
- **Prometheus**: Stores time-series metrics data
- **OpenTelemetry**: Distributed tracing and metrics collection
- **Grafana**: Visualization of metrics and traces

The monitoring dashboard is available at http://localhost:3001 when running with Docker Compose.


<div align="left"><a href="#top">⬆ Return</a></div>

---

</div> <!-- closing div for top -->

<img width="862" height="913" alt="Screenshot 2025-07-22 123641" src="https://github.com/user-attachments/assets/92933fde-7c8f-4ee4-b035-583e4c191d4a" />


<img width="1253" height="572" alt="Screenshot 2025-07-22 124329" src="https://github.com/user-attachments/assets/984128a1-bf3f-49c3-89a9-afe0d93e0ac5" />


<img width="1909" height="907" alt="Screenshot 2025-07-22 123805" src="https://github.com/user-attachments/assets/0ae71758-b705-449b-999b-13f72eb8c0f1" />


<img width="1909" height="659" alt="Screenshot 2025-07-22 123730" src="https://github.com/user-attachments/assets/c0b0521f-be86-42dc-9ec4-5d9bb6f27f27" />




---

## Architecture and Design Patterns

To make the code easier to understand and maintain, the project applies a simple, explicit design pattern in the
WebSocket messaging area:

- Factory Method: A ChatMessageFactory centralizes creation of ChatMessage objects for common cases (CHAT, JOIN, LEAVE).
  This removes message-building details from listeners/controllers and makes the intent clearer.

Where to look:

- ChatMessageFactory: src/main/java/com/example/time/box/domain/factory/ChatMessageFactory.java
- Usage example: src/main/java/com/example/time/box/event/WebSocketEventListener.java

Benefits:

- Single responsibility: WebSocketEventListener focuses on handling events, while the factory handles message
  construction.
- Consistency: All emitted messages are created in the same way (timestamp/type fields are consistently set).
- Extensibility: New message variants can be added in one place.

Additional Organization Notes:

- The project uses a layered structure (controller, service, repository, security, etc.) with domain separated from
  persistence entities.
- For future refactors, consider extracting use-case (application service) classes for complex flows and introducing
  Strategy for pluggable policies when requirements grow.
