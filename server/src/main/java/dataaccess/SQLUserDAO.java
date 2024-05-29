package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO(){}

    public void clear(){
    }

    public void createUser(String username, String password, String email){

    }

    public UserData getUser(String username) throws DataAccessException{
        return new UserData("","","");
    }
}
