package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.Objects;

public class UserService {
    MemoryUserDAO myUser;
    MemoryAuthDAO myAuth;

    public UserService(MemoryUserDAO myUser, MemoryAuthDAO myAuth){
        this.myUser = myUser;
        this.myAuth = myAuth;
    }

    public AuthData register(UserData user) throws DataAccessException{
        if (myUser.getUser(user.username()) == null) {
            myUser.createUser(user.username(), user.password(), user.email());
            return login(user);
        }
        throw new DataAccessException("{ \"message\": \"Error: already taken\" }");
    }

    public AuthData login(UserData user) throws DataAccessException {
        UserData tempUser = myUser.getUser(user.username());
        if (tempUser.username() != null && Objects.equals(tempUser.password(), user.password())) {
            return myAuth.createAuth(user.username());
        }
        return null;
    }
    public void logout(String authToken) {
        if (myAuth.getAuth(authToken) != null){
            myAuth.deleteAuth(authToken);
        }
    }
}
