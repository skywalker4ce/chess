package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameboard = new ChessBoard();
    private TeamColor team = TeamColor.WHITE;           // this may need to change from a private variable

    public ChessGame() {
        setBoard(this.gameboard);
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;                                           //this will probably have to change as well to determine switching teams
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameboard.getPiece(startPosition) == null){                         //This cloned error I'm throwing might not work
            return null;
        }

        ChessPiece piece = gameboard.getPiece(startPosition);
        Collection<ChessMove> allMoves = piece.pieceMoves(gameboard, startPosition);
        // makes a clone of the board
        ChessBoard clonedBoard;
        clonedBoard = gameboard.clone();
        System.out.println(gameboard);
        System.out.println(clonedBoard);
        if (isInCheckmate(team)){
            int i = 0;
        }
        else if (isInCheck(team)){
            int i = 0;
        }
        return allMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition initialPosition = move.getStartPosition();
        Collection<ChessMove> potentialMoves = validMoves(initialPosition);
        if (potentialMoves.contains(move)){
            //Update the chessboard to reflect this move
            gameboard.addPiece(move.getEndPosition(), gameboard.getPiece(initialPosition));
            gameboard.addPiece(initialPosition, null);

        }
        else {
            throw new InvalidMoveException("That is an invalid move");
        }                                                                       //This might not be the correct way to handle an error
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
//        int row = 1;
//        int col = 1;
//        for (ChessPiece[] boardRow : gameboard.getBoard()){
//            for (ChessPiece piece : boardRow){
//                if (piece != null && piece.getTeamColor() != teamColor){
//                    Collection<ChessMove> moves = piece.pieceMoves(getBoard(), new ChessPosition(row, col));
//                }
//                ++col;
//            }
//            ++row;
//        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameboard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameboard;
    }
}
