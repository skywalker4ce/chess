package websocket.messages;

public class NotificationMessage extends ServerMessage{
    String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }
}
