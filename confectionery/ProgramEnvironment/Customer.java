package ProgramEnvironment;

import java.util.ArrayList;

public class Customer {
    private String name;
    private int id;
    private int balance;
    private int discountCode;
    private static ArrayList<Customer> customers = new ArrayList<>();

    public Customer(String name, int id) {
        this.name = name;
        this.id = id;
        this.balance = 0;
        this.discountCode = -1;

        customers.add(this);
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }

    public void setDiscountCode(int discountCode) {
        this.discountCode = discountCode;
    }

    public int getDiscountCode() {
        return discountCode;
    }

    public static Customer getCustomerById(int id) {
        for (Customer key : customers) {
            if (key.id == id)
                return key;
        }
        return null;
    }

    public void increaseCustomerBalance(int balance) {
        this.balance += balance;
    }

    public void decreaseCustomerBalance(int balance) {
        this.balance -= balance;
    }
}
