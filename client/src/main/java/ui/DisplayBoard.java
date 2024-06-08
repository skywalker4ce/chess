package ui;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;


import static ui.EscapeSequences.*;


public class DisplayBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final String EMPTY = " ";
    private String color;
    private ChessBoard board;
    private Collection<ChessMove> validMoves;
    ChessPosition currentPosition;


    public void display(ChessBoard board, String color, Collection<ChessMove> validMoves, ChessPosition currentPosition) {
        this.board = board;
        this.color = color;
        this.validMoves = validMoves;
        this.currentPosition = currentPosition;

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        drawHeaders(out);

        setOriginal(out);
    }

    private void drawHeaders(PrintStream out) {

        playerColor(out);
        out.print(EMPTY.repeat(3));
        String[] headersBlack = { "h", "g", "f", "e", "d", "c", "b", "a" };
        String[] headersWhite = { "a", "b", "c", "d", "e", "f", "g", "h" };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (Objects.equals(color, "BLACK")){
                drawHeader(out, headersBlack[boardCol]);
            }
            else {
                drawHeader(out, headersWhite[boardCol]);
            }
        }
        out.print(EMPTY.repeat(3));
        setOriginal(out);
        out.println();
    }

    private void drawHeader(PrintStream out, String headerText) {
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
    }

    private void printHeaderText(PrintStream out, String player) {
        out.print(player);

        playerColor(out);
    }

    private void drawChessBoard(PrintStream out) {

        if (Objects.equals(color, "BLACK")) {
            for (int boardRow = 1; boardRow < BOARD_SIZE_IN_SQUARES + 1; ++boardRow) {
                drawRowOfSquares(out, boardRow);
            }
        }
        else {
            for (int boardRow = 8; boardRow > 0; --boardRow) {
                drawRowOfSquares(out, boardRow);
            }
        }
    }

    private void drawRowOfSquares(PrintStream out, int boardRow) {
        String[] sidersWhite = {"1", "2", "3", "4", "5", "6", "7", "8"};
        boardRow = drawEdge(out, boardRow, sidersWhite);

        String squareColor;
        if (Objects.equals(color, "WHITE")) {
            if (boardRow % 2 == 0) {
                squareColor = SET_BG_COLOR_WHITE;
            } else {
                squareColor = SET_BG_COLOR_BLACK;
            }
        }
        else {
            if (boardRow % 2 != 0) {
                squareColor = SET_BG_COLOR_WHITE;
            } else {
                squareColor = SET_BG_COLOR_BLACK;
            }
        }

        if (Objects.equals(color, "WHITE")){
            for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES + 1; ++boardCol) {
                squareColor = makeBoard(out, boardRow, boardCol, squareColor);
            }
        }
        else {
            for (int boardCol = 8; boardCol > 0; --boardCol) {
                squareColor = makeBoard(out, boardRow, boardCol, squareColor);
            }
        }

        boardRow = drawEdge(out, boardRow, sidersWhite);

        setOriginal(out);

        out.println();
    }

    private int drawEdge(PrintStream out, int boardRow, String[] sidersWhite) {
        playerColor(out);
        out.print(EMPTY);
        if (Objects.equals(color, "WHITE")) {
            out.print(sidersWhite[--boardRow]);
            boardRow++;
        } else {
            out.print(sidersWhite[--boardRow]);
            boardRow++;
        }
        out.print(EMPTY);
        return boardRow;
    }

    private String makeBoard(PrintStream out, int boardRow, int boardCol, String squareColor) {
        ChessPosition position = new ChessPosition(boardRow, boardCol);
        ChessPiece piece = board.getPiece(position);
        Boolean partOfValidMoves = false;

        for (ChessMove move: validMoves){
            if (move.getEndPosition().equals(position)){
                partOfValidMoves = true;
                break;
            }
        }

        if (piece != null){
            String pieceRepresentation;
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
            }
            else {
                out.print(SET_TEXT_COLOR_MAGENTA);
            }
            pieceRepresentation = switch (piece.getPieceType()) {
                case PAWN -> "P";
                case KING -> "K";
                case QUEEN -> "Q";
                case BISHOP -> "B";
                case KNIGHT -> "N";
                case ROOK -> "R";
            };

            if (position.equals(currentPosition)){
                out.print(SET_BG_COLOR_RED + EMPTY);
                out.print(pieceRepresentation);
                out.print(EMPTY + squareColor);
            }
            else {
                if (partOfValidMoves && Objects.equals(squareColor, SET_BG_COLOR_BLACK)){
                    out.print(SET_BG_COLOR_DARK_GREEN + EMPTY);
                    out.print(pieceRepresentation);
                    out.print(EMPTY + squareColor);
                }
                else if (partOfValidMoves && Objects.equals(squareColor, SET_BG_COLOR_WHITE)){
                    out.print(SET_BG_COLOR_GREEN + EMPTY);
                    out.print(pieceRepresentation);
                    out.print(EMPTY + squareColor);
                }
                else {
                    out.print(squareColor + EMPTY);
                    out.print(squareColor + pieceRepresentation);
                    out.print(squareColor + EMPTY);
                }
            }

        }
        else {
            if (partOfValidMoves && Objects.equals(squareColor, SET_BG_COLOR_BLACK)){
                out.print(SET_BG_COLOR_DARK_GREEN + EMPTY.repeat(3) + squareColor);
            }
            else if (partOfValidMoves && Objects.equals(squareColor, SET_BG_COLOR_WHITE)){
                out.print(SET_BG_COLOR_GREEN + EMPTY.repeat(3) + squareColor);
            }
            else {
                out.print(squareColor + EMPTY.repeat(3));
            }
        }

        if (squareColor.equals(SET_BG_COLOR_WHITE)){
            squareColor = SET_BG_COLOR_BLACK;
        }
        else {
            squareColor = SET_BG_COLOR_WHITE;
        }
        return squareColor;
    }

    private void playerColor(PrintStream out) {
        if (Objects.equals(color, "WHITE")) {
            setWhiteBorder(out);
        }
        else {
            setBlackBorder(out);
        }
    }

    private void setWhiteBorder(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_ITALIC + SET_TEXT_COLOR_DARK_GREY);
    }

    private void setBlackBorder(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_ITALIC + SET_TEXT_COLOR_DARK_GREY);
    }

    private void setOriginal(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR + RESET_TEXT_ITALIC);
    }
}
