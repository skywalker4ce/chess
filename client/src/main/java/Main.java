import chess.*;
import web.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_BOLD_FAINT;
import static ui.EscapeSequences.SET_TEXT_BOLD;

public class Main {
    static ServerFacade facade = new ServerFacade();
    private String authToken;

    public static void main(String[] args) throws Exception {
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

    private static void preLoginMenu() throws Exception {
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
                    facade.register(username, password, email);
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
        Scanner scanner = new Scanner(System.in);
        menu:
        while (true){
            System.out.println(SET_TEXT_BOLD + "Enter a number (1-4) to continue:" + RESET_TEXT_BOLD_FAINT);
            System.out.println("1. Create a Game");
            System.out.println("2. List all active Games");
            System.out.println("3. Observe a Game");
            System.out.println("4. Join a Game");
            System.out.println("5. Logout");
            System.out.println("6. Help");
            var input = scanner.next();
            switch (input) {
                case "1":
                    System.out.println("Enter a name for the game: ");
                    String gameName = scanner.next();
                    System.out.println("This is where you would make the request to create a game " + gameName);
                    break;
                case "2":
                    System.out.println("This is where you would list all active games ");
                    break;
                case "3":
                    System.out.println("Enter the number of the game you would like to observe: ");
                    String gameNumber = scanner.next();
                    System.out.println("This is where you would find the game and display the chessboard " + gameNumber);
                    break;
                case "4":
                    System.out.println("Enter the number of the game you would like to join: ");
                    String gameNumberToJoin = scanner.next();
                    System.out.println("This is where you would find the game, update it, and display the chessboard " + gameNumberToJoin);
                    break;
                case "5":
                    break menu;
                case "6":
                    System.out.println("Type '1' to create a new Chess game");
                    System.out.println("Type '2' to get a list of the current active games");
                    System.out.println("Type '3' to watch an active game");
                    System.out.println("Type '4' to join an active game");
                    System.out.println("Type '5' to logout \n");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}