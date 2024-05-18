package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private final ArrayList<GameData> gameDataArrayList = new ArrayList<>();
    private int genGameID = 0;

    public MemoryGameDAO(){}

    @Override
    public String toString() {
        return "MemoryGameDAO{" +
                "gameDataArrayList=" + gameDataArrayList +
                ", genGameID=" + genGameID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryGameDAO that = (MemoryGameDAO) o;
        return genGameID == that.genGameID && Objects.equals(gameDataArrayList, that.gameDataArrayList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameDataArrayList, genGameID);
    }

    @Override
    public void clear(){
        if (!gameDataArrayList.isEmpty()){
            gameDataArrayList.clear();
        }
    }

    @Override
    public void createGame(String gameName){
        genGameID++;
        GameData myGame = new GameData(genGameID, "White = null", "Black = null", gameName, new ChessGame());
        gameDataArrayList.add(myGame);
    }

    @Override
    public GameData getGame(int gameID){
        for (GameData game : gameDataArrayList){
            if (game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames(){
        ArrayList<GameData> gameList = new ArrayList<>();
        if(!gameDataArrayList.isEmpty()) {
            gameList.addAll(gameDataArrayList);
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData game, String username, String playerColor){
        GameData updatedGame = game;
        if(Objects.equals(playerColor, "BLACK")){
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        else if (Objects.equals(playerColor, "WHITE")){
            updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        }
        int index = gameDataArrayList.indexOf(game);
        gameDataArrayList.set(index, updatedGame);
    }
}
