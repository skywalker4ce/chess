package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class GameService {
    MemoryUserDAO myUser = new MemoryUserDAO();
    MemoryAuthDAO myAuth = new MemoryAuthDAO();
    MemoryGameDAO myGame = new MemoryGameDAO();


}
