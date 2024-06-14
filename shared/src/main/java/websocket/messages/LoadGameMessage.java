package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage{
    GameData game; //this data type might need to change!!

    public LoadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}
