package run;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class Run {
    private static Connection connection;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Connect to postgreSQL server
        String url = "jdbc:postgresql://localhost:5432/A3";
        String user = "postgres";
        String password = "12311";

        scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server.");
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }


        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    getAllStudents();
                    break;

                case 2:
                    System.out.println("Enter First Name:");
                    String fName = scanner.nextLine();
                    System.out.println("Enter Last Name:");
                    String lName = scanner.nextLine();
                    System.out.println("Enter Email:");
                    String email = scanner.nextLine();
                    System.out.println("Enter Enrollment Date (eg 2014-09-02):");
                    String dateString = scanner.nextLine();

                    addStudent(fName, lName, email, dateString);
                    break;

                case 3:
                    System.out.println("Enter Student ID to Update:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter New Email:");
                    String newEmail = scanner.nextLine();

                    updateStudentEmail(id, newEmail);
                    break;

                case 4:
                    System.out.println("Enter Student ID to Delete:");
                    int deleteId = scanner.nextInt();
                     scanner.nextLine();

                    deleteStudent(deleteId);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
        
        
    }

    // getAllStudents() retrieves all student tuples from the database
    private static void getAllStudents() {
        String query = "SELECT * FROM students";
        try (
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {
                
                while (resultSet.next()) {
                    String id = resultSet.getString("student_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String enrollmentDate = resultSet.getString("enrollment_date");

                    // Print or process the retrieved data
                    System.out.println("Student ID: " + id + ", Name: " + firstName + " " + lastName + ", Email: " + 
                                        email + ", Enrollment Date: " + enrollmentDate);
                }
        } catch (SQLException e) {
            System.out.println("Error getting students.");
            e.printStackTrace();
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

    // addStudent(fName, lName, email, dateString) adds a new student to the database
    private static void addStudent(String fName, String lName, String email, String dateString) {
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

    // updateStudentEmail(id, newEmail) updates the email of the student matching the id to the newEmail
    private static void updateStudentEmail(int id, String newEmail) {
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

    // deteleStudent(id) deletes the student matching the provided id from the database 
    private static void deleteStudent(int id) {
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

