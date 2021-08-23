package ProgramEnvironment;

import java.util.ArrayList;

public class Warehouse {
    private int amount;
    private String materialName;
    private static ArrayList<Warehouse> warehouses = new ArrayList<>();

    public Warehouse(String materialName, int amount) {
        this.materialName = materialName;
        setAmount(amount);
        warehouses.add(this);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void increaseMaterial(int amount) {
        this.amount += amount;
    }
    public void decreaseMaterial(int amount) {
        this.amount -= amount;
    }

    public static Warehouse getWarehouseByName(String name){
        for (Warehouse key: warehouses) {
            if (key.materialName.equals(name))
                return key;
        }
        return null;
    }
}
