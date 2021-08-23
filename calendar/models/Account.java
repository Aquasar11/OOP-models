package models;

import java.util.ArrayList;
import java.util.Objects;

public class Account {
    private final String username;
    private String password;
    private final int id;
    private static final ArrayList<Account> allAccounts;
    private final ArrayList<Calendar> accountCalendars;
    private final ArrayList<Integer> accountEnableCalendars;
    private boolean firstLogin;

    static {
        allAccounts = new ArrayList<>();
    }

    public Account(String username, String password) {
        this.id = allAccounts.size() + 1;
        this.username = username;
        this.password = password;
        this.firstLogin = true;
        accountCalendars = new ArrayList<>();
        accountEnableCalendars = new ArrayList<>();

        allAccounts.add(this);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static Account getAccountByUsername(String username) {
        for (Account account : allAccounts) {
            if (account.getUsername().equals(username))
                return account;
        }
        return null;
    }

    public static ArrayList<Account> getAllAccounts() {
        return allAccounts;
    }

    public static void removeAccount(String username) {
        ArrayList<Calendar> iterator = new ArrayList<>(Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountCalendars());
        for (Calendar calendar : iterator) {
            if (calendar.getOwner().getUsername().equals(username))
                Calendar.deleteCalendar(calendar.getId());
        }
        allAccounts.remove(Account.getAccountByUsername(username));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Calendar> getAccountCalendars() {
        return accountCalendars;
    }

    public ArrayList<Integer> getAccountEnableCalendars() {
        return accountEnableCalendars;
    }

    public void addCalendar(Calendar calendar) {
        this.accountCalendars.add(calendar);
    }

    public void removeCalendar(Calendar calendar) {
        this.accountCalendars.remove(calendar);
        if (accountEnableCalendars.contains(calendar))
            this.accountEnableCalendars.remove(calendar);
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

}
