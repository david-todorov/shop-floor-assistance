# shop-floor-assistance
Project for developing operator guides and assistance tools for pharmaceutical production lines.

## Docker

### Prerequisites

Before running the application, ensure you have the following installed:

- **Docker Engine**: Make sure Docker is installed and running on your machine.
- **Docker Compose**: You will also need Docker Compose. It typically comes with Docker Desktop, but you can install it separately.

### Running the whole application with Docker Compose

To run both the backend and frontend applications using Docker Compose, follow these steps:

1. **Navigate to the root directory of the project**:
   ```bash
   cd ~/path/to/shop-floor-assistance

2. **Compose up command**:
   ```bash
   docker-compose up --build --detach

### Running runnig Backend or Frontend individually 
   ```bash
   docker-compose up --build --detach backend
   ```bash
   docker-compose up --build --detach frontend

### Stopping containers
   ```bash
   docker-compose down
   ```bash
   docker-compose stop backend
   ```bash
   docker-compose stop frontend


### Ports Used
1. The backend will be available at http://localhost:8080
2. The frontend will be available at http://localhost (on port 80). The Angular application runs on top of Nginx, which is why it uses port 80 instead of the default Angular development server port (4200).

