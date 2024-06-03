package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.DatabaseManager.getConnection;

public class SQLAuthDAO implements AuthDAO{
    private String genAuthToken = UUID.randomUUID().toString();

    public SQLAuthDAO() {}

    public void clear(){
        var clearAuthData = "TRUNCATE TABLE auth;";
        Connection conn = null;

        try {
            // Connect to the specific database
            conn = getConnection();
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearAuthData)) {
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

    public AuthData createAuth(String username) throws DataAccessException {
        genAuthToken = UUID.randomUUID().toString();
        Connection conn = null;

        var createAuthSQL = "INSERT INTO auth (username, authToken) values (?, ?);";

        try {
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(createAuthSQL)){
                stmt.setString(1, username);
                stmt.setString(2, genAuthToken);

                stmt.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e){
            throw new DataAccessException("Username can't be empty");
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
        return new AuthData(genAuthToken, username);
    }

    public AuthData getAuth(String authToken){
        var getAuthSQL = "SELECT authToken, username FROM auth WHERE authToken = ?;";
        Connection conn = null;

        try{
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(getAuthSQL)){
                stmt.setString(1, authToken);
                try(ResultSet rs = stmt.executeQuery()){
                    if (rs.next()){
                        authToken = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthData(authToken, username);
                    }
                    else{
                        return null;
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e){
            return null;                                            //might need to change this
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

    public void deleteAuth(String authToken){
        var deleteAuthSQL = "DELETE FROM auth WHERE authToken = ?;";
        Connection conn = null;

        try {
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(deleteAuthSQL)){
                stmt.setString(1, authToken);

                stmt.executeUpdate();

            }
        }
        catch (DataAccessException | SQLException e){
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
