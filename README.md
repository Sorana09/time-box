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
<img src="https://img.shields.io/badge/XML-005FAD.svg?style=flat&logo=XML&logoColor=white" alt="XML">
<img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=flat&logo=PostgreSQL&logoColor=white" alt="PostgreSQL">
<img src="https://img.shields.io/badge/YAML-CB171E.svg?style=flat&logo=YAML&logoColor=white" alt="YAML">

</div>
<br>

---

## Table of Contents

- [Overview](#overview)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Usage](#usage)
    - [Testing](#testing)
- [Project Structure](#project-structure)

---

## Overview

Time-Box is a developer-friendly microservice focused on efficient time management and user activity tracking. Built with Spring Boot, it offers a scalable, secure, and observable architecture that integrates seamlessly with modern monitoring tools.

**Why Time-Box?**

This project simplifies building time-tracking applications with features designed for performance and security. The core features include:

-  **🔍 Observability:** Integrated with Prometheus and OpenTelemetry for comprehensive performance monitoring.
-  **🛠️ Containerization:** Lightweight Docker images built with Maven for consistent deployment.
-  **🔒 Security:** Robust security configurations to protect user data and sessions.
-  **📈 Metrics:** Real-time tracking of user engagement, subject views, and achievement activities.
-  **🗃️ Data Management:** Modular repositories and entities for efficient data access and manipulation.
-  🌐 **REST API:** Full suite of endpoints for managing users, subjects, sessions, and achievements.

---

## Project Structure

```sh
└── time-box/
    ├── Dockerfile
    ├── compose.yaml
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src
        └── main
```

---

## Getting Started

### Prerequisites

This project requires the following dependencies:

- **Programming Language:** Java
- **Package Manager:** Maven
- **Container Runtime:** Docker

### Installation

Build time-box from the source and install dependencies:

1. **Clone the repository:**

    ```sh
    ❯ git clone https://github.com/Sorana09/time-box
    ```

2. **Navigate to the project directory:**

    ```sh
    ❯ cd time-box
    ```

3. **Install the dependencies:**

**Using [docker](https://www.docker.com/):**

```sh
❯ docker build -t Sorana09/time-box .
```
**Using [maven](https://maven.apache.org/):**

```sh
❯ mvn install
```

### Usage

Run the project with:

**Using [docker](https://www.docker.com/):**

```sh
docker run -it {image_name}
```
**Using [maven](https://maven.apache.org/):**

```sh
mvn exec:java
```
---

<div align="left"><a href="#top">⬆ Return</a></div>

---
