package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import models.Customer;
import utils.DBHelper;

public class CustomerDAO {

    // Add a new customer
    public static void addCustomer(models.Customer customer) {
        String sql = "INSERT INTO customers (name, email, password, phone, address, balance) VALUES (?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = utils.DBHelper.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPassword());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getAddress());
            pstmt.setDouble(6, customer.getBalance());
            pstmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    public static ArrayList<Customer> getAllCustomers() {
        ArrayList<models.Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (java.sql.Connection conn = utils.DBHelper.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
                java.sql.ResultSet result = pstmt.executeQuery()) {
            while (result.next()) {
                models.Customer customer = new models.Customer(result.getString("name"), result.getString("email"),
                        result.getString("password"), result.getString("phone"), result.getString("address"),
                        result.getDouble("balance"));

                System.out.println(result.getDouble("balance"));
                customers.add(customer);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error retrieving customers: " + e.getMessage());
        }
        return customers;
    }

    // Get a customer by email
    public static models.Customer getCustomer(String identifier, String password) {
        String sql = "SELECT * FROM customers WHERE (email = ? OR phone = ?) AND password = ?";
        try (java.sql.Connection conn = utils.DBHelper.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier);
            pstmt.setString(3, password);
            java.sql.ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                return new models.Customer(result.getString("name"), result.getString("email"),
                        result.getString("password"),
                        result.getString("phone"), result.getString("address"), result.getDouble("balance"));
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error retrieving customer: " + e.getMessage());
        }
        return null; // Return null if no customer found
    }

    // Update a customer's balance
    public static void updateCustomerBalance(String identifier, double newBalance) {
        String sql = "UPDATE customers SET balance = ? WHERE email = ? OR phone = ?";

        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);// Round to 2 decimal places
            stmt.setString(2, identifier); // as email
            stmt.setString(3, identifier); // as phone

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No customer found with the given email or phone.");
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error updating customer balance: " + e.getMessage());
        }

    }

}
