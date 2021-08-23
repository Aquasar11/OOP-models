package controller;

import models.Account;
import models.Calendar;
import models.Events;
import models.Tasks;
import view.IO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    public static boolean isAlreadyExists(String username) {
        return Account.getAccountByUsername(username) != null;
    }

    public static boolean isPasswordWeak(String password) {
        if (password.length() < 5)
            return true;
        else {
            Matcher matcherUpperCase = Pattern.compile("[A-Z]+").matcher(password);
            Matcher matcherLowerCase = Pattern.compile("[a-z]+").matcher(password);
            Matcher matcherNumber = Pattern.compile("[0-9]+").matcher(password);
            return !(matcherUpperCase.find() && matcherLowerCase.find() && matcherNumber.find());
        }
    }

    public static void registerAccount(String username, String password) {
        new Account(username, password);
    }

    public static void loginAccount(String username) {
        if (Objects.requireNonNull(Account.getAccountByUsername(username)).isFirstLogin()) {
            Objects.requireNonNull(Account.getAccountByUsername(username)).addCalendar(new Calendar(username, username));
            System.out.println("calendar created successfully!");
        }
        Objects.requireNonNull(Account.getAccountByUsername(username)).setFirstLogin(false);
        Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountEnableCalendars().removeAll
                (Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountEnableCalendars());
        if (Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountCalendars().size() != 0) {
            int firstId = 0;
            for (Calendar calendar : Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountCalendars()) {
                if (calendar.getTitle().equals(username)) {
                    firstId = calendar.getId();
                    break;
                }
            }
            for (Calendar calendar : Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountCalendars()) {
                if (calendar.getId() < firstId && calendar.getTitle().equals(username))
                    firstId = calendar.getId();
            }
            if (firstId != 0)
                enableCalendar(username, firstId);

        }
        IO.loginMenu(username);
    }

    public static void changePassword(String username, String newPassword) {
        Objects.requireNonNull(Account.getAccountByUsername(username)).setPassword(newPassword);
    }

    public static boolean isPasswordWrong(String username, String password) {
        return !Objects.requireNonNull(Account.getAccountByUsername(username)).getPassword().equals(password);
    }

    public static void removeAccount(String username) {
        Account.removeAccount(username);
    }

    public static void createANewCalendar(String title, String username) {
        Objects.requireNonNull(Account.getAccountByUsername(username)).addCalendar(new Calendar(title, username));
    }

    public static boolean isCalendarExists(int calendarId) {
        return Calendar.getCalendarById(calendarId) != null;
    }

    public static boolean isCalendarBelongsToUser(String username, int calendarId) {
        ArrayList<Calendar> userCalendars = Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountCalendars();
        for (Calendar calendar : userCalendars) {
            if (calendar.getId() == calendarId)
                return true;
        }
        return false;
    }

    public static void enableCalendar(String username, int calendarId) {
        Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountEnableCalendars().add(calendarId);
    }

    public static void disableCalendar(String username, int calendarId) {
        Integer integer = calendarId;
        Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountEnableCalendars().remove(integer);
    }

    public static void deleteCalendar(int calendarId) {
        Calendar.deleteCalendar(calendarId);
    }

    public static boolean doesUserOwnThisCalendar(String username, int calendarId) {
        return Objects.requireNonNull(Calendar.getCalendarById(calendarId)).getOwner().getUsername().equals(username);
    }

    public static void removeTheCalendar(String username, int calendarId) {
        Objects.requireNonNull(Account.getAccountByUsername(username)).removeCalendar(Calendar.getCalendarById(calendarId));
        Objects.requireNonNull(Calendar.getCalendarById(calendarId)).getSharedWith().remove(Objects.requireNonNull(Account.getAccountByUsername(username)));
    }

    public static void changeCalendarTitle(int calendarId, String title) {
        Objects.requireNonNull(Calendar.getCalendarById(calendarId)).setTitle(title);
    }

    public static void shareCalendarWithUsers(int calendarId, String[] users) {
        for (String name : users) {
            if (!Objects.requireNonNull(Account.getAccountByUsername(name)).getAccountCalendars().contains(Calendar.getCalendarById(calendarId))) {
                Objects.requireNonNull(Account.getAccountByUsername(name)).addCalendar(Calendar.getCalendarById(calendarId));
                Objects.requireNonNull(Calendar.getCalendarById(calendarId)).sharedWith(Account.getAccountByUsername(name));
            }
        }
    }

    public static boolean isDateInvalid(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu_MM_dd").withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return true;
        }
        return false;
    }

    public static boolean isEventExists(String title) {
        return Events.getEventsByTitle(title) != null;
    }

    public static boolean isTaskExists(String title) {
        return Tasks.getTasksByTitle(title) != null;
    }

    public static void deleteEvent(String title) {
        Objects.requireNonNull(Events.getEventsByTitle(title)).getCalendar().removeEvents(Events.getEventsByTitle(title));
        Events.removeEvent(title);
    }

    public static void deleteTask(String title) {
        Objects.requireNonNull(Tasks.getTasksByTitle(title)).getCalendar().removeTasks(Tasks.getTasksByTitle(title));
        Tasks.removeTask(title);
    }

    public static void addEvent(int calendarId, String title, String startDateString, boolean repeat, String repeatString, String repeatAmountString, boolean meeting) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu_MM_dd").withResolverStyle(ResolverStyle.STRICT);
        LocalDate startingDate = LocalDate.parse(startDateString, dateFormatter);

        ArrayList<LocalDate> repeats = findRepeats(startingDate, repeat, repeatString, repeatAmountString);

        Events newEvent = new Events(title, startingDate, meeting, repeat, Calendar.getCalendarById(calendarId), repeats, repeatString);
        Objects.requireNonNull(Calendar.getCalendarById(calendarId)).addEvents(newEvent);
    }

    private static ArrayList<LocalDate> findRepeats(LocalDate startingDate, boolean repeat, String repeatString, String repeatAmountString) {
        ArrayList<LocalDate> repeatDates = new ArrayList<>();
        if (!repeat)
            return repeatDates;
        else if (Pattern.matches("[\\d]+", repeatString)) {
            int repeatAmount = Integer.parseInt(repeatString);
            for (int i = 1; i <= repeatAmount; i++) {
                switch (repeatAmountString) {
                    case "D":
                        repeatDates.add(startingDate.plusDays(i));
                        break;
                    case "W":
                        repeatDates.add(startingDate.plusWeeks(i));
                        break;
                    case "M":
                        repeatDates.add(startingDate.plusMonths(i));
                        break;
                }
            }
            return repeatDates;
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu_MM_dd").withResolverStyle(ResolverStyle.STRICT);
            LocalDate endDate = LocalDate.parse(repeatString, dateFormatter);

            while (startingDate.isBefore(endDate)) {
                switch (repeatAmountString) {
                    case "D":
                        startingDate = startingDate.plusDays(1);
                        repeatDates.add(startingDate);
                        break;
                    case "W":
                        startingDate = startingDate.plusWeeks(1);
                        repeatDates.add(startingDate);
                        break;
                    case "M":
                        startingDate = startingDate.plusMonths(1);
                        repeatDates.add(startingDate);
                        break;
                }
            }
            return repeatDates;
        }
    }

    public static void addTask(int calendarId, String title, String startDateString, boolean repeat, String repeatString, String repeatAmountString, boolean meeting,
                               String startTimeString, String endTimeString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu_MM_dd").withResolverStyle(ResolverStyle.STRICT);
        LocalDate startingDate = LocalDate.parse(startDateString, dateFormatter);

        ArrayList<LocalDate> repeats = findRepeats(startingDate, repeat, repeatString, repeatAmountString);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH_mm").withResolverStyle(ResolverStyle.STRICT);
        LocalTime startTime = LocalTime.parse(startTimeString, timeFormatter);
        LocalTime endTime = LocalTime.parse(endTimeString, timeFormatter);

        Tasks newTask = new Tasks(title, startingDate, meeting, repeat, startTime, endTime, Calendar.getCalendarById(calendarId), repeats, repeatString);
        Objects.requireNonNull(Calendar.getCalendarById(calendarId)).addTasks(newTask);
    }

    public static void editEvent(String title, String field, String newValue) {
        if (field.equals("title"))
            changeEventTitle(title, newValue);
        else if (field.equals("repeat"))
            changeEventRepeats(title, newValue);
        else if (field.equals("meet"))
            changeEventMeeting(title, newValue);
        else if (Pattern.matches("kind[\\s]+of[\\s]+repeat", field))
            changeEventKindOfRepeat(title, newValue);
    }

    private static void changeEventKindOfRepeat(String title, String newValue) {
        Events currentEvent = Events.getEventsByTitle(title);
        ArrayList<LocalDate> newRepeats = findRepeats(currentEvent.getStartDate(), currentEvent.isRepeat(), currentEvent.getRepeatString(), newValue);
        currentEvent.setRepeatDates(newRepeats);
    }

    private static void changeEventMeeting(String title, String newValue) {
        if (newValue.equals("T")) {
            new Scanner(System.in).nextLine();
            Objects.requireNonNull(Events.getEventsByTitle(title)).setMeeting(true);
        } else Objects.requireNonNull(Events.getEventsByTitle(title)).setMeeting(false);
    }

    private static void changeEventRepeats(String title, String newValue) {
        Events currentEvent = Events.getEventsByTitle(title);
        if (newValue.equals("None")) {
            currentEvent.setRepeat(false);
            currentEvent.setRepeatDates(new ArrayList<>());
        } else {
            ArrayList<LocalDate> newRepeats = findRepeats(currentEvent.getStartDate(), true, newValue, "D");
            currentEvent.setRepeatDates(newRepeats);
        }
    }

    private static void changeEventTitle(String title, String newValue) {
        Objects.requireNonNull(Events.getEventsByTitle(title)).setTitle(newValue);
    }

    public static void editTask(String title, String field, String newValue) {
        if (field.equals("title"))
            changeTaskTitle(title, newValue);
        else if (field.equals("repeat"))
            changeTaskRepeat(title, newValue);
        else if (field.equals("meet"))
            changeTaskMeeting(title, newValue);
        else if (Pattern.matches("start[\\s]+time", field))
            changeTaskStartTime(title, newValue);
        else if (Pattern.matches("end[\\s]+time", field))
            changeTaskEndTime(title, newValue);
        else if (Pattern.matches("kind[\\s]+of[\\s]+repeat", field))
            changeTaskKindOfRepeat(title, newValue);
    }

    private static void changeTaskKindOfRepeat(String title, String newValue) {
        Tasks currentTask = Tasks.getTasksByTitle(title);
        ArrayList<LocalDate> newRepeats = findRepeats(currentTask.getStartDate(), currentTask.isRepeat(), currentTask.getRepeatString(), newValue);
        currentTask.setRepeatDates(newRepeats);
    }

    private static void changeTaskEndTime(String title, String newValue) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH_mm").withResolverStyle(ResolverStyle.STRICT);
        LocalTime endTime = LocalTime.parse(newValue, timeFormatter);

        Tasks.getTasksByTitle(title).setEndTime(endTime);
    }

    private static void changeTaskStartTime(String title, String newValue) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH_mm").withResolverStyle(ResolverStyle.STRICT);
        LocalTime startTime = LocalTime.parse(newValue, timeFormatter);

        Tasks.getTasksByTitle(title).setStartTime(startTime);
    }

    private static void changeTaskMeeting(String title, String newValue) {
        if (newValue.equals("T")) {
            new Scanner(System.in).nextLine();
            Objects.requireNonNull(Tasks.getTasksByTitle(title)).setMeeting(true);
        } else Objects.requireNonNull(Tasks.getTasksByTitle(title)).setMeeting(false);
    }

    private static void changeTaskRepeat(String title, String newValue) {
        Tasks currentTask = Tasks.getTasksByTitle(title);
        if (newValue.equals("None")) {
            currentTask.setRepeat(false);
            currentTask.setRepeatDates(new ArrayList<>());
        } else {
            ArrayList<LocalDate> newRepeats = findRepeats(currentTask.getStartDate(), true, newValue, "D");
            currentTask.setRepeatDates(newRepeats);
        }
    }

    private static void changeTaskTitle(String title, String newValue) {
        Objects.requireNonNull(Tasks.getTasksByTitle(title)).setTitle(newValue);
    }

    public static void showAllEventsOnDate(String username, String dateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu_MM_dd").withResolverStyle(ResolverStyle.STRICT);
        LocalDate date = LocalDate.parse(dateString, dateFormatter);

        ArrayList<Events> allEnabledEventsOfDate = new ArrayList<>();

        for (Integer calendarId : Account.getAccountByUsername(username).getAccountEnableCalendars()) {
            for (Events event : Calendar.getCalendarById(calendarId).getAllEvents()) {
                if (isEventHappenedInThisDate(date, event))
                    allEnabledEventsOfDate.add(event);
            }
        }

        allEnabledEventsOfDate.sort(new Comparator<Events>() {
            @Override
            public int compare(Events o1, Events o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        for (Events event : allEnabledEventsOfDate) {
            System.out.println(event);
        }
    }

    private static boolean isEventHappenedInThisDate(LocalDate date, Events event) {
        if (event.getStartDate().isEqual(date))
            return true;
        else {
            for (LocalDate repeatDate : event.getRepeatDates()) {
                if (repeatDate.isEqual(date))
                    return true;
            }
            return false;
        }
    }

    public static void showAllTasksOnDate(String username, String dateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu_MM_dd").withResolverStyle(ResolverStyle.STRICT);
        LocalDate date = LocalDate.parse(dateString, dateFormatter);

        ArrayList<Tasks> allEnabledTasksOfDate = new ArrayList<>();

        for (Integer calendarId : Account.getAccountByUsername(username).getAccountEnableCalendars()) {
            for (Tasks task : Calendar.getCalendarById(calendarId).getAllTasks()) {
                if (isTaskHappenedInThisDate(date, task))
                    allEnabledTasksOfDate.add(task);
            }
        }

        allEnabledTasksOfDate.sort(new Comparator<Tasks>() {
            @Override
            public int compare(Tasks o1, Tasks o2) {
                if (o1.getStartTime().isBefore(o2.getStartTime()))
                    return -1;
                else if (o1.getStartTime().isAfter(o2.getStartTime()))
                    return 1;
                else
                    return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        for (Tasks task : allEnabledTasksOfDate) {
            System.out.println(task);
        }
    }

    private static boolean isTaskHappenedInThisDate(LocalDate date, Tasks task) {
        if (task.getStartDate().isEqual(date))
            return true;
        else {
            for (LocalDate repeatDate : task.getRepeatDates()) {
                if (repeatDate.isEqual(date))
                    return true;
            }
            return false;
        }
    }
}
