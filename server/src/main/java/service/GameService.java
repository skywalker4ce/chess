package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    MemoryAuthDAO myAuth = new MemoryAuthDAO();
    MemoryGameDAO myGame = new MemoryGameDAO();

    public ArrayList<GameData> listGames(String authToken){
        if (myAuth.getAuth(authToken) != null){
            return myGame.listGames();
        }
        else
            return null; // CHANGE!!!
    }

    public int createGame(String authToken, String gameName){
        if (myAuth.getAuth(authToken) != null){
            return myGame.createGame(gameName);
        }
        else
            return 0; //Change this
    }

    public void joinGame(String authToken, String playerColor, int gameID){
        AuthData tempAuth = myAuth.getAuth(authToken);
        if (tempAuth != null){
            GameData tempGame = myGame.getGame(gameID);
            if (tempGame != null){
                myGame.updateGame(tempGame, tempAuth.username(), playerColor);
            }
        }
    }
}
