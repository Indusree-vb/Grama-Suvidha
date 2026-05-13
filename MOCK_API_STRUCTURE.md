# Grama-Suvidha Portal - Mock API & Data Structure Documentation

This document outlines the data structure used for simulating the project and feedback data in the Grama-Suvidha Portal application.

## 1. Project Entity Structure
The projects are stored locally using Room and initialized in `GramaApplication.kt`.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | String | Unique identifier for the project (e.g., "PROJ-001") |
| `nameEn` | String | Project name in English |
| `nameKn` | String | Project name in Kannada |
| `locationEn` | String | Project location in English |
| `locationKn` | String | Project location in Kannada |
| `budget` | String | Total allocated budget (e.g., "₹10,00,000") |
| `statusEn` | String | Current status in English (ONGOING, COMPLETED, PLANNED) |
| `statusKn` | String | Current status in Kannada |
| `completionPercentage` | Int | Progress percentage (0 to 100) |
| `descriptionEn` | String | Detailed description in English |
| `descriptionKn` | String | Detailed description in Kannada |
| `beforeImageUrl` | String | URL for the "Before" or "Current" project photo |
| `afterImageUrl` | String | URL for the "After" photo (if completed) |

## 2. Feedback & Issue Entity Structure
This structure handles both general citizen feedback and formal issue reports.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | Int | Auto-generated primary key |
| `projectId` | String | ID of the project the feedback relates to |
| `citizenName` | String | Name of the person providing feedback (default: "Anonymous") |
| `rating` | Int | Citizen rating (1 to 5 stars) |
| `issueDescription` | String | The text content of the feedback or issue |
| `isIssue` | Boolean | True if reported as an issue, False if general feedback |
| `timestamp` | Long | System time when the feedback was submitted |

## 3. Localization Strategy
The app uses a dual-field approach for strings in the database to ensure offline support for both Kannada and English without needing complex translation lookups at runtime.

- **Example JSON Representation (Mocked):**
```json
{
  "id": "PROJ-001",
  "nameEn": "Main Road Repair",
  "nameKn": "ಮುಖ್ಯ ರಸ್ತೆ ದುರಸ್ತಿ",
  "completionPercentage": 85,
  "statusEn": "ONGOING"
}
```

## 4. Implementation Details
- **Database:** Android Room Persistence Library.
- **Initialization:** Data is pre-populated in `GramaApplication.onCreate()` to simulate a fetched API response.
- **Images:** Loaded dynamically via Coil from external URLs.
