package dataaccess;

import model.GameData;
import org.junit.jupiter.api.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLGameDAOTest {
    SQLGameDAO myGame = new SQLGameDAO();

    @BeforeEach
    public void setup(){
        myGame.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Create Game Positive Test")
    public void createGamePositive() throws DataAccessException {
        int gameID = myGame.createGame("Sky's Game");
        Assertions.assertEquals(gameID,1);
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Negative Test")
    public void createGameNegative() throws DataAccessException {
        int gameID = myGame.createGame(null);
        assertEquals(0, gameID);
    }

    @Test
    @Order(3)
    @DisplayName("Get Game Positive Test")
    public void getGamePositive() throws DataAccessException {
        int gameID = myGame.createGame("Sky's game");
        GameData tempGame = myGame.getGame(gameID);
        Assertions.assertEquals(tempGame.gameName(), "Sky's game");
    }

    @Test
    @Order(4)
    @DisplayName("Get Game Negative Test")
    public void getGameNegative() throws DataAccessException {
        myGame.createGame("Sky's game");
        GameData tempGame = myGame.getGame(123);
        Assertions.assertNull(tempGame);
    }

    @Test
    @Order(5)
    @DisplayName("List Games Positive Test")
    public void listGamesPositive() throws DataAccessException {
        myGame.createGame("Sky's game");
        myGame.createGame("Another Game");
        ArrayList<GameData> myGameList = myGame.listGames();
        Assertions.assertEquals(myGameList.size(),2);
    }

    @Test
    @Order(6)
    @DisplayName("List Games Negative Test")
    public void listGamesNegative() throws DataAccessException {
        myGame.createGame("Sky's game");
        myGame.createGame(null);
        ArrayList<GameData> myGameList = myGame.listGames();
        Assertions.assertEquals(myGameList.size(),1);
    }

    @Test
    @Order(7)
    @DisplayName("Update Game Positive Test")
    public void updateGamePositive() throws DataAccessException {
        int gameID = myGame.createGame("Sky's game");
        GameData tempGame = myGame.getGame(gameID);
        myGame.updateGame(tempGame, "Sky", "BLACK");
        tempGame = myGame.getGame(gameID);
        Assertions.assertEquals(tempGame.blackUsername(), "Sky");
    }

    @Test
    @Order(8)
    @DisplayName("Update Game Negative Test")
    public void updateGameNegative() throws DataAccessException {
        int gameID = myGame.createGame("Sky's game");
        GameData tempGame = myGame.getGame(gameID);
        myGame.updateGame(tempGame, "Sky", "BLACK");
        myGame.updateGame(tempGame, "Isaac", "WHITE");
        myGame.updateGame(tempGame, "Nathan", "GREEN");
        tempGame = myGame.getGame(gameID);
        Assertions.assertEquals(tempGame.blackUsername(), "Sky");
        Assertions.assertEquals(tempGame.whiteUsername(), "Isaac");
    }


    @Test
    @Order(9)
    @DisplayName("Clear Positive Test")
    public void clearPositive() throws DataAccessException {
        int gameID = myGame.createGame("Sky's game");
        myGame.clear();
        Assertions.assertNull(myGame.getGame(gameID));
    }

}
