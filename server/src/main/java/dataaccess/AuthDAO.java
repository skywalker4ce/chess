package dataaccess;

import model.AuthData;

public interface AuthDAO {
    //clears the auth database;
    void clear();

    //creates an auth object
    String createAuth(String username);

    //gets an auth object
    AuthData getAuth(String authToken);

    //deletes an auth object
    void deleteAuth(String authToken);
}