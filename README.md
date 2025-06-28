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

Time-Box is a comprehensive time management and productivity tracking system designed to help users monitor and optimize their study and work sessions. The application allows users to track their time spent on different subjects, set goals, monitor progress, and earn achievements for their productivity.

With Time-Box, users can:
- Track study/work sessions by subject
- Set daily and weekly goals
- Monitor productivity metrics
- Earn achievements for consistent work
- Analyze performance trends

Whether you're a student trying to optimize your study schedule or a professional tracking work productivity, Time-Box provides the tools to help you manage your time effectively and achieve your goals.

## Features

### User Management
- User registration and authentication
- Profile management
- Email verification

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

The easiest way to run the application is using Docker Compose, which will set up the application, database, and monitoring tools:

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

### User API

- `GET /users` - Get all users
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create a new user (sign up)
- `DELETE /users/{id}` - Delete a user
- `GET /users/time-studied/{id}` - Get total time studied for a user
- `GET /users/days-streak/{id}` - Get current days streak for a user
- `PUT /users/{id}/weekly-goal` - Set weekly goal for a user
- `PUT /users/{id}/daily-study-time` - Set daily study time goal for a user
- `GET /users/{id}/avg-session` - Get average session length for a user
- `GET /users/{id}/today-sessions` - Get number of sessions today for a user
- `GET /users/{id}/most-productive-subject` - Get most productive subject for a user
- `GET /users/{id}/longest-session` - Get longest session for a user
- `GET /users/{id}/daily-study-time` - Get daily study time for a user

### Subject API

The application provides endpoints for managing subjects, including creating, retrieving, updating, and deleting subjects.

### Session API

The application provides endpoints for managing sessions, including creating, retrieving, and analyzing study/work sessions.

### Achievement API

The application provides endpoints for managing achievements, including creating, retrieving, and tracking progress towards achievements.

## Security

The application uses Spring Security for authentication and authorization:

- Password hashing for secure storage
- Email verification for new accounts
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
