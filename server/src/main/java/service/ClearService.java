package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class ClearService {
    MemoryUserDAO myUser;
    MemoryAuthDAO myAuth;
    MemoryGameDAO myGame;

    public ClearService(MemoryUserDAO myUser, MemoryAuthDAO myAuth, MemoryGameDAO myGame){
        this.myUser = myUser;
        this.myAuth = myAuth;
        this.myGame = myGame;

    }

    public void clearApplication(){
        myUser.clear();
        myGame.clear();
        myAuth.clear();
    }
}
