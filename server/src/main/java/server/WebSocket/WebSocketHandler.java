package server.WebSocket;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@WebSocket
public class WebSocketHandler{

    Map<Integer, Set<Session>> connections = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        try{
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(UserGameCommand.class, new UserGameCommandTypeAdapter());
            Gson gson = builder.create();

            UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

            String username = getUsername(command.getAuthString());

            saveSession(command.getGameID(), session);

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
        GameData game = new SQLGameDAO().getGame(command.getGameID());
        if (game == null){
            throw new Exception("Invalid Game ID");
        }
        LoadGameMessage loadGame = new LoadGameMessage(game);
        var jsonLoadGame = new Gson().toJson(loadGame);
        session.getRemote().sendString(jsonLoadGame);

        //NOTIFICATION
        String title;
        if (game.blackUsername().equals(username)){
            title = "BLACK";
        }
        else if (game.whiteUsername().equals(username)){
            title = "WHITE";
        }
        else {
            title = "OBSERVER";
        }
        Set<Session> tempSet = connections.get(command.getGameID());
        for (Session tempSession : tempSet){
            if (!tempSession.equals(session)){
                String notification = username + " joined the game as " + title;
                sendNotification(tempSession, new NotificationMessage(notification));
            }
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand command){

    }

    private void leaveGame(Session session, String username, LeaveCommand command){

    }

    private void resign(Session session, String username, ResignCommand command){

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

    private void sendNotification(Session session, NotificationMessage message) throws IOException {
        var jsonNotification = new Gson().toJson(message);
        session.getRemote().sendString(jsonNotification);
    }

    private void sendError(Session session, ErrorMessage error) throws IOException {
        var jsonError = new Gson().toJson(error);
        session.getRemote().sendString(jsonError);
    }

}
