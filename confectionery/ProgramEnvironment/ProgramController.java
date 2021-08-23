package ProgramEnvironment;


import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramController {
    public static Confectionary confectionary;
    private static final Scanner scanner = new Scanner(System.in);

    public static void run() {
        String firstCommand = scanner.nextLine();

        while (!firstCommand.trim().equals("create confectionary")) {
            if (firstCommand.trim().equals("end"))
                return;
            System.out.println("invalid command");
            firstCommand = scanner.nextLine();
        }

        confectionary = new Confectionary();
        runTheConfectionary();
    }

    private static void runTheConfectionary() {
        String command = scanner.nextLine();
        while (!command.trim().equals("end")) {
            if (Pattern.matches("^add customer id [0-9]+ name [A-Za-z ]+$", command.trim()))
                addCustomer(getCommandMatcher(command.trim(), "^add customer id ([0-9]+) name ([A-Za-z ]+)$"));
            else if (Pattern.matches("^increase balance customer [0-9]+ amount [0-9]+$", command.trim()))
                chargeCustomerBalance(getCommandMatcher(command.trim(), "^increase balance customer ([0-9]+) amount ([0-9]+)$"));
            else if (Pattern.matches("^add warehouse material ([A-Za-z ]+) amount ([0-9]+)$", command.trim()))
                addWarehouse(getCommandMatcher(command.trim(), "^add warehouse material ([A-Za-z ]+) amount ([0-9]+)$"));
            else if (Pattern.matches("^increase warehouse material [A-Za-z ]+ amount [0-9]+$", command.trim()))
                increaseWarehouseMaterial(getCommandMatcher(command.trim(), "^increase warehouse material ([A-Za-z ]+) amount ([0-9]+)$"));
            else if (Pattern.matches("^add sweet name ([A-Za-z ]+) price ([0-9]+) materials: ((?:[A-Za-z ]+ [0-9,]+)+)$", command.trim()))
                addSweet(getCommandMatcher(command.trim(), "^add sweet name ([A-Za-z ]+) price ([0-9]+) materials: ((?:[A-Za-z ]+ [0-9,]+)+)$"));
            else if (Pattern.matches("^increase sweet ([A-Za-z ]+) amount ([0-9]+)$", command.trim()))
                increaseSweet(getCommandMatcher(command.trim(), "^increase sweet ([A-Za-z ]+) amount ([0-9]+)$"));
            else if (Pattern.matches("^add discount code ([0-9]+) price ([0-9]+)$", command.trim()))
                addDiscount(getCommandMatcher(command.trim(), "^add discount code ([0-9]+) price ([0-9]+)$"));
            else if (Pattern.matches("^add discount code code ([0-9]+) to customer id ([0-9]+)$", command.trim()))
                addDiscountToCustomer(getCommandMatcher(command.trim(), "^add discount code code ([0-9]+) to customer id ([0-9]+)$"));
            else if (Pattern.matches("^sell sweet ([A-Za-z ]+) amount ([0-9]+) to customer ([0-9]+)$", command.trim()))
                sellSweet(getCommandMatcher(command.trim(), "^sell sweet ([A-Za-z ]+) amount ([0-9]+) to customer ([0-9]+)$"));
            else if (Pattern.matches("^accept transaction ([0-9]+)$", command.trim()))
                acceptTransaction(getCommandMatcher(command.trim(), "^accept transaction ([0-9]+)$"));
            else if (Pattern.matches("print transactions list", command.trim()))
                printTransactions();
            else if (Pattern.matches("print income", command.trim()))
                printIncome();
            else
                System.out.println("invalid command");
            command = scanner.nextLine();
        }
    }

    private static void addCustomer(Matcher matcher) {
        matcher.find();
        int customerId = Integer.parseInt(matcher.group(1));
        String customerName = matcher.group(2);

        if (Customer.getCustomerById(customerId) != null) {
            System.out.println("customer with this id already exists");
        } else {
            new Customer(customerName, customerId);
        }
    }

    private static void chargeCustomerBalance(Matcher matcher) {
        matcher.find();
        int customerId = Integer.parseInt(matcher.group(1));
        int chargeAmount = Integer.parseInt(matcher.group(2));

        if (Customer.getCustomerById(customerId) == null) {
            System.out.println("customer not found");
        } else {
            Customer.getCustomerById(customerId).increaseCustomerBalance(chargeAmount);
        }
    }

    private static void addWarehouse(Matcher matcher) {
        matcher.find();
        String warehouseName = matcher.group(1);
        int warehouseAmount = Integer.parseInt(matcher.group(2));

        if (Warehouse.getWarehouseByName(warehouseName) != null) {
            System.out.println("warehouse having this material already exists");
        } else {
            new Warehouse(warehouseName, warehouseAmount);
        }
    }

    private static void increaseWarehouseMaterial(Matcher matcher) {
        matcher.find();
        String warehouseName = matcher.group(1);
        int warehouseIncreaseAmount = Integer.parseInt(matcher.group(2));

        if (Warehouse.getWarehouseByName(warehouseName) == null) {
            System.out.println("warehouse not found");
        } else {
            Warehouse.getWarehouseByName(warehouseName).increaseMaterial(warehouseIncreaseAmount);
        }
    }

    private static void addSweet(Matcher matcher) {
        matcher.find();
        String sweetName = matcher.group(1);
        int sweetPrice = Integer.parseInt(matcher.group(2));

        String[] rawMaterialAndAmount = matcher.group(3).split(", ");
        HashMap<String, Integer> sweetMaterial = new HashMap<>();
        StringBuilder insufficientMaterials = new StringBuilder();
        for (String materialAndAmount : rawMaterialAndAmount) {
            Matcher materialAndAmountGetter = Pattern.compile("([\\w ]+) ([\\d]+)").matcher(materialAndAmount);
            while (materialAndAmountGetter.find()) {
                sweetMaterial.put(materialAndAmountGetter.group(1), Integer.parseInt(materialAndAmountGetter.group(2)));
                if (Warehouse.getWarehouseByName(materialAndAmountGetter.group(1)) == null)
                    insufficientMaterials.append(" ").append(materialAndAmountGetter.group(1));
            }
        }
        if (sweetMaterialIsReady(sweetMaterial))
            new Sweet(sweetName, sweetPrice, sweetMaterial);
        else
            System.out.println("not found warehouse(s):" + insufficientMaterials);
    }

    private static boolean sweetMaterialIsReady(HashMap<String, Integer> sweetMaterial) {
        for (String materialName : sweetMaterial.keySet()) {
            if (Warehouse.getWarehouseByName(materialName) == null)
                return false;
        }
        return true;
    }


    private static void increaseSweet(Matcher matcher) {
        matcher.find();
        String sweetName = matcher.group(1);
        int sweetAmount = Integer.parseInt(matcher.group(2));
        if (Sweet.getSweetByName(sweetName) == null) {
            System.out.println("sweet not found");
            return;
        }
        if (warehousesMaterialsReady(sweetName, sweetAmount)) {
            Sweet.getSweetByName(sweetName).increaseSweet(sweetAmount);
            Sweet.getSweetByName(sweetName).decreaseMaterialOfSweetFromWarehouse(sweetAmount);
        } else
            printInsufficientWarehousesMaterials(sweetName, sweetAmount);
    }

    private static boolean warehousesMaterialsReady(String sweetName, int sweetAmount) {
        int totalAmount;
        for (String warehouseName : Sweet.getSweetByName(sweetName).getMaterials().keySet()) {
            totalAmount = sweetAmount * Sweet.getSweetByName(sweetName).getMaterials().get(warehouseName);
            if (totalAmount > Warehouse.getWarehouseByName(warehouseName).getAmount())
                return false;
        }
        return true;
    }

    private static void printInsufficientWarehousesMaterials(String sweetName, int sweetAmount) {
        StringBuilder insufficientMaterial = new StringBuilder();
        int totalAmount;
        for (String warehouseName : Sweet.getSweetByName(sweetName).getMaterials().keySet()) {
            totalAmount = sweetAmount * Sweet.getSweetByName(sweetName).getMaterials().get(warehouseName);
            if (totalAmount > Warehouse.getWarehouseByName(warehouseName).getAmount())
                insufficientMaterial.append(" ").append(warehouseName);
        }
        System.out.println("insufficient material(s):" + insufficientMaterial);
    }

    private static void addDiscount(Matcher matcher) {
        matcher.find();
        int discountCode = Integer.parseInt(matcher.group(1));
        int discountPrice = Integer.parseInt(matcher.group(2));

        if (Confectionary.isDiscountExist(discountCode)) {
            System.out.println("discount with this code already exists");
        } else {
            Confectionary.addDiscount(discountCode, discountPrice);
        }
    }

    private static void addDiscountToCustomer(Matcher matcher) {
        matcher.find();
        int discountCode = Integer.parseInt(matcher.group(1));
        int customerId = Integer.parseInt(matcher.group(2));
        if (Confectionary.isDiscountExist(discountCode) && Customer.getCustomerById(customerId) != null)
            Customer.getCustomerById(customerId).setDiscountCode(discountCode);
        else if (!Confectionary.isDiscountExist(discountCode))
            System.out.println("discount code not found");
        else if (Customer.getCustomerById(customerId) == null)
            System.out.println("customer not found");

    }

    private static void sellSweet(Matcher matcher) {
        matcher.find();
        String sweetName = matcher.group(1);
        int sweetAmount = Integer.parseInt(matcher.group(2));
        int customerId = Integer.parseInt(matcher.group(3));

        if (Sweet.getSweetByName(sweetName) == null) {
            System.out.println("sweet not found");
            return;
        }
        if (Sweet.getSweetByName(sweetName).getAmount() < sweetAmount) {
            System.out.println("insufficient sweet");
            return;
        }
        if (Customer.getCustomerById(customerId) == null) {
            System.out.println("customer not found");
            return;
        }

        int totalValue = sweetAmount * Sweet.getSweetByName(sweetName).getPrice();
        int customerDiscountCode = Customer.getCustomerById(customerId).getDiscountCode();
        int finalValue;
        if (Customer.getCustomerById(customerId).getDiscountCode() != -1 &&
                Confectionary.getDiscountPriceByCode(customerDiscountCode) != -1)
            finalValue = totalValue - Confectionary.getDiscountPriceByCode(customerDiscountCode);
        else
            finalValue = totalValue;

        if (finalValue > Customer.getCustomerById(customerId).getBalance()) {
            System.out.println("customer has not enough money");
            return;
        }

        Transaction currentTransaction = new Transaction(customerId, totalValue, Customer.getCustomerById(customerId).getDiscountCode());
        Sweet.getSweetByName(sweetName).decreaseSweet(sweetAmount);
        if (Customer.getCustomerById(customerId).getDiscountCode() != -1)
            Customer.getCustomerById(customerId).setDiscountCode(-1);

        System.out.println("transaction " + currentTransaction.getId() + " successfully created");
    }

    private static void acceptTransaction(Matcher matcher) {
        matcher.find();
        int transactionId = Integer.parseInt(matcher.group(1));
        if (Transaction.getTransactionById(transactionId) != null && !Transaction.getTransactionById(transactionId).isTransactionAccepted()) {
            Transaction confirmTransaction = Transaction.getTransactionById(transactionId);
            confirmTransaction.setAccepted(true);
            confirmTransaction.exchangeMoney();
        } else
            System.out.println("no waiting transaction with this id was found");
    }

    private static void printTransactions() {
        for (Transaction item : Transaction.getTransactions()) {
            if (item.isTransactionAccepted()) {
                System.out.println("transaction " + item.getId() + ": " + item.getCustomerId() + " " +
                        item.getAmount() + " " + item.getDiscountCode() + " " + item.getFinalPayment());
            }
        }
    }

    private static void printIncome() {
        System.out.println(confectionary.getBalance());
    }

    private static Matcher getCommandMatcher(String input, String regex) {
        Pattern regexPattern = Pattern.compile(regex);
        return regexPattern.matcher(input);
    }
}
