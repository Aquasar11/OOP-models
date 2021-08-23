package Models;

import Models.ObjectsOnTheBoard.AA;
import Models.ObjectsOnTheBoard.Ship;

import java.util.ArrayList;

public class Board {
    private String[][] board;
    private ArrayList<Ship> destroyedShips;

    public Board() {
        this.board = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = String.valueOf("  ");
            }
        }
        this.destroyedShips = new ArrayList<>();
    }

    public void putShipOnBoard(int xPosition, int yPosition, int shipSize, boolean vertical) {
        if (vertical) {
            for (int i = 0; i < shipSize; i++) {
                board[yPosition - 1 + i][xPosition - 1] = String.valueOf("S" + shipSize);
            }
        } else {
            for (int i = 0; i < shipSize; i++) {
                board[yPosition - 1][xPosition - 1 + i] = String.valueOf("S" + shipSize);
            }
        }
    }

    public void addDestroyedShips(Ship ship) {
        this.destroyedShips.add(ship);
    }

    public void putMineOnBoard(int xPosition, int yPosition) {
        board[yPosition - 1][xPosition - 1] = String.valueOf("Mm");
    }

    public void putAAOnBoard(int position, boolean vertical) {
        if (vertical) {
            for (int j = position - 1; j < position + 2; j++) {
                for (int i = 0; i < 10; i++) {
                    if (board[i][j].equals("  "))
                        board[i][j] = String.valueOf("AA");
                }
            }
        } else {
            for (int i = position - 1; i < position + 2; i++) {
                for (int j = 0; j < 10; j++) {
                    if (board[i][j].equals("  "))
                        board[i][j] = String.valueOf("AA");
                }
            }
        }
    }

    public void destroyedAAOnBoard(ArrayList<AA> AALists) {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (board[i][j].equals("AA"))
                    board[i][j] = String.valueOf("  ");
            }
        }
        for (AA aa : AALists) {
            this.putAAOnBoard(aa.getFirstPosition(), aa.isVertical());
        }
    }

    public void makeShipInvisible(int xPosition, int yPosition) {
        board[yPosition - 1][xPosition - 1] = String.valueOf("I" + board[yPosition - 1][xPosition - 1].substring(1));
    }

    public String[][] getBoard() {
        return board;
    }

    public void showBoard() {
        StringBuilder boardRow = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            boardRow.append("|");
            for (int j = 0; j < 10; j++) {
                boardRow.append(board[i][j]).append("|");
            }
            System.out.println(boardRow);
            boardRow.delete(0, boardRow.length());
        }
    }

    public void showAsEnemyBoard() {
        StringBuilder boardRow = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            boardRow.append("|");
            for (int j = 0; j < 10; j++) {
                if (board[i][j].equals("XX"))
                    boardRow.append(board[i][j]).append("|");
                else if (board[i][j].charAt(0) == 'D')
                    boardRow.append(findThePart(i, j)).append("|");
                else if (board[i][j].equals("MX"))
                    boardRow.append(board[i][j]).append("|");
                else
                    boardRow.append("  ").append("|");
            }
            System.out.println(boardRow);
            boardRow.delete(0, boardRow.length());
        }
    }

    private String findThePart(int i, int j) {
        for (Ship ship : this.destroyedShips) {
            for (int[] shipDestroyedPositions : ship.getShipDestroyedPositions()) {
                if (shipDestroyedPositions[0] == j + 1 && shipDestroyedPositions[1] == i + 1)
                    return "D" + ship.getShipSize();
            }
        }
        return "DX";
    }

    public void putBombInSea(int xPosition, int yPosition) {
        board[yPosition - 1][xPosition - 1] = String.valueOf("XX");
    }

    public void destroyMine(int xPosition, int yPosition) {
        board[yPosition - 1][xPosition - 1] = String.valueOf("MX");
    }

    public void destroyShipPart(int xPosition, int yPosition, Ship ship) {
        board[yPosition - 1][xPosition - 1] = String.valueOf("D" + ship.getShipSize());
    }
}
