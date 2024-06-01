package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.DatabaseManager.getConnection;

public class SQLAuthDAO implements AuthDAO{
    private String genAuthToken = UUID.randomUUID().toString();

    public SQLAuthDAO() {}

    public void clear(){
        var clearAuthData = "DELETE FROM auth;";

        try {
            // Connect to the specific database
            var conn = getConnection();
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearAuthData)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public AuthData createAuth(String username){
        genAuthToken = UUID.randomUUID().toString();

        var createAuthSQL = "INSERT INTO auth (username, authToken) values (?, ?);";

        try {
            var conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(createAuthSQL)){
                stmt.setString(1, username);
                stmt.setString(2, genAuthToken);

                stmt.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e){
            System.out.println(e.getMessage());
        }
        return new AuthData(genAuthToken, username);
    }

    public AuthData getAuth(String authToken){
        var getAuthSQL = "SELECT authToken, username FROM auth WHERE authToken = ?;";

        try{
            var conn = getConnection();
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
    }

    public void deleteAuth(String authToken){

        var deleteAuthSQL = "DELETE FROM auth WHERE authToken = ?;";

        try {
            var conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(deleteAuthSQL)){
                stmt.setString(1, authToken);

                stmt.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
