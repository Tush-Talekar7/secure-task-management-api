#  Secure User Management API

A production-ready Spring Boot REST API implementing JWT authentication, role-based authorization, pagination, structured responses, and Swagger documentation.

Built to demonstrate secure backend development practices suitable for real-world SaaS applications.

---

##  Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Spring Data JPA
- Swagger (OpenAPI)
- Maven

---

##  Features

✔ User Registration  
✔ Secure Login (JWT Authentication)  
✔ Role-Based Access Control (ADMIN / USER)  
✔ Structured API Responses  
✔ Pagination & Sorting  
✔ Swagger API Documentation  
✔ Global Exception Handling  
✔ MySQL Integration  


## Authentication Flow

1. User registers
2. User logs in
3. Server generates JWT token
4. Client sends token in Authorization header: Authorization: Bearer <your_token>
5. Protected endpoints validate token
6. Role-based authorization applied

How to run :
mvn spring-boot:run
