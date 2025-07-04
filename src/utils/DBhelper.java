package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBhelper {

    private static final String DB_URL = "jdbc:sqlite:fawry.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);

    }

    public static void initializeDatabase() {

    }

}
