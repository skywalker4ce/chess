package chess;

import java.util.ArrayList;
import java.util.Objects;

public class PieceMoves {
    private ArrayList<ChessMove> moves = new ArrayList<chess.ChessMove>();
    private ChessPosition currentPosition;
    private ChessBoard board;

    public PieceMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition = currentPosition;
    }


    public void diagonal(String direction) {
        ChessPosition nextPosition = currentPosition;
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        while ((currentRow > -1 && currentRow < 8) && (currentCol > -1 && currentCol < 8)){

            //Checks to see which direction the piece is going
            if (Objects.equals(direction, "Up Left")) {
                if (currentRow == 7 || currentCol == 0)
                    break;
                int row = (++currentRow);
                int col = (--currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Up Right")) {
                if (currentRow == 7 || currentCol == 7)
                    break;
                int row = (++currentRow);
                int col = (++currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Down Right")){
                if (currentRow == 0 || currentCol == 7)
                    break;
                int row = (--currentRow);
                int col = (++currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Down Left")){
                if (currentRow == 0 || currentCol == 0)
                    break;
                int row = (--currentRow);
                int col = (--currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }

            if (board.getPiece(nextPosition) != null) {
                //checks to see if they are the same color
                if (board.getPiece(nextPosition).getTeamColor() == board.getPiece(currentPosition).getTeamColor())
                    break;
                // if they aren't the same color, this adds the position then jumps out of the loop
                else if (board.getPiece(nextPosition).getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                    ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                    moves.add(myChessMove);
                    break;
                }
            }
            else {
                ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                moves.add(myChessMove);
                if (board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING)
                    break;
            }
        }
    }

    public void straight(String direction) {
        ChessPosition nextPosition = currentPosition;
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        while ((currentRow > -1 && currentRow < 8) && (currentCol > -1 && currentCol < 8)){

            //Checks to see which direction the piece is going
            if (Objects.equals(direction, "Up")) {
                if (currentRow == 7)
                    break;
                int row = (++currentRow);
                int col = currentCol;
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Right")) {
                if (currentCol == 7)
                    break;
                int row = currentRow;
                int col = (++currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Down")){
                if (currentRow == 0)
                    break;
                int row = (--currentRow);
                int col = currentCol;
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Left")){
                if (currentCol == 0)
                    break;
                int row = currentRow;
                int col = (--currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }

            if (board.getPiece(nextPosition) != null) {
                //checks to see if they are the same color
                if (board.getPiece(nextPosition).getTeamColor() == board.getPiece(currentPosition).getTeamColor())
                    break;
                    // if they aren't the same color, this adds the position then jumps out of the loop
                else if (board.getPiece(nextPosition).getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                    ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                    moves.add(myChessMove);
                    break;
                }
            }
            else {
                ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                moves.add(myChessMove);
                if (board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING)
                    break;
            }
        }
    }

    public ArrayList<ChessMove> bishopMoves(){
        String direction = "Up Left";
        diagonal(direction);
        direction = "Up Right";
        diagonal(direction);
        direction = "Down Left";
        diagonal(direction);
        direction = "Down Right";
        diagonal(direction);
        return moves;
    }

    public ArrayList<ChessMove> rookMoves(){
        String direction = "Up";
        straight(direction);
        direction = "Right";
        straight(direction);
        direction = "Left";
        straight(direction);
        direction = "Down";
        straight(direction);
        return moves;
    }

    public ArrayList<ChessMove> queenMoves(){
        bishopMoves();
        rookMoves();
        return moves;
    }

    public ArrayList<ChessMove> kingMoves(){
        bishopMoves();
        rookMoves();
        return moves;
    }
}
