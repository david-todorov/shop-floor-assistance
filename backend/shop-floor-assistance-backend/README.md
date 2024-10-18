
# Shop Floor Assistance Backend

## Required Versions

- **Java**: 17
- **Spring Boot**: 3.3.4

## Running the Application

1. Use Maven to install the dependencies

2. Then Run the ShopFloorAssistanceBackendApplication, if the application successfully starts the backend will be available at: http://localhost:8080/

# API Documentation

## User Login Endpoint

`POST /auth/login`

### Description
This endpoint authenticates users (either 'editor' or 'operator') based on their credentials. Upon successful authentication, a JSON Web Token (JWT) is issued, along with timestamps for token creation and expiration.

### Request Body
The login request requires a JSON body with the following parameters:

```json
{
    "username": "operator",
    "password": "operator"
}
```

### Responses

On successful authentication, the API responds with a JSON object based on the `AuthenticationUserResponseTO` class, containing the token, creation timestamp, and expiration timestamp.

#### Success Response

```json
{ 
	"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
	 "createdAt": "2024-10-15 12:30:00",
	 "expiresAt": "2024-10-15 13:30:00"
 }
 ```

#### Errors Responses:

#### Invalid Credentials

-   **Status Code:** 401 Unauthorized

-   **Response:**
```json    
{
    "error": "Invalid username or password."
}
```


#### Forbidden Access

-   **Status Code:** 403 Forbidden

-   **Response:**
```json
{
    "error": "Access to this resource is forbidden."
}
```

### Authorization Header for Protected Endpoints

Once authenticated, include the token in the `Authorization` header to access other secured endpoints, for example with `Bearer`

### Usage Example

#### cURL Example
```bash
curl -X POST http:/localhost/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "operator",
    "password": "operator"
  }'
  ```
## Editors Endpoints

#### `GET /editor/orders`
- **Description**: Returns a list of all available orders in the system.
- **Response**: List of orders as TOs from the database.

---

#### `POST /editor/orders`
- **Description**: Creates and saves a new order, returning the saved order as a Transfer Object (TO).
- **Throws**:
    - Exception if the provided order number is `null`.
    - Exception if an order with the same `order number` already exists.
- **Response**: The saved order as TO
---

#### `PUT /editor/orders/{id}`
- **Description**: Updates an existing order.
- **Parameters**:
    - `{id}`: The unique identifier of the order to be updated.
- **Throws**:
    - Exception if the provided order number or ID is `null`.
    - Exception if the provided ID does not exist in the database.
    - Exception if another order has the same `order number`, as it cannot be renamed.
- **Response**: The updated order as TO
---

#### `DELETE /editor/orders/{id}`
- **Description**: Deletes an existing order identified by the specified `id`.
- **Parameters**:
    - `{id}`: The unique identifier of the order to be deleted.
- **Throws**:
    - Exception if the provided `id` is `null`.
    - Exception if the order with the specified `id` does not exist in the database.
- **Response**: A confirmation message indicating successful deletion or an error message if the deletion fails.

---
