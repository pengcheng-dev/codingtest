
# BGL Coding Test Application

This application is based on Spring Boot backend, and a ReactJS frontend to manage `TEntryTransaction` records.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java JDK 17 or higher
- MySQL Server 8
- Node.js and npm
- Maven 3
- Docker Desktop

## Installation

Follow these steps to get your development environment running:

### Clone the Repository

```bash
git clone https://github.com/pengcheng-dev/codingtest.git
```

### Set Up using Docker Desktop
```bash
cd codingtest
```
Build Images:
```bash
docker-compose build
```
Run Containers:
```bash
docker-compose up
```
Access the application at: `http://localhost:3000`.

Stop the Application:
```bash
docker-compose down
```

### Set Up manually step by step
### Set Up the Backend

Navigate to the tools directory:
```bash
cd codingtest/tools
```
Generate encrypted password for mysql:
```bash
java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="yourMySQLPassword" password="yourSecretKey" algorithm=PBEWithMD5AndTripleDES
```

Navigate to the backend directory:
```bash
cd codingtest/backend
```

Adjust the `src/main/resources/application.properties` for your database configuration:

```properties
jasypt.encryptor.password=yourSecretKey
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=ENC(encrypted_password)
```

Build the project with Maven:

```bash
mvn clean install
```

Run the application:

```bash
cd target
java -jar backend-1.0-SNAPSHOT.jar
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
cd codingtest/backend
./mvn test
```

## Architecture & Design

### Entity Design

This project uses a series of JPA entities to model the database schema for handling various types of `EntryTransaction` records. Below is a breakdown of the design decisions and relationships among entities:

#### TEntryTransaction
- **Purpose**: Represents transactions in the system, serving as the central entity around which the application is built.
- **Relationships**:
    - **ManyToOne with TAccount**: Each transaction is linked to a specific account, indicating the ownership or origin of the transaction.
    - **Polymorphism with Entry types**: Utilizes inheritance to connect various entry types such as `BasicBankEntry`, `InvestmentEntry`, etc.

#### Entry Types (Inheritance Strategy)
- **Design Choice**: Used the `JOINED` strategy for inheritance to optimize performance and data normalization.
- **Types**:
    - `BasicBankEntry`: Represents basic banking transactions.
    - `InvestmentEntry`: Caters to investments with specific attributes.
- **Benefits**:
    - **Flexibility**: Allows easy addition of new types.
    - **Maintainability**: Changes to common fields need to be updated in one place only.

### Data Integrity and Scalability
- **Foreign Key Constraints**: Ensure data integrity through explicit constraints in the database schema, and subtype entries will be deleted as an orphan.
- **Indexes**: TODO: Create indexes to improve query performance, especially important as the dataset grows.

### Future Considerations
- **Scalability**: The design supports scaling by allowing for easy sharding of the `TEntryTransaction` table based on `accountId` or other columns.