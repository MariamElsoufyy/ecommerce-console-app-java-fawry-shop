package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

    private static final String DB_URL = "jdbc:sqlite:fawryShop.db";

    // connect to database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);

    }

    // intialize database //it has only one table
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create products table if it doesn't exist
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL UNIQUE, "
                    + "price REAL NOT NULL, "
                    + "quantity INTEGER NOT NULL DEFAULT 0, "
                    + "shippable BOOLEAN NOT NULL DEFAULT 0, "
                    + "expirable BOOLEAN NOT NULL DEFAULT 0, "
                    + "weight REAL DEFAULT NULL, "
                    + "weight_unit CHAR(1) DEFAULT NULL, "
                    + "expiration_date TEXT DEFAULT NULL"
                    + ");";

            stmt.execute(createProductsTable);
            System.out.println("Products table created successfully");
            // Insert sample products
            String insertSampleProducts = """
                        INSERT OR IGNORE INTO products
                        (name, price, quantity, shippable, expirable, weight, weight_unit, expiration_date)
                        VALUES
                        ('milk', 25.5, 20, 0, 1, NULL, NULL, '2025-10-01'),
                        ('choco', 15.0, 50, 1, 0, 200.0, 'g', NULL),
                        ('det', 45.0, 10, 1, 0, 1.0, 'k', NULL),
                        ('yog', 10.0, 30, 0, 1, NULL, NULL, '2025-07-10'),
                        ('cup', 100.0, 15, 0, 0, NULL, NULL, NULL);

                    """;

            stmt.execute(insertSampleProducts);
            System.out.println("Sample products inserted successfully");

            // Create Customers table if it doesn't exist
            String createCustomersTable = "CREATE TABLE IF NOT EXISTS customers ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "email TEXT NOT NULL UNIQUE, "
                    + "password TEXT NOT NULL, "
                    + "phone TEXT NOT NULL UNIQUE, "
                    + "address TEXT NOT NULL,"
                    + "balance REAL NOT NULL DEFAULT 0.0"
                    + ");";
            stmt.execute(createCustomersTable);
            System.out.println("Customers table created successfully");
            System.out.println("Database initialized successfully");

        } catch (SQLException e) {
            System.out.println("DB initialization error: " + e.getMessage());
        }

    }
}
