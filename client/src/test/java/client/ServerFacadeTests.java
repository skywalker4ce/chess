package client;

import org.junit.jupiter.api.*;
import server.Server;
import web.ServerFacade;


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

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.length() > 10);
    }

}
