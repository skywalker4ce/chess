import chess.*;
import model.GameData;
import ui.DisplayBoard;
import web.ServerFacade;

import java.util.*;

import static ui.EscapeSequences.*;

public class Main {
    static ServerFacade facade = new ServerFacade();

    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        for (int i = 0; i < 3; i++) {
            System.out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_WHITE + "Welcome to 240 Chess. Press 1 to continue: " + RESET_TEXT_BOLD_FAINT);
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
        String authToken;
        facade.clear();
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
                    if (username == null || password == null || email == null){
                        System.out.println(SET_TEXT_COLOR_RED + "No fields can be blank. Please try again" + SET_TEXT_COLOR_WHITE);
                    }
                    else {
                        authToken = facade.register(username, password, email);
                        if (authToken != null){
                            postLoginMenu(authToken);
                        }
                        else {
                            System.out.println(SET_TEXT_COLOR_RED + "Username already taken. Please choose another one." + SET_TEXT_COLOR_WHITE);
                        }
                    }
                    break;
                case "2":
                    System.out.println("Insert your Username: ");
                    String loginUsername = scanner.next();
                    System.out.println("Insert your Password: ");
                    String loginPassword = scanner.next();
                    if (loginUsername == null || loginPassword == null){
                        System.out.println(SET_TEXT_COLOR_RED + "No fields can be blank. Please try again" + SET_TEXT_COLOR_WHITE);
                    }
                    else {
                        authToken = facade.login(loginUsername, loginPassword);
                        if (authToken != null){
                            postLoginMenu(authToken);
                        }
                        else {
                            System.out.println(SET_TEXT_COLOR_RED + "Username already taken. Please choose another one." + SET_TEXT_COLOR_WHITE);
                        }
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
                    System.out.println(SET_BG_COLOR_RED + "Invalid input" + SET_TEXT_COLOR_WHITE);
                    break;
            }
        }
    }

    private static void postLoginMenu(String authToken) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Map<Integer, GameData> gameMap = new TreeMap<>();
        int i = 1;
        menu:
        while (true){
            System.out.println(SET_TEXT_BOLD + "Enter a number (1-6) to continue:" + RESET_TEXT_BOLD_FAINT);
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
                     if (!facade.createGame(gameName, authToken)){
                         System.out.println(SET_BG_COLOR_RED + "Error: Couldn't create the game. Try Again" + SET_TEXT_COLOR_WHITE);
                     }
                    break;
                case "2":
                    ArrayList<GameData> games = facade.listGames(authToken);
                    gameMap = new TreeMap<>();
                    i = 1;
                    if (games != null){
                        for (GameData game : games){
                            gameMap.put(i, game);
                            i++;
                        }
                        for (Map.Entry<Integer, GameData> entry : gameMap.entrySet()) {
                            System.out.println(entry.getKey() + ". " + entry.getValue());
                        }
                    }
                    else {
                        System.out.println(SET_BG_COLOR_BLUE + "No Active Games" + SET_TEXT_COLOR_WHITE);
                    }
                    break;
                case "3":
                    System.out.println("Enter the number of the game you would like to observe: ");
                    String gameNumber = scanner.next();
                    System.out.println("This is where you would find the game and display the chessboard " + gameNumber);
                    break;
                case "4":
                    System.out.println("Enter the number of the game you would like to join: ");
                    int gameNumberToJoin = scanner.nextInt();
                    System.out.println("Enter 'WHITE' or 'BLACK' to choose a color");
                    String playerColor = scanner.next();
                    if (gameNumberToJoin <= gameMap.size()){
                        GameData game = gameMap.get(gameNumberToJoin);
                        String response = facade.joinGame(playerColor, game.gameID(), authToken);
                        if (!Objects.equals(response, "Error")){
                            DisplayBoard board = new DisplayBoard();
                            board.display(game.game().getBoard(), "WHITE");
                        }
                        else {
                            System.out.println(SET_BG_COLOR_RED+ "Color already taken. Choose another one or choose another game to join." + SET_TEXT_COLOR_WHITE);
                        }
                    }
                    System.out.println("This is where you would find the game, update it, and display the chessboard " + gameNumberToJoin);
                    break;
                case "5":
                    facade.logout(authToken);
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