package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager() {
        intializedDatabase();

    }

    public Connection getConnection() {
        return connection;
    }

    private void intializedDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mtcgdb", "edin", "mtcg123");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Die Verbindung zur Datenbank ist misslungen");
        }

    }

}
