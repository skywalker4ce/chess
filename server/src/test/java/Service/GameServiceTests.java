package Service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.BadRequestException;
import service.ClearService;
import service.GameService;
import service.UnauthorizedException;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {
    MemoryUserDAO myUser = new MemoryUserDAO();
    MemoryGameDAO myGame = new MemoryGameDAO();
    MemoryAuthDAO myAuth = new MemoryAuthDAO();
    UserService myUserService = new UserService(myUser, myAuth);
    GameService myGameService = new GameService(myAuth, myGame);
    ClearService myClearService = new ClearService(myUser, myAuth, myGame);
    UserData randomUser1 = new UserData("Sky", "Password43", "email1@gmail.com");
    UserData randomUser2 = new UserData("Isaac", "Password29", "email2@gmail.com");
    UserData randomUser3 = new UserData("Nath", "Password10", "email3@gmail.com");
    AuthData auth1;
    AuthData auth2;
    AuthData auth3;


    @BeforeEach
    public void setup() throws UnauthorizedException, DataAccessException {
        myClearService.clearApplication();
        myUserService.register(randomUser1);
        myUserService.register(randomUser2);
        myUserService.register(randomUser3);
        auth1 = myUserService.login(randomUser1);
        auth2 = myUserService.login(randomUser2);
        auth3 = myUserService.login(randomUser3);
    }

    @Test
    @Order(1)
    @DisplayName("Create Games Positive Test")
    public void createGamesPositive() throws UnauthorizedException {
        int Game1ID = myGameService.createGame(auth1.authToken(), "Game1");
        Assertions.assertNotNull(myGame.getGame(Game1ID));
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Negative Test")
    public void createGamesNegative() throws UnauthorizedException {
        try{
            myGameService.createGame("RandomAuth", "RandomName");
            fail("Expected to fail");
        }
        catch (UnauthorizedException e){
            Assertions.assertEquals("{ \"message\": \"Error: unauthorized\" }", e.getMessage());
        }

    }

    @Test
    @Order(3)
    @DisplayName("List Games Positive Test")
    public void listGamesPositive() throws UnauthorizedException {
        myGameService.createGame(auth1.authToken(), "Game1");
        myGameService.createGame(auth2.authToken(), "Game2");
        myGameService.createGame(auth3.authToken(), "Game3");
        myGameService.createGame(auth1.authToken(), "Game4");
        ArrayList<GameData> myGames = myGameService.listGames(auth1.authToken());
        Assertions.assertEquals(myGames.toArray().length, 4);
    }

    @Test
    @Order(4)
    @DisplayName("List Games Negative Test")
    public void listGamesNegative() throws UnauthorizedException {
        myGameService.createGame(auth1.authToken(), "Game1");
        myGameService.createGame(auth2.authToken(), "Game2");
        myGameService.createGame(auth3.authToken(), "Game3");
        myGameService.createGame(auth1.authToken(), "Game4");
        try {
            myGameService.listGames("RandomAuth");
            fail("Should have failed");
        }
        catch (UnauthorizedException e){
            assertEquals("{ \"message\": \"Error: unauthorized\" }", e.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Join Game Positive Test")
    public void JoinGamePositive() throws UnauthorizedException, BadRequestException, DataAccessException {
        int game1ID = myGameService.createGame(auth1.authToken(), "Game1");
        myGameService.joinGame(auth1.authToken(), "WHITE", game1ID);
        GameData updatedGame = myGame.getGame(game1ID);
        Assertions.assertEquals(updatedGame.gameName(), "Game1");
        Assertions.assertEquals(updatedGame.whiteUsername(), "Sky");
        Assertions.assertNull(updatedGame.blackUsername());
    }

    @Test
    @Order(6)
    @DisplayName("Join Game Negative Test")
    public void JoinGameNegative() throws UnauthorizedException, BadRequestException, DataAccessException {
        int game1ID = myGameService.createGame(auth1.authToken(), "Game1");
        myGameService.joinGame(auth1.authToken(), "WHITE", game1ID);
        myGameService.joinGame(auth2.authToken(), "BLACK", game1ID);
        try {
            myGameService.joinGame(auth3.authToken(), "WHITE", game1ID);
            fail("Should not make it here");
        }
        catch (DataAccessException e){
            assertEquals("{ \"message\": \"Error: already taken\" }", e.getMessage());
        }

    }

}
