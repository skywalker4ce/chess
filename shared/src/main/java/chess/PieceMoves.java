package chess;

import java.util.ArrayList;

public class PieceMoves {
    private ArrayList<ChessMove> moves = new ArrayList<chess.ChessMove>();
    private ChessPosition currentPosition;
    private ChessBoard board;

    public PieceMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition = currentPosition;
    }


    public void diagonal(){

    }

    public ArrayList<ChessMove> bishopMoves(){
        ChessPosition nextPosition = new ChessPosition(1,8);
        ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
        moves.add(myChessMove);
        return moves;
    }
}
