package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import request.CreateGameRequest;
import service.GameService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static dataaccess.DatabaseManager.getConnection;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() {}

    public void clear(){
        var clearGameData = "TRUNCATE TABLE game;";
        ClearDAO clear = new ClearDAO();
        clear.clearData(clearGameData);
    }

    public int createGame(String gameName){
        if (gameName == null){
            return 0;
        }
        var createGameSQL = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?);";
        var serializer = new Gson();
        ChessGame game = new ChessGame();
        var tempGame = serializer.toJson(game);
        Connection conn = null;

        try {
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(createGameSQL, PreparedStatement.RETURN_GENERATED_KEYS)){
                stmt.setString(1, null);
                stmt.setString(2, null);
                stmt.setString(3, gameName);
                stmt.setString(4, tempGame);
                stmt.executeUpdate();
                try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    return generatedKeys.getInt(1);
                }

            }
        }
        catch (DataAccessException | SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
        return 0;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        var getGameSQL = "SELECT whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";
        Connection conn = null;

        try{
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(getGameSQL)){
                stmt.setInt(1, gameID);
                try(ResultSet rs = stmt.executeQuery()){
                    if (rs.next()){
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String game = rs.getString("game");
                        var serializer = new Gson();
                        ChessGame tempGame = serializer.fromJson(game, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, tempGame);
                    }
                    else{
                        return null;
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e){
            throw new DataAccessException("Some error with the SQL");
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }

    public ArrayList<GameData> listGames(){
        var getGameListSQL = "SELECT * FROM game;";
        ArrayList<GameData> gameList = new ArrayList<>();
        Connection conn = null;

        try{
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(getGameListSQL)){
                try(ResultSet rs = stmt.executeQuery()){
                    while (rs.next()){
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String game = rs.getString("game");
                        var serializer = new Gson();
                        ChessGame tempGame = serializer.fromJson(game, ChessGame.class);
                        GameData myGame = new GameData(gameID, whiteUsername, blackUsername, gameName, tempGame);
                        gameList.add(myGame);
                    }
                    return gameList;
                }
            }
        }
        catch (DataAccessException | SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
        return gameList;
    }

    public void updateGame(GameData game, String username, String playerColor){
        if (Objects.equals(playerColor, "WHITE")) {
            var updateGameSQL = "UPDATE game SET whiteUsername = ? WHERE gameID = ?;";
            executeUpdate(game, username, updateGameSQL);
        }
        else if (Objects.equals(playerColor, "BLACK")) {
            var updateGameSQL = "UPDATE game SET blackUsername = ? WHERE gameID = ?;";
            executeUpdate(game, username, updateGameSQL);
        }
    }

    private void executeUpdate(GameData game, String username, String updateGameSQL) {
        Connection conn = null;
        try{
            conn = getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(updateGameSQL)){
                stmt.setString(1, username);
                stmt.setInt(2, game.gameID());
                stmt.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
}
