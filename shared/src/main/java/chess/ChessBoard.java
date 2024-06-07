package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] board = new ChessPiece[8][8]; //make private!!!!!!!!

    public ChessBoard() {
        
    }

    @Override
    public ChessBoard clone() {
        ChessBoard clone;
        try {
            clone = (ChessBoard) super.clone();
            ChessPiece[][] clonedBoard = new ChessPiece[8][8];
            int row = 0;
            for (ChessPiece[] boardRow : board){
                int col = 0;
                for (ChessPiece piece : boardRow){
                    if (piece == null){
                        clonedBoard[row][col] = null;
                        ++col;
                        continue;
                    }
                    ChessPiece clonedPiece = (ChessPiece) piece.clone();
                    clonedBoard[row][col] = clonedPiece;
                    ++col;                                                            //This might need to be changed in the piece function
                }
                ++row;
            }
            clone.board = clonedBoard;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return clone;
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessPiece[][] getBoard(){
        return board;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (board[position.getRow()][position.getColumn()] == null){
            return null;
        }
        else
            return board[position.getRow()][position.getColumn()];
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //pawns
        for (int i = 1; i < board.length + 1; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        // rest of the pieces
        for (int i = 1; i < board.length + 1 ; i++){
            if (i == 1 || i == 8) {
                addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
            }
            else if ( i == 2 || i == 7) {
                addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            }
            else if (i == 3 || i == 6){
                addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            }
            else if (i == 5){
                addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
            }
            else if (i == 4){
                addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
            }
        }
    }
}
