package Models;

import java.util.ArrayList;

public class Player {
    private String playerName;
    private String playerPassword;
    private int playerBalance;
    private static ArrayList<Player> players = new ArrayList<>();
    private int wins;
    private int draws;
    private int looses;
    private int score;
    private int numberOfMines;
    private int numberOfAntiaircraft;
    private int numberOfAirplane;
    private int numberOfScanner;
    private int numberOfInvisible;

    public Player(String playerName, String playerPassword) {
        this.playerName = playerName;
        this.playerPassword = playerPassword;
        this.playerBalance = 50;
        this.wins = 0;
        this.looses = 0;
        this.draws = 0;
        this.score = 0;
        this.numberOfAirplane = 0;
        this.numberOfAntiaircraft = 0;
        this.numberOfInvisible = 0;
        this.numberOfMines = 0;
        this.numberOfScanner = 0;

        players.add(this);
    }

    public void setPlayerBalance(int playerBalance) {
        this.playerBalance = playerBalance;
    }

    public int getPlayerBalance() {
        return playerBalance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerPassword() {
        return playerPassword;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static Player getPlayerByName(String playerName) {
        for (Player player : players) {
            if (player.playerName.equals(playerName))
                return player;
        }
        return null;
    }

    public void increaseWins(int wins) {
        this.wins += wins;
    }

    public int getWins() {
        return wins;
    }

    public void increaseDraws(int draws) {
        this.draws += draws;
    }

    public int getDraws() {
        return draws;
    }

    public void increaseLooses(int looses) {
        this.looses += looses;
    }

    public int getLooses() {
        return looses;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void buyElementFromShop(String productName, int amount) {
        switch (productName) {
            case "airplane":
                this.numberOfAirplane += amount;
                break;
            case "antiaircraft":
                this.numberOfAntiaircraft += amount;
                break;
            case "invisible":
                this.numberOfInvisible += amount;
                break;
            case "mine":
                this.numberOfMines += amount;
                break;
            case "scanner":
                this.numberOfScanner += amount;
                break;
        }
    }

    public void increaseBalance(int amount) {
        this.playerBalance += amount;
    }

    public void setNumberOfAirplane(int numberOfAirplane) {
        this.numberOfAirplane = numberOfAirplane;
    }

    public void setNumberOfAntiaircraft(int numberOfAntiaircraft) {
        this.numberOfAntiaircraft = numberOfAntiaircraft;
    }

    public void setNumberOfInvisible(int numberOfInvisible) {
        this.numberOfInvisible = numberOfInvisible;
    }

    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }

    public void setNumberOfScanner(int numberOfScanner) {
        this.numberOfScanner = numberOfScanner;
    }

    public int getNumberOfAirplane() {
        return numberOfAirplane;
    }

    public int getNumberOfAntiaircraft() {
        return numberOfAntiaircraft;
    }

    public int getNumberOfInvisible() {
        return numberOfInvisible;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public int getNumberOfScanner() {
        return numberOfScanner;
    }

    public static void removePlayer(String playerName) {
        players.remove(Player.getPlayerByName(playerName));
    }
}
