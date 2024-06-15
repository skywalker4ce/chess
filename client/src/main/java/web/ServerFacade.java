package web;

import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import result.ListGamesResult;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

public class ServerFacade {
    private HttpCommunicator connector = new HttpCommunicator();
    int port;
    Menu menu;
    WebSocketCommunicator communicator;

    public ServerFacade(int port){
        this.port = port;
    }

    public ServerFacade(int port, Menu menu) throws Exception {
        this.port = port;
        this.menu = menu;
        this.communicator = new WebSocketCommunicator(port, menu);
    }

    public String register(String username, String password, String email) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/user");
        String requestType = "POST";
        UserData user = new UserData(username, password, email);
        Gson gson = new Gson();
        var jsonUser = gson.toJson(user);
        String responseString = connector.connect(uri, requestType, null, jsonUser);
        if (responseString != null) {
            AuthData classObject = gson.fromJson(responseString, AuthData.class);
            return classObject.authToken();
        }
        else
            return null;
    }

    public String login(String username, String password) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/session");
        String requestType = "POST";
        LoginRequest user = new LoginRequest(username, password);
        var jsonUser = new Gson().toJson(user);
        String responseString = connector.connect(uri, requestType, null, jsonUser);
        if (responseString != null) {
            AuthData classObject = new Gson().fromJson(responseString, AuthData.class);
            return classObject.authToken();
        }
        else
            return null;
    }

    public void logout(String authToken) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/session");
        String requestType = "DELETE";
        if (connector.connect(uri, requestType, authToken, null) == null){
            throw new Exception("AuthToken error");
        }
    }

    public Boolean createGame(String gameName, String authToken) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/game");
        String requestType = "POST";
        CreateGameRequest game = new CreateGameRequest(gameName);
        var jsonGame = new Gson().toJson(game);
        String response = connector.connect(uri, requestType, authToken, jsonGame);
        return response != null;
    }

    public ArrayList<GameData> listGames(String authToken) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/game");
        String requestType = "GET";
        String responseString = connector.connect(uri, requestType, authToken, null);
        if (!Objects.equals(responseString, "Error")){
            ListGamesResult list = new Gson().fromJson(responseString, ListGamesResult.class);
            if (list != null) {
                return list.games();
            }
            else {
                return null;
            }
        }
        else{
            throw new Exception("AuthToken Error");
        }
    }

    public String joinGame(String playerColor, int gameID, String authToken) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/game");
        String requestType = "PUT";
        JoinGameRequest game = new JoinGameRequest(playerColor, gameID);
        var jsonGame = new Gson().toJson(game);
        return connector.connect(uri, requestType, authToken, jsonGame);
    }

    public void clear() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/db");
        String requestType = "DELETE";
        connector.connect(uri, requestType, null, null);
    }

    public void connect(String authToken, int gameID) throws Exception {
        ConnectCommand connect = new ConnectCommand(authToken, gameID);
        var jsonConnect = new Gson().toJson(connect);
        communicator.send(jsonConnect);
    }

    public void makeMove(ChessMove move, String authToken, int gameID) throws Exception {
        MakeMoveCommand makeMove = new MakeMoveCommand(authToken, gameID, move);
        var jsonMakeMove = new Gson().toJson(makeMove);
        communicator.send(jsonMakeMove);
    }

    public void resign(String authToken, int gameID) throws Exception {
        ResignCommand resign = new ResignCommand(authToken, gameID);
        var jsonResign = new Gson().toJson(resign);
        communicator.send(jsonResign);
    }

    public void leave(String authToken, int gameID) throws Exception {
        LeaveCommand leave = new LeaveCommand(authToken, gameID);
        var jsonLeave = new Gson().toJson(leave);
        communicator.send(jsonLeave);
    }
}
