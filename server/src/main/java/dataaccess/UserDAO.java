package dataaccess;

import model.UserData;

public interface UserDAO {
    //clears the user database;
    void clear();

    //creates a user object
    void createUser(String username, String password, String email);

    //gets a user object
    UserData getUser(String username) throws DataAccessException;
}
