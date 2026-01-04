# API Endpoints

The Opportunity Service provides a RESTful API for managing professional opportunities.

**Base Path**: `/api/opportunities`

## 📋 Endpoint Summary

### 1. Create Opportunity
- **URL**: `POST /api/opportunities`
- **Description**: Creates a new opportunity.
- **Request Body**: `OpportunityCreateRequest` (JSON)
- **Success Response**: `201 Created`

### 2. Get Opportunity by ID
- **URL**: `GET /api/opportunities/{id}`
- **Description**: Retrieves details of a specific opportunity.
- **Success Response**: `200 OK`

### 3. List All Opportunities
- **URL**: `GET /api/opportunities`
- **Description**: Retrieves a list of all opportunities.
- **Success Response**: `200 OK`

### 4. Delete Opportunity
- **URL**: `DELETE /api/opportunities/{id}`
- **Description**: Deletes an opportunity by its ID.
- **Success Response**: `200 OK`

## 🧱 Data Models

### Opportunity Types
The system supports three types of opportunities via the `OpportunityType` enum:
- `HACKATHON`
- `CERTIFICATION`
- `PROMO`

### Standard Response Structure
All endpoints return a consistent `ApiResponse` wrapper:

```json
{
  "status": 200,
  "message": "OK",
  "data": { ... }
}
```

## ✅ Validation Rules

- **Required Fields**: title, description, type, startDate, endDate.
- **Date Constraints**: The `endDate` must be chronologically after the `startDate`.
- **Enum Mapping**: The `type` field must be one of the valid `OpportunityType` values (case-sensitive).
