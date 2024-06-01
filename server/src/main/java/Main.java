import chess.*;
import dataaccess.DataAccessException;
import server.Server;

import static dataaccess.DatabaseManager.createDatabase;
import static dataaccess.DatabaseManager.createTables;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server chessServer = new Server();
        chessServer.run(8080);

    }
}