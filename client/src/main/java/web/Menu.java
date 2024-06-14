package web;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.DisplayBoard;
import websocket.messages.ServerMessage;

import java.util.*;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class Menu implements ServerMessageObserver {
    private ServerFacade facade = new ServerFacade(8080, this);
    private String colorOfPlayer = null;

    public String getColorOfPlayer(){
        return colorOfPlayer;
    }

    @Override
    public void notify(ServerMessage message){

    }

    public void preLoginMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String authToken;
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

    private void postLoginMenu(String authToken) throws Exception {
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
                    case1CreateGame(authToken, scanner);
                    break;
                case "2":
                    gameMap = case2ListGames(authToken);
                    break;
                case "3":
                    case3ObserveGame(authToken, scanner, gameMap);
                    break;
                case "4":
                    case4JoinGame(authToken, scanner, gameMap);
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


    private void gameMenu(GameData game, String authToken) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(SET_TEXT_BOLD + "Enter a number (1-6) to continue:" + RESET_TEXT_BOLD_FAINT);
            System.out.println("1. Make a Move");
            System.out.println("2. Highlight Legal Moves");
            System.out.println("3. Redraw the Chess Board");
            System.out.println("4. Resign");
            System.out.println("5. Leave");
            System.out.println("6. Help");
            var input = scanner.next();
            String response;
            switch (input) {
                case "1":
                    System.out.println("Enter a move: (first square position followed by second ex: b6b5)");
                    String position = scanner.next();
                    boolean correctData = true;
                    char startRow = position.charAt(0);
                    if (startRow < 97 || startRow > 104){
                        correctData = false;
                    }
                    char startCol = position.charAt(1);
                    if (startCol < 49 || startCol > 56){
                        correctData = false;
                    }
                    char endRow = position.charAt(2);
                    if (endRow < 97 || endRow > 104){
                        correctData = false;
                    }
                    char endCol = position.charAt(3);
                    if (endCol < 49 || endCol > 56){
                        correctData = false;
                    }
                    ChessPiece.PieceType promotionPiece = null;
                    if (correctData){
                        ChessMove move = new ChessMove(new ChessPosition((char) startRow, (int) startCol - '1'), new ChessPosition((char) endRow, (int) endCol - '1'), promotionPiece);
                        try {
                            facade.makeMove(move, authToken, game.gameID());
                        }
                        catch (Exception e){
                            System.out.println(SET_TEXT_COLOR_RED + e.getMessage() + RESET_TEXT_COLOR);
                        }
                    }
                    break;
                case "2":
                    System.out.println("Enter a move: (Piece position ex: a2)");
                    response = scanner.next();
                    boolean correctPosition = true;
                    char row = response.charAt(0);
                    if (row < 97 || row > 104){
                        correctPosition = false;
                    }
                    char col = response.charAt(1);
                    if (col < 49 || col > 56){
                        correctPosition = false;
                    }
                    if (correctPosition){
                        ChessPosition wantedPosition = new ChessPosition(row, (int) col - '1');
                        DisplayBoard displayBoard = new DisplayBoard();
                        Collection<ChessMove> validMoves = game.game().validMoves(wantedPosition);
                        if (Objects.equals(colorOfPlayer, "OBSERVER")){
                            displayBoard.display(game.game().getBoard(), "WHITE", validMoves, wantedPosition);
                        }
                        else {
                            displayBoard.display(game.game().getBoard(), colorOfPlayer, validMoves, wantedPosition);
                        }
                    }
                    break;
                case "3":
                    DisplayBoard newBoard = new DisplayBoard();
                    try {
                        ArrayList<GameData> newGameList = facade.listGames(authToken);
                        for (GameData gameData : newGameList){
                            if (gameData.gameID() == game.gameID()){
                                game = gameData;
                            }
                        }
                    }
                    catch (Exception e){
                        System.out.println(SET_TEXT_COLOR_RED + e.getMessage() + RESET_TEXT_COLOR);
                    }
                    if (Objects.equals(colorOfPlayer, "OBSERVER")) {
                        newBoard.display(game.game().getBoard(), "WHITE", null, null);
                    }
                    else {
                        newBoard.display(game.game().getBoard(), colorOfPlayer, null, null);
                    }
                    break;
                case "4":
                    System.out.println("Are you sure you want to resign? (y/n)");
                    response = scanner.next();
                    if (Objects.equals(response, "y") || Objects.equals(response, "Y")) {
                        try {
                            facade.resign(authToken, game.gameID());
                        }
                        catch (Exception e) {
                            System.out.println(SET_TEXT_COLOR_RED + e.getMessage() + RESET_TEXT_COLOR);
                        }
                    }
                    break;
                case "5":
                    System.out.println("Are you sure you want to leave? (y/n)");
                    response = scanner.next();
                    if (Objects.equals(response, "y") || Objects.equals(response, "Y")) {
                        try {
                            facade.leave(authToken, game.gameID());
                        }
                        catch (Exception e) {
                            System.out.println(SET_TEXT_COLOR_RED + e.getMessage() + RESET_TEXT_COLOR);
                        }
                    }
                    break;
                case "6":
                    System.out.println("Type '1' to make a move. Player will type the position of a piece to move and where they want to move it.");
                    System.out.println("Type '2' to highlight all the legal moves of the specified piece");
                    System.out.println("Type '3' to redraw the chess board");
                    System.out.println("Type '4' to resign from the game");
                    System.out.println("Type '5' to leave the game \n");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }





    private void case1CreateGame(String authToken, Scanner scanner) throws Exception {
        System.out.println("Enter a name for the game: ");
        String gameName = scanner.next();
        if (!facade.createGame(gameName, authToken)){
            System.out.println(SET_TEXT_COLOR_RED + "Error: Couldn't create the game. Try Again" + RESET_TEXT_COLOR);
        }
    }

    private Map<Integer, GameData> case2ListGames(String authToken) throws Exception {
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

    private void case3ObserveGame(String authToken, Scanner scanner, Map<Integer, GameData> gameMap) {
        System.out.println("Enter the number of the game you would like to observe: ");
        try {
            int gameNumber = scanner.nextInt();
            colorOfPlayer = "OBSERVER";
            if (gameNumber <= gameMap.size()) {
                GameData game = gameMap.get(gameNumber);
                facade.connect(authToken, game.gameID());
                gameMenu(game, authToken);
            }
        }
        catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "Please enter a number." + RESET_TEXT_COLOR);
            return;
        }
    }

    private void case4JoinGame(String authToken, Scanner scanner, Map<Integer, GameData> gameMap) {
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
                this.colorOfPlayer = "WHITE";
            }
            else if (Objects.equals(playerColor, "b")){
                playerColor = "BLACK";
                this.colorOfPlayer = "BLACK";
            }
            else {
                System.out.println(SET_TEXT_COLOR_RED + "Invalid Color" + RESET_TEXT_COLOR);
                return;
            }
            if (gameNumberToJoin <= gameMap.size()){
                GameData game = gameMap.get(gameNumberToJoin);
                if ((playerColor.equals("WHITE") && game.whiteUsername() != null) || (playerColor.equals("BLACK") && game.blackUsername() != null)){
                    System.out.println(SET_TEXT_COLOR_RED + "Color already Taken" + RESET_TEXT_COLOR);
                    return;
                }
                String response = facade.joinGame(playerColor, game.gameID(), authToken);
                if (!Objects.equals(response, "Error")){
                    facade.connect(authToken, game.gameID());
                    gameMenu(game, authToken);
                }
                else {
                    System.out.println(SET_TEXT_COLOR_RED+ "Color already taken. Choose another one or choose another game to join." + RESET_TEXT_COLOR);
                }
            }
        }
        catch (Exception e) {
            //System.out.println(SET_TEXT_COLOR_RED + "Please enter a number." + RESET_TEXT_COLOR);
            System.out.println(SET_TEXT_COLOR_RED + e.getMessage() + RESET_TEXT_COLOR);
            return;
        }
    }
}
