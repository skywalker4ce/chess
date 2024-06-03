package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLUserDAOTest {
    SQLUserDAO myUser = new SQLUserDAO();

    @BeforeEach
    public void setup(){
        myUser.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Create User Positive Test")
    public void createUserPositive() throws DataAccessException {
        myUser.createUser("Sky", "Password1234", "email@gmail.com");
        Assertions.assertEquals(myUser.getUser("Sky").email(), "email@gmail.com");
    }

    @Test
    @Order(2)
    @DisplayName("Create User Negative Test")
    public void createUserNegative() throws DataAccessException {
        try {
            myUser.createUser(null, null, null);
            fail("Expected an error");
        }
        catch (DataAccessException e){
            Assertions.assertEquals("fields can't be empty", e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Get User Positive Test")
    public void getUserPositive() throws DataAccessException {
        myUser.createUser("Sky", "Password1234", "email@gmail.com");
        UserData tempUser = myUser.getUser("Sky");
        Assertions.assertNotEquals(tempUser.password(), "Password1234");
    }

    @Test
    @Order(4)
    @DisplayName("Get User Negative Test")
    public void getUserNegative() throws DataAccessException {
        myUser.createUser("Sky", "Password1234", "email@gmail.com");
        Assertions.assertNull(myUser.getUser("Isaac"));
    }

    @Test
    @Order(5)
    @DisplayName("Clear Positive Test")
    public void clearPositive() throws DataAccessException {
        myUser.createUser("Sky", "Password1234", "email@gmail.com");
        myUser.clear();
        Assertions.assertNull(myUser.getUser("Sky"));
    }

}
