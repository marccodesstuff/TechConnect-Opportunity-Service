# Database and Configuration

The Opportunity Service uses PostgreSQL for data persistence and Spring Cloud for environment-based configuration.

## 💾 Database Schema

The service manages a single primary table: `opportunities`.

### Table: `opportunities`

- `id`: BigInt (Primary Key, Generated)
- `title`: VarChar
- `description`: Text
- `type`: VarChar (Mapped from `OpportunityType` enum: HACKATHON, CERTIFICATION, PROMO)
- `start_date`: Timestamp
- `end_date`: Timestamp
- `created_at`: Timestamp
- `updated_at`: Timestamp

## ⚙️ Configuration Properties

Configuration is managed in `src/main/resources/application.yml`. 

### Environment Overrides

| Property | Environment Variable | Default |
|---|---|---|
| `server.port` | `SERVER_PORT` | `8081` |
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db:5432/techconnect` |
| `spring.datasource.username` | `POSTGRES_USER` | `postgres` |
| `spring.datasource.password` | `POSTGRES_PASSWORD` | `postgres` |
| `eureka.client.service-url.defaultZone` | `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | `http://discovery-server:8761/eureka/` |

## 🚀 Service Registration

The service is configured as a Eureka Client:

- `register-with-eureka: true`
- `fetch-registry: true`
- **Instance ID**: `opportunity-service`

## 🏥 Health Monitoring

The service exposes health and info endpoints via Spring Boot Actuator:
- `http://localhost:8081/actuator/health`
- `http://localhost:8081/actuator/info`
