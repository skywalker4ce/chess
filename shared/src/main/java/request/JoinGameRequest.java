package request;

import chess.ChessPiece;

public record JoinGameRequest(String playerColor, int gameID) {
}
