package web;

import com.google.gson.Gson;
import model.UserData;
import java.net.URI;

public class ServerFacade {
    private ServerConnector connector = new ServerConnector();

    public void register(String username, String password, String email) throws Exception {
        URI uri = new URI("http://localhost:8080/user");
        String requestType = "POST";
        UserData user = new UserData(username, password, email);
        var jsonUser = new Gson().toJson(user);
        connector.connect(uri, requestType, null, jsonUser);
    }

    public void login(){

    }

    public void logout(){

    }

    public void createGame(){

    }

    public void listGames(){

    }

    public void joinGame(){

    }

    public void clear(){

    }
}
