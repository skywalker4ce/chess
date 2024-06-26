package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ChessPiece clone = (ChessPiece) super.clone();
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMoves pieceMoves = new PieceMoves(board, myPosition);
        if (this.type == PieceType.BISHOP){
            ArrayList<ChessMove> moves =  pieceMoves.bishopMoves();
            return moves;
        }
        else if (this.type == PieceType.ROOK){
            ArrayList<ChessMove> moves =  pieceMoves.rookMoves();
            return moves;
        }
        else if (this.type == PieceType.QUEEN){
            ArrayList<ChessMove> moves = pieceMoves.queenMoves();
            return moves;
        }
        else if (this.type == PieceType.KING){
            ArrayList<ChessMove> moves = pieceMoves.kingMoves();
            return moves;
        }
        else if (this.type == PieceType.KNIGHT) {
            ArrayList<ChessMove> moves = pieceMoves.knightMoves();
            return moves;
        }
        else if (this.type == PieceType.PAWN) {
            ArrayList<ChessMove> moves = pieceMoves.pawnMoves();
            return moves;
        }
        return null;
    }
}
