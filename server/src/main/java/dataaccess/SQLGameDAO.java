package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() {}

    public void clear(){

    }

    public int createGame(String gameName){
        return 1;
    }

    public GameData getGame(int gameID){
        return new GameData(1, "", "", "", new ChessGame());
    }

    public ArrayList<GameData> listGames(){
        return new ArrayList<>();
    }

    public void updateGame(GameData game, String username, String playerColor){

    }
}
