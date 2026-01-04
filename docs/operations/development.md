# Development Guide

Instructions for building and running the TechConnect Opportunity Service.

## Prerequisites

- **Java 17** (JDK)
- **Maven 3.8+**
- **PostgreSQL**
- (Optional) **Docker**

## 🏗️ Building the Project

The project is managed with Maven. To generate a runnable JAR:

```bash
mvn clean package
```

The artifact will be created at `target/tp-opportunity-service-0.0.1-SNAPSHOT.jar`.

## 🏃 Running the Service

### 1. Locally via Maven
Ensure you have a local PostgreSQL instance running or override the credentials via environment variables.

```bash
mvn spring-boot:run
```

### 2. via Docker
```bash
docker build -t opportunity-service .
docker run -p 8081:8081 opportunity-service
```

## 🧪 Testing

The service includes unit and integration tests based on Spring Boot Test and JUnit 5.

```bash
mvn test
```

### Key Areas to Test:
- **Validation Logic**: Verify that invalid dates or empty fields are rejected.
- **REST Surface**: Verify that controllers return the correct `ApiResponse` structure.
- **Persistence**: Verify that the JPA repository correctly manages data in the database.

## 🛠️ Tooling

- **Lombok**: Used for reducing boilerplate. Ensure your IDE has the Lombok plugin installed.
- **Actuator**: Use the health endpoints to verify the service can connect to its database and Eureka.
