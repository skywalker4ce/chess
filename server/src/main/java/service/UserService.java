package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    SQLUserDAO myUser;
    SQLAuthDAO myAuth;

    public UserService(SQLUserDAO myUser, SQLAuthDAO myAuth){
        this.myUser = myUser;
        this.myAuth = myAuth;
    }

    public AuthData register(UserData user) throws DataAccessException, UnauthorizedException{
        if (myUser.getUser(user.username()) == null) {
            myUser.createUser(user.username(), user.password(), user.email());
            return login(user);
        }
        throw new DataAccessException("{ \"message\": \"Error: already taken\" }");
    }

    public AuthData login(UserData user) throws DataAccessException, UnauthorizedException {
        UserData tempUser = myUser.getUser(user.username());
        if (tempUser != null && tempUser.username() != null && BCrypt.checkpw(user.password(), tempUser.password())) {
            return myAuth.createAuth(user.username());
        }
        throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
    }

    public void logout(String authToken) throws UnauthorizedException{
        if (myAuth.getAuth(authToken) != null){
            myAuth.deleteAuth(authToken);
        }
        else {
            throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
        }
    }
}
