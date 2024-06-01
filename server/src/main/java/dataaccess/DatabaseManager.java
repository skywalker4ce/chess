package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {                //I changed this to be public!! Not sure if that's allowed
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void createTables() throws DataAccessException {
        var clearUserData = "DELETE FROM user;";
        var clearAuthData = "DELETE FROM auth;";
        var clearGameData = "DELETE FROM game;";

        var userTableCreationSQL = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + ".user (" +
                "username VARCHAR(20) PRIMARY KEY NOT NULL, " +
                "password VARCHAR(72) NOT NULL, " +
                "email VARCHAR(30) " +
                ");";

        var authTableCreationSQL = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + ".auth (" +
                "authToken VARCHAR(40) PRIMARY KEY NOT NULL, " +
                "username VARCHAR(20) NOT NULL " +
                ");";

        var gameTableCreationSQL = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + ".game (" +
                "gameID INT PRIMARY KEY AUTO_INCREMENT, " +
                "whiteUsername VARCHAR(20), " +
                "blackUsername VARCHAR(20), " +
                "gameName VARCHAR(20), " +
                "chessGame LONGTEXT " +
                ");";

        try {
            // Connect to the specific database
            String databaseURL = CONNECTION_URL + '/' + DATABASE_NAME;
            Connection conn = DriverManager.getConnection(databaseURL, USER, PASSWORD);
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearUserData)) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearAuthData)) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearGameData)) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(userTableCreationSQL)) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(authTableCreationSQL)) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(gameTableCreationSQL)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
