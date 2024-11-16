
# Shop Floor Assistance Backend Deployment

## Required Versions

- **Java**: 17
- **Spring Boot**: 3.3.4
- **Maven**: 3.9.9

## Running the Application

### Step 1: Set Environment Variables

Your application requires the following environment variables for proper configuration:
- `DB_URL`: Database URL
- `JWT_SECRET_KEY`: The secret key must be an HMAC hash string of 256 bits otherwise, the token generation will throw an error.
- `JWT_DURATION_IN_MILLISECONDS`: Duration of the JWT in milliseconds
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password
  The preferred way is to directly set the environment variables in `application.properties` file, rather than pass them, similarly to `application-test.properties`.
  Please examine both files, especially `spring.jpa.hibernate.ddl-auto` property.

### Step 2: Install Dependencies

To install all the necessary dependencies for the application, run the following command at the root of your project:

```bash
./mvnw clean install
```

This will download the required dependencies and compile the application.

### Step 3: Run the Application

To run the Spring Boot application, use the following Maven command:

```bash
./mvnw spring-boot:run
```

If the application starts successfully, the backend will be available at: [http://localhost:8080/](http://localhost:8080/)

### Additional Notes

- If you are running the application on a server or different environment, make sure to configure the environment variables accordingly (e.g., using `export` for Linux/macOS or `set` for Windows).
- If you need to package the application into a `.jar` file for deployment, use the following command:

  ```bash
  ./mvnw clean package
  ```

  This will create a `.jar` file in the `target` directory that you can run as a standalone application using:

  ```bash
  java -jar target/shop-floor-assistance-backend-<version>.jar
  ```

---

By following these steps, you should be able to set up and run the **Shop Floor Assistance Backend** application successfully.

