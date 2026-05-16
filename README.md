# Order Management System

REST API for order and product management built with
Spring Boot and JWT authentication.

## Features
- JWT-based authentication and authorization
- Role-based access control (Admin/User)
- Product management with pagination and search
- Order placement with stock validation
- Order status tracking (PENDING/CONFIRMED/DELIVERED/CANCELLED)
- Stock restoration on order cancellation
- Global exception handling
- Swagger UI documentation

## Tech Stack
Java 21 · Spring Boot 3.5 · MySQL · Spring Security ·
Spring Data JPA · JWT · Lombok

## Roles & Access

### Admin
- Add, update, delete products
- View all orders
- Update order status

### User
- View and search products
- Place orders
- View own orders
- Cancel pending orders

## How to Run

1. Create MySQL database:
   CREATE DATABASE ordermanagement;

2. Copy example properties:
   cp src/main/resources/application.properties.example
   src/main/resources/application.properties

3. Update application.properties with your credentials

4. Run OrderManagementApplication.java

5. Test APIs: http://localhost:8080/swagger-ui/index.html

## API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/register | Register new user |
| POST | /auth/login | Login and get JWT token |

### Products
| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | /products | User/Admin | List with pagination |
| GET | /products/search | User/Admin | Search by name |
| GET | /products/{id} | User/Admin | Get product |
| POST | /products/admin/add | Admin | Add product |
| PUT | /products/admin/{id} | Admin | Update product |
| DELETE | /products/admin/{id} | Admin | Delete product |

### Orders
| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | /orders/place | User | Place order |
| GET | /orders/my | User | My orders |
| PUT | /orders/{id}/cancel | User | Cancel order |
| GET | /orders/all | Admin | All orders |
| PUT | /orders/status/{id} | Admin | Update status |

## Sample Requests

### Register
POST /auth/register
{
"name": "John",
"email": "john@gmail.com",
"password": "john123",
"role": "USER"
}

### Login
POST /auth/login
{
"email": "john@gmail.com",
"password": "john123"
}

### Place Order
POST /orders/place
Authorization: Bearer {token}
{
"productId": 1,
"quantity": 2
}