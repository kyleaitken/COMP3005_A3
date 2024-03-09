# COMP3005_A3

Kyle Aitken
COMP 3005 A3 P1
101280013

This application provides some basic functions for performing operations on the students table in the PostgreSQL database

# Pre-requesites

Ensure you have the following installed on your machine:

- Java Development Kit 17 (JDK)
- PostgreSQL

### Clone the Repository

```bash
git clone https://github.com/kyleaitken/COMP3005_A3.git
``` 

# Databse Set Up
1. Create a PostgreSQL database named A3
2. Update the url, user, and password variables in src/main/java/run/Run.java with your PostgreSQL credentials.

3. Create the students table in pgAdmin:

CREATE TABLE Students (
student_id SERIAL PRIMARY KEY,
first_name VARCHAR(255) NOT NULL,
last_name VARCHAR(255) NOT NULL,
email VARCHAR(255) UNIQUE NOT NULL,
enrollment_date DATE DEFAULT CURRENT_DATE
);

4. Insert some initial data into the students table:

INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES
('John', 'Doe', 'john.doe@example.com', '2023-09-01'),
('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),
('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');


# Compiling and Running with Gradle
Navigate to the project directory and run:
```bash
./gradlew build
./gradlew run
```

# Compiling and Running without Gradle
Navigate to the root direction of the project

Compile:
```bash
javac -cp .:lib/postgresql-42.6.1.jar -d build/classes/java/main src/main/java/run/Run.java
```
Run:
```bash
java -cp build/classes/java/main:lib/postgresql-42.6.1.jar run.Run
```