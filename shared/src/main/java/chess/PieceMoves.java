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

            if (addMove(nextPosition)) break;
        }
    }

    private boolean addMove(ChessPosition nextPosition) {
        if (board.getPiece(nextPosition) != null) {
            //checks to see if they are the same color
            if (board.getPiece(nextPosition).getTeamColor() == board.getPiece(currentPosition).getTeamColor())
                return true;
            // if they aren't the same color, this adds the position then jumps out of the loop
            else if (board.getPiece(nextPosition).getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                moves.add(myChessMove);
                return true;
            }
        }
        else {
            ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
            moves.add(myChessMove);
            if (board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING)
                return true;
        }
        return false;
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

            if (addMove(nextPosition)) break;
        }
    }

    public void lShape(){
        ChessPosition nextPosition = currentPosition;
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        if (currentRow > 1){
            if (currentCol < 7) {
                int row = currentRow - 2;
                int col = currentCol + 1;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }

            if (currentCol > 0) {
                int row = currentRow - 2;
                int col = currentCol - 1;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }
        }
        if (currentCol > 1){
            if (currentRow < 7) {
                int row = currentRow + 1;
                int col = currentCol - 2;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }

            if (currentRow > 0) {
                int row = currentRow - 1;
                int col = currentCol - 2;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }
        }

        if (currentCol < 6) {
            if (currentRow > 0) {
                int row = currentRow - 1;
                int col = currentCol + 2;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }

            if (currentRow < 7) {
                int row = currentRow + 1;
                int col = currentCol + 2;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }
        }

        if (currentRow < 6) {
            if (currentCol < 7) {
                int row = currentRow + 2;
                int col = currentCol + 1;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }

            if (currentCol > 0) {
                int row = currentRow + 2;
                int col = currentCol - 1;
                nextPosition = new ChessPosition(++row, ++col);
                addMovesFunc(nextPosition);
            }
        }
    }

    public void pawn(){
        ChessPosition nextPosition = currentPosition;
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        if (board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            int row = ++currentRow;
            int col = currentCol;
            nextPosition = new ChessPosition(++row, ++col);
            if (board.getPiece(nextPosition) == null) {
                if (currentPosition.getRow() == 6){
                    promotionPieceFunc(nextPosition);
                }
                else {
                    ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                    moves.add(myChessMove);
                }
            }
            currentRow = currentPosition.getRow();
            currentCol = currentPosition.getColumn();
            if (currentCol < 7) {
                row = ++currentRow;
                col = ++currentCol;
                nextPosition = new ChessPosition(++row, ++col);
                addWhitePawnMove(nextPosition);
            }
            currentRow = currentPosition.getRow();
            currentCol = currentPosition.getColumn();
            if (currentCol > 0) {
                row = ++currentRow;
                col = --currentCol;
                nextPosition = new ChessPosition(++row, ++col);
                addWhitePawnMove(nextPosition);
            }
            currentRow = currentPosition.getRow();
            currentCol = currentPosition.getColumn();
            if (currentRow == 1) {
                row = currentRow + 2;
                col = currentCol;
                nextPosition = new ChessPosition(++row, ++col);
                currentRow = currentPosition.getRow();
                currentCol = currentPosition.getColumn();
                row = currentRow + 1;
                addPawnMove(nextPosition, currentCol, row);
            }
        }

        else if (board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            currentRow = currentPosition.getRow();
            currentCol = currentPosition.getColumn();
            int row = --currentRow;
            int col = currentCol;
            nextPosition = new ChessPosition(++row, ++col);
            if (board.getPiece(nextPosition) == null) {
                if (currentPosition.getRow() == 1){
                    promotionPieceFunc(nextPosition);
                }
                else {
                    ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                    moves.add(myChessMove);
                }
            }
            currentRow = currentPosition.getRow();
            currentCol = currentPosition.getColumn();
            if (currentCol < 7) {
                row = --currentRow;
                col = ++currentCol;
                nextPosition = new ChessPosition(++row, ++col);
                addBlackPawnMove(nextPosition);
            }
            currentRow = currentPosition.getRow();
            currentCol = currentPosition.getColumn();
            if (currentCol > 0) {
                row = --currentRow;
                col = --currentCol;
                nextPosition = new ChessPosition(++row, ++col);
                addBlackPawnMove(nextPosition);
            }
            currentRow = currentPosition.getRow();
            currentCol = currentPosition.getColumn();
            if (currentRow == 6) {
                row = currentRow - 2;
                col = currentCol;
                nextPosition = new ChessPosition(++row, ++col);
                currentRow = currentPosition.getRow();
                currentCol = currentPosition.getColumn();
                row = currentRow - 1;
                addPawnMove(nextPosition, currentCol, row);
            }
        }
    }

    private void addPawnMove(ChessPosition nextPosition, int currentCol, int row) {
        int col;
        col = currentCol;
        ChessPosition otherPosition = new ChessPosition(++row, ++col);
        if (board.getPiece(nextPosition) == null && board.getPiece(otherPosition) == null) {
            ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
            moves.add(myChessMove);
        }
    }

    private void addBlackPawnMove(ChessPosition nextPosition) {
        if ((board.getPiece(nextPosition) != null) && ((board.getPiece(currentPosition).getTeamColor() != board.getPiece(nextPosition).getTeamColor()))) {
            if (currentPosition.getRow() == 1){
                promotionPieceFunc(nextPosition);
            }
            else {
                ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                moves.add(myChessMove);
            }
        }
    }

    private void addWhitePawnMove(ChessPosition nextPosition) {
        if ((board.getPiece(nextPosition) != null) && ((board.getPiece(currentPosition).getTeamColor() != board.getPiece(nextPosition).getTeamColor()))) {
            if (currentPosition.getRow() == 6){
                promotionPieceFunc(nextPosition);
            }
            else {
                ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
                moves.add(myChessMove);
            }
        }
    }

    // adds all the promotion piece option moves
    public void promotionPieceFunc(ChessPosition nextPosition){
        ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, ChessPiece.PieceType.KNIGHT);
        moves.add(myChessMove);
        myChessMove = new ChessMove(currentPosition, nextPosition, ChessPiece.PieceType.QUEEN);
        moves.add(myChessMove);
        myChessMove = new ChessMove(currentPosition, nextPosition, ChessPiece.PieceType.ROOK);
        moves.add(myChessMove);
        myChessMove = new ChessMove(currentPosition, nextPosition, ChessPiece.PieceType.BISHOP);
        moves.add(myChessMove);
    }

    //adds a move for the knight function
    public void addMovesFunc(ChessPosition nextPosition){
        if ((board.getPiece(nextPosition) != null) && (board.getPiece(nextPosition).getTeamColor() != board.getPiece(currentPosition).getTeamColor())) {
            ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
            moves.add(myChessMove);
        }
        else if (board.getPiece(nextPosition) == null) {
            ChessMove myChessMove = new ChessMove(currentPosition, nextPosition, null);
            moves.add(myChessMove);
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

    public ArrayList<ChessMove> knightMoves(){
        lShape();
        return moves;
    }

    public ArrayList<ChessMove> pawnMoves() {
        pawn();
        return moves;
    }
}
