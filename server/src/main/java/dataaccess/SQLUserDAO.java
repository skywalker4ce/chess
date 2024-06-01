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
        var clearUserData = "DELETE FROM user;";

        try {
            // Connect to the specific database
            var conn = getConnection();
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearUserData)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createUser(String username, String password, String email){
        var createUserSQL = "INSERT INTO user (username, password, email) values (?, ?, ?)";

        String newPassword = hashPassword(password);
        try {
            var conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(createUserSQL)){
                stmt.setString(1, username);
                stmt.setString(2, newPassword);
                stmt.setString(3, email);

                stmt.executeUpdate();

            }
        }
        catch (DataAccessException | SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException{
        var getUserSQL = "SELECT username, password, email FROM user WHERE username = ?";

        try{
            var conn = getConnection();
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

    }
}
