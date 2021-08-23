package Models.ObjectsOnTheBoard;

import java.util.ArrayList;

public class AA {
    private int idOnTheBoard;
    private ArrayList<int[]> positions;
    private int firstPosition;
    private boolean vertical;

    public AA(int idOnTheBoard, int firstPosition, boolean vertical) {
        this.idOnTheBoard = idOnTheBoard;
        this.vertical = vertical;
        this.positions = new ArrayList<>();
        this.firstPosition = firstPosition;

        if (!vertical) {
            for (int j = firstPosition; j < firstPosition + 3; j++) {
                for (int i = 1; i <= 10; i++) {
                    this.positions.add(new int[]{i, j});
                }
            }
        } else {
            for (int i = firstPosition; i < firstPosition + 3; i++) {
                for (int j = 1; j <= 10; j++) {
                    this.positions.add(new int[]{i, j});
                }
            }
        }
    }

    public int getFirstPosition() {
        return firstPosition;
    }

    public int getIdOnTheBoard() {
        return idOnTheBoard;
    }

    public ArrayList<int[]> getPositions() {
        return positions;
    }

    public boolean isVertical() {
        return vertical;
    }

}
