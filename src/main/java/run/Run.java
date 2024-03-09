package run;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class Run {
    public static void main(String[] args) {
        // Connect to postgreSQL server
        String url = "jdbc:postgresql://localhost:5432/A3";
        String user = "postgres";
        String password = "12311";

        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the PostgreSQL server.");

            while (true) {
                printMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        getAllStudents(connection);
                        break;
                    case 2:
                        addStudent(connection, scanner);
                        break;
                    case 3:
                        updateStudentEmail(connection, scanner);
                        break;
                    case 4:
                        deleteStudent(connection, scanner);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }   
    }

    private static void getAllStudents(Connection connection) throws SQLException {
        String query = "SELECT * FROM students";
        try (
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {
                
                System.out.println("Executing get all students query:");

                while (resultSet.next()) {
                    String id = resultSet.getString("student_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String enrollmentDate = resultSet.getString("enrollment_date");

                    // Print or process the retrieved data
                    System.out.println("Student ID: " + id + ", Name: " + firstName + " " + lastName + ", Email: " + email + ", Enrollment Date: " + enrollmentDate);
                }
        }
    }

    private static void printMenu() {
        System.out.println("Select database operation:");
        System.out.println("1 - Get all students");
        System.out.println("2 - Add student");
        System.out.println("3 - Update student email");
        System.out.println("4 - Delete student");
        System.out.println("0 - Exit");
    }

    private static void addStudent(Connection connection, Scanner scanner) {
        System.out.println("Enter First Name:");
        String fName = scanner.nextLine();
        System.out.println("Enter Last Name:");
        String lName = scanner.nextLine();
        System.out.println("Enter Email:");
        String email = scanner.nextLine();
        System.out.println("Enter Enrollment Date (eg 2014-09-02):");
        String dateString = scanner.nextLine();
        java.sql.Date date = java.sql.Date.valueOf(dateString);

        String query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, email);
            preparedStatement.setDate(4, date);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student added successfully.");
            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding student.");
            e.printStackTrace();
        }
    }

    private static void updateStudentEmail(Connection connection, Scanner scanner) {
        System.out.println("Enter Student ID to Update:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New Email:");
        String newEmail = scanner.nextLine();

        String query = "UPDATE students SET email = ? WHERE student_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student email updated successfully.");
            } else {
                System.out.println("Failed to update student email.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating student.");
            e.printStackTrace();
        }

    }

    private static void deleteStudent(Connection connection, Scanner scanner) {
        System.out.println("Enter Student ID to Delete:");
        int id = scanner.nextInt();
        scanner.nextLine();

        String query = "DELETE FROM students WHERE student_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Failed to delete student.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student.");
            e.printStackTrace();
        }
    }
}

