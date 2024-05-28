package server;

import dataaccess.DataAccessException;
import service.UnauthorizedException;
import spark.*;

public class Server {


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
            catch(UnauthorizedException e){
                res.status(401);
                return e.getMessage();
            }
        });

        Spark.delete("/session", (req, res) -> {
            try {
                res.status(200);
                return myHandler.logoutHandler(req);
            }
            catch(UnauthorizedException e){
                res.status(401);
                return e.getMessage();
            }
        });

        Spark.get("/game", (req, res) -> {
            try {
                res.status(200);
                return myHandler.listGamesHandler(req);
            }
            catch (UnauthorizedException e){
                res.status(401);
                return e.getMessage();
            }
        });

        Spark.post("/game", (req, res) -> {
            try {
                res.status(200);
                return myHandler.createGamesHandler(req);
            }
            catch (UnauthorizedException e){
                res.status(401);
                return e.getMessage();
            }
        });

        Spark.put("/game", (req, res) -> {
            try {
                res.status(200);
                return myHandler.joinGameHandler(req);
            }
            catch (BadRequestException e){
                res.status(400);
                return e.getMessage();
            }
            catch (UnauthorizedException e){
                res.status(401);
                return e.getMessage();
            }
            catch (DataAccessException e){
                res.status(403);
                return e.getMessage();
            }
        });

        Spark.delete("/db", (req, res) -> {
            res.status(200);
            myHandler.clearApplicationHandler();
            return "{}";
        });
    }
}
