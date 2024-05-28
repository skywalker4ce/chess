package Service;


import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.UnauthorizedException;
import service.UserService;

import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {
    MemoryUserDAO myUser = new MemoryUserDAO();
    MemoryGameDAO myGame = new MemoryGameDAO();
    MemoryAuthDAO myAuth = new MemoryAuthDAO();
    UserService myUserService = new UserService(myUser, myAuth);
    ClearService myClearService = new ClearService(myUser, myAuth, myGame);

    @BeforeEach
    public void setup(){
        myClearService.clearApplication();
    }



    @Test
    @Order(1)
    @DisplayName("Register Positive Test")
    public void registerPositive() throws UnauthorizedException, DataAccessException {
        myUserService.register(new UserData("Sky", "Password1234", "email@gmail.com"));
        UserData testUser = myUser.getUser("Sky");
        Assertions.assertEquals(testUser, new UserData("Sky", "Password1234", "email@gmail.com"));
    }

    @Test
    @Order(2)
    @DisplayName("Register Negative Test")
    public void registerNegative() throws UnauthorizedException, DataAccessException {
        myUserService.register(new UserData("Sky", "Password1234", "email@gmail.com"));
        try {
            myUserService.register(new UserData("Sky", "Password1234", "email@gmail.com"));
            fail("Expected an error");
        }
        catch (DataAccessException e){
            Assertions.assertEquals("{ \"message\": \"Error: already taken\" }", e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Login Positive Test")
    public void loginPositive() throws UnauthorizedException, DataAccessException {
        myUserService.register(new UserData("Sky", "Password1234", "email@gmail.com"));
        AuthData testAuth = myUserService.login(new UserData("Sky", "Password1234", "email@gmail.com"));
        AuthData loginAuth = myAuth.getAuth(testAuth.authToken());
        Assertions.assertEquals(loginAuth.username(), "Sky");

    }

    @Test
    @Order(4)
    @DisplayName("Login Negative Test")
    public void loginNegative() throws UnauthorizedException, DataAccessException {
        myUserService.register(new UserData("Sky", "Password1234", "email@gmail.com"));
        try {
            myUserService.login(new UserData("ky", "Password1234", "email@gmail.com"));
            fail("Expected an Error");
        }
        catch (UnauthorizedException e){
            Assertions.assertEquals("{ \"message\": \"Error: unauthorized\" }", e.getMessage());
        }


    }




}
