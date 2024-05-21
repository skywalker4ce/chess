package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import spark.Request;

public class Handler {
    private static MemoryGameDAO myGameMemory = new MemoryGameDAO();
    private static MemoryAuthDAO myAuthMemory = new MemoryAuthDAO();
    private static MemoryUserDAO myUserMemory = new MemoryUserDAO();

    public UserData registerHandler(Request req){
        var serializer = new Gson();

        var objFromJson = serializer.fromJson(req.body(), UserData.class);
        System.out.println(objFromJson);
        return objFromJson;
    }
}
