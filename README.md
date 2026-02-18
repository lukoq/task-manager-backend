# Task Manager Backend

This is a REST API built with Java and Spring Boot.

---

## Getting Started

### 1️⃣ Clone the repository

```bash
git clone https://github.com/lukoq/task-manager-backend.git
cd task-manager-backend
```

### 2️⃣ Start required services (Docker)

```bash
docker compose up -d
```

### 3️⃣ Run the application

Windows:

```bash
.\mvnw spring-boot:run
```

Mac/Linux:

```bash
./mvnw spring-boot:run
```

---

## Configuration

Before running the application, make sure to configure the following property in:

```
src/main/resources/application.properties
```

Add your JWT secret key:

```properties
jwt.secret=${JWT_SECRET_KEY}
```

