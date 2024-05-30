package service;

import dataaccess.*;

public class ClearService {
    SQLUserDAO myUser;
    SQLAuthDAO myAuth;
    SQLGameDAO myGame;

    public ClearService(SQLUserDAO myUser, SQLAuthDAO myAuth, SQLGameDAO myGame){
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
