# HRGenius

HRGenius is a Spring Boot based HR management system for employee data, recruitment, payroll, performance, and analytics.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

## Run Backend

From the project root:

### PowerShell

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
.\mvnw spring-boot:run
```

### CMD

```cmd
set DB_USERNAME=root && set DB_PASSWORD=your_password && mvnw spring-boot:run
```

## Default Database

- URL: `jdbc:mysql://localhost:3306/hrgenius`
- Driver: `com.mysql.cj.jdbc.Driver`

Create the `hrgenius` database in MySQL before starting the backend.
