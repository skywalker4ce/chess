package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.getConnection;

public class ClearDAO {

    public void clearData(String clearSQL){
        Connection conn = null;

        try {
            // Connect to the specific database
            conn = getConnection();
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearSQL)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
}
