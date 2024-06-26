package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    //clears the game database;
    void clear();

    //creates a game object
    int createGame(String gameName);

    //gets a game object
    GameData getGame(int gameID) throws DataAccessException;

    //returns a list of the games
    ArrayList<GameData> listGames();

    //updates the game
    void updateGame(GameData game, String username, String playerColor);
}
