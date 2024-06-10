package websocket.commands;

public class ConnectCommand extends UserGameCommand{
    int gameID;

    public ConnectCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.CONNECT;
        this.gameID = gameID;
    }


}
