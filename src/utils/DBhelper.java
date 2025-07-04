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
                    + "expiration_date TEXT DEFAULT NULL,"
                    + " UNIQUE(name, expiration_date) "

                    + ");";

            stmt.execute(createProductsTable);
            System.out.println("Products table created successfully");
            // Create Customers table if it doesn't exist
            String createCustomersTable = "CREATE TABLE IF NOT EXISTS customers ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "email TEXT NOT NULL UNIQUE, "
                    + "password TEXT NOT NULL, "
                    + "phone TEXT NOT NULL UNIQUE, "
                    + "address TEXT NOT NULL"
                    + ");";
            stmt.execute(createCustomersTable);
            System.out.println("Customers table created successfully");
            System.out.println("Database initialized successfully");

        } catch (SQLException e) {
            System.out.println("DB initialization error: " + e.getMessage());
        }

    }
}
