package web;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {
    private ServerConnector connector = new ServerConnector();

    public String register(String username, String password, String email) throws Exception {
        URI uri = new URI("http://localhost:8080/user");
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
        URI uri = new URI("http://localhost:8080/session");
        String requestType = "POST";
        LoginRequest user = new LoginRequest(username, password);
        var jsonUser = new Gson().toJson(user);
        String responseString = connector.connect(uri, requestType, null, jsonUser);
        AuthData classObject = new Gson().fromJson(responseString, AuthData.class);
        return classObject.authToken();
    }

    public String logout(String authToken) throws Exception {
        URI uri = new URI("http://localhost:8080/session");
        String requestType = "DELETE";
        connector.connect(uri, requestType, authToken, null);
        return null;
    }

    public void createGame(String gameName, String authToken) throws Exception {
        URI uri = new URI("http://localhost:8080/game");
        String requestType = "POST";
        CreateGameRequest game = new CreateGameRequest(gameName);
        var jsonGame = new Gson().toJson(game);
        connector.connect(uri, requestType, authToken, jsonGame);
    }

    public void listGames(String authToken) throws Exception {
        URI uri = new URI("http://localhost:8080/game");
        String requestType = "GET";
        connector.connect(uri, requestType, authToken, null);
    }

    public void joinGame(String playerColor,int gameID, String authToken) throws Exception {
        URI uri = new URI("http://localhost:8080/game");
        String requestType = "PUT";
        JoinGameRequest game = new JoinGameRequest(playerColor, gameID);
        var jsonGame = new Gson().toJson(game);
        connector.connect(uri, requestType, authToken, jsonGame);
    }

    public void clear() throws Exception {
        URI uri = new URI("http://localhost:8080/db");
        String requestType = "DELETE";
        connector.connect(uri, requestType, null, null);
    }
}
