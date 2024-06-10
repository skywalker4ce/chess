package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    int gameID;
    ChessMove move;

    public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }
}
