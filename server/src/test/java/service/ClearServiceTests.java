package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTests {
    SQLUserDAO myUser = new SQLUserDAO();
    SQLGameDAO myGame = new SQLGameDAO();
    SQLAuthDAO myAuth = new SQLAuthDAO();
    UserService myUserService = new UserService(myUser, myAuth);
    GameService myGameService = new GameService(myAuth, myGame);
    ClearService myClearService = new ClearService(myUser, myAuth, myGame);
    UserData randomUser1 = new UserData("Sky", "Password43", "email1@gmail.com");
    UserData randomUser2 = new UserData("Isaac", "Password29", "email2@gmail.com");
    UserData randomUser3 = new UserData("Nath", "Password10", "email3@gmail.com");
    AuthData auth1;
    AuthData auth2;
    AuthData auth3;
    AuthData auth4;
    int game1;
    int game2;
    int game3;


    @BeforeEach
    public void setup() throws UnauthorizedException, DataAccessException {
        myClearService.clearApplication();
        auth4 = myUserService.register(randomUser1);
        myUserService.register(randomUser2);
        myUserService.register(randomUser3);
        auth1 = myUserService.login(randomUser1);
        auth2 = myUserService.login(randomUser2);
        auth3 = myUserService.login(randomUser3);
        game1 = myGameService.createGame(auth1.authToken(), "Game1");
        game2 = myGameService.createGame(auth2.authToken(), "Game2");
        game3 = myGameService.createGame(auth3.authToken(), "Game3");
    }

    @Test
    @Order(1)
    @DisplayName("Clear data Positive Test")
    public void clearDataPositive() throws DataAccessException {
        myClearService.clearApplication();
        Assertions.assertNull(myGame.getGame(game1));
        Assertions.assertNull(myGame.getGame(game2));
        Assertions.assertNull(myGame.getGame(game3));
        Assertions.assertNull(myAuth.getAuth(auth2.authToken()));
        Assertions.assertNull(myAuth.getAuth(auth1.authToken()));
        Assertions.assertNull(myAuth.getAuth(auth3.authToken()));
        Assertions.assertNull(myAuth.getAuth(auth4.authToken()));
        Assertions.assertNull(myUser.getUser("Sky"));
        Assertions.assertNull(myUser.getUser("Isaac"));
        Assertions.assertNull(myUser.getUser("Nath"));
    }

}
