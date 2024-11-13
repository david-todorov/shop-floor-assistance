
# Shop Floor Assistance Backend

## Required Versions

- **Java**: 17
- **Spring Boot**: 3.3.4

## Running the Application

1. Use Maven to install the dependencies

2. Then Run the ShopFloorAssistanceBackendApplication, if the application successfully starts the backend will be available at: http://localhost:8080/

# API Documentation

## User Login Endpoint

#STABLE
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

#STABLE
#### `GET /editor/orders`
- **Description**: Returns a list of all available orders in the system.
- **Response**: List of orders as TOs from the database.
---

#STABLE
#### `POST /editor/orders`
- **Description**: Creates and saves a new order, returning the saved order as a Transfer Object (TO).
- **Throws**:
  - Exception if the provided order number is `null`.
  - Exception if an order with the same `order number` already exists.
  - Exception if any name property from Order level to Item level is `null` or `empty`
  - Exception if the provided `product after`  does `not exist` but can be null
  - Exception if the provided `product before`  does `not exist` but can be null
  - Exception if the provided `equipment` list is `null` but can be empty
  - Exception if any of the `equipment` list elements are `null` or does `not exist`
- **Response**: The saved order as TO
---

#STABLE
#### `PUT /editor/orders/{orderId}`
- **Description**: Updates an existing order.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be updated.
- **Throws**:
  - Exception if the provided order number or orderId is `null`.
  - Exception if the provided ID does not exist in the database.
  - Exception if another order has the same `order number`, as it cannot be renamed.
  - Exception if any name property from Order level to Item level is `null` or `empty`
  - Exception if the provided `product after`  does `not exist` but can be null
  - Exception if the provided `product before`  does `not exist` but can be null
  - Exception if the provided `equipment` list is `null` but can be empty
  - Exception if any of the `equipment` list elements are `null` or does `not exist`
- **Response**: The updated order as TO
---

#STABLE
#### `DELETE /editor/orders/{orderId}`
- **Description**: Deletes an existing order identified by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be deleted.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: A confirmation message indicating successful deletion or an error message if the deletion fails.
---

#STABLE
#### `GET /editor/orders/{orderId}`
- **Description**: Retrieves an existing order identified by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be retrieved.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: The requested  order as TO
---

#STABLE
#### `GET /editor/products`
- **Description**: Returns a list of all available products in the system.
- **Response**: List of products as TOs from the database.

#STABLE
#### `POST /editor/products`
- **Description**: Creates and saves a new product, returning the saved product as a Transfer Object (TO).
- **Throws**:
  - Exception if the provided order number is `null`.
  - Exception if a product with the same `product number` already exists.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The saved order as TO
---

#STABLE
#### `PUT /editor/products/{productId}`
- **Description**: Updates an existing product.
- **Parameters**:
  - `{productId}`: The unique identifier of the product to be updated.
- **Throws**:
  - Exception if the provided product number or productId is `null`.
  - Exception if the provided productId does not exist in the database.
  - Exception if another product has the same `product number`, as it cannot be renamed.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The updated product as TO
---

#STABLE
#### `DELETE /editor/products/{productId}`
- **Description**: Deletes an existing product identified by the specified `productId`.
- **Parameters**:
  - `{productId}`: The unique identifier of the product to be deleted.
- **Throws**:
  - Exception if the provided `productId` is `null`.
  - Exception if the product with the specified `productId` does not exist in the database.
- **Response**: A confirmation message indicating successful deletion or an error message if the deletion fails.
---

#STABLE
#### `GET /editor/products/{productId}`
- **Description**: Retrieves an existing product identified by the specified `productId`.
- **Parameters**:
  - `{id}`: The unique identifier of the product to be retrieved.
- **Throws**:
  - Exception if the provided `productId` is `null`.
  - Exception if the product with the specified `productId` does not exist in the database.
- **Response**: The requested  product as TO
---

#STABLE
#### `GET /editor/equipment`
- **Description**: Returns a list of all available equipment in the system.
- **Response**: List of equipment as TOs from the database.

#STABLE
#### `POST /editor/equipment`
- **Description**: Creates and saves new equipment, returning the saved equipment as a Transfer Object (TO).
- **Throws**:
  - Exception if the provided equipment number is `null`.
  - Exception if equipment with the same `equipment number` already exists.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The saved order as TO
---

#STABLE
#### `PUT /editor/equipment/{equipmentId}`
- **Description**: Updates an existing equipment.
- **Parameters**:
  - `{equipmentId}`: The unique identifier of the equipment to be updated.
- **Throws**:
  - Exception if the provided equipment number or equipmentId is `null`.
  - Exception if the provided ID does not exist in the database.
  - Exception if another equipment has the same `equipment number`, as it cannot be renamed.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The updated equipment as TO
---

#STABLE
#### `DELETE /editor/equipment/{equipmentId}`
- **Description**: Deletes existing equipment identified by the specified `equipmentId`.
- **Parameters**:
  - `{equipmentId}`: The unique identifier of the equipment to be deleted.
- **Throws**:
  - Exception if the provided `equipmentId` is `null`.
  - Exception if the equipment with the specified `equipmentId` does not exist in the database.
- **Response**: A confirmation message indicating successful deletion or an error message if the deletion fails.
---

#STABLE
#### `GET /editor/equipment/{equipmentId}`
- **Description**: Retrieves existing equipment identified by the specified `equipmentId`.
- **Parameters**:
  - `{equipmentId}`: The unique identifier of the equipment to be retrieved.
- **Throws**:
  - Exception if the provided `equipmentId` is `null`.
  - Exception if the equipment with the specified `equipmentId` does not exist in the database.
- **Response**: The requested  equipment as TO
---


###Suggestions
#STABLE, BUT WITHOUT ORDERS IS USELESS
#### `GET /editor/equipment/suggestions`
- **Description**: Retrieves top `n` referenced equipment.
- **Response**: List of `equipment`TO
---

#STABLE, BUT WITHOUT ORDERS IS USELESS
#### `GET /editor/products/suggestions`
- **Description**: Retrieves top `n` referenced products.
- **Response**: List of `products` TO
---

#STABLE, BUT WITHOUT ORDERS IS USELESS
#### `GET /editor/workflows/suggestions`
- **Description**: Retrives list of workflows from orders, which consider the provided product
  as `after` and does `not` have  `before` product
- **Response**: List of `products` TO
---


## Operator Endpoints
#STABLE
#### `GET /operator/orders`
- **Description**: Returns a list of all available orders in the system
- **Response**: List of orders as TOs from the database.
---

#STABLE
#### `GET /operator/orders/{orderId}`
- **Description**: Retrieves an existing order identified by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be retrieved.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: The requested  order as TO, examine please the OperatorOrderTO, if some questions ask me
---

#STABLE
#### `POST /operator/start/{orderId}`
- **Description**:  Starts an execution of an order and returns `started` execution TO.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be started.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: Started execution as `execution TO`
---

#STABLE
#### `PUT /operator/finish/{executionId}`
- **Description**:  Finish an execution of an order and return the `finished` execution TO.
- **Parameters**:
  - `{executionId}`: The unique identifier of the started execution.
- **Throws**:
  - Exception if the provided `executionId` is `null`.
  - Exception if the execution with the specified `executionId` does not exist in the database.
- **Response**: Finished execution as `execution TO`
---

#STABLE
#### `PUT /operator/abort/{executionId}`
- **Description**:  Abort an execution of an order and return `aborted` execution TO.
- **Parameters**:
  - `{executionId}`: The unique identifier of the started execution.
- **Throws**:
  - Exception if the provided `executionId` is `null`.
  - Exception if the execution with the specified `executionId` does not exist in the database.
- **Response**: Aborted execution as `execution TO`
---

#STABLE
#### `GET /operator/forecast/{orderId}`
- **Description**: Retrieves a forecast for an order by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be forecasted.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: The requested  forecast as TO
---
