package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;
import request.LoginRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import service.ClearService;
import service.GameService;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;

import java.util.ArrayList;

public class Handler {
    private static MemoryGameDAO myGameMemory = new MemoryGameDAO();
    private static MemoryAuthDAO myAuthMemory = new MemoryAuthDAO();
    private static MemoryUserDAO myUserMemory = new MemoryUserDAO();

    private void checkInput(UserData obj) throws BadRequestException{
        if (obj.username() == null || obj.password() == null){
            throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
        }
    }

    public String registerHandler(Request req) throws DataAccessException, BadRequestException, UnauthorizedException {
        try {
            var serializer = new Gson();
            UserService myUserService = new UserService(myUserMemory, myAuthMemory);
            //takes the object and turns it into a RegisterRequest class
            var objFromJson = serializer.fromJson(req.body(), UserData.class);
            checkInput(objFromJson);
            AuthData myAuthObject = myUserService.register(objFromJson);
            return serializer.toJson(myAuthObject);
        }
        catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
        catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public String loginHandler(Request req) throws DataAccessException, UnauthorizedException {
        try {
            var serializer = new Gson();
            UserService myUserService = new UserService(myUserMemory, myAuthMemory);
            var objFromJson = serializer.fromJson(req.body(), LoginRequest.class);
            UserData tempUser = new UserData(objFromJson.username(), objFromJson.password(), "");
            AuthData myAuthObject = myUserService.login(tempUser);
            return serializer.toJson(myAuthObject);
        }
        catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public String logoutHandler(Request req) throws UnauthorizedException{
        try {
            UserService myUserService = new UserService(myUserMemory, myAuthMemory);
            var authToken = req.headers("Authorization");
            myUserService.logout(authToken);
            return "";
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public String listGamesHandler(Request req){
        var serializer = new Gson();
        GameService myGameService = new GameService(myAuthMemory, myGameMemory);
        ArrayList<GameData> myGameList = myGameService.listGames(req.headers().toString());
        var myListGameResult = new ListGamesResult(myGameList);
        return serializer.toJson(myListGameResult);
    }

    public String createGamesHandler(Request req) throws UnauthorizedException{
        try {
            var serializer = new Gson();
            GameService myGameService = new GameService(myAuthMemory, myGameMemory);
            int tempGameID = 0;
            var authToken = req.headers("Authorization");
            tempGameID = myGameService.createGame(authToken, req.body());
            var myCreateGameResult = new CreateGameResult(tempGameID);
            return serializer.toJson(myCreateGameResult);
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        }

    }

    public String joinGameHandler(Request req) throws UnauthorizedException, BadRequestException, DataAccessException{
        try {
            var serializer = new Gson();
            GameService myGameService = new GameService(myAuthMemory, myGameMemory);
            var objFromJson = serializer.fromJson(req.body(), JoinGameRequest.class);
            var authToken = req.headers("Authorization");
            myGameService.joinGame(authToken, objFromJson.playerColor(), objFromJson.gameID());
            return "";
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        }
        catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clearApplicationHandler(){
        ClearService myClearService = new ClearService(myUserMemory, myAuthMemory, myGameMemory);
        myClearService.clearApplication();
    }
}
