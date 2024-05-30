package dataaccess;

import model.UserData;

import java.sql.PreparedStatement;

import static dataaccess.DatabaseManager.getConnection;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO(){}

    public void clear(){
    }

    public void createUser(String username, String password, String email){
        createUserSQL

        try {
            var conn = getConnection();
            try(PreparedStatement preparedStatement = conn.prepareStatement(createUserSQL)){

            }
        }
        catch (DataAccessException e){
            System.out.println(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException{
        return new UserData("","","");
    }
}
