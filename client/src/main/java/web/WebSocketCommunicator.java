package web;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.messages.*;
import javax.websocket.*;
import java.net.URI;


public class WebSocketCommunicator extends Endpoint {
    private Session session;
    private Menu menu;

    public WebSocketCommunicator(int port, Menu menu) throws Exception {
        this.menu = menu;
        URI uri = new URI("ws://localhost:" + port + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                try{
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter());
                    Gson gson = builder.create();

                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    menu.notify(serverMessage);

                } catch (Exception ex) {
                    menu.notify(new ErrorMessage("Error: " + ex.getMessage()));
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

}
