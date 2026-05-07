# TaskFlow API

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?logo=docker&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?logo=swagger&logoColor=black)

TaskFlow is a **task and project management REST API** built with **Hexagonal Architecture (Ports & Adapters)**.
It provides JWT-based authentication, project and member management, task workflows, and task comments.

## Table of Contents

- [Project Description](#project-description)
- [Architecture Overview](#architecture-overview)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Run Locally (without Docker)](#run-locally-without-docker)
  - [Run with Docker](#run-with-docker)
- [Environment Variables](#environment-variables)
- [API Documentation (Swagger/OpenAPI)](#api-documentation-swaggeropenapi)
- [Endpoints by Controller](#endpoints-by-controller)

## Project Description

TaskFlow centralizes project execution with:

- user registration and authentication
- project creation and lifecycle management
- role-based member management in projects
- task creation, assignment, status transitions, and filtering
- comments on tasks

## Architecture Overview

TaskFlow follows **Hexagonal Architecture**:

- **Domain (Core):** entities, business rules, and use cases
- **Inbound Ports:** contracts used by controllers (`domain/ports/in`)
- **Outbound Ports:** contracts for external resources (`domain/ports/out`)
- **Adapters:** implementations for web, persistence, and security (`infra/...`)

```text
src/main/java/kaua/felix/taskflow
├── domain
│   ├── entity
│   ├── ports
│   │   ├── in
│   │   └── out
│   └── service
└── infra
    ├── web          (REST controllers, DTOs, mappers)
    ├── persistence  (JPA entities, repositories, adapters)
    ├── security     (JWT auth and security config)
    └── config       (OpenAPI/Swagger config)
```

## Tech Stack

- Java 21
- Spring Boot
- Spring Security + JWT
- JPA/Hibernate
- PostgreSQL
- Docker + Docker Compose
- Swagger/OpenAPI (springdoc)
- Maven Wrapper

## Getting Started

### Prerequisites

- JDK 21
- Docker & Docker Compose (for containerized execution)

### Run Locally (without Docker)

1. Start PostgreSQL (example with Docker):

```bash
docker compose up -d db
```

2. Create `.env` based on `.env.example`.

3. Run the application:

```bash
./mvnw spring-boot:run
```

If the script is not executable, run:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Application base URL: `http://localhost:8080`

### Run with Docker

1. Create `.env.docker` using the same variables as `.env.example`.

2. Run full stack:

```bash
docker compose up --build
```

Application base URL: `http://localhost:8080`

## Environment Variables

Template file: `.env.example`

```env
DB_URL=your_URL
DB_USERNAME=your_Username
DB_PASSWORD=your_Password
JWT_SECRET=your_SecretKey # minimum 32 characters required for HS256
JWT_EXPIRATION=your_ExpirationTime
```

## API Documentation (Swagger/OpenAPI)

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints by Controller

### AuthController (`/api/auth`)

- `POST /api/auth/login`
- `POST /api/auth/register`

### UserController (`/api/users`)

- `GET /api/users/me`
- `PUT /api/users/me`
- `PUT /api/users/me/password`

### ProjectController (`/api/projects`)

- `POST /api/projects`
- `GET /api/projects`
- `GET /api/projects/{id}`
- `PUT /api/projects/{id}`
- `PATCH /api/projects/{id}/archive`
- `POST /api/projects/{id}/members`
- `DELETE /api/projects/{id}/members`
- `GET /api/projects/{projectId}/tasks`
- `POST /api/projects/{projectId}/tasks`

Task list query params:

- `page` (default `0`)
- `size` (default `10`)
- `status` (optional)
- `priority` (optional)
- `deadline` (optional, `YYYY-MM-DD`)

### TaskController (`/api/tasks`)

- `GET /api/tasks/{id}`
- `PUT /api/tasks/{id}`
- `PATCH /api/tasks/{id}/status`
- `PATCH /api/tasks/{id}/assign`
- `DELETE /api/tasks/{id}`
- `POST /api/tasks/{id}/comments`
- `DELETE /api/tasks/{id}/comments/{commentId}`
