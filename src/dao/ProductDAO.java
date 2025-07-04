package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import models.Product;
import utils.DBHelper;

public class ProductDAO {

    public static void addProduct(Product product) {
        String sql = "INSERT INTO products (name, price, quantity, shippable, expirable, weight, weight_unit, expiration_date) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setBoolean(4, product.isShippable());
            stmt.setBoolean(5, product.isExpirable());

            // weight and weightUnit only meaningful if shippable
            if (product.isShippable()) {
                stmt.setDouble(6, product.getWeight());
                stmt.setString(7, String.valueOf(product.getWeightUnit())); // 'g' or 'k'
            } else {
                stmt.setNull(6, java.sql.Types.REAL);
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }

            // expiration date only meaningful if expirable
            if (product.isExpirable() && product.getExpirationDate() != null) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String dateStr = formatter.format(product.getExpirationDate());
                stmt.setString(8, dateStr);
            } else {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            }

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                System.out.println("Product already exists or no rows affected.");
                return;

            } else if (rows > 1) {
                System.out.println("More than one row affected, check your database integrity.");
                return;
            } else if (rows == 1) {
                System.out.println("Product added successfully.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public static void updateProduct(Product product) {
        if (product.getQuantity() == 0) {
            // Delete the product if quantity is zero
            deleteProductByName(product.getName());
            System.out.println("Product is deleted because quantity is zero.");
            return;
        }

        String sql = "UPDATE products SET price = ?, quantity = ?, shippable = ?, expirable = ?, weight = ?, weight_unit = ?, expiration_date = ? WHERE name = ?";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, product.getPrice());
            stmt.setInt(2, product.getQuantity());
            stmt.setBoolean(3, product.isShippable());
            stmt.setBoolean(4, product.isExpirable());

            // weight and weightUnit
            if (product.isShippable()) {
                stmt.setDouble(5, product.getWeight());
                stmt.setString(6, String.valueOf(product.getWeightUnit()));
            } else {
                stmt.setNull(5, java.sql.Types.REAL);
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }

            // expiration date
            if (product.isExpirable() && product.getExpirationDate() != null) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
                stmt.setString(7, formatter.format(product.getExpirationDate()));
            } else {
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }

            stmt.setString(8, product.getName());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public static void deleteProductByName(String productName) {
        String sql = "DELETE FROM products WHERE name = ?";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }

    }

    public static Product getProductByName(String productName) {
        String sql = "SELECT * FROM products WHERE name = ?";

        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productName);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                double price = result.getDouble("price");
                int quantity = result.getInt("quantity");
                boolean shippable = result.getBoolean("shippable");
                boolean expirable = result.getBoolean("expirable");

                double weight = result.getDouble("weight");
                char weightUnit = 'g';
                String weightUnitStr = result.getString("weight_unit");
                if (weightUnitStr != null && !weightUnitStr.isEmpty()) {
                    weightUnit = weightUnitStr.charAt(0);
                }

                String expDateStr = result.getString("expiration_date");
                java.util.Date expirationDate = null;
                if (expDateStr != null) {
                    expirationDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(expDateStr);
                }

                return new Product(
                        productName,
                        price,
                        quantity,
                        shippable,
                        expirable,
                        weight,
                        weightUnit,
                        expirationDate);
            }

        } catch (SQLException | java.text.ParseException e) {
            System.out.println("Error retrieving product: " + e.getMessage());
        }

        return null;
    }

    public static ArrayList<Product> getExpiredProducts() {
        ArrayList<Product> expiredProducts = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE expirable = 1 AND expiration_date IS NOT NULL AND expiration_date < ?";

        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String today = formatter.format(new java.util.Date());

            stmt.setString(1, today);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String name = result.getString("name");
                double price = result.getDouble("price");
                int quantity = result.getInt("quantity");
                boolean shippable = result.getBoolean("shippable");
                boolean expirable = result.getBoolean("expirable");

                double weight = result.getDouble("weight");
                String weightUnitStr = result.getString("weight_unit");
                char weightUnit = (weightUnitStr != null && !weightUnitStr.isEmpty()) ? weightUnitStr.charAt(0) : 'g';

                String expDateStr = result.getString("expiration_date");
                Date expirationDate = null;
                if (expDateStr != null) {
                    expirationDate = formatter.parse(expDateStr);
                }

                Product product = new Product(
                        name,
                        price,
                        quantity,
                        shippable,
                        expirable,
                        weight,
                        weightUnit,
                        expirationDate);

                expiredProducts.add(product);
            }

        } catch (SQLException | java.text.ParseException e) {
            System.out.println("Error fetching expired products: " + e.getMessage());
        }

        if (expiredProducts.isEmpty()) {
            System.out.println("No expired products found.");
        } else {
            System.out.println("Found " + expiredProducts.size() + " expired products.");
        }

        return expiredProducts;
    }

    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                String name = result.getString("name");
                double price = result.getDouble("price");
                int quantity = result.getInt("quantity");
                boolean shippable = result.getBoolean("shippable");
                boolean expirable = result.getBoolean("expirable");

                double weight = result.getDouble("weight");
                String weightUnitStr = result.getString("weight_unit");
                char weightUnit = (weightUnitStr != null && !weightUnitStr.isEmpty()) ? weightUnitStr.charAt(0) : 'g';

                String expDateStr = result.getString("expiration_date");
                Date expirationDate = null;
                if (expDateStr != null) {
                    expirationDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(expDateStr);
                }

                Product product = new Product(
                        name,
                        price,
                        quantity,
                        shippable,
                        expirable,
                        weight,
                        weightUnit,
                        expirationDate);

                products.add(product);
            }

        } catch (SQLException | java.text.ParseException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }

        return products;
    }

    public static void clearExpiredProducts() {
        String sql = "DELETE FROM products WHERE expirable = 1 AND expiration_date IS NOT NULL AND expiration_date < ?";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String today = formatter.format(new java.util.Date());

            stmt.setString(1, today);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cleared expired products");
            } else {
                System.out.println("No expired products to clear.");
            }

        } catch (SQLException e) {
            System.out.println("Error clearing expired products: " + e.getMessage());
        }
    }

    public static void clearAllProducts() {
        String sql = "DELETE FROM products";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cleared all products");
            } else {
                System.out.println("No products to clear.");

            }

        } catch (SQLException e) {
            System.out.println("Error clearing all products: " + e.getMessage());
        }
    }

}
