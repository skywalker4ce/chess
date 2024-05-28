package Service;


import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {
    MemoryUserDAO myUser = new MemoryUserDAO();
    MemoryGameDAO myGame = new MemoryGameDAO();
    MemoryAuthDAO myAuth = new MemoryAuthDAO();


    @Test
    @Order(1)
    @DisplayName("Clear Test")
    public void clearTest(){
        myUser.createUser("Sky", "Password1234", "email@gmail.com");
        Assertions.assertNull(myUser.getUser("ky"));
    }


}
