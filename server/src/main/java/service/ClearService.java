package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class ClearService {
    MemoryUserDAO myUser = new MemoryUserDAO();
    MemoryAuthDAO myAuth = new MemoryAuthDAO();
    MemoryGameDAO myGame = new MemoryGameDAO();

    public void clearApplication(){
        myUser.clear();
        myGame.clear();
        myAuth.clear();
    }
}
