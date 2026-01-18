# Full-Stack Microservices System. A micro model/example of an architecture for a larger application.

This is a production-grade, Swarm-ready full-stack application built with Spring Boot microservices and a React frontend. It manages tasks that are created without assigned users and later enriched via a background processor that fetches usernames from a user service based on task category. The system is modular, secure, reproducible, and optimized for recruiter-grade presentation. 
A micro exmaple for showing all the main technologies we use for at our main application, such as:
## 🧱 Architecture Overview

- **Backend:** Spring Boot microservices (Gateway, Auth, User, Task)
- **Frontend:** React + TypeScript with MUI and Formik
- **Database:** Shared PostgreSQL across all services
- **Caching:** Redisons (Redis-backed Java cache) in User Service
- **Authentication:** JWT-based via Auth Service
- **Logging:** Fluentd for centralized log aggregation
- **Orchestration:** Docker Compose / Swarm-ready

---

## 🔐 Microservices Breakdown

### Gateway Service
- Routes requests to backend services
- Validates JWT tokens
- Applies role-based access control

### Auth Service
- Issues JWT tokens with claims: `sub`, `roles`, `scope`, `exp`, `tenantId`
- Supports login and token refresh
- Provides service-to-service tokens with `scope: SERVICE`
- Uses Redis for session tracking

### User Service
- Stores users in PostgreSQL
- Uses Redisons to cache users by category (`Map<String category, List<UserDto>>`)
- On user creation:
  - Saves to DB
  - Updates Redisons cache
- Endpoint: `GET /internal/users/by-category/{category}` returns usernames from cache

### Task Service
- Stores tasks with fields: `id`, `title`, `category`, `assignedUsernames`, `createdAt`
- `assignedUsernames` is a `List<String>` (initially empty)
- Background processor:
  - Runs every minute (`@Scheduled`)
  - Fetches tasks with empty `assignedUsernames`
  - Calls User Service with task category
  - Receives usernames and updates the task

---

## 🖥️ Frontend Features

Built with React and TypeScript using MUI and Formik.

### Pages

#### 1. User Page
- Displays users in a DataGrid
- Toolbar with category filters
- Modal form to create new user
- Modal to view user details

#### 2. Task Page
- Displays tasks in a DataGrid
- Toolbar with filters (category, assigned usernames)
- Modal form to create new task (title + category only)
- Modal to view task details

### UX Enhancements
- Pagination and sorting
- Filtering by category and usernames
- Custom hook `useZMutation` for API calls with loading/error states

---

## 🚀 Setup Instructions

### Prerequisites
- Java 18
- Node.js 18+
- Docker + Docker Compose
- Redis and PostgreSQL running locally or via Compose

### Running Locally

```bash
docker-compose up
```

This will start all services including Redis, PostgreSQL, Fluentd, and the full backend stack.

### Frontend

```bash
cd frontend
npm install
npm start
```

---

## 📦 Folder Structure

- `/gateway-service`
- `/auth-service`
- `/user-service`
- `/task-service`
- `/shared-security`
- `/frontend`
- `/docker-compose.yml`
- `/README.md`

---

## 📜 API Documentation

Each service exposes Swagger at `/swagger-ui.html`. Key endpoints include:

- `POST /api/tasks` — create task
- `GET /api/tasks` — list tasks
- `POST /api/users` — create user
- `GET /api/users` — list users
- `GET /internal/users/by-category/{category}` — internal user lookup

---

## 🛡️ Security Notes

- All internal endpoints (`/internal/**`) require `scope: SERVICE`
- Feign clients use `FeignSecurityConfig` to attach JWT tokens
- Token validation is handled via `JwtTokenValidator` in `shared-security`

---

## 🧠 Background Processor Logic

- Runs every 60 seconds
- Fetches tasks with empty `assignedUsernames`
- Calls User Service with task category
- Receives usernames from Redisons cache
- Updates and saves task

---

## 🧊 Caching Strategy

- Redisons used in User Service
- Users cached by category
- On cache miss, loads from DB and populates cache
- Cache updated on user creation

---

## 📄 License

This project is proprietary and not open for contributions.

> **Note:** This project is not open for contributions. Please do not submit pull requests or issues.

---

## 📞 Contact

For questions or collaboration inquiries, please reach out privately. This repository is intended for demonstration and portfolio purposes only.
```

Let me know if you want me to generate a matching `LICENSE` file or `.gitignore` next.
