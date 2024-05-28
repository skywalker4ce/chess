package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import server.BadRequestException;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    MemoryAuthDAO myAuth = new MemoryAuthDAO();
    MemoryGameDAO myGame = new MemoryGameDAO();

    public GameService(MemoryAuthDAO myAuth, MemoryGameDAO myGame){
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public ArrayList<GameData> listGames(String authToken) throws UnauthorizedException{
        if (myAuth.getAuth(authToken) != null){
            return myGame.listGames();
        }
        throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException{
        if (myAuth.getAuth(authToken) != null){
            return myGame.createGame(gameName);
        }
        throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws UnauthorizedException, BadRequestException, DataAccessException {
        AuthData tempAuth = myAuth.getAuth(authToken);
        if (tempAuth != null){
            GameData tempGame = myGame.getGame(gameID);
            if (tempGame != null){
                if (playerColor == null){
                    throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
                }
                else if ((tempGame.blackUsername() != null && playerColor.equals("BLACK")) ||
                        (tempGame.whiteUsername() != null && playerColor.equals("WHITE"))){
                    throw new DataAccessException("{ \"message\": \"Error: already taken\" }");
                }
                myGame.updateGame(tempGame, tempAuth.username(), playerColor);
            }
            else {
                throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
            }
        }
        else{
            throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
        }
    }
}
