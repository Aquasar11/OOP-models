package Controllers;

import Models.Board;
import Models.ElementsInShop;
import Models.Game;
import Models.ObjectsOnTheBoard.AA;
import Models.ObjectsOnTheBoard.Ship;
import Models.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menus {
    private static final Scanner scanner = new Scanner(System.in);

    public static void registerMenu() {
        String command = scanner.nextLine();
        while (!command.equals("exit")) {
            if (Pattern.matches("^register ([^\\s]+) ([^\\s]+)$", command)) {
                registerPlayer(command);
            } else if (Pattern.matches("^login ([^\\s]+) ([^\\s]+)$", command)) {
                loginPlayer(command);
            } else if (Pattern.matches("^remove ([^\\s]+) ([^\\s]+)$", command)) {
                removePlayer(command);
            } else if (command.equals("list_users")) {
                printUsersList();
            } else if (command.equals("help")) {
                System.out.println("register [username] [password]\n" +
                        "login [username] [password]\n" +
                        "remove [username] [password]\n" +
                        "list_users\n" +
                        "help\n" +
                        "exit");
            } else
                System.out.println("invalid command");
            command = scanner.nextLine();
        }
        System.out.println("program ended");
    }

    private static void printUsersList() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : Player.getPlayers()) {
            playerNames.add(player.getPlayerName());
        }
        Collections.sort(playerNames);
        for (String name : playerNames) {
            System.out.println(name);
        }
    }

    private static void removePlayer(String input) {
        if (!Pattern.matches("^remove [\\w]+ [^\\s]+$", input))
            System.out.println("username format is invalid");
        else if (!Pattern.matches("^remove [\\w]+ [\\w]+$", input))
            System.out.println("password format is invalid");
        else {
            Matcher loginMatcher = Pattern.compile("^remove ([\\w]+) ([\\w]+)$").matcher(input.trim());
            loginMatcher.find();
            String username = loginMatcher.group(1);
            String password = loginMatcher.group(2);
            if (Player.getPlayerByName(username) == null)
                System.out.println("no user exists with this username");
            else if (!Player.getPlayerByName(username).getPlayerPassword().equals(password))
                System.out.println("incorrect password");
            else {
                Player.removePlayer(username);
                System.out.println("removed " + username + " successfully");
            }
        }
    }

    private static void loginPlayer(String input) {
        if (!Pattern.matches("^login [\\w]+ [^\\s]+$", input.trim()))
            System.out.println("username format is invalid");
        else if (!Pattern.matches("^login [\\w]+ [\\w]+$", input.trim()))
            System.out.println("password format is invalid");
        else {
            Matcher loginMatcher = Pattern.compile("^login ([\\w]+) ([\\w]+)$").matcher(input.trim());
            loginMatcher.find();
            String username = loginMatcher.group(1);
            String password = loginMatcher.group(2);
            if (Player.getPlayerByName(username) == null)
                System.out.println("no user exists with this username");
            else if (!Player.getPlayerByName(username).getPlayerPassword().equals(password))
                System.out.println("incorrect password");
            else {
                System.out.println("login successful");
                mainMenu(username);
            }
        }
    }

    private static void registerPlayer(String input) {
        if (!Pattern.matches("^register [\\w]+ [^\\s]+$", input.trim()))
            System.out.println("username format is invalid");
        else if (!Pattern.matches("^register [\\w]+ [\\w]+$", input.trim()))
            System.out.println("password format is invalid");
        else {
            Matcher registerMatcher = Pattern.compile("^register ([\\w]+) ([\\w]+)$").matcher(input.trim());
            registerMatcher.find();
            String username = registerMatcher.group(1);
            String password = registerMatcher.group(2);
            if (Player.getPlayerByName(username) == null) {
                new Player(username, password);
                System.out.println("register successful");
            } else
                System.out.println("a user exists with this username");
        }
    }

    private static void mainMenu(String username) {
        String command = scanner.nextLine();
        while (!command.equals("logout")) {
            if (Pattern.matches("^new_game ([^\\s]+)$", command)) {
                newGame(username, command.substring(9));
            } else if (command.equals("scoreboard")) {
                scoreboard();
            } else if (command.equals("shop")) {
                shopMenu(username);
            } else if (command.equals("list_users")) {
                printUsersList();
            } else if (command.equals("help")) {
                System.out.println("new_game [username]\n" +
                        "scoreboard\n" +
                        "list_users\n" +
                        "shop\n" +
                        "help\n" +
                        "logout");
            } else
                System.out.println("invalid command");
            command = scanner.nextLine();
        }
        System.out.println("logout successful");
    }

    private static void newGame(String username, String rivalName) {
        if (!Pattern.matches("[\\w]+", rivalName))
            System.out.println("username format is invalid");
        else if (username.equals(rivalName))
            System.out.println("you must choose another player to start a game");
        else if (Player.getPlayerByName(rivalName) == null)
            System.out.println("no user exists with this username");
        else {
            Game currentGame = new Game(Player.getPlayerByName(username), Player.getPlayerByName(rivalName));
            System.out.println("new game started successfully between " + username + " and " + rivalName);
            arrangeBoardMenu(currentGame, username, rivalName, false);
        }
    }

    private static void scoreboard() {
        ArrayList<Player> players = Player.getPlayers();

        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = 0; j < players.size() - i - 1; j++) {
                if (needSwap(players.get(j), players.get(j + 1)))
                    Collections.swap(players, j, j + 1);
            }
        }

        for (Player player : players) {
            System.out.println(player.getPlayerName() + " " + player.getScore() + " " +
                    player.getWins() + " " + player.getDraws() + " " + player.getLooses());
        }
    }


    private static boolean needSwap(Player player1, Player player2) {
        if (player1.getScore() < player2.getScore())
            return true;
        else if (player1.getWins() < player2.getWins() &&
                player1.getScore() == player2.getScore())
            return true;
        else if (player1.getDraws() < player2.getDraws() &&
                player1.getScore() == player2.getScore() &&
                player1.getWins() == player2.getWins())
            return true;
        else if (player1.getLooses() > player2.getLooses() &&
                player1.getScore() == player2.getScore() &&
                player1.getWins() == player2.getWins() &&
                player1.getDraws() == player2.getDraws())
            return true;
        else if (player1.getLooses() == player2.getLooses() &&
                player1.getScore() == player2.getScore() &&
                player1.getWins() == player2.getWins() &&
                player1.getDraws() == player2.getDraws() &&
                player1.getPlayerName().compareTo(player2.getPlayerName()) > 0) {
            return true;
        }
        return false;
    }

    private static void shopMenu(String username) {
        String command = scanner.nextLine();
        while (!command.equals("back")) {
            if (Pattern.matches("^buy ([\\S]+) ([\\d]+)$", command)) {
                buy(username, command);
            } else if (command.equals("show-amount")) {
                System.out.println(Player.getPlayerByName(username).getPlayerBalance());
            } else if (command.equals("help")) {
                System.out.println("buy [product] [number]\n" +
                        "show-amount\n" +
                        "help\n" +
                        "back");
            } else
                System.out.println("invalid command");
            command = scanner.nextLine();
        }
    }

    private static void buy(String username, String command) {
        if (!Pattern.matches("^buy (mine|antiaircraft|airplane|scanner|invisible) ([^\\s]+)", command))
            System.out.println("there is no product with this name");
        else if (!Pattern.matches("^buy (mine|antiaircraft|airplane|scanner|invisible) ([1-9][0-9]*)$", command))
            System.out.println("invalid number");
        else {
            Matcher buyProductMatcher = Pattern.compile("^buy (mine|antiaircraft|airplane|scanner|invisible) ([\\d]+)$").matcher(command);
            buyProductMatcher.find();
            String productName = buyProductMatcher.group(1);
            int productAmount = Integer.parseInt(buyProductMatcher.group(2));
            if (Player.getPlayerByName(username).getPlayerBalance() < (productAmount * ElementsInShop.getElementByName(productName).getElementValue()))
                System.out.println("you don't have enough money");
            else {
                Player.getPlayerByName(username).buyElementFromShop(productName, productAmount);
                int newBalance = Player.getPlayerByName(username).getPlayerBalance() - (productAmount * ElementsInShop.getElementByName(productName).getElementValue());
                Player.getPlayerByName(username).setPlayerBalance(newBalance);
            }
        }
    }

    private static void arrangeBoardMenu(Game game, String username, String rivalName, boolean rivalsTurn) {
        String command = scanner.nextLine();
        while (true) {
            if (Pattern.matches("^put S([\\d]+) ([\\d]+),([\\d]+) -([\\S])$", command)) {
                putShipOnBoard(game, username, command, rivalsTurn);
            } else if (Pattern.matches("^put-mine ([\\d]+),([\\d]+)$", command)) {
                putMineOnBoard(game, username, command, rivalsTurn);
            } else if (Pattern.matches("^put-antiaircraft ([\\d]+) -([\\w])$", command)) {
                putAAOnBoard(game, username, command, rivalsTurn);
            } else if (Pattern.matches("^invisible ([\\d]+),([\\d]+)$", command)) {
                makeShipInvisible(game, username, command, rivalsTurn);
            } else if (command.equals("show-my-board")) {
                printPlayerBoard(game, rivalsTurn);
            } else if (command.equals("finish-arranging")) {
                if (isReady(game, rivalsTurn)) {
                    System.out.println("turn completed");
                    break;
                } else
                    System.out.println("you must put all ships on the board");
            } else if (command.equals("help")) {
                System.out.println("put S[number] [x],[y] [-h|-v]\n" +
                        "put-mine [x],[y]\n" +
                        "put-antiaircraft [s] [-h|-v]\n" +
                        "invisible [x],[y]\n" +
                        "show-my-board\n" +
                        "help\n" +
                        "finish-arranging");
            } else
                System.out.println("invalid command");
            command = scanner.nextLine();
        }
        if (!rivalsTurn)
            arrangeBoardMenu(game, username, rivalName, true);
        else
            gameMenu(game, username, rivalName);
    }

    private static void printPlayerBoard(Game game, boolean rivalsTurn) {
        if (!rivalsTurn)
            game.getFirstPlayerBoard().showBoard();
        else
            game.getSecondPlayerBoard().showBoard();
    }

    private static void makeShipInvisible(Game game, String player, String command, boolean rivalsTurn) {
        if (!Pattern.matches("^invisible ([1-9]|10),([1-9]|10)$", command))
            System.out.println("wrong coordination");
        else {
            Matcher makeShipInvisibleMatcher = Pattern.compile("^invisible ([1-9]|10),([1-9]|10)$").matcher(command);
            makeShipInvisibleMatcher.find();
            int xPosition = Integer.parseInt(makeShipInvisibleMatcher.group(1));
            int yPosition = Integer.parseInt(makeShipInvisibleMatcher.group(2));
            if (!rivalsTurn) {
                if (game.getFirstPlayer().getNumberOfInvisible() == 0)
                    System.out.println("you don't have enough invisible");
                else if (!isThereAShip(game, xPosition, yPosition, rivalsTurn))
                    System.out.println("there is no ship on this place on the board");
                else if (isThereAlreadyInvisible(game, xPosition, yPosition, rivalsTurn))
                    System.out.println("this place has already made invisible");
                else {
                    game.getFirstPlayerBoard().makeShipInvisible(xPosition, yPosition);
                    game.getFirstPlayer().setNumberOfInvisible(game.getFirstPlayer().getNumberOfInvisible() - 1);
                }
            } else {
                if (game.getSecondPlayer().getNumberOfInvisible() == 0)
                    System.out.println("you don't have enough invisible");
                else if (!isThereAShip(game, xPosition, yPosition, rivalsTurn))
                    System.out.println("there is no ship on this place on the board");
                else if (isThereAlreadyInvisible(game, xPosition, yPosition, rivalsTurn))
                    System.out.println("this place has already made invisible");
                else {
                    game.getSecondPlayerBoard().makeShipInvisible(xPosition, yPosition);
                    game.getSecondPlayer().setNumberOfInvisible(game.getSecondPlayer().getNumberOfInvisible() - 1);
                }
            }
        }
    }

    private static boolean isThereAlreadyInvisible(Game game, int xPosition, int yPosition, boolean rivalsTurn) {
        if (!rivalsTurn) {
            return game.getFirstPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'I';
        } else {
            return game.getSecondPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'I';
        }
    }

    private static boolean isThereAShip(Game game, int xPosition, int yPosition, boolean rivalsTurn) {
        if (!rivalsTurn) {
            return game.getFirstPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'S' ||
                    game.getFirstPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'I';
        } else {
            return game.getSecondPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'S' ||
                    game.getSecondPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'I';
        }
    }

    private static void putAAOnBoard(Game game, String player, String command, boolean rivalsTurn) {
        if (!Pattern.matches("^put-antiaircraft ([1-9]|10) -([\\w])$", command))
            System.out.println("wrong coordination");
        else if (Pattern.matches("^put-antiaircraft (9|10) -([\\w])$", command))
            System.out.println("off the board");
        else if (!Pattern.matches("^put-antiaircraft (1|2|3|4|5|6|7|8) -(v|h)$", command))
            System.out.println("invalid direction");
        else {
            Matcher putAAMatcher = Pattern.compile("^put-antiaircraft (1|2|3|4|5|6|7|8) -(v|h)$").matcher(command);
            putAAMatcher.find();
            int position = Integer.parseInt(putAAMatcher.group(1));
            boolean vertical = false;
            if (putAAMatcher.group(2).equals("v"))
                vertical = true;
            if (!rivalsTurn) {
                if (game.getFirstPlayer().getNumberOfAntiaircraft() == 0)
                    System.out.println("you don't have enough antiaircraft");
                else {
                    game.getFirstPlayerBoard().putAAOnBoard(position, vertical);
                    ArrayList<AA> newAA = game.getFirstPlayerAAs();
                    newAA.add(new AA(game.getFirstPlayerAAs().size(), position, vertical));
                    game.setFirstPlayerAAs(newAA);
                    game.getFirstPlayer().setNumberOfAntiaircraft(game.getFirstPlayer().getNumberOfAntiaircraft() - 1);
                }
            } else {
                if (game.getSecondPlayer().getNumberOfAntiaircraft() == 0)
                    System.out.println("you don't have enough antiaircraft");
                else {
                    game.getSecondPlayerBoard().putAAOnBoard(position, vertical);
                    ArrayList<AA> newAA = game.getSecondPlayerAAs();
                    newAA.add(new AA(game.getSecondPlayerAAs().size(), position, vertical));
                    game.setSecondPlayerAAs(newAA);
                    game.getSecondPlayer().setNumberOfAntiaircraft(game.getSecondPlayer().getNumberOfAntiaircraft() - 1);
                }
            }
        }
    }

    private static void putMineOnBoard(Game game, String player, String command, boolean rivalsTurn) {
        if (!Pattern.matches("^put-mine (1|2|3|4|5|6|7|8|9|10),(1|2|3|4|5|6|7|8|9|10)$", command))
            System.out.println("wrong coordination");
        else {
            Matcher putMineMatcher = Pattern.compile("^put-mine (1|2|3|4|5|6|7|8|9|10),(1|2|3|4|5|6|7|8|9|10)$").matcher(command);
            putMineMatcher.find();
            int xPosition = Integer.parseInt(putMineMatcher.group(1));
            int yPosition = Integer.parseInt(putMineMatcher.group(2));
            if (!rivalsTurn) {
                if (!(game.getFirstPlayer().getNumberOfMines() > 0))
                    System.out.println("you don't have enough mine");
                else if (checkMineCollision(game, xPosition, yPosition, rivalsTurn))
                    System.out.println("collision with the other ship or mine on the board");
                else {
                    game.getFirstPlayerBoard().putMineOnBoard(xPosition, yPosition);
                    game.getFirstPlayer().setNumberOfMines(game.getFirstPlayer().getNumberOfMines() - 1);
                }
            } else {
                if (!(game.getSecondPlayer().getNumberOfMines() > 0))
                    System.out.println("you don't have enough mine");
                else if (checkMineCollision(game, xPosition, yPosition, rivalsTurn))
                    System.out.println("collision with the other ship or mine on the board");
                else {
                    game.getSecondPlayerBoard().putMineOnBoard(xPosition, yPosition);
                    game.getSecondPlayer().setNumberOfMines(game.getSecondPlayer().getNumberOfMines() - 1);
                }
            }
        }
    }

    private static boolean checkMineCollision(Game game, int xPosition, int yPosition, boolean rivalsTurn) {
        if (!rivalsTurn) {
            return !game.getFirstPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].equals("  ") &&
                    !game.getFirstPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].equals("AA");
        } else {
            return !game.getSecondPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].equals("  ") &&
                    !game.getSecondPlayerBoard().getBoard()[yPosition - 1][xPosition - 1].equals("AA");
        }
    }

    private static void putShipOnBoard(Game game, String player, String command, boolean rivalsTurn) {
        if (!Pattern.matches("^put S(1|2|3|4) ([\\d]+),([\\d]+) -([\\S])$", command))
            System.out.println("invalid ship number");
        else if (!Pattern.matches("^put S(1|2|3|4) (1|2|3|4|5|6|7|8|9|10),(1|2|3|4|5|6|7|8|9|10) -([\\S])$", command))
            System.out.println("wrong coordination");
        else if (!Pattern.matches("^put S(1|2|3|4) (1|2|3|4|5|6|7|8|9|10),(1|2|3|4|5|6|7|8|9|10) -(h|v)$", command))
            System.out.println("invalid direction");
        else {
            Matcher putShipMatcher = Pattern.compile("^put S(1|2|3|4) (1|2|3|4|5|6|7|8|9|10),(1|2|3|4|5|6|7|8|9|10) -(h|v)$").matcher(command);
            putShipMatcher.find();
            int shipSize = Integer.parseInt(putShipMatcher.group(1));
            int xPosition = Integer.parseInt(putShipMatcher.group(2));
            int yPosition = Integer.parseInt(putShipMatcher.group(3));
            boolean vertical = false;
            if (putShipMatcher.group(4).equals("v"))
                vertical = true;
            if (!rivalsTurn) {
                if (isOutOfBounds(shipSize, xPosition, yPosition, vertical))
                    System.out.println("off the board");
                else if (game.numberOfRemainingShipByModel(shipSize) == (5 - shipSize))
                    System.out.println("you don't have this type of ship");
                else if (checkCollision(game, shipSize, xPosition, yPosition, vertical, false))
                    System.out.println("collision with the other ship or mine on the board");
                else {
                    game.getFirstPlayerBoard().putShipOnBoard(xPosition, yPosition, shipSize, vertical);
                    ArrayList<Ship> newShips = game.getFirstPlayerShips();
                    newShips.add(new Ship(shipSize, xPosition, yPosition, vertical));
                    game.setFirstPlayerShips(newShips);
                    game.decreaseNumberOfShipBySize(shipSize);
                    game.setRemainingShipForFirstPlayer(game.getRemainingShipForFirstPlayer() - 1);
                }
            } else {
                if (isOutOfBounds(shipSize, xPosition, yPosition, vertical))
                    System.out.println("off the board");
                else if (game.numberOfRemainingShipByModel(shipSize) == 0)
                    System.out.println("you don't have this type of ship");
                else if (checkCollision(game, shipSize, xPosition, yPosition, vertical, true))
                    System.out.println("collision with the other ship or mine on the board");
                else {
                    game.getSecondPlayerBoard().putShipOnBoard(xPosition, yPosition, shipSize, vertical);
                    ArrayList<Ship> newShips = game.getSecondPlayerShips();
                    newShips.add(new Ship(shipSize, xPosition, yPosition, vertical));
                    game.setSecondPlayerShips(newShips);
                    game.decreaseNumberOfShipBySize(shipSize);
                    game.setRemainingShipForSecondPlay(game.getRemainingShipForSecondPlay() - 1);
                }
            }
        }
    }

    private static boolean isOutOfBounds(int shipSize, int xPosition, int yPosition, boolean vertical) {
        if (vertical)
            return yPosition + shipSize > 11;
        else
            return xPosition + shipSize > 11;
    }

    private static boolean checkCollision(Game game, int shipSize, int xPosition, int yPosition, boolean vertical, boolean secondPlayer) {
        if (!secondPlayer) {
            if (vertical) {
                for (int i = 0; i < shipSize; i++) {
                    if (!game.getFirstPlayerBoard().getBoard()[yPosition - 1 + i][xPosition - 1].equals("  ") &&
                            !game.getFirstPlayerBoard().getBoard()[yPosition - 1 + i][xPosition - 1].equals("AA"))
                        return true;
                }
            } else {
                for (int i = 0; i < shipSize; i++) {
                    if (!game.getFirstPlayerBoard().getBoard()[yPosition - 1][xPosition - 1 + i].equals("  ") &&
                            !game.getFirstPlayerBoard().getBoard()[yPosition - 1][xPosition - 1 + i].equals("AA"))
                        return true;
                }
            }
            return false;
        } else {
            if (vertical) {
                for (int i = 0; i < shipSize; i++) {
                    if (!game.getSecondPlayerBoard().getBoard()[yPosition - 1 + i][xPosition - 1].equals("  ") &&
                            !game.getSecondPlayerBoard().getBoard()[yPosition - 1 + i][xPosition - 1].equals("AA"))
                        return true;
                }
            } else {
                for (int i = 0; i < shipSize; i++) {
                    if (!game.getSecondPlayerBoard().getBoard()[yPosition - 1][xPosition - 1 + i].equals("  ") &&
                            !game.getSecondPlayerBoard().getBoard()[yPosition - 1][xPosition - 1 + i].equals("AA"))
                        return true;
                }
            }
            return false;
        }
    }

    private static boolean isReady(Game game, boolean rivalsTurn) {
        if (!rivalsTurn &&
                (game.getRemainingShipForFirstPlayer() == 0))
            return true;
        else return (game.getRemainingShipForSecondPlay() == 0);
    }

    private static void gameMenu(Game game, String username, String rivalName) {
        String command = scanner.nextLine();
        while (!game.isFinished() && !command.equals("forfeit")) {
            if (command.equals("show-turn"))
                System.out.println(username + "'s turn");
            else if (Pattern.matches("^bomb ([\\d]+),([\\d]+)$", command)) {
                bomb(game, username, rivalName, command);
            } else if (Pattern.matches("^put-airplane ([\\d]+),([\\d]+) -([\\w])$", command)) {
                sendAirplane(game, username, rivalName, command);
            } else if (Pattern.matches("^scanner ([\\d]+),([\\d]+)$", command)) {
                scanTheBoard(game, username, command);
            } else if (command.equals("show-my-board")) {
                showMyBoard(game, username);
            } else if (command.equals("show-rival-board")) {
                showEnemyBoard(game, rivalName);
            } else if (command.equals("help")) {
                System.out.println("bomb [x],[y]\n" +
                        "put-airplane [x],[y] [-h|-v]\n" +
                        "scanner [x],[y]\n" +
                        "show-turn\n" +
                        "show-my-board\n" +
                        "show-rival-board\n" +
                        "help\n" +
                        "forfeit");
            } else
                System.out.println("invalid command");
            checkGameStatus(game);
            if (!game.isFinished())
                command = scanner.nextLine();
        }
        if (command.equals("forfeit"))
            forfeit(game, username, rivalName);
    }

    private static void checkGameStatus(Game game) {
        if (drawHappens(game) && !game.isFinished()) {
            System.out.println("draw");
            game.countScores();
            game.getFirstPlayer().increaseBalance(25 + game.getFirstPlayerScore());
            game.getSecondPlayer().increaseBalance(25 + game.getSecondPlayerScore());
            game.getFirstPlayer().increaseScore(1);
            game.getSecondPlayer().increaseScore(1);
            game.getFirstPlayer().increaseDraws(1);
            game.getSecondPlayer().increaseDraws(1);
            game.setFinished(true);
        } else if (anyoneWins(game) && !game.isFinished()) {
            game.setFinished(true);
            game.countScores();
            findTheWinner(game);
        }
    }

    private static void findTheWinner(Game game) {
        int firstPlayerCounter = 0;
        for (Ship ship : game.getFirstPlayerShips()) {
            if (ship.getShipRemainingPositions().size() == 0)
                firstPlayerCounter += 1;
        }
        if (firstPlayerCounter == 10) {
            game.getFirstPlayer().increaseBalance(game.getFirstPlayerScore());
            game.getSecondPlayer().increaseBalance(50 + game.getSecondPlayerScore());
            game.getFirstPlayer().increaseLooses(1);
            game.getSecondPlayer().increaseWins(1);
            game.getSecondPlayer().increaseScore(3);
            System.out.println(game.getSecondPlayer().getPlayerName() + " is winner");
        } else {
            game.getFirstPlayer().increaseBalance(50 + game.getFirstPlayerScore());
            game.getSecondPlayer().increaseBalance(game.getSecondPlayerScore());
            game.getFirstPlayer().increaseWins(1);
            game.getSecondPlayer().increaseLooses(1);
            game.getFirstPlayer().increaseScore(3);
            System.out.println(game.getFirstPlayer().getPlayerName() + " is winner");
        }
    }

    private static boolean anyoneWins(Game game) {
        int firstPlayerCounter = 0;
        int secondPlayerCounter = 0;
        for (Ship ship : game.getFirstPlayerShips()) {
            if (ship.getShipRemainingPositions().size() == 0)
                firstPlayerCounter += 1;
        }
        for (Ship ship : game.getSecondPlayerShips()) {
            if (ship.getShipRemainingPositions().size() == 0)
                secondPlayerCounter += 1;
        }
        return firstPlayerCounter == 10 || secondPlayerCounter == 10;
    }

    private static boolean drawHappens(Game game) {
        int firstPlayerCounter = 0;
        int secondPlayerCounter = 0;
        for (Ship ship : game.getFirstPlayerShips()) {
            if (ship.getShipRemainingPositions().size() == 0)
                firstPlayerCounter += 1;
        }
        for (Ship ship : game.getSecondPlayerShips()) {
            if (ship.getShipRemainingPositions().size() == 0)
                secondPlayerCounter += 1;
        }
        return firstPlayerCounter == 10 && secondPlayerCounter == 10;
    }

    private static void sendAirplane(Game game, String username, String rivalName, String command) {
        if (!Pattern.matches("^put-airplane ([1-9]|10),([1-9]|10) -([\\w])$", command))
            System.out.println("wrong coordination");
        else if (!Pattern.matches("^put-airplane ([1-9]|10),([1-9]|10) -(h|v)$", command))
            System.out.println("invalid direction");
        else {
            Matcher airplaneMatcher = Pattern.compile("^put-airplane ([1-9]|10),([1-9]|10) -(h|v)$").matcher(command);
            airplaneMatcher.find();
            int xPosition = Integer.parseInt(airplaneMatcher.group(1));
            int yPosition = Integer.parseInt(airplaneMatcher.group(2));
            boolean vertical = false;
            if (airplaneMatcher.group(3).equals("v"))
                vertical = true;
            if (isAirplaneOutOfBounds(xPosition, yPosition, vertical))
                System.out.println("off the board");
            else if (game.getFirstPlayer().getPlayerName().equals(username)) {
                if (game.getFirstPlayer().getNumberOfAirplane() == 0)
                    System.out.println("you don't have airplane");
                else if (isInAABound(game.getSecondPlayerAAs(), xPosition, yPosition, vertical, rivalName, game)) {
                    System.out.println("the rival's antiaircraft destroyed your airplane");
                    game.getFirstPlayer().setNumberOfAirplane(game.getFirstPlayer().getNumberOfAirplane() - 1);
                } else {
                    destroyWithAirplane(game, username, rivalName, xPosition, yPosition, vertical);
                    game.getFirstPlayer().setNumberOfAirplane(game.getFirstPlayer().getNumberOfAirplane() - 1);
                }
            } else {
                if (game.getSecondPlayer().getNumberOfAirplane() == 0)
                    System.out.println("you don't have airplane");
                else if (isInAABound(game.getFirstPlayerAAs(), xPosition, yPosition, vertical, rivalName, game)) {
                    System.out.println("the rival's antiaircraft destroyed your airplane");
                    game.getSecondPlayer().setNumberOfAirplane(game.getSecondPlayer().getNumberOfAirplane() - 1);
                } else {
                    destroyWithAirplane(game, username, rivalName, xPosition, yPosition, vertical);
                    game.getSecondPlayer().setNumberOfAirplane(game.getSecondPlayer().getNumberOfAirplane() - 1);
                }
            }
        }
    }

    private static boolean isInAABound(ArrayList<AA> listOfAAs, int xPosition, int yPosition, boolean vertical, String name, Game game) {
        for (AA aa : listOfAAs) {
            if (vertical) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 5; j++) {
                        for (int[] position : aa.getPositions()) {
                            if (position[0] == xPosition + i && position[1] == yPosition + j) {
                                game.removeAA(name, aa);
                                if (game.getFirstPlayer().getPlayerName().equals(name))
                                    game.getFirstPlayerBoard().destroyedAAOnBoard(game.getFirstPlayerAAs());
                                else
                                    game.getSecondPlayerBoard().destroyedAAOnBoard(game.getSecondPlayerAAs());
                                return true;
                            }
                        }
                    }
                }
                return false;
            } else {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (int[] position : aa.getPositions()) {
                            if (position[0] == xPosition + i && position[1] == yPosition + j) {
                                game.removeAA(name, aa);
                                if (game.getFirstPlayer().getPlayerName().equals(name))
                                    game.getFirstPlayerBoard().destroyedAAOnBoard(game.getFirstPlayerAAs());
                                else
                                    game.getSecondPlayerBoard().destroyedAAOnBoard(game.getSecondPlayerAAs());
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    private static void destroyWithAirplane(Game game, String username, String rivalName, int xPosition, int yPosition, boolean vertical) {
        int destroyerCounter = 0;
        if (vertical) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 5; j++) {
                    if (game.getFirstPlayer().getPlayerName().equals(username)) {
                        if (isDamaged(game.getSecondPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitShip(game, game.getSecondPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isDamaged(game.getSecondPlayerBoard(), xPosition + i, yPosition + j) &&
                                isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            DestroyShip(game, game.getSecondPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isMine(game, game.getSecondPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitMine(game, xPosition + i, yPosition + j, game.getSecondPlayerBoard(), rivalName, username);
                        } else if (!isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            game.getSecondPlayerBoard().putBombInSea(xPosition + i, yPosition + j);
                        }
                    } else {
                        if (isDamaged(game.getFirstPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitShip(game, game.getFirstPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isDamaged(game.getFirstPlayerBoard(), xPosition + i, yPosition + j) &&
                                isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            DestroyShip(game, game.getFirstPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isMine(game, game.getFirstPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitMine(game, xPosition + i, yPosition + j, game.getFirstPlayerBoard(), rivalName, username);
                        } else if (!isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            game.getFirstPlayerBoard().putBombInSea(xPosition + i, yPosition + j);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 2; j++) {
                    if (game.getFirstPlayer().getPlayerName().equals(username)) {
                        if (isDamaged(game.getSecondPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitShip(game, game.getSecondPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isDamaged(game.getSecondPlayerBoard(), xPosition + i, yPosition + j) &&
                                isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            DestroyShip(game, game.getSecondPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isMine(game, game.getSecondPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitMine(game, xPosition + i, yPosition + j, game.getSecondPlayerBoard(), rivalName, username);
                        } else if (!isDestroyed(game.getSecondPlayerBoard(), xPosition + i, yPosition + j)) {
                            game.getSecondPlayerBoard().putBombInSea(xPosition + i, yPosition + j);
                        }
                    } else {
                        if (isDamaged(game.getFirstPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitShip(game, game.getFirstPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isDamaged(game.getFirstPlayerBoard(), xPosition + i, yPosition + j) &&
                                isShipDestroyed(game, rivalName, xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            DestroyShip(game, game.getFirstPlayerBoard(), xPosition + i, yPosition + j, rivalName);
                            destroyerCounter += 1;
                        } else if (isMine(game, game.getFirstPlayerBoard(), xPosition + i, yPosition + j) &&
                                !isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            hitMine(game, xPosition + i, yPosition + j, game.getFirstPlayerBoard(), rivalName, username);
                        } else if (!isDestroyed(game.getFirstPlayerBoard(), xPosition + i, yPosition + j)) {
                            game.getFirstPlayerBoard().putBombInSea(xPosition + i, yPosition + j);
                        }
                    }
                }
            }
        }
        if (destroyerCounter == 0)
            System.out.println("target not found");
        else
            System.out.println(destroyerCounter + " pieces of rival's ships was damaged");
    }


    private static boolean isAirplaneOutOfBounds(int xPosition, int yPosition, boolean vertical) {
        if (vertical)
            return xPosition > 9 || yPosition > 6;
        else
            return yPosition > 9 || xPosition > 6;
    }

    private static void bomb(Game game, String username, String rivalName, String command) {
        if (!Pattern.matches("^bomb ([1-9]|10),([1-9]|10)$", command))
            System.out.println("wrong coordination");
        else {
            Matcher bombMatcher = Pattern.compile("^bomb ([1-9]|10),([1-9]|10)$").matcher(command);
            bombMatcher.find();
            int xPosition = Integer.parseInt(bombMatcher.group(1));
            int yPosition = Integer.parseInt(bombMatcher.group(2));
            if (game.getFirstPlayer().getPlayerName().equals(username)) {
                if (isDestroyed(game.getSecondPlayerBoard(), xPosition, yPosition))
                    System.out.println("this place has already destroyed");
                else if (isDamaged(game.getSecondPlayerBoard(), xPosition, yPosition) && !isShipDestroyed(game, rivalName, xPosition, yPosition)) {
                    System.out.println("the rival's ship was damaged");
                    hitShip(game, game.getSecondPlayerBoard(), xPosition, yPosition, rivalName);
                } else if (isDamaged(game.getSecondPlayerBoard(), xPosition, yPosition) && isShipDestroyed(game, rivalName, xPosition, yPosition)) {
                    System.out.println("the rival's ship" + DestroyShip(game, game.getSecondPlayerBoard(), xPosition, yPosition, rivalName) + " was destroyed");
                } else if (isMine(game, game.getSecondPlayerBoard(), xPosition, yPosition)) {
                    System.out.println("you destroyed the rival's mine");
                    System.out.println("turn completed");
                    hitMine(game, xPosition, yPosition, game.getSecondPlayerBoard(), rivalName, username);
                    gameMenu(game, rivalName, username);
                } else {
                    System.out.println("the bomb fell into sea");
                    System.out.println("turn completed");
                    game.getSecondPlayerBoard().putBombInSea(xPosition, yPosition);
                    gameMenu(game, rivalName, username);
                }
            } else {
                if (isDestroyed(game.getFirstPlayerBoard(), xPosition, yPosition))
                    System.out.println("this place has already destroyed");
                else if (isDamaged(game.getFirstPlayerBoard(), xPosition, yPosition) && !isShipDestroyed(game, rivalName, xPosition, yPosition)) {
                    System.out.println("the rival's ship was damaged");
                    hitShip(game, game.getFirstPlayerBoard(), xPosition, yPosition, rivalName);
                } else if (isDamaged(game.getFirstPlayerBoard(), xPosition, yPosition) && isShipDestroyed(game, rivalName, xPosition, yPosition)) {
                    System.out.println("the rival's ship" + DestroyShip(game, game.getFirstPlayerBoard(), xPosition, yPosition, rivalName) + " was destroyed");
                } else if (isMine(game, game.getFirstPlayerBoard(), xPosition, yPosition)) {
                    System.out.println("you destroyed the rival's mine");
                    System.out.println("turn completed");
                    hitMine(game, xPosition, yPosition, game.getFirstPlayerBoard(), rivalName, username);
                    gameMenu(game, rivalName, username);
                } else {
                    System.out.println("the bomb fell into sea");
                    System.out.println("turn completed");
                    game.getFirstPlayerBoard().putBombInSea(xPosition, yPosition);
                    gameMenu(game, rivalName, username);
                }
            }
        }
    }

    private static void hitMine(Game game, int xPosition, int yPosition, Board enemyBoard, String username, String rivalName) {
        enemyBoard.destroyMine(xPosition, yPosition);
        if (game.getFirstPlayer().getPlayerName().equals(username)) {
            if (isDamaged(game.getSecondPlayerBoard(), xPosition, yPosition) &&
                    !isShipDestroyed(game, rivalName, xPosition, yPosition) &&
                    !isDestroyed(game.getSecondPlayerBoard(), xPosition, yPosition)) {
                hitShip(game, game.getSecondPlayerBoard(), xPosition, yPosition, rivalName);
            } else if (isDamaged(game.getSecondPlayerBoard(), xPosition, yPosition) &&
                    isShipDestroyed(game, rivalName, xPosition, yPosition) &&
                    !isDestroyed(game.getSecondPlayerBoard(), xPosition, yPosition)) {
                DestroyShip(game, game.getSecondPlayerBoard(), xPosition, yPosition, rivalName);
            } else if (isMine(game, game.getSecondPlayerBoard(), xPosition, yPosition) &&
                    !isDestroyed(game.getSecondPlayerBoard(), xPosition, yPosition)) {
                hitMine(game, xPosition, yPosition, game.getSecondPlayerBoard(), rivalName, username);
            } else if (!isDestroyed(game.getSecondPlayerBoard(), xPosition, yPosition)) {
                game.getSecondPlayerBoard().putBombInSea(xPosition, yPosition);
            }
        } else {
            if (isDamaged(game.getFirstPlayerBoard(), xPosition, yPosition) &&
                    !isShipDestroyed(game, rivalName, xPosition, yPosition) &&
                    !isDestroyed(game.getFirstPlayerBoard(), xPosition, yPosition)) {
                hitShip(game, game.getFirstPlayerBoard(), xPosition, yPosition, rivalName);
            } else if (isDamaged(game.getFirstPlayerBoard(), xPosition, yPosition) &&
                    isShipDestroyed(game, rivalName, xPosition, yPosition) &&
                    !isDestroyed(game.getFirstPlayerBoard(), xPosition, yPosition)) {
                DestroyShip(game, game.getFirstPlayerBoard(), xPosition, yPosition, rivalName);
            } else if (isMine(game, game.getFirstPlayerBoard(), xPosition, yPosition) &&
                    !isDestroyed(game.getFirstPlayerBoard(), xPosition, yPosition)) {
                hitMine(game, xPosition, yPosition, game.getFirstPlayerBoard(), rivalName, username);
            } else if (!isDestroyed(game.getFirstPlayerBoard(), xPosition, yPosition)) {
                game.getFirstPlayerBoard().putBombInSea(xPosition, yPosition);
            }
        }
    }

    private static int DestroyShip(Game game, Board board, int xPosition, int yPosition, String rivalName) {
        game.getShipByPosition(rivalName, xPosition, yPosition).destroyShipParts(xPosition, yPosition);
        board.addDestroyedShips(game.getShipByPosition(rivalName, xPosition, yPosition));
        board.destroyShipPart(xPosition, yPosition, game.getShipByPosition(rivalName, xPosition, yPosition));
        return game.getShipByPosition(rivalName, xPosition, yPosition).getShipSize();
    }

    private static void hitShip(Game game, Board board, int xPosition, int yPosition, String rivalName) {
        game.getShipByPosition(rivalName, xPosition, yPosition).destroyShipParts(xPosition, yPosition);
        board.destroyShipPart(xPosition, yPosition, game.getShipByPosition(rivalName, xPosition, yPosition));
    }

    private static boolean isMine(Game game, Board board, int xPosition, int yPosition) {
        return board.getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'M';
    }

    private static boolean isShipDestroyed(Game game, String rivalName, int xPosition, int yPosition) {
        return game.getShipByPosition(rivalName, xPosition, yPosition).willBeDestroyed();
    }

    private static boolean isDamaged(Board board, int xPosition, int yPosition) {
        return board.getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'I' || board.getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'S';
    }

    private static boolean isDestroyed(Board board, int xPosition, int yPosition) {
        return board.getBoard()[yPosition - 1][xPosition - 1].charAt(1) == 'X' || board.getBoard()[yPosition - 1][xPosition - 1].charAt(0) == 'D';
    }

    private static void scanTheBoard(Game game, String username, String command) {
        if (!Pattern.matches("^scanner ([1-9]|10),([1-9]|10)$", command))
            System.out.println("wrong coordination");
        else if (!Pattern.matches("^scanner ([1-8]),([1-8])$", command))
            System.out.println("off the board");
        else {
            Matcher scanMatcher = Pattern.compile("^scanner ([1-8]),([1-8])$").matcher(command);
            scanMatcher.find();
            int xPosition = Integer.parseInt(scanMatcher.group(1));
            int yPosition = Integer.parseInt(scanMatcher.group(2));
            if (game.getFirstPlayer().getPlayerName().equals(username)) {
                if (game.getFirstPlayer().getNumberOfScanner() == 0)
                    System.out.println("you don't have scanner");
                else {
                    showScannedBoard(xPosition, yPosition, game.getSecondPlayerBoard());
                    game.getFirstPlayer().setNumberOfScanner(game.getFirstPlayer().getNumberOfScanner() - 1);
                }
            } else {
                if (game.getSecondPlayer().getNumberOfScanner() == 0)
                    System.out.println("you don't have scanner");
                else {
                    showScannedBoard(xPosition, yPosition, game.getFirstPlayerBoard());
                    game.getSecondPlayer().setNumberOfScanner(game.getSecondPlayer().getNumberOfScanner() - 1);
                }
            }
        }
    }

    private static void showScannedBoard(int xPosition, int yPosition, Board enemyBoard) {
        StringBuilder scannerRow = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            scannerRow.append("|");
            for (int j = 0; j < 3; j++) {
                if (enemyBoard.getBoard()[yPosition - 1 + i][xPosition - 1 + j].charAt(0) == 'S')
                    scannerRow.append("SX").append("|");
                else
                    scannerRow.append("  ").append("|");
            }
            System.out.println(scannerRow);
            scannerRow.delete(0, scannerRow.length());
        }
    }


    private static void showEnemyBoard(Game game, String rivalName) {
        if (game.getFirstPlayer().getPlayerName().equals(rivalName))
            game.getFirstPlayerBoard().showAsEnemyBoard();
        else
            game.getSecondPlayerBoard().showAsEnemyBoard();
    }

    private static void showMyBoard(Game game, String username) {
        if (game.getFirstPlayer().getPlayerName().equals(username))
            game.getFirstPlayerBoard().showBoard();
        else
            game.getSecondPlayerBoard().showBoard();
    }

    private static void forfeit(Game game, String username, String rivalName) {
        System.out.println(username + " is forfeited\n" +
                rivalName + " is winner");
        game.setFinished(true);
        game.countScores();
        if (game.getFirstPlayer().getPlayerName().equals(username)) {
            game.getFirstPlayer().increaseBalance(-50);
            game.getSecondPlayer().increaseBalance(game.getSecondPlayerScore());
            game.getFirstPlayer().increaseScore(-1);
            game.getSecondPlayer().increaseScore(2);
            game.getFirstPlayer().increaseLooses(1);
            game.getSecondPlayer().increaseWins(1);
        } else {
            game.getSecondPlayer().increaseBalance(-50);
            game.getFirstPlayer().increaseBalance(game.getFirstPlayerScore());
            game.getSecondPlayer().increaseLooses(1);
            game.getFirstPlayer().increaseWins(1);
            game.getFirstPlayer().increaseScore(2);
            game.getSecondPlayer().increaseScore(-1);
        }
    }
}
