# API Documentation

## Authentication Endpoint

#### `POST /auth/login`
- **Description**:This endpoint authenticates users (either 'editor' or 'operator') based on their credentials. Upon successful authentication, a JSON Web Token (JWT) is issued, along with timestamps for token creation and expiration.
- **Request Body**:
  - `LoginUserRequestTO`: The authentication request
- **Throws**:
  - Exception if provided credentials does not match a user in database
- **Response**: Authentication response as `AuthenticationUserResponseTO`
---

## Editors Endpoints
### Orders
#### `GET /editor/orders`
- **Description**: Returns a list of all available orders in the system.
- **Response**: List of orders as `EditorOrderTO` from the database.
---
#### `POST /editor/orders`
- **Description**: Creates and saves a new order, returning the saved order as a `EditorOrderTO`.
- **Request Body**:
  - `EditorOrderTO`: The new order
- **Throws**:
  - Exception if the provided order number is `null`.
  - Exception if an order with the same `order number` already exists.
  - Exception if any name property from Order level to Item level is `null` or `empty`
  - Exception if the provided `product after`  does `not exist` but can be null
  - Exception if the provided `product before`  does `not exist` but can be null
  - Exception if the provided `equipment` list is `null` but can be empty
  - Exception if any of the `equipment` list elements are `null` or does `not exist`
- **Response**: The saved order as `EditorOrderTO`
---
#### `PUT /editor/orders/{orderId}`
- **Description**: Updates an existing order.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be updated.
- **Request Body**:
- `EditorOrderTO`: The updated order
- **Throws**:
  - Exception if the provided order number or orderId is `null`.
  - Exception if the provided ID does not exist in the database.
  - Exception if another order has the same `order number`, as it cannot be renamed.
  - Exception if any name property from Order level to Item level is `null` or `empty`
  - Exception if the provided `product after`  does `not exist` but can be null
  - Exception if the provided `product before`  does `not exist` but can be null
  - Exception if the provided `equipment` list is `null` but can be empty
  - Exception if any of the `equipment` list elements are `null` or does `not exist`
- **Response**: The updated order as `EditorOrderTO`
---
#### `DELETE /editor/orders/{orderId}`
- **Description**: Deletes an existing order identified by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be deleted.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: A confirmation message indicating successful deletion or an error message if the deletion fails.
---
#### `GET /editor/orders/{orderId}`
- **Description**: Retrieves an existing order as `EditorOrderTO` identified by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be retrieved.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: The requested  order as `EditorOrderTO`
---

###Products
#### `GET /editor/products`
- **Description**: Returns a list of all available products in the system.
- **Response**: List of products as `EditorProductTO` from the database.
---
#### `POST /editor/products`
- **Description**: Creates and saves a new product, returning the saved product as a `EditorProductTO`.
- **Request Body**:
  - `EditorProductTO`: The new product
- **Throws**:
  - Exception if the provided `product number` is `null`.
  - Exception if a product with the same `product number` already exists.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The saved order as `EditorProductTO`
---
#### `PUT /editor/products/{productId}`
- **Description**: Updates an existing product.
- **Parameters**:
  - `{productId}`: The unique identifier of the product to be updated.
- **Request Body**:
  - `EditorProductTO`: The updated product
- **Throws**:
  - Exception if the provided `product number` or `productId` is `null`.
  - Exception if the provided `productId` does not exist in the database.
  - Exception if another product has the same `product number`, as it cannot be renamed.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The updated product as `EditorProductTO`
---
#### `DELETE /editor/products/{productId}`
- **Description**: Deletes an existing product identified by the specified `productId`.
- **Parameters**:
  - `{productId}`: The unique identifier of the product to be deleted.
- **Throws**:
  - Exception if the provided `productId` is `null`.
  - Exception if the product with the specified `productId` does not exist in the database.
- **Response**: A confirmation message indicating successful deletion or an error message if the deletion fails.
---
#### `GET /editor/products/{productId}`
- **Description**: Retrieves an existing product as `EditorProductTO` identified by the specified `productId`.
- **Parameters**:
  - `{id}`: The unique identifier of the product to be retrieved.
- **Throws**:
  - Exception if the provided `productId` is `null`.
  - Exception if the product with the specified `productId` does not exist in the database.
- **Response**: The requested  product as `EditorProductTO`
---

###Equipment
#### `GET /editor/equipment`
- **Description**: Returns a list of all available equipment as `EditorEquipmentTO` in the system.
- **Response**: List of equipment as `EditorEquipmentTO` from the database.
#### `POST /editor/equipment`
- **Description**: Creates and saves new equipment, returning the saved equipment as a `EditorEquipmentTO`.
- **Request Body**:
  - `EditorEquipmentTO`: The new equipment
- **Throws**:
  - Exception if the provided equipment number is `null`.
  - Exception if equipment with the same `equipment number` already exists.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The saved order as `EditorEquipmentTO`
---
#### `PUT /editor/equipment/{equipmentId}`
- **Description**: Updates an existing equipment.
- **Parameters**:
  - `{equipmentId}`: The unique identifier of the equipment to be updated.
- **Request Body**:
  - `EditorEquipmentTO`: The updated equipment
- **Throws**:
  - Exception if the provided equipment number or equipmentId is `null`.
  - Exception if the provided ID does not exist in the database.
  - Exception if another equipment has the same `equipment number`, as it cannot be renamed.
  - Exception if any mandatory property  is `null` or `empty`
- **Response**: The updated equipment as `EditorEquipmentTO`
---
#### `DELETE /editor/equipment/{equipmentId}`
- **Description**: Deletes existing equipment identified by the specified `equipmentId`.
- **Parameters**:
  - `{equipmentId}`: The unique identifier of the equipment to be deleted.
- **Throws**:
  - Exception if the provided `equipmentId` is `null`.
  - Exception if the equipment with the specified `equipmentId` does not exist in the database.
- **Response**: A confirmation message indicating successful deletion or an error message if the deletion fails.
---
#### `GET /editor/equipment/{equipmentId}`
- **Description**: Retrieves existing equipment as `EditorEquipmentTO` identified by the specified `equipmentId`.
- **Parameters**:
  - `{equipmentId}`: The unique identifier of the equipment to be retrieved.
- **Throws**:
  - Exception if the provided `equipmentId` is `null`.
  - Exception if the equipment with the specified `equipmentId` does not exist in the database.
- **Response**: The requested  equipment as `EditorEquipmentTO`
---

### Suggestions
#### `GET /editor/equipment/suggestions`
- **Description**: Retrieves top `n` referenced equipment.
- **Response**: List of `EditorEquipmentTO`
---
#### `GET /editor/products/suggestions`
- **Description**: Retrieves top `n` referenced products.
- **Response**: List of `EditorProductTO`
---
#### `POST /editor/workflows/suggestions`
- **Description**: Retrives list of workflows from orders, which consider the provided product
  as `after` and does `not` have  `before` product
- **Request Body**:
  - `EditorProductTO`: The criteria for suggestions
- **Response**: List of `EditorWorkflowTO`
---
#### `POST /editor/tasks/suggestions`
- **Description**: Retrives list of tasks from orders, which consider the provided product
  as `after` and does `not` have  `before` product
- **Request Body**:
  - `EditorProductTO`: The criteria for suggestions
- **Response**: List of `EditorTaskTO`
---
#### `POST /editor/items/suggestions`
- **Description**: Retrives list of items from orders, which consider the provided product
  as `after` and does `not` have  `before` product
- **Request Body**:
  - `EditorProductTO`: The criteria for suggestions
- **Response**: List of `EditorItemTO`
---

## Operator Endpoints

### Orders
#### `GET /operator/orders`
- **Description**: Returns a list of all available orders in the system
- **Response**: List of orders as `OperatorOrderTO` from the database.
---
#### `GET /operator/orders/{orderId}`
- **Description**: Retrieves an existing order identified by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be retrieved.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: The requested  order as `OperatorOrderTO`
---

### Executions
#### `POST /operator/start/{orderId}`
- **Description**:  Starts an execution of an order and returns started `OperatorExecutionTO`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be started.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: Started execution as `OperatorExecutionTO`
---
#### `PUT /operator/finish/{executionId}`
- **Description**:  Finish an execution of an order and returns finished `OperatorExecutionTO`.
- **Parameters**:
  - `{executionId}`: The unique identifier of the started execution.
- **Throws**:
  - Exception if the provided `executionId` is `null`.
  - Exception if the execution with the specified `executionId` does not exist in the database.
- **Response**: Finished execution as `OperatorExecutionTO`
---
#### `PUT /operator/abort/{executionId}`
- **Description**:  Abort an execution of an order and returns aborted `OperatorExecutionTO`.
- **Parameters**:
  - `{executionId}`: The unique identifier of the started execution.
- **Throws**:
  - Exception if the provided `executionId` is `null`.
  - Exception if the execution with the specified `executionId` does not exist in the database.
- **Response**: Aborted execution as `OperatorExecutionTO`
---
#### `GET /operator/forecast/{orderId}`
- **Description**: Retrieves a forecast for an order by the specified `orderId`.
- **Parameters**:
  - `{orderId}`: The unique identifier of the order to be forecasted.
- **Throws**:
  - Exception if the provided `orderId` is `null`.
  - Exception if the order with the specified `orderId` does not exist in the database.
- **Response**: The requested  forecast as `OperatorForecastTO`
---
