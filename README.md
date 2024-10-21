# Meeting Calendar Assistant API

## Overview

The Meeting Calendar Assistant API provides a comprehensive solution for managing employee calendars and scheduling meetings. This API allows users to create employee records, book meetings, check for available time slots, and efficiently manage scheduling conflicts.

## Technologies Used

- **Java 17**: The programming language used for building the application.
- **Spring Boot**: A framework to simplify the development of Java applications and to create standalone, production-ready applications.
- **Spring Data JPA**: Used for data access and management with MySQL.
- **MySQL**: The relational database used for storing employee and meeting data.
- **Lombok**: A Java library to reduce boilerplate code through annotations.
- **Dotenv**: A library for loading environment variables from a `.env` file.
- **Maven**: The build automation tool used for project management and dependency management.

## Prerequisites

- **Java 17** or higher
- **Maven**
- **MySQL Database**
- **Postman** (for testing APIs)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/RohanEkashinge/Meeting-Calendar-Assistant.git
cd Meeting_Calendar_Assistant
```

### 2. Create a .env File
In the root directory of the project, create a .env file with the following content:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/userDatabase
SPRING_DATASOURCE_USERNAME=userName
SPRING_DATASOURCE_PASSWORD=userMysqlPassword

```
Replace the placeholders:

```bash userDatabase``` : The name of your MySQL database.

```bash userName```     : Your MySQL username.

``` bash userMysqlPassword``` : Your MySQL password.

### 3. Specify the Path of the .env File
In the root directory of the project, create a .env file with the following content:

```bash
Dotenv dotenv = Dotenv.configure()
                .directory("path/to/your/project/root") // Full path to the project root
                .load();

```

### 4. Run the Application
Once the .env file is configured, you can start the Spring Boot application using the following command:

```bash
mvn spring-boot:run
```
### 5. Access API Documentation
In the root directory of the project, you will find the API documentation file located at:

```bash
docs/meeting_calendar_assistant_api.postman_collection.json
```
You can import this file into Postman for testing the API endpoints.

## API Usage Instructions

### 1. Create Employees:

- First, create an employee by sending a POST request to ```bash/api/employees``` with the following JSON body:
```bash
{
    "id": "employee1",
    "name": "John Doe"
}
```

- Next, create another employee by sending a POST request to ```bash /api/employees``` with the following JSON body:
```bash
{
    "id": "employee2",
    "name": "Jonny Eod"
}
```

### 2. Book a Meeting:

- Send a POST request to ```bash /api/meetings``` to book a meeting for employee1
``` bash
{
    "title": "Team Standup",
    "startTime": "2024-10-21T09:00:00",
    "endTime": "2024-10-21T09:30:00",
    "employee": {
        "id": "employee1"
    }
}
```

### 3. Get Free Slots:

- Send a GET request to ```bash /api/meetings/free-slots?employeeIds=employee1,employee2&duration=30``` to check for free slots for both employees.

### 4. Check Conflicts:

Send a POST request to ```bash /api/meetings/check-conflicts?participantIds=employee1,employee2``` to check for conflicts when trying to schedule another meeting.


## JUnit Tests
This project includes unit tests written using JUnit to ensure the functionality of the service layer. The tests can be found in the 

``` bash 
src/test/java/com/example/Meeting_Calendar_Assistant/service serviceImpl
```
directory.

### Running the Tests
To run the tests, you can use your IDE's built-in test runner or use the command line. Here are the steps for both:


**Using an IDE**

#### 1. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).

#### 2. Navigate to the 
```bash
src/test/java/com/example/Meeting_Calendar_Assistant/service/serviceImpl
```
directory.

#### 3. Right-click on the test class (e.g., EmployeeServiceImplTest.java or MeetingServiceImplTest.java) and select Run 'EmployeeServiceImplTest' (or the appropriate test).

**Using Command Line**

If you are using Maven, you can run the tests from the command line by executing:
```bash
mvn test
```

### Test Coverage
The tests cover the following functionalities:

-  EmployeeServiceImpl
   
   - Saving an employee
   - Handling validation and exceptions (if applicable)

- MeetingServiceImpl
    - Booking a meeting
    - Finding meetings for an employee
    - Checking for meeting conflicts
    - Finding free slots for meetings

Make sure to run the tests after making any changes to the service layer to ensure everything is working correctly.


This API provides a robust framework for managing meetings and employee schedules. Follow the instructions above to set up your environment, run the application, and test the API endpoints effectively.
