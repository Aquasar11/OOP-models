package Models;

import Models.ObjectsOnTheBoard.AA;
import Models.ObjectsOnTheBoard.Ship;

import java.util.ArrayList;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private Board firstPlayerBoard;
    private Board secondPlayerBoard;
    private int firstPlayerScore;
    private int secondPlayerScore;
    private int remainingShipForFirstPlayer;
    private int remainingShipForSecondPlay;
    private int numberOfS1Ship;
    private int numberOfS2Ship;
    private int numberOfS3Ship;
    private int numberOfS4Ship;
    private ArrayList<AA> firstPlayerAAs;
    private ArrayList<AA> secondPlayerAAs;
    private ArrayList<Ship> firstPlayerShips;
    private ArrayList<Ship> secondPlayerShips;
    private boolean finished;

    public Game(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayerBoard = new Board();
        this.secondPlayerBoard = new Board();
        this.firstPlayerScore = 0;
        this.secondPlayerScore = 0;
        this.remainingShipForSecondPlay = 10;
        this.remainingShipForFirstPlayer = 10;
        this.numberOfS1Ship = 8;
        this.numberOfS2Ship = 6;
        this.numberOfS3Ship = 4;
        this.numberOfS4Ship = 2;
        this.firstPlayerAAs = new ArrayList<>();
        this.secondPlayerAAs = new ArrayList<>();
        this.firstPlayerShips = new ArrayList<>();
        this.secondPlayerShips = new ArrayList<>();
        this.setNumberOfS1Ship(8);
        this.setNumberOfS2Ship(6);
        this.setNumberOfS3Ship(4);
        this.setNumberOfS4Ship(2);
        this.finished = false;
    }


    public int getNumberOfS1Ship() {
        return numberOfS1Ship;
    }

    public int getNumberOfS2Ship() {
        return numberOfS2Ship;
    }

    public int getNumberOfS3Ship() {
        return numberOfS3Ship;
    }

    public int getNumberOfS4Ship() {
        return numberOfS4Ship;
    }

    public void setNumberOfS1Ship(int numberOfS1Ship) {
        this.numberOfS1Ship = numberOfS1Ship;
    }

    public void setNumberOfS2Ship(int numberOfS2Ship) {
        this.numberOfS2Ship = numberOfS2Ship;
    }

    public void setNumberOfS3Ship(int numberOfS3Ship) {
        this.numberOfS3Ship = numberOfS3Ship;
    }

    public void setNumberOfS4Ship(int numberOfS4Ship) {
        this.numberOfS4Ship = numberOfS4Ship;
    }

    public Board getFirstPlayerBoard() {
        return firstPlayerBoard;
    }

    public Board getSecondPlayerBoard() {
        return secondPlayerBoard;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public int getFirstPlayerScore() {
        return firstPlayerScore;
    }

    public int getSecondPlayerScore() {
        return secondPlayerScore;
    }

    public int getRemainingShipForFirstPlayer() {
        return remainingShipForFirstPlayer;
    }

    public int getRemainingShipForSecondPlay() {
        return remainingShipForSecondPlay;
    }

    public void setRemainingShipForFirstPlayer(int remainingShipForFirstPlayer) {
        this.remainingShipForFirstPlayer = remainingShipForFirstPlayer;
    }

    public void setRemainingShipForSecondPlay(int remainingShipForSecondPlay) {
        this.remainingShipForSecondPlay = remainingShipForSecondPlay;
    }

    public int numberOfRemainingShipByModel(int shipSize) {
        if (shipSize == 1)
            return getNumberOfS1Ship();
        else if (shipSize == 2)
            return getNumberOfS2Ship();
        else if (shipSize == 3)
            return getNumberOfS3Ship();
        else if (shipSize == 4)
            return getNumberOfS4Ship();
        return -1;
    }

    public void decreaseNumberOfShipBySize(int shipSize) {
        if (shipSize == 1)
            setNumberOfS1Ship(getNumberOfS1Ship() - 1);
        else if (shipSize == 2)
            setNumberOfS2Ship(getNumberOfS2Ship() - 1);
        else if (shipSize == 3)
            setNumberOfS3Ship(getNumberOfS3Ship() - 1);
        else if (shipSize == 4)
            setNumberOfS4Ship(getNumberOfS4Ship() - 1);
    }

    public ArrayList<AA> getFirstPlayerAAs() {
        return firstPlayerAAs;
    }

    public ArrayList<AA> getSecondPlayerAAs() {
        return secondPlayerAAs;
    }

    public ArrayList<Ship> getFirstPlayerShips() {
        return firstPlayerShips;
    }

    public ArrayList<Ship> getSecondPlayerShips() {
        return secondPlayerShips;
    }

    public void setSecondPlayerAAs(ArrayList<AA> secondPlayerAAs) {
        this.secondPlayerAAs = secondPlayerAAs;
    }

    public void setFirstPlayerShips(ArrayList<Ship> firstPlayerShips) {
        this.firstPlayerShips = firstPlayerShips;
    }

    public void setSecondPlayerShips(ArrayList<Ship> secondPlayerShips) {
        this.secondPlayerShips = secondPlayerShips;
    }

    public void setFirstPlayerAAs(ArrayList<AA> firstPlayerAAs) {
        this.firstPlayerAAs = firstPlayerAAs;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Ship getShipByPosition(String player, int xPosition, int yPosition) {
        if (player.equals(firstPlayer.getPlayerName())) {
            for (Ship ship : firstPlayerShips) {
                for (int[] positions : ship.getShipRemainingPositions()) {
                    if (positions[0] == xPosition && positions[1] == yPosition)
                        return ship;
                }
                for (int[] positions : ship.getShipDestroyedPositions()) {
                    if (positions[0] == xPosition && positions[1] == yPosition)
                        return ship;
                }
            }
            return null;
        } else {
            for (Ship ship : secondPlayerShips) {
                for (int[] positions : ship.getShipRemainingPositions()) {
                    if (positions[0] == xPosition && positions[1] == yPosition)
                        return ship;
                }
                for (int[] positions : ship.getShipDestroyedPositions()) {
                    if (positions[0] == xPosition && positions[1] == yPosition)
                        return ship;
                }
            }
            return null;
        }
    }


    public void setSecondPlayerScore(int secondPlayerScore) {
        this.secondPlayerScore = secondPlayerScore;
    }

    public void setFirstPlayerScore(int firstPlayerScore) {
        this.firstPlayerScore = firstPlayerScore;
    }

    public void removeAA(String username, AA aa) {
        if (getFirstPlayer().getPlayerName().equals(username)) {
            firstPlayerAAs.remove(aa);
        } else {
            secondPlayerAAs.remove(aa);
        }
    }

    public void countScores() {
        int firstPlayerScoreCounter = 0;
        int secondPlayerScoreCounter = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (firstPlayerBoard.getBoard()[i][j].charAt(0) == 'D')
                    secondPlayerScoreCounter += 1;
                else if (firstPlayerBoard.getBoard()[i][j].equals("MX"))
                    secondPlayerScoreCounter -= 1;
                if (secondPlayerBoard.getBoard()[i][j].charAt(0) == 'D')
                    firstPlayerScoreCounter += 1;
                else if (secondPlayerBoard.getBoard()[i][j].equals("MX"))
                    firstPlayerScoreCounter -= 1;
            }
        }
        this.setFirstPlayerScore(this.getFirstPlayerScore() + firstPlayerScoreCounter);
        this.setSecondPlayerScore(this.getSecondPlayerScore() + secondPlayerScoreCounter);
    }
}
