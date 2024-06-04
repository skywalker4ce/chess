import chess.*;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_BOLD_FAINT;
import static ui.EscapeSequences.SET_TEXT_BOLD;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        for (int i = 0; i < 3; i++) {
            System.out.println(SET_TEXT_BOLD + "Welcome to 240 Chess. Press 1 to continue: " + RESET_TEXT_BOLD_FAINT);
            Scanner scanner = new Scanner(System.in);
            var input = scanner.next();
            if (Objects.equals(input, "1")){
                preLoginMenu();
                break;
            }
        }
        System.out.println("Come Back Soon!");
    }

    private static void preLoginMenu(){
        Scanner scanner = new Scanner(System.in);
        menu:
        while (true){
            System.out.println(SET_TEXT_BOLD + "Enter a number (1-4) to continue:" + RESET_TEXT_BOLD_FAINT);
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Quit");
            System.out.println("4. Help");
            var input = scanner.next();
            switch (input) {
                case "1":
                    System.out.println("Enter a Username: ");
                    String username = scanner.next();
                    System.out.println("Enter a Password: ");
                    String password = scanner.next();
                    System.out.println("Enter a valid Email: ");
                    String email = scanner.next();
                    System.out.println("This is where you would make the request to register a user " + username + " " + password + " " + email);
                    postLoginMenu();
                    break;
                case "2":
                    System.out.println("Insert your Username: ");
                    String loginUsername = scanner.next();
                    System.out.println("Insert your Password: ");
                    String loginPassword = scanner.next();
                    System.out.println("This is where you would make the request to login a user " + loginUsername + " " + loginPassword + " ");
                    if (true){ //Username and password exist
                        postLoginMenu();
                    }
                    break;
                case "3":
                    break menu;
                case "4":
                    System.out.println("Type '1' to register a user");
                    System.out.println("Type '2' to login a user");
                    System.out.println("Type '3' to exit the program \n");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }

    private static void postLoginMenu(){

    }
}