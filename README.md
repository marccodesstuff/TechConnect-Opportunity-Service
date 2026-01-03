# TP Opportunity Service

Manages Hackathons, Certifications, and Promos.

Design choices:
- Single table `opportunities` using `OpportunityType` enum
- DTOs implemented as Java 17 records
- Date validation: `endDate` must be after `startDate` via class-level constraint
- Multi-stage Dockerfile for local builds

Run in compose: `docker compose up --build tp-opportunity-service`