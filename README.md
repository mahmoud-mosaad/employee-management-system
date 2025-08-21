# Employee Management System

A robust REST API for an Employee Management System, built with Spring Boot.

---

## ✨ Key Features

* **Full CRUD Functionality** for Employees and Departments.
* **Role-Based Security** with `ADMIN` and `USER` roles using HTTP Basic Auth.
* **Pagination** for retrieving lists of employees.
* **Interactive API Documentation** via Swagger UI.
* **Centralized Exception Handling** for consistent error responses.
* **DTO Pattern** with MapStruct for secure data transfer.
* **Daily Cron Job** for generating summary reports.
* **Unit Tests** covering the business logic layer.

---

## 🛠️ Tech Stack

* **Framework**: Spring Boot 3
* **Data**: Spring Data JPA & H2 In-Memory Database
* **Security**: Spring Security
* **API Docs**: Swagger (OpenAPI 3)
* **Utilities**: MapStruct

---

## 🚀 Getting Started

### Prerequisites

* Java 17 or later
* Apache Maven

### Installation & Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/mahmoud-mosaad/employee-management-system.git
    ```

2.  **Navigate to the project directory:**
    ```bash
    cd employee-management-system
    ```

3.  **Build the project:**
    ```bash
    mvn clean install
    ```

4.  **Run the application:**
    ```bash
    java -jar target/employee-system-demo-1.0.0.jar
    ```
    The application will start on `http://localhost:8080`.

---

## 📚 API Documentation

Once the application is running, you can access the live, interactive **Swagger UI** documentation in your browser:

➡️ **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

---

## 🔐 Security Credentials

The API is secured using HTTP Basic Authentication. Use the following credentials to test the endpoints.

| Username | Password   | Role  |
|----------|------------|-------|
| `admin`  | `admin123` | ADMIN |
| `user`   | `user123`  | USER  |

---

## 📊 Database Design (ERD)

The database schema is included in the repository. The ERD visually represents the **one-to-many** relationship between Departments and Employees.

* **Location**: `docs/erd.jpg`

---

## 🧪 API Testing (Postman)

A Postman collection is provided to test all API endpoints. It includes pre-configured requests for both `ADMIN` and `USER` roles to demonstrate the security rules.

* **Location**: `docs/postman_collection.json`

---

## 🏛️ Key Design Patterns

This project uses several key design patterns to ensure the code is clean, maintainable, and scalable.

* **API Contract Interface**: The API is defined in a Java `interface`, creating a clean separation between the public contract and its internal implementation.

* **Standardized Response Envelope**: All API responses use a consistent wrapper (`ApiResponseDTO`). This makes the API predictable and simplifies client-side handling, especially for pagination.

* **Abstract Base Controller**: An `AbstractController` centralizes response-building logic, keeping the endpoint implementations clean and free of boilerplate code.

* **Service Abstraction**: Services are defined by `interfaces` to decouple the web layer from the business logic, which improves testability and flexibility.