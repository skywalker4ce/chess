package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.getConnection;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() {}

    public void clear(){
        var clearGameData = "DELETE FROM game;";

        try {
            // Connect to the specific database
            var conn = getConnection();
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearGameData)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
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
