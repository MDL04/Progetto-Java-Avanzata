package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che permette di ottenere una connessione al database
 */

public class DBManager {
    private static final String DB_URL = "jdbc:sqlite:wordageddon.db";
    private static Connection connection;

    /**
     * Tenta di instaurare una connessione con il database
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
}
