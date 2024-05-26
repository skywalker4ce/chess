package server;

import dataaccess.DataAccessException;
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
        Handler myHandler = new Handler();

        Spark.post("/user", (req, res) -> {
            try {
                res.status(200);
                return myHandler.registerHandler(req);
            }
            catch(DataAccessException e){
                res.status(403);
                return e.getMessage();
            }
            catch(BadRequestException e){
                res.status(400);
                return e.getMessage();
            }
        });

        Spark.post("/session", (req, res) -> {
            try {
                res.status(200);
                return myHandler.loginHandler(req);
            }
            catch(DataAccessException e){
                res.status(403);
                return e.getMessage();
            }
        });

        Spark.delete("/session", (req, res) -> {
            res.status(200);
            return myHandler.logoutHandler(req);
        });

        Spark.get("/game", (req, res) -> {
            res.status(200);
            return myHandler.listGamesHandler(req);
        });

        Spark.post("/game", (req, res) -> {
            res.status(200);
            return myHandler.createGamesHandler(req);
        });

        Spark.put("/game", (req, res) -> {
            res.status(200);
            return myHandler.joinGameHandler(req);
        });

        Spark.delete("/db", (req, res) -> {
            res.status(200);
            myHandler.clearApplicationHandler();
            return "";
        });
    }
}
