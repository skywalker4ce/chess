package websocket.messages;

public class NotificationMessage extends ServerMessage{
    String message;

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.serverMessageType = ServerMessageType.ERROR;
        this.message = message;
    }
}
