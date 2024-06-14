package web;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import ui.DisplayBoard;
import websocket.messages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;


public class WebSocketCommunicator extends Endpoint {
    private Session session;
    private Menu menu;

    public WebSocketCommunicator(int port, Menu menu) throws Exception {
        this.menu = menu;
        URI uri = new URI("ws://localhost:" + port + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                try{
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter());
                    Gson gson = builder.create();

                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);

                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> loadGame(session, (LoadGameMessage) serverMessage);
                        case ERROR -> error(session, (ErrorMessage) serverMessage);
                        case NOTIFICATION -> notification(session, (NotificationMessage) serverMessage);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private void loadGame(Session session, LoadGameMessage message) throws IOException {
        GameData game = message.getGame();
        DisplayBoard display = new DisplayBoard();
        if (Objects.equals(menu.getColorOfPlayer(), "OBSERVER")){
            display.display(game.game().getBoard(), "WHITE", null, null);
        }
        else {
            display.display(game.game().getBoard(), menu.getColorOfPlayer(), null, null);
        }
    }

    private void error(Session session, ErrorMessage message){

    }

    private void notification(Session session, NotificationMessage message){
    }

}
