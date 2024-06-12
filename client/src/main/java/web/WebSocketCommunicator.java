package web;



import javax.websocket.*;
import java.io.IOException;
import java.net.URI;


public class WebSocketCommunicator extends Endpoint {
    private Session session;

    public WebSocketCommunicator(int port) throws Exception {
        URI uri = new URI("ws://localhost:" + port + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
        //this.session = session;
        System.out.println("Server Connected");
        try {
            this.session.getBasicRemote().sendText("Help");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
