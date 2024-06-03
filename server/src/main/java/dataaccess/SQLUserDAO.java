package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO(){}

    private String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void clear(){
        var clearUserData = "TRUNCATE TABLE user;";
        ClearDAO clear = new ClearDAO();
        clear.clearData(clearUserData);
    }

    public void createUser(String username, String password, String email) throws DataAccessException {
        var createUserSQL = "INSERT INTO user (username, password, email) values (?, ?, ?)";
        Connection conn = null;

        String newPassword = hashPassword(password);
        try {
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(createUserSQL)){
                stmt.setString(1, username);
                stmt.setString(2, newPassword);
                stmt.setString(3, email);

                stmt.executeUpdate();

            }
        }
        catch (DataAccessException | SQLException e){
            throw new DataAccessException("fields can't be empty");
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

    public UserData getUser(String username) throws DataAccessException{
        var getUserSQL = "SELECT username, password, email FROM user WHERE username = ?";
        Connection conn = null;

        try{
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(getUserSQL)){
                stmt.setString(1, username);
                try(ResultSet rs = stmt.executeQuery()){
                    if (rs.next()){
                        username = rs.getString("username");
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserData(username, password, email);
                    }
                    else{
                        return null;
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e){
            throw new DataAccessException("Some error with the SQL");
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
