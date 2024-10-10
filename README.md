
# shop-floor-assistance
Project for developing operator guides and assistance tools for pharmaceutical production lines.

## Docker

### Prerequisites

Before running the application, ensure you have the following installed:

- **Docker Engine**: Make sure Docker is installed and running on your machine.
- **Docker Compose**: Docker Compose is also required. It typically comes with Docker Desktop, but you can install it separately if needed.

### Running the Application with Docker Compose

To run both the backend and frontend applications using Docker Compose, follow these steps:

1. **Navigate to the root directory of the project**:
   ```bash
   cd ~/path/to/shop-floor-assistance
   ```

2. **Run the Compose command**:
   ```bash
   docker-compose up --build --detach
   ```

### Running Backend, Frontend, or Database Individually

- **Running only the Backend** (will start the database as a dependency):
   ```bash
   docker-compose up --build --detach backend
   ```

- **Running only the Frontend**:
   ```bash
   docker-compose up --build --detach frontend
   ```

- **Running only the Database**:
   ```bash
   docker-compose up --build --detach database
   ```

### Stopping Containers

- **Stopping all containers**:
   ```bash
   docker-compose down
   ```

- **Stopping only the Backend**:
   ```bash
   docker-compose stop backend
   ```

- **Stopping only the Frontend**:
   ```bash
   docker-compose stop frontend
   ```

- **Stopping only the Database**:
   ```bash
   docker-compose stop database
   ```

### Environment Variables

- **`.env` file**: An `.env` file, required for creating the database and backend container, is included in the repository as an example.
- **Running Backend Locally**: If you run the backend from an IDE, ensure the database is started first. Identical environment variables should be set to match those in Docker.
- **Local Database URL**: If the backend runs outside a Docker container, make sure the `databaseUrl` points to your local database (e.g., `localhost`).

### Database Data Persistence

- The database used in this application is configured to be persistent by utilizing Docker volumes.
- In the `docker-compose.yml` file, a named volume (`postgres_data`) is created and mapped to the PostgreSQL container’s data directory (`/var/lib/postgresql/data`)
- Additionally, the data is stored in a folder named `database` in the repository.
- This setup ensures that all database data remains intact even when the container is stopped or removed, allowing for seamless data management across container lifecycles.

### Ports Used

- **Backend**: Available at [http://localhost:8080](http://localhost:8080)
- **Frontend**: Available at [http://localhost](http://localhost) on port 80 (served by Nginx, not Angular's development server).
