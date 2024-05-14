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
    //private ChessBoard clonedBoard = gameboard.clone();
    private TeamColor team = TeamColor.WHITE;
    private Boolean makeMoveBoolean = false;                                 // this may need to change from a private variable

    public ChessGame() {
        gameboard.resetBoard();
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
        if (gameboard.getPiece(startPosition) == null){
            return null;
        }

        ChessPiece piece = gameboard.getPiece(startPosition);
        Collection<ChessMove> allMoves = piece.pieceMoves(gameboard, startPosition);
        // makes a clone of the board



        ArrayList<ChessMove> updatedMoves = new ArrayList<>();
        for (ChessMove move : allMoves){
            ChessBoard clonedBoard = gameboard.clone();
            makeMoveBoolean = true;
            gameboard.addPiece(move.getEndPosition(), gameboard.getPiece(startPosition));
            gameboard.addPiece(startPosition, null);
            if (!isInCheck(piece.getTeamColor())){
                updatedMoves.add(move);
            }
            gameboard = clonedBoard.clone();
        }
        allMoves = updatedMoves;
        makeMoveBoolean = false;
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
        if (potentialMoves != null && potentialMoves.contains(move) && gameboard.getPiece(initialPosition).getTeamColor() == team){
            //Update the chessboard to reflect this move
            if (move.getPromotionPiece() != null){
                ChessPiece.PieceType promotion = move.getPromotionPiece();
                gameboard.addPiece(move.getEndPosition(), new ChessPiece(team, promotion));
            }
            else {
                gameboard.addPiece(move.getEndPosition(), gameboard.getPiece(initialPosition));
            }
            gameboard.addPiece(initialPosition, null);
            if (team == TeamColor.WHITE){
                setTeamTurn(TeamColor.BLACK);
            }
            else {
                setTeamTurn(TeamColor.WHITE);
            }

        }
        else {
            throw new InvalidMoveException("That is an invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // find where current team's king is at
        ChessPosition kingPosition = new ChessPosition(1,1);
        int row = 1;
        for (ChessPiece[] boardRow : gameboard.getBoard()){
            int col = 1;
            for (ChessPiece piece : boardRow){
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){
                    kingPosition = new ChessPosition(row, col);
                }
                ++col;
            }
            ++row;
        }
        // See if any of the moves gets to the king
        row = 1;
        for (ChessPiece[] boardRow : gameboard.getBoard()){
            int col = 1;
            for (ChessPiece piece : boardRow){
                if (piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> opposingTeamMoves = (piece.pieceMoves(getBoard(), new ChessPosition(row, col)));
                    for (ChessMove move : opposingTeamMoves){
                        if (move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
                ++col;
            }
            ++row;
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        else {
            return checkForValidMoves(teamColor);
        }
    }

    private boolean checkForValidMoves(TeamColor teamColor){
        int row = 1;
        for (ChessPiece[] boardRow : gameboard.getBoard()){
            int col = 1;
            for (ChessPiece piece : boardRow){
                if (piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> potentialMoves = validMoves(new ChessPosition(row, col));
                    if (potentialMoves != null && !potentialMoves.isEmpty()){
                        return false;
                    }
                }
                ++col;
            }
            ++row;
        }
        return true;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return checkForValidMoves(teamColor);
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
