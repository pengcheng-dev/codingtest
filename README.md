
# BGL Coding Test Application

This application is based on Spring Boot backend, and a ReactJS frontend to manage `TEntryTransaction` records.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java JDK 17 or higher
- MySQL Server 8
- Node.js and npm
- Maven 3

## Installation

Follow these steps to get your development environment running:

### Clone the Repository

```bash
git clone https://github.com/pengcheng-dev/codingtest.git
```

### Set Up the Backend

Navigate to the backend directory:
```bash
cd codingtest
```

Adjust the `src/main/resources/application.properties` for your database configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Build the project with Maven:

```bash
mvn clean install
```

Run the application:

```bash
cd target
java -jar codingtest-1.0-SNAPSHOT.jar
```

The backend should now be running on `http://localhost:8080`.

### Initialize the account table
execute the `src/main/resources/initialize_account.sql` to initialize account table

### Set Up the Frontend

Navigate to the frontend directory from the root of the project:

```bash
cd ../frontend
```

Install dependencies:

```bash
npm install axios react-router-dom formik yup date-fns
```

Start the React application:

```bash
npm start
```

The frontend should now be accessible at `http://localhost:3000`.

## Usage

Use the application to create, read, update, and delete `TEntryTransaction` records. Navigate through the UI to manage these records effectively.

## Running Tests

To run backend tests:

```bash
cd codingtest
./mvn test
```