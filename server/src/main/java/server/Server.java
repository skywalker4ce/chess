package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import spark.*;

public class Server {
    private static MemoryGameDAO myGameMemory = new MemoryGameDAO();
    private static MemoryAuthDAO myAuthMemory = new MemoryAuthDAO();
    private static MemoryUserDAO myUserMemory = new MemoryUserDAO();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private static void createRoutes() {


        Spark.post("/user", (req, res) -> {
            Handler myHandler = new Handler();
            res.status(200);
            return myHandler.registerHandler(req);
        });


    }
}
