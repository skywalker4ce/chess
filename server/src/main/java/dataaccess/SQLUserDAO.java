package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.getConnection;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO(){}

    private String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void clear(){
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

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User created successfully.");
                } else {
                    System.out.println("Failed to create user.");
                }
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
