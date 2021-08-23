package models;

import java.util.ArrayList;
import java.util.Objects;

public class Calendar {
    private final int id;
    private String title;
    private final Account owner;
    private final ArrayList<Account> sharedWith;
    private final ArrayList<Events> allEvents;
    private final ArrayList<Tasks> allTasks;
    private static final ArrayList<Calendar> allCalendars;
    private static int idCounter;

    static {
        allCalendars = new ArrayList<>();
        idCounter = 0;
    }

    public Calendar(String title, String owner) {
        this.title = title;
        this.owner = Account.getAccountByUsername(owner);
        this.sharedWith = new ArrayList<>();
        this.sharedWith.add(Account.getAccountByUsername(owner));
        this.allEvents = new ArrayList<>();
        this.allTasks = new ArrayList<>();
        this.id = idCounter + 1;
        idCounter += 1;

        allCalendars.add(this);
    }

    @Override
    public String toString() {
        return "Calendar: " + this.title + " " + this.id;
    }

    public int getId() {
        return id;
    }

    public static ArrayList<Calendar> getAllCalendars() {
        return allCalendars;
    }

    public String getTitle() {
        return title;
    }

    public Account getOwner() {
        return owner;
    }

    public static Calendar getCalendarById(int calendarId) {
        for (Calendar calendar : allCalendars) {
            if (calendar.id == calendarId)
                return calendar;
        }
        return null;
    }

    public static void deleteCalendar(int calendarId) {
        for (Account account : Calendar.getCalendarById(calendarId).getSharedWith()) {
            account.removeCalendar(Calendar.getCalendarById(calendarId));
        }
        allCalendars.remove(Calendar.getCalendarById(calendarId));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void sharedWith(Account account) {
        this.sharedWith.add(account);
    }

    public ArrayList<Account> getSharedWith() {
        return sharedWith;
    }

    public void addTasks(Tasks task) {
        this.allTasks.add(task);
    }

    public void removeTasks(Tasks task) {
        this.allTasks.remove(task);
    }

    public void addEvents(Events events) {
        this.allEvents.add(events);
    }

    public void removeEvents(Events events) {
        this.allEvents.remove(events);
    }

    public ArrayList<Events> getAllEvents() {
        return allEvents;
    }

    public ArrayList<Tasks> getAllTasks() {
        return allTasks;
    }

}
