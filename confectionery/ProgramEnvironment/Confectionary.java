package ProgramEnvironment;

import java.util.HashMap;

public class Confectionary {
    private int balance;
    private static HashMap<Integer, Integer> discounts = new HashMap<>();

    public Confectionary(){
        this.setBalance(0);
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void increaseBalance(int balance) {
        this.balance += balance;
    }

    public static void addDiscount(int code, int price) {
        if (discounts.containsKey(code))
            System.out.println("discount with this code already exists");
        else
            discounts.put(code, price);
    }

    public static boolean isDiscountExist(int code) {
        return discounts.containsKey(code);
    }

    public static int getDiscountPriceByCode(int code) {
        if (discounts.containsKey(code))
            return discounts.get(code);
        return -1;
    }
}
