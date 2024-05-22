package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import request.LoginRequest;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;

public class Handler {
    private static MemoryGameDAO myGameMemory = new MemoryGameDAO();
    private static MemoryAuthDAO myAuthMemory = new MemoryAuthDAO();
    private static MemoryUserDAO myUserMemory = new MemoryUserDAO();



    public String registerHandler(Request req){
        var serializer = new Gson();
        UserService myUserService = new UserService(myUserMemory, myAuthMemory);

        //takes the object and turns it into a RegisterRequest class
        var objFromJson = serializer.fromJson(req.body(), UserData.class);

        AuthData myAuthObject = myUserService.register(objFromJson);

        return serializer.toJson(myAuthObject);
    }

    public String loginHandler(Request req){
        var serializer = new Gson();
        UserService myUserService = new UserService(myUserMemory, myAuthMemory);
        var objFromJson = serializer.fromJson(req.body(), LoginRequest.class);
        UserData tempUser = new UserData(objFromJson.username(), objFromJson.password(), "");
        AuthData myAuthObject = myUserService.login(tempUser);
        return serializer.toJson(myAuthObject);
    }

    public String logoutHandler(Request req){
        var serializer = new Gson();
        UserService myUserService = new UserService(myUserMemory, myAuthMemory);
        return ""; // change this
    }

    public String listGamesHandler(Request req){
        var serializer = new Gson();
        GameService myGameService = new GameService(myAuthMemory, myGameMemory);
        myGameService.listGames(req.headers().toString());
    }

    public String createGamesHandler(Request req){

    }

    public String joinGameHandler(Request req){

    }

    public void clearApplicationHandler(){
        ClearService myClearService = new ClearService(myUserMemory, myAuthMemory, myGameMemory);
        myClearService.clearApplication();
    }
}
