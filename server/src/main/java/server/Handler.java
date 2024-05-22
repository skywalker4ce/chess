package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import service.UserService;
import spark.Request;
import spark.Response;

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

        var objToJson = serializer.toJson(myAuthObject);
        System.out.println(objFromJson);
        System.out.println(objToJson);
        return objToJson;
    }
}
