package dataaccess;

import model.AuthData;

public interface AuthDAO {
    //clears the auth database;
    void clear();

    //creates an auth object
    void createAuth();

    //gets an auth object
    AuthData getAuth();

    //deletes an auth object
    void deleteAuth();
}
