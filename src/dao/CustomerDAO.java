package dao;

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

    // Get a customer by email
    public static models.Customer getCustomer(String identifier, String password) {
        String sql = "SELECT * FROM customers WHERE (email = ? OR phone = ?)";
        try (java.sql.Connection conn = utils.DBHelper.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier);
            // pstmt.setString(3, password);
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

        try (java.sql.Connection conn = utils.DBHelper.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, identifier); // as email
            pstmt.setString(3, identifier); // as phone

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No customer found with the given email or phone.");
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error updating customer balance: " + e.getMessage());
        }
    }

}
