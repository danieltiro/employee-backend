# Employee Management System API

A robust RESTful API for employee management built with Spring Boot. Including creation, updates, filtering, and batch operations.

## Features

- Full CRUD operations for employee management
- Batch processing with transactional integrity
- Soft delete functionality
- Advanced filtering and pagination
- API documentation with Swagger

## Tech Stack

- Java 17
- Spring Boot 3.2.x
- Spring Data JPA
- PostgreSQL
- Lombok
- Swagger/OpenAPI 3.0
- Maven

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.6+
- PostgreSQL

### 1. Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/employee-management.git
cd backend-service
```

### 2. Configure database in application.properties

- spring.datasource.url=jdbc:postgresql://server_database:server_port_database/your_database_name
- spring.datasource.username=your_username_database
- spring.datasource.password=your_password_database

### 3. Build the application

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

### Prerequisites

The API will be available at http://localhost:8081

### API Documentation

Once the application is running, you can access the Swagger UI documentation at:
http://localhost:8081/swagger-ui/index.html

### Example usage

Open a new terminal window and execute the following commands.

- Create an employee

```bash
curl --location 'http://localhost:8081/api/v1/employees' --header 'Content-Type: application/json' --data '{"name":"Daniel","firstname":"Tiro","active":true,"birthdate":"1984-01-03","genre":"M","position":"Java Programmer","dni":"TIBD841213Q10"}'
```

- Update an employee (Inserts a valid id)

```bash
curl --location --request PUT 'http://localhost:8081/api/v1/employees' --header 'Content-Type: application/json' --data '{"id":"cfb36183-0744-492a-91d6-d8a4d60f5e79","firstname":"Tiros","lastname":"Bravos","middlename":"Joav","name":"Daniel","genre":"M","birthdate":"2000-01-01","dni":"TIBD84121310","position":"Java Programmer","active":true}'
```

- Delete employee (Insert a valid id)

```bash
curl --location --request DELETE 'http://localhost:8081/api/v1/employees/cfb36183-0744-492a-91d6-d8a4d60f5e79' --data ''
```

- Get all employees

```bash
curl --location 'http://localhost:8081/api/v1/employees/all?page=0&size=5&sort=createdAt%2Casc'
```