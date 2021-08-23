package ProgramEnvironment;

import java.util.ArrayList;

public class Transaction {
    private static int idCounter = 1;
    private int id;
    private int customerId;
    private int amount;
    private int discountCode;
    private int discountPrice;
    private int finalPayment;
    private boolean isAccepted;
    private static ArrayList<Transaction> transactions = new ArrayList<>();

    public Transaction(int customerId, int amount, int discountCode) {
        this.customerId = customerId;
        this.amount = amount;
        this.discountCode = discountCode;
        this.id = idCounter;
        idCounter += 1;
        this.discountPrice = Confectionary.getDiscountPriceByCode(discountCode);
        if (discountPrice == -1)
            this.finalPayment = amount;
        else {
            if (amount > discountPrice)
                this.finalPayment = amount - discountPrice;
            else
                this.finalPayment = 0;
        }
        isAccepted = false;

        transactions.add(this);
    }

    public int getId() {
        return id;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public int getDiscountCode() {
        return discountCode;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getAmount() {
        return amount;
    }

    public int getFinalPayment() {
        return finalPayment;
    }

    public boolean isTransactionAccepted() {
        return isAccepted;
    }

    public void exchangeMoney() {
        ProgramController.confectionary.increaseBalance(finalPayment);
        Customer.getCustomerById(customerId).decreaseCustomerBalance(finalPayment);
    }

    public static Transaction getTransactionById(int id) {
        for (Transaction key : transactions) {
            if (key.id == id)
                return key;
        }
        return null;
    }

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
