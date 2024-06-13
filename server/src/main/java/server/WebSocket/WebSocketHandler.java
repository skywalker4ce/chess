package server.WebSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import model.AuthData;

import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.EOFException;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.*;


@WebSocket
public class WebSocketHandler{

    Map<Integer, Set<Session>> connections = new HashMap<>();
    String playerTitle = null;
    Boolean resigned = false;
    SQLGameDAO gameFunctions = new SQLGameDAO();
    GameData chessGame;

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        try{
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(UserGameCommand.class, new UserGameCommandTypeAdapter());
            Gson gson = builder.create();

            UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

            String username = getUsername(command.getAuthString());

            saveSession(command.getGameID(), session);

            getPlayerInfo(command, username);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (Exception ex) {
            sendError(session, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, ConnectCommand command) throws Exception {
        //LOAD_GAME message
        //chessGame = gameFunctions.getGame(command.getGameID());
        LoadGameMessage loadGame = new LoadGameMessage(chessGame);
        var jsonLoadGame = new Gson().toJson(loadGame);
        session.getRemote().sendString(jsonLoadGame);

        //NOTIFICATION
        String notification = username + " joined the game as " + playerTitle;
        sendMessageToOthers(session, command, notification);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws Exception {
        if (Objects.equals(playerTitle, "OBSERVER")){
            throw new Exception("Can't make a move as an observer");
        }
        //chessGame = gameFunctions.getGame(command.getGameID());
        try {
            //VALIDATE and MAKE_MOVE
            if (!Objects.equals(chessGame.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor().toString(), playerTitle)) {
                throw new Exception("You can't move that piece");
            }
            else {
                chessGame.game().makeMove(command.getMove());
                gameFunctions.updateGame(chessGame, username, playerTitle);
                chessGame = gameFunctions.getGame(command.getGameID());
            }

            //LOAD_GAME and NOTIFICATION
            LoadGameMessage loadGame = new LoadGameMessage(chessGame);
            var jsonLoadGame = new Gson().toJson(loadGame);
            String location = "Input move here";
            String notification = username + " moved to " + location;
            Set<Session> tempSet = connections.get(command.getGameID());
            for (Session tempSession : tempSet){
                tempSession.getRemote().sendString(jsonLoadGame);
                if (!tempSession.equals(session)){
                    sendNotification(tempSession, new NotificationMessage(notification));
                }
            }

            //CHECK, CHECKMATE, and STALEMATE
            if (chessGame.game().isInStalemate(chessGame.game().getTeamTurn())){
                notification = chessGame.game().getTeamTurn() + " is in Stalemate.";
                sendMessageToAll(command, notification);
            }
            else if (chessGame.game().isInCheckmate(chessGame.game().getTeamTurn())) {
                notification = chessGame.game().getTeamTurn() + " is in Checkmate.";
                sendMessageToAll(command, notification);
            }
            else if (chessGame.game().isInCheck(chessGame.game().getTeamTurn())){
                notification = chessGame.game().getTeamTurn() + " is in Check.";
                sendMessageToAll(command, notification);
            }
        }

        catch (InvalidMoveException e){
            throw new Exception(e.getMessage());
        }
    }

    private void leaveGame(Session session, String username, LeaveCommand command) throws DataAccessException, IOException {
        //UPDATE GAME INFO
        //chessGame = gameFunctions.getGame(command.getGameID());
        gameFunctions.updateGame(chessGame, null, playerTitle);
        Set<Session> tempSet = connections.get(command.getGameID());
        tempSet.remove(session);
        resigned = false;

        //NOTIFICATION
        String notification = username + " left the game.";
        sendMessageToOthers(session, command, notification);
    }

    private void resign(Session session, String username, ResignCommand command) throws Exception {
        if (Objects.equals(playerTitle, "OBSERVER")){
            throw new Exception("Can't resign as an observer");
        }

        if (!resigned) {
            resigned = true;
        }
        else {
            throw new Exception("Already resigned.");
        }

        //UPDATE GAME TO RESIGN STATUS
        //chessGame = gameFunctions.getGame(command.getGameID());
        chessGame.game().setIsOver(true);
        gameFunctions.updateGame(chessGame, username, playerTitle);
        chessGame = gameFunctions.getGame(command.getGameID());

        //NOTIFICATION
        String notification = username + " resigned.";
        sendMessageToAll(command, notification);
    }

    private String getUsername(String authToken) throws Exception {
        SQLAuthDAO user = new SQLAuthDAO();
        AuthData auth = user.getAuth(authToken);
        if (auth == null){
            throw new Exception("Invalid authToken");
        }
        return auth.username();
    }

    private void saveSession(int gameID, Session session){
        if (connections.isEmpty() || !connections.containsKey(gameID)){
            Set<Session> sessions = new HashSet<>();
            sessions.add(session);
            connections.put(gameID, sessions);
        }
        else {
            Set<Session> tempSet = connections.get(gameID);
            tempSet.add(session);
            }
    }

    private void getPlayerInfo(UserGameCommand command, String username) throws Exception {
        chessGame = gameFunctions.getGame(command.getGameID());
        if (chessGame == null){
            throw new Exception("Invalid Game ID");
        }
        if (chessGame.blackUsername() != null && chessGame.blackUsername().equals(username)){
            playerTitle = "BLACK";
        }
        else if (chessGame.whiteUsername() != null && chessGame.whiteUsername().equals(username)){
            playerTitle = "WHITE";
        }
        else {
            playerTitle = "OBSERVER";
        }
    }

    private void sendMessageToAll(UserGameCommand command, String notification) throws IOException {
        Set<Session> tempSet = connections.get(command.getGameID());
        for (Session tempSession : tempSet){
            sendNotification(tempSession, new NotificationMessage(notification));
        }
    }

    private void sendMessageToOthers(Session session, UserGameCommand command, String notification) throws IOException {
        Set<Session> tempSet = connections.get(command.getGameID());
        for (Session tempSession : tempSet){
            if (!tempSession.equals(session)){
                sendNotification(tempSession, new NotificationMessage(notification));
            }
        }
    }

    private void sendNotification(Session session, NotificationMessage message) throws IOException {
        var jsonNotification = new Gson().toJson(message);
        session.getRemote().sendString(jsonNotification);
    }

    private void sendError(Session session, ErrorMessage error) throws IOException {
        var jsonError = new Gson().toJson(error);
        session.getRemote().sendString(jsonError);
    }

}
