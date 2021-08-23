package Models.ObjectsOnTheBoard;

import java.util.ArrayList;

public class Ship {
    private int shipSize;
    private boolean vertical;
    private int numberOfRemainingParts;
    private ArrayList<int[]> shipRemainingPositions;
    private ArrayList<int[]> shipDestroyedPositions;

    public Ship(int shipSize, int xPosition, int yPosition, boolean vertical) {
        this.shipSize = shipSize;
        this.numberOfRemainingParts = shipSize;
        this.vertical = vertical;
        this.shipDestroyedPositions = new ArrayList<>();
        this.shipRemainingPositions = new ArrayList<>();

        if (vertical) {
            for (int i = 0; i < shipSize; i++) {
                shipRemainingPositions.add(new int[]{xPosition, yPosition + i});
            }
        } else {
            for (int i = 0; i < shipSize; i++) {
                shipRemainingPositions.add(new int[]{xPosition + i, yPosition});
            }
        }
    }

    public boolean isVertical() {
        return vertical;
    }

    public int getShipSize() {
        return shipSize;
    }

    public int getNumberOfRemainingParts() {
        return numberOfRemainingParts;
    }

    public ArrayList<int[]> getShipDestroyedPositions() {
        return shipDestroyedPositions;
    }

    public ArrayList<int[]> getShipRemainingPositions() {
        return shipRemainingPositions;
    }

    public void destroyShipParts(int xPosition, int yPosition) {
        ArrayList<int[]> items = new ArrayList<>(this.shipRemainingPositions);
        for (int[] position : items) {
            if (position[0] == xPosition && position[1] == yPosition) {
                this.shipRemainingPositions.remove(position);
                this.shipDestroyedPositions.add(position);
            }
        }
    }

    public boolean willBeDestroyed() {
        return this.getShipDestroyedPositions().size() == this.getShipSize() - 1;
    }

}
