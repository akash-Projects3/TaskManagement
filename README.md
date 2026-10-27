# Task Management API

A RESTful Task Management API built using Java, Spring Boot, Spring Security, Spring Data JPA, and PostgreSQL.

## Features

- User registration and login
- Password hashing using BCrypt
- JWT-based authentication
- Create, read, update, and delete tasks
- Tasks restricted to their authenticated owner
- Request validation and centralized exception handling
- PostgreSQL persistence
- Unit tests using JUnit and Mockito

## Technologies

- Java 25
- Spring Boot
- Spring Security
- Spring Data JPA and Hibernate
- PostgreSQL
- Maven
- JSON Web Tokens (JWT)
- JUnit and Mockito

## Prerequisites

- Java 25
- PostgreSQL
- Git

## Database Setup

Create a PostgreSQL database and user, or use an existing local setup.

Configure the following environment variables:

- `DB_URL`: JDBC URL for the database
- `DB_USERNAME`: PostgreSQL username
- `DB_PASSWORD`: PostgreSQL password
- `JWT_SECRET`: Base64-encoded secret used to sign JWTs

Example for a local database:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/taskmanagement"
export DB_USERNAME="taskuser"
export DB_PASSWORD="your-database-password"
export JWT_SECRET="$(openssl rand -base64 32)"
```

Do not commit actual passwords or JWT secrets to version control.

## Run the Application

From the project root:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

The API runs at:

`http://localhost:8080`

The application uses Hibernate schema updates to manage the database tables.

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Log in and receive a JWT |

Example registration request:

```json
{
  "name": "Akash",
  "email": "akash@example.com",
  "password": "Password123!"
}
```

Example login request:

```json
{
  "email": "akash@example.com",
  "password": "Password123!"
}
```

Use the returned JWT for protected endpoints:

`Authorization: Bearer <your-token>`

### Tasks

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/tasks` | List the authenticated user's tasks |
| POST | `/api/tasks` | Create a task |
| GET | `/api/tasks/{id}` | Get a task by ID |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

Example task request:

```json
{
  "title": "Complete assignment",
  "description": "Finish and submit the Task Management API"
}
```

Task endpoints require a valid JWT. Users can access only their own tasks.

### Health Check

`GET /actuator/health`

## Run Tests

Run all tests with:

```bash
./mvnw clean test
```

The service tests cover user registration, password encoding, task operations, and user-specific task access.

## Project Structure

```text
src/main/java/com/ikaansh/taskmanagement/
├── config/
├── controller/
├── entity/
├── exception/
├── repository/
├── security/
└── service/

src/test/java/com/ikaansh/taskmanagement/
├── TaskmanagementApplicationTests.java
└── service/
    ├── UserServiceTest.java
    └── TaskServiceTest.java
```
