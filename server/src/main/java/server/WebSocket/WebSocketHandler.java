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
import websocket.messages.LoadGameMessage;

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
            ex.printStackTrace();
           //sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException, DataAccessException {
        //LOAD_GAME message
        GameData game = new SQLGameDAO().getGame(command.getGameID());
        LoadGameMessage loadGame = new LoadGameMessage(game);
        var jsonLoadGame = new Gson().toJson(loadGame);
        session.getRemote().sendString(jsonLoadGame);

        //NOTIFICATION
        Set<Session> tempSet = connections.get(command.getGameID());
        for (Session tempSession : tempSet){
            if (!tempSession.equals(session)){
                sendNotification(tempSession, "Random message");
            }
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand command){

    }

    private void leaveGame(Session session, String username, LeaveCommand command){

    }

    private void resign(Session session, String username, ResignCommand command){

    }

    private String getUsername(String authToken){
        SQLAuthDAO user = new SQLAuthDAO();
        AuthData auth = user.getAuth(authToken);
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

    private void sendNotification(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }

}
