# Autoshop

## Project Description

**Autoshop**  is a backend application for an online auto store.
The system supports user registration, an auto parts catalog, a shopping cart, order placement, promo codes, file uploads, and email notifications.
The project is built on Spring Boot 3.5 and implements a modern architecture with JWT authentication, roles, logging, and a Docker infrastructure.



---

## Main Features

1. **User Management**:
- CRUD operations for creating, editing and deleting user data.


2. **Authentication and Authorization System**:
- Registration and login for users.
- Generating JWT tokens to secure API routes.

3. **Catalog and SpareParts Management**:
- Adding and editing data.
- Support for sorting, filter and pagination.

4. **Cart Management**:
- CRUD operations for creating, editing and deleting data.
- Ability to use promo.

4. **Order Management**:
- Operations for checking out, canceling and viewing status data.
- Ability to use promo.

5. **User Appointments**:
- Adding an item to your cart, activating a promo code, and placing an order.
- Automatic deletion of outdated records.

6. **Added notifications by email**:
- After registration, you will receive an SMS confirming your registration.

7. **File Managenent**:
- Uploading and storing catalog, parts pictures.

8. **Added Promocodes**:
- You can apply promo codes when placing an order to receive discounts.

9. **Centralization of logging and monitoring**:
- Connecting custom request logging via [LogInterceptor](./src/main/java/by/rublevskaya/interceptor/LogInterceptor.java).
- AOP aspect TimingAspect for measuring method execution time.
- Logging via @Slf4j
10. **Security:**
- Authentication and authorization using JWT.
- Password hashing with BCrypt.
11. **Database Migration:**
- Using FlyWay for database version control and migration.
- Database schema management with JPA and Hibernate.
- Database: PostgreSQL (can be replaced with any other supported database).
---




## REST API Documentation

## Overview

This API provides a comprehensive set of endpoints for managing data

## Base URL

`http://localhost:8080`


## Authentication

The API provides authentication endpoints for both users and admin.

### User Registration

```http
POST /security/registration
```


### Login

```http
POST /security/login
```

### Get JWT Token

```http
GET /security/jwt
```

### Confirm Email

```http
GET /security/confirm?token={token}
```

###Get Security info by Id

```http
GET /security/{id}
```

### Grant Admin Role (Admin only)

```http
POST /security/{id}/admin
```


## Users

### Create User

```http
POST /user
```



### Get All Users

```http
GET /user
```

### Get User by ID

```http
GET /user/{id}
```

### Update User(for authorized user)

```http
PUT /users/
```

### Delete User

```http
DELETE /users/{id}
```
### Get Users with Pagination

```http
GET /user/pagination/{page}/{size}
```

### Sort Users by Field

```http
GET /user/sort/{field}
``` 

## Spare Parts

### Create SparePart

```http
POST /catalog/spare-parts
```

### Get All SpareParts

```http
GET /catalog/spare-parts
```

### Get SparePart by ID

```http
GET /catalog/spare-parts/{id}
```

### Update SparePart

```http
PUT /catalog/spare-parts/
```



### Get SpareParts with specific filters

```http
GET /catalog/spare-parts/filter
```


### Get SpareParts by Category

```http
GET /catalog/spare-parts/category/{category}
```
### Get SpareParts with pagination and sorting

```http
GET /catalog/spare-parts/pagination/{page}/{size}
GET /catalog/spare-parts/sort/{field}
```

## Catalog
### Get All Catalogs

```http
GET /catalog
```

### Get Catalog By ID

```http
GET /catalog/{id}
```

### Update Catalog

```http
PUT /catalog
```

### Create Catalog

```http
POST /catalog
```

### Delete Catalog

```http
DELETE /catalog/{id}
```

## Cart

### Create Cart
```http
POST /cart
```


### Get Cart(for authorized user)

```http
GET /cart
```

### Clear Cart(for authorized user)

```http
DELETE /cart/clear
```

### Add Item to Cart(authorized user)

```http
PUT /cart/add
```



### Apply Promo Code to Cart(authorized user)

```http
PATCH /cart/promo
```


### Delete Cart Item

```http
DELETE /cart/delete/{productId}
```

## Orders

### Checkout Order(for authorized user)

```http
POST /order/checkout
```

### Cancel Order(for authorized user)

```http
DELETE /order/cancel/{orderId}
```

### Update Order Status (Admin only)

```http
Patch /order/status/{orderId}
```

### Get All Orders (Admin only)

```http
GET /order
```

### Get My Orders (for authorized user)

```http
GET /order/my
``` 

### Get Order by ID

```http
GET /order/{Id}
```

## PromoCode(admin only)

### Create PromoCode

```http
PUT /promo
```

### Get PromoCode by ID

```http
GET /promo/{id}
```

### Get All PromoCodes

```http
GET /promo
```
### Delete PromoCode

```http
DELETE /promo/{id}
```

## Email Notifications

### Send Email Notification

```http
GET /notify
```

## File

### Upload File

```http
POST /file/upload
```

### Download File

```http
GET /file/{filename}
```

### Get All File Names

```http
GET /file
```

### Delete File

```http
DELETE /file/{filename}
```



## Actuator Endpoints



### Application Info

```http
GET /actuator/info
```

## Notes

1. All endpoints require proper authentication unless specified otherwise.
2. Replace `{id}` and `{userId}` with actual IDs in the requests.
3. Timestamps should be in ISO 8601 format (e.g., "2023-11-01T10:30:00").
---

## Architecture

### Layers:
1. **Controllers**:
- Isolate the external API from the internal business logic.
2. **Services**:
- Implement the business logic of the system.
3. **Repositories**:
- Responsible for direct interaction with the database.
4. **Entities**:
- Describing database tables using JPA.
5. **DTOs**:
- Processing data for transfer between layers.

---
## Testing

- DataBase DDL 
- Postman collection

---
## Installation and launch

### Requirements
- Java 21+
- Maven
- PostgreSQL

### Launching the project
1. Clone the repository.
2. Configure the database settings in the `application.properties` file.
3. Compile the project:
```shell script
mvn clean install
```
4. Run the project:
```shell script
java -jar target/FinalProject_AutoShop-001-SNAPSHOP.jar
```
5. The application will be available on port `8081`.

---

## Contacts

- Author: Evgeniy Gorbenko
- Email: evgeniy.gorbenko2@gmail.com
