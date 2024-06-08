import chess.*;
import model.GameData;
import ui.DisplayBoard;
import web.ServerFacade;

import java.util.*;

import static ui.EscapeSequences.*;

public class Main {
    static ServerFacade facade = new ServerFacade(8080);

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
                        System.out.println(SET_TEXT_COLOR_RED + "No fields can be blank. Please try again" + RESET_TEXT_COLOR);
                    }
                    else {
                        authToken = facade.register(username, password, email);
                        if (authToken != null){
                            postLoginMenu(authToken);
                        }
                        else {
                            System.out.println(SET_TEXT_COLOR_RED + "Username already taken. Please choose another one." + RESET_TEXT_COLOR);
                        }
                    }
                    break;
                case "2":
                    System.out.println("Insert your Username: ");
                    String loginUsername = scanner.next();
                    System.out.println("Insert your Password: ");
                    String loginPassword = scanner.next();
                    if (loginUsername == null || loginPassword == null){
                        System.out.println(SET_TEXT_COLOR_RED + "No fields can be blank. Please try again" + RESET_TEXT_COLOR);
                    }
                    else {
                        authToken = facade.login(loginUsername, loginPassword);
                        if (authToken != null){
                            postLoginMenu(authToken);
                        }
                        else {
                            System.out.println(SET_TEXT_COLOR_RED + "Username already taken. Please choose another one." + RESET_TEXT_COLOR);
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
                    System.out.println(SET_TEXT_COLOR_RED + "Invalid input" + RESET_TEXT_COLOR);
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
                         System.out.println(SET_TEXT_COLOR_RED + "Error: Couldn't create the game. Try Again" + RESET_TEXT_COLOR);
                     }
                    break;
                case "2":
                    gameMap = case2(authToken);
                    break;
                case "3":
                    System.out.println("Enter the number of the game you would like to observe: ");
                    try {
                        int gameNumber = scanner.nextInt();
                        if (gameNumber <= gameMap.size()) {
                            GameData game = gameMap.get(gameNumber);
                            DisplayBoard board = new DisplayBoard();
                            board.display(game.game().getBoard(), "WHITE");
                            board.display(game.game().getBoard(), "BLACK");
                        }
                    }
                    catch (Exception e) {
                        System.out.println(SET_TEXT_COLOR_RED + "Please enter a number." + RESET_TEXT_COLOR);
                        break;
                    }
                    break;
                case "4":
                    System.out.println("Enter the number of the game you would like to join: ");
                    try {
                        int gameNumberToJoin = scanner.nextInt();
                        if (gameNumberToJoin < 1 || gameNumberToJoin > gameMap.size()) {
                            System.out.println(SET_TEXT_COLOR_RED + "Invalid Game number. Try again." + RESET_TEXT_COLOR);
                        }
                        System.out.println("Enter 'w' for WHITE or 'b' for BLACK to choose a color");
                        String playerColor = scanner.next();
                        if (Objects.equals(playerColor, "w")){
                            playerColor = "WHITE";
                        }
                        else if (Objects.equals(playerColor, "b")){
                            playerColor = "BLACK";
                        }
                        else {
                            System.out.println(SET_TEXT_COLOR_RED + "Invalid Color" + RESET_TEXT_COLOR);
                            break;
                        }
                        if (gameNumberToJoin <= gameMap.size()){
                            GameData game = gameMap.get(gameNumberToJoin);
                            if ((playerColor.equals("WHITE") && game.whiteUsername() != null) || (playerColor.equals("BLACK") && game.blackUsername() != null)){
                                System.out.println(SET_TEXT_COLOR_RED + "Color already Taken" + RESET_TEXT_COLOR);
                                break;
                            }
                            String response = facade.joinGame(playerColor, game.gameID(), authToken);
                            if (!Objects.equals(response, "Error")){
                                DisplayBoard board = new DisplayBoard();
                                board.display(game.game().getBoard(), "WHITE");
                                board.display(game.game().getBoard(), "BLACK");
                            }
                            else {
                                System.out.println(SET_TEXT_COLOR_RED+ "Color already taken. Choose another one or choose another game to join." + RESET_TEXT_COLOR);
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.println(SET_TEXT_COLOR_RED + "Please enter a number." + RESET_TEXT_COLOR);
                        break;
                    }
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

    private static Map<Integer, GameData> case2(String authToken) throws Exception {
        int i;
        Map<Integer, GameData> gameMap;
        ArrayList<GameData> games = facade.listGames(authToken);
        gameMap = new TreeMap<>();
        i = 1;
        if (!games.isEmpty()){
            for (GameData game : games){
                gameMap.put(i, game);
                i++;
            }
            for (Map.Entry<Integer, GameData> entry : gameMap.entrySet()) {
                System.out.println(entry.getKey() + ". GameName: " + entry.getValue().gameName() + "| WhitePlayer: " +
                        entry.getValue().whiteUsername() + "| BlackPlayer: " + entry.getValue().blackUsername());
            }
        }
        else {
            System.out.println(SET_TEXT_COLOR_BLUE + "No Active Games" + RESET_TEXT_COLOR);
        }
        return gameMap;
    }
}