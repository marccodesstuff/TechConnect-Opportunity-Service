# TechConnect Opportunity Service

The core domain service for managing professional opportunities, including Hackathons, Certifications, and Promos. Built with Spring Boot, Java 17, and PostgreSQL.

## 📖 Documentation

A comprehensive guide to the operation, API, and architecture of this repository can be found in the **[docs/ folder](./docs/README.md)**.

### Quick Links
- **[System Context](./docs/operations/system-context.md)** – Role in the microservices ecosystem.
- **[API Endpoints](./docs/operations/api-endpoints.md)** – Technical REST API documentation.
- **[Database & Config](./docs/operations/database-and-configuration.md)** – Schema and environment settings.
- **[Development Guide](./docs/operations/development.md)** – Build and run instructions.

## Quick Start (Docker)

To run the Opportunity Service along with its database dependencies:

```bash
docker build -t techconnect-opportunity-service .
docker run -p 8081:8081 techconnect-opportunity-service
```

*Note: Requires a running PostgreSQL instance and Discovery Server for full functionality.*