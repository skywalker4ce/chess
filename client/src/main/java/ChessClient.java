import chess.*;
import web.Menu;

import java.util.*;

import static ui.EscapeSequences.*;

public class ChessClient {

    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        for (int i = 0; i < 3; i++) {
            System.out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_WHITE + "Welcome to 240 Chess. Press 1 to continue: " + RESET_TEXT_BOLD_FAINT);
            Scanner scanner = new Scanner(System.in);
            var input = scanner.next();
            if (Objects.equals(input, "1")) {
                Menu menu = new Menu();
                menu.preLoginMenu();
                break;
            }
        }
        System.out.println("Come Back Soon!");
    }
}


