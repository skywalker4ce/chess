package client;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import web.ServerFacade;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void each() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Register Positive Test")
    void registerPositive() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authToken.length() > 10);
    }

    @Test
    @Order(2)
    @DisplayName("Register Negative Test")
    void registerNegative() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        String authToken = facade.register("player1", "password", "p1@email.com");
        Assertions.assertNull(authToken);
    }

    @Test
    @Order(3)
    @DisplayName("Login Positive Test")
    void loginPositive() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.logout(authToken);
        String newAuthToken = facade.login("player1", "password");
        Assertions.assertTrue(newAuthToken.length() > 10);
    }

    @Test
    @Order(4)
    @DisplayName("Login Negative Test")
    void loginNegative() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.logout(authToken);
        String newAuthToken = facade.login("player1", "password1");
        Assertions.assertNull(newAuthToken);
    }

    @Test
    @Order(5)
    @DisplayName("Logout Positive Test")
    void logoutPositive() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.logout(authToken);
        try {
            facade.logout(authToken);
        }
        catch (Exception e) {
            Assertions.assertEquals("AuthToken error", e.getMessage());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Logout Negative Test")
    void logoutNegative() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        try {
            facade.logout("Random authToken");
        }
        catch (Exception e) {
            Assertions.assertEquals("AuthToken error", e.getMessage());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Create Game Positive Test")
    void createGamePositive() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        Boolean created = facade.createGame("myGame", authToken);
        Assertions.assertTrue(created);
    }

    @Test
    @Order(8)
    @DisplayName("Create Game Negative Test")
    void createGameNegative() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        Boolean created = facade.createGame("myGame", "Random AuthToken");
        Assertions.assertFalse(created);
    }

    @Test
    @Order(9)
    @DisplayName("List Games Positive Test")
    void listGamesPositive() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.createGame("myGame1", authToken);
        facade.createGame("myGame2", authToken);
        facade.createGame("myGame3", authToken);
        ArrayList<GameData> games = facade.listGames(authToken);
        Assertions.assertEquals(3, games.size());
    }

    @Test
    @Order(10)
    @DisplayName("List Games Negative Test")
    void listGamesNegative() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        ArrayList<GameData> noGames = facade.listGames(authToken);
        Assertions.assertEquals(0, noGames.size());
        facade.createGame("myGame1", authToken);
        try {
            ArrayList<GameData> games = facade.listGames("Random AuthToken");
        }
        catch (Exception e){
            Assertions.assertEquals("AuthToken Error", e.getMessage());
        }
    }

    @Test
    @Order(11)
    @DisplayName("Join Game Positive Test")
    void joinGamePositive() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.createGame("myGame1", authToken);
        facade.createGame("myGame2", authToken);
        facade.createGame("myGame3", authToken);
        ArrayList<GameData> games = facade.listGames(authToken);
        GameData joinedGame = games.get(1);
        facade.joinGame("WHITE", joinedGame.gameID(), authToken);
        games = facade.listGames(authToken);
        joinedGame = games.get(1);
        Assertions.assertEquals("player1", joinedGame.whiteUsername());
    }

    @Test
    @Order(12)
    @DisplayName("Join Game Negative Test")
    void joinGameNegative() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        String authToken2 = facade.register("player2", "password2", "p2@email.com");
        String authToken3 = facade.register("player3", "password3", "p3@email.com");
        facade.createGame("myGame1", authToken);
        facade.createGame("myGame2", authToken);
        facade.createGame("myGame3", authToken);
        ArrayList<GameData> games = facade.listGames(authToken);
        GameData joinedGame = games.get(1);
        facade.joinGame("WHITE", joinedGame.gameID(), authToken);
        facade.joinGame("BLACK", joinedGame.gameID(), authToken2);
        String joined = facade.joinGame("BLACK", joinedGame.gameID(), authToken3);
        Assertions.assertNull(joined);
    }

    @Test
    @Order (13)
    @DisplayName("Clear Data Test")
    void clearData() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        String authToken2 = facade.register("player2", "password2", "p2@email.com");
        facade.createGame("myGame1", authToken);
        facade.createGame("myGame2", authToken);
        facade.createGame("myGame3", authToken);
        ArrayList<GameData> games = facade.listGames(authToken);
        GameData joinedGame = games.get(1);
        facade.joinGame("WHITE", joinedGame.gameID(), authToken);
        facade.joinGame("BLACK", joinedGame.gameID(), authToken2);
        facade.clear();
        String response = facade.login("player1", "password");
        Assertions.assertNull(response);
    }

}
