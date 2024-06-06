package ui;
import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class DisplayBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = " ";


    public void display(ChessBoard board, String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        drawHeaders(out);

        setOriginal(out);
    }

    private void drawHeaders(PrintStream out) {

        setBorder(out);
        out.print(EMPTY.repeat(3));
        String[] headers = { "h", "g", "f", "e", "d", "c", "b", "a" };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
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

        setBorder(out);
    }

    private void drawChessBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow);

        }
    }

    private void drawRowOfSquares(PrintStream out, int boardRow) {
        String[] siders = { "1", "2", "3", "4", "5", "6", "7", "8" };
        out.print(SET_BG_COLOR_BLUE + EMPTY);
        out.print(SET_TEXT_ITALIC + SET_TEXT_COLOR_DARK_GREY + siders[boardRow]);
        out.print(SET_BG_COLOR_BLUE + EMPTY);

        String squareColor;
        if (boardRow % 2 == 0) {
            squareColor = SET_BG_COLOR_WHITE;
        }
        else {
            squareColor = SET_BG_COLOR_BLACK;
        }

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            out.print(squareColor + EMPTY.repeat(3));
            if (squareColor.equals(SET_BG_COLOR_WHITE)){
                squareColor = SET_BG_COLOR_BLACK;
            }
            else {
                squareColor = SET_BG_COLOR_WHITE;
            }
        }

        out.print(SET_BG_COLOR_BLUE + EMPTY);
        out.print(SET_TEXT_ITALIC + SET_TEXT_COLOR_DARK_GREY + siders[boardRow]);
        out.print(SET_BG_COLOR_BLUE + EMPTY);

        setOriginal(out);

        out.println();
    }

    private void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void setBorder(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_ITALIC + SET_TEXT_COLOR_DARK_GREY);
    }

    private void setDarkGray(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
    }

    private void setOriginal(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR + SET_TEXT_FAINT);
    }

    private void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }
}
