package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLAuthDAOTest {
    SQLAuthDAO myAuth = new SQLAuthDAO();

    @BeforeEach
    public void setup(){
        myAuth.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Create Auth Positive Test")
    public void createAuthPositive() throws DataAccessException {
        AuthData tempAuth = myAuth.createAuth("Sky");
        Assertions.assertEquals(tempAuth.username(), "Sky");
    }

    @Test
    @Order(2)
    @DisplayName("Create Auth Negative Test")
    public void createAuthNegative() throws DataAccessException {
        try {
            myAuth.createAuth(null);
            fail("Expected an error");
        }
        catch (DataAccessException e){
            Assertions.assertEquals("Username can't be empty", e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Get Auth Positive Test")
    public void getAuthPositive() throws DataAccessException {
        AuthData tempAuth = myAuth.createAuth("Sky");
        AuthData tempAuth2 = myAuth.getAuth(tempAuth.authToken());
        Assertions.assertEquals(tempAuth2, tempAuth);
    }

    @Test
    @Order(4)
    @DisplayName("Get Auth Negative Test")
    public void getAuthNegative() throws DataAccessException {
        myAuth.createAuth("Sky");
        Assertions.assertNull(myAuth.getAuth("RandomAuthToken"));
    }

    @Test
    @Order(5)
    @DisplayName("Delete Auth Positive Test")
    public void deleteAuthPositive() throws DataAccessException {
        AuthData tempAuth = myAuth.createAuth("Sky");
        myAuth.deleteAuth(tempAuth.authToken());
        Assertions.assertNull(myAuth.getAuth(tempAuth.authToken()));
    }

    @Test
    @Order(6)
    @DisplayName("Delete Auth Negative Test")
    public void deleteAuthNegative() throws DataAccessException {
        AuthData tempAuth = myAuth.createAuth("Sky");
        myAuth.deleteAuth("RandomAuthToken");
        Assertions.assertNotNull(myAuth.getAuth(tempAuth.authToken()));
    }


    @Test
    @Order(7)
    @DisplayName("Clear Positive Test")
    public void clearPositive() throws DataAccessException {
        AuthData tempAuth = myAuth.createAuth("Sky");
        myAuth.clear();
        Assertions.assertNull(myAuth.getAuth(tempAuth.authToken()));
    }

}

