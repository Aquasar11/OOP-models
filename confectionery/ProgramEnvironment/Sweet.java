package ProgramEnvironment;

import java.util.ArrayList;
import java.util.HashMap;

public class Sweet {
    private String name;
    private int price;
    private int amount;
    private HashMap<String,Integer> materials;
    private static ArrayList<Sweet> sweets = new ArrayList<>();

    public Sweet (String name,int price, HashMap<String,Integer> materials){
        this.name = name;
        this.price = price;
        this.materials = materials;

        sweets.add(this);
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Integer> getMaterials() {
        return materials;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increaseSweet(int amount){
        this.amount += amount;
    }

    public void decreaseSweet(int amount){
        this.amount -= amount;
    }

    public void decreaseMaterialOfSweetFromWarehouse(int amount){
        int totalAmount;
        for (String materialName: materials.keySet()) {
            totalAmount = amount * materials.get(materialName);
            Warehouse.getWarehouseByName(materialName).decreaseMaterial(totalAmount);
        }
    }

    public static Sweet getSweetByName(String name){
        for (Sweet key: sweets) {
            if (key.name.equals(name))
                return key;
        }
        return null;
    }
}
