package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    MemoryUserDAO myUser;
    MemoryAuthDAO myAuth;

    public UserService(MemoryUserDAO myUser, MemoryAuthDAO myAuth){
        this.myUser = myUser;
        this.myAuth = myAuth;
    }

    public AuthData register(UserData user) {
        if (myUser.getUser(user.username()) == null){
            myUser.createUser(user.username(), user.password(), user.email());
            return login(user);
        }
        else
            return null; //this will need to be changed with error handling
    }
    public AuthData login(UserData user) {
        UserData tempUser = myUser.getUser(user.username());
        if (tempUser.username() != null && Objects.equals(tempUser.password(), user.password())){
            return myAuth.createAuth(user.username());
        }
        else
            return null; // this will also need to be changed
    }
    public void logout(String authToken) {
        if (myAuth.getAuth(authToken) != null){
            myAuth.deleteAuth(authToken);
        }
    }
}
