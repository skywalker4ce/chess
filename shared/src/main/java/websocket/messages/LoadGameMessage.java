package websocket.messages;

public class LoadGameMessage extends ServerMessage{
    int game; //this data type might need to change!!

    public LoadGameMessage(ServerMessageType type, int game) {
        super(type);
        this.serverMessageType = ServerMessageType.LOAD_GAME;
        this.game = game;
    }
}
