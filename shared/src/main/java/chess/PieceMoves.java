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
        while ((currentRow > 0 && currentRow < 7) && (currentCol > 0 && currentCol < 7)){

            //Checks to see which direction the piece is going
            if (Objects.equals(direction, "Up Left")) {
                int row = (++currentRow);
                int col = (--currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Up Right")) {
                int row = (++currentRow);
                int col = (++currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Down Right")){
                int row = (--currentRow);
                int col = (++currentCol);
                nextPosition = new ChessPosition(++row, ++col);
            }
            else if (Objects.equals(direction, "Down Left")){
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
            }
            if ((currentRow == 0 || currentRow == 7) || (currentCol == 0 || currentCol == 7))
                break;
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
}
