package view;

import controller.Controller;
import models.Account;
import models.Calendar;
import models.Events;
import models.Tasks;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IO {
    private static final Scanner scanner = new Scanner(System.in);

    public static void registerMenu() {
        String command = scanner.nextLine();
        while (!command.trim().equals("Exit")) {
            if (Pattern.matches("^Register[\\s]+[\\S]+[\\s]+[\\S]+$", command.trim())) {
                registerCommand(command.trim());
            } else if (Pattern.matches("^Login[\\s]+[\\S]+[\\s]+[\\S]+$", command.trim())) {
                loginCommand(command.trim());
            } else if (Pattern.matches("^Change[\\s]+Password[\\s]+[\\S]+[\\s]+[\\S]+[\\s]+[\\S]+$", command.trim())) {
                changePasswordCommand(command.trim());
            } else if (Pattern.matches("^Remove[\\s]+[\\S]+[\\s]+[\\S]+$", command.trim())) {
                removeAccountCommand(command.trim());
            } else if (Pattern.matches("^Show[\\s]+All[\\s]+Usernames$", command.trim())) {
                showListOfAccounts();
            } else
                System.out.println("invalid command!");
            command = scanner.nextLine();
        }
    }

    private static void showListOfAccounts() {
        ArrayList<Account> allAccounts = Account.getAllAccounts();
        if (allAccounts.size() == 0)
            System.out.println("nothing");
        else {
            ArrayList<String> allNames = new ArrayList<>();
            for (Account account : allAccounts) {
                allNames.add(account.getUsername());
            }
            Collections.sort(allNames);
            for (String name : allNames) {
                System.out.println(name);
            }
        }
    }

    private static void removeAccountCommand(String command) {
        Matcher matcher = Pattern.compile("^Remove[\\s]+([\\S]+)[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String username = matcher.group(1);
        String password = matcher.group(2);
        if (!Pattern.matches("^Remove[\\s]+[\\w]+[\\s]+[\\S]+$", command))
            System.out.println("invalid username!");
        else if (!Controller.isAlreadyExists(username))
            System.out.println("no user exists with this username");
        else if (!Pattern.matches("^Remove[\\s]+[\\w]+[\\s]+[\\w]+$", command))
            System.out.println("invalid password!");
        else if (Controller.isPasswordWrong(username, password))
            System.out.println("password is wrong!");
        else {
            System.out.println("removed successfully!");
            Controller.removeAccount(username);
        }
    }

    private static void changePasswordCommand(String command) {
        Matcher matcher = Pattern.compile("^Change[\\s]+Password[\\s]+([\\S]+)[\\s]+([\\S]+)[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String username = matcher.group(1);
        String oldPassword = matcher.group(2);
        String newPassword = matcher.group(3);
        if (!Pattern.matches("^Change[\\s]+Password[\\s]+[\\w]+[\\s]+[\\S]+[\\s]+[\\S]+$", command))
            System.out.println("invalid username!");
        else if (!Controller.isAlreadyExists(username))
            System.out.println("no user exists with this username");
        else if (!Pattern.matches("^Change[\\s]+Password[\\s]+[\\w]+[\\s]+[\\w]+[\\s]+[\\S]+$", command))
            System.out.println("invalid old password!");
        else if (Controller.isPasswordWrong(username, oldPassword))
            System.out.println("password is wrong!");
        else if (!Pattern.matches("^Change[\\s]+Password[\\s]+[\\w]+[\\s]+[\\w]+[\\s]+[\\w]+$", command))
            System.out.println("invalid new password!");
        else if (Controller.isPasswordWeak(newPassword))
            System.out.println("new password is weak!");
        else {
            System.out.println("password changed successfully!");
            Controller.changePassword(username, newPassword);
        }
    }

    private static void loginCommand(String command) {
        Matcher matcher = Pattern.compile("^Login[\\s]+([\\S]+)[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String username = matcher.group(1);
        String password = matcher.group(2);
        if (!Pattern.matches("^Login[\\s]+[\\w]+[\\s]+[\\S]+$", command))
            System.out.println("invalid username!");
        else if (!Controller.isAlreadyExists(username))
            System.out.println("no user exists with this username");
        else if (!Pattern.matches("^Login[\\s]+[\\w]+[\\s]+[\\w]+$", command))
            System.out.println("invalid password!");
        else if (Controller.isPasswordWrong(username, password))
            System.out.println("password is wrong!");
        else {
            System.out.println("login successful!");
            Controller.loginAccount(username);
        }
    }

    private static void registerCommand(String command) {
        Matcher matcher = Pattern.compile("^Register[\\s]+([\\S]+)[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String username = matcher.group(1);
        String password = matcher.group(2);
        if (!Pattern.matches("^Register[\\s]+[\\w]+[\\s]+[\\S]+$", command))
            System.out.println("invalid username!");
        else if (Controller.isAlreadyExists(username))
            System.out.println("a user exists with this username");
        else if (!Pattern.matches("^Register[\\s]+[\\w]+[\\s]+[\\w]+$", command))
            System.out.println("invalid password!");
        else if (Controller.isPasswordWeak(password))
            System.out.println("password is weak!");
        else {
            System.out.println("register successful!");
            Controller.registerAccount(username, password);
        }
    }

    public static void loginMenu(String username) {
        String command = scanner.nextLine();
        while (!command.trim().equals("Logout")) {
            if (Pattern.matches("^Create[\\s]+New[\\s]+Calendar[\\s]+[\\S]+$", command.trim())) {
                createANewCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Open[\\s]+Calendar[\\s]+[\\d]+$", command.trim())) {
                openCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Enable[\\s]+Calendar[\\s]+[\\d]+$", command.trim())) {
                enableCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Disable[\\s]+Calendar[\\s]+[\\d]+$", command.trim())) {
                disableCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Delete[\\s]+Calendar[\\s]+[\\d]+$", command.trim())) {
                deleteCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Remove[\\s]+Calendar[\\s]+[\\d]+$", command.trim())) {
                removeCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Edit[\\s]+Calendar[\\s]+[\\d]+[\\s]+[\\S]+$", command.trim())) {
                editCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Share[\\s]+Calendar[\\s]+[\\d]+[\\s]+.+$", command.trim())) {
                shareCalendarCommand(username, command.trim());
            } else if (Pattern.matches("^Show[\\s]+(?:[0-9]{4}_[0-9]{2}_[0-9]{2})$", command.trim())) {
                showDateCommand(username, command.trim());
            } else if (Pattern.matches("^Show[\\s]+Calendars$", command.trim())) {
                showCalendarCommand(username);
            } else if (Pattern.matches("^Show[\\s]+Enabled[\\s]+Calendars$", command.trim())) {
                showEnabledCalendarCommand(username);
            } else
                System.out.println("invalid command!");
            command = scanner.nextLine();
        }
        System.out.println("logout successful");
    }

    private static void showEnabledCalendarCommand(String username) {
        ArrayList<Integer> allEnableCalendars = Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountEnableCalendars();
        if (allEnableCalendars.size() == 0)
            System.out.println("nothing");
        else {
            int printCounter = 0;
            Collections.sort(allEnableCalendars);
            for (Integer id : allEnableCalendars) {
                if (Controller.isCalendarExists(id)) {
                    System.out.println(Calendar.getCalendarById(id));
                    printCounter += 1;
                }
            }
            if (printCounter == 0)
                System.out.println("nothing");
        }
    }

    private static void showCalendarCommand(String username) {
        ArrayList<Calendar> allCalendars = Objects.requireNonNull(Account.getAccountByUsername(username)).getAccountCalendars();
        if (allCalendars.size() == 0)
            System.out.println("nothing");
        else {
            ArrayList<Integer> allIds = new ArrayList<>();
            for (Calendar calendar : allCalendars) {
                if (calendar.getOwner().getUsername().equals(username))
                    allIds.add(calendar.getId());
            }
            Collections.sort(allIds);
            for (Integer id : allIds) {
                System.out.println(Calendar.getCalendarById(id));
            }
        }
    }

    private static void showDateCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Show[\\s]+((?:[0-9]{4}_[0-9]{2}_[0-9]{2})$)").matcher(command);
        matcher.find();
        String date = matcher.group(1);
        if (Controller.isDateInvalid(date))
            System.out.println("date is invalid!");
        else {
            System.out.println("events on " + date + ":");
            Controller.showAllEventsOnDate(username, date);
            System.out.println("tasks on " + date + ":");
            Controller.showAllTasksOnDate(username, date);
        }
    }

    private static void shareCalendarCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Share[\\s]+Calendar[\\s]+([\\d]+)[\\s]+(.+)$").matcher(command);
        matcher.find();
        int calendarId;
        try {
            calendarId = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            System.out.println("there is no calendar with this ID!");
            return;
        }
        String listOfUsers = matcher.group(2);
        if (!Controller.isCalendarExists(calendarId))
            System.out.println("there is no calendar with this ID!");
        else if (!Controller.isCalendarBelongsToUser(username, calendarId))
            System.out.println("you have no calendar with this ID!");
        else if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to share this calendar!");
        else {
            String[] users = listOfUsers.split("[\\s]+");
            for (String user : users) {
                if (!Pattern.matches("[\\w]+$", user)) {
                    System.out.println("invalid username!");
                    return;
                } else if (!Controller.isAlreadyExists(user)) {
                    System.out.println("no user exists with this username!");
                    return;
                }
            }
            System.out.println("calendar shared!");
            Controller.shareCalendarWithUsers(calendarId, users);
        }
    }

    private static void editCalendarCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Edit[\\s]+Calendar[\\s]+([\\d]+)[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        int calendarId;
        try {
            calendarId = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            System.out.println("there is no calendar with this ID!");
            return;
        }
        String title = matcher.group(2);
        if (!Controller.isCalendarExists(calendarId))
            System.out.println("there is no calendar with this ID!");
        else if (!Controller.isCalendarBelongsToUser(username, calendarId))
            System.out.println("you have no calendar with this ID!");
        else if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to edit this calendar!");
        else if (!Pattern.matches("^[\\w]+$", title))
            System.out.println("invalid title!");
        else {
            System.out.println("calendar title edited!");
            Controller.changeCalendarTitle(calendarId, title);
        }
    }

    private static void removeCalendarCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Remove[\\s]+Calendar[\\s]+([\\d]+)$").matcher(command);
        matcher.find();
        int calendarId;
        try {
            calendarId = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            System.out.println("there is no calendar with this ID!");
            return;
        }
        if (!Controller.isCalendarExists(calendarId))
            System.out.println("there is no calendar with this ID!");
        else if (!Controller.isCalendarBelongsToUser(username, calendarId))
            System.out.println("you have no calendar with this ID!");
        else if (!Controller.doesUserOwnThisCalendar(username, calendarId)) {
            System.out.println("calendar removed!");
            Controller.removeTheCalendar(username, calendarId);
        } else {
            System.out.println("do you want to delete this calendar?");
            String answer = scanner.nextLine();
            if (answer.trim().equals("no"))
                System.out.println("OK!");
            else {
                System.out.println("calendar deleted!");
                Controller.deleteCalendar(calendarId);
            }
        }
    }

    private static void deleteCalendarCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Delete[\\s]+Calendar[\\s]+([\\d]+)").matcher(command);
        matcher.find();
        int calendarId;
        try {
            calendarId = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            System.out.println("there is no calendar with this ID!");
            return;
        }
        if (!Controller.isCalendarExists(calendarId))
            System.out.println("there is no calendar with this ID!");
        else if (!Controller.isCalendarBelongsToUser(username, calendarId))
            System.out.println("you have no calendar with this ID!");
        else if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to delete this calendar!");
        else {
            System.out.println("calendar deleted!");
            Controller.deleteCalendar(calendarId);
        }
    }

    private static void disableCalendarCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Disable[\\s]+Calendar[\\s]+([\\d]+)$").matcher(command);
        matcher.find();
        int calendarId;
        try {
            calendarId = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            System.out.println("there is no calendar with this ID!");
            return;
        }
        if (!Controller.isCalendarExists(calendarId))
            System.out.println("there is no calendar with this ID!");
        else if (!Controller.isCalendarBelongsToUser(username, calendarId))
            System.out.println("you have no calendar with this ID!");
        else {
            System.out.println("calendar disabled successfully!");
            Controller.disableCalendar(username, calendarId);
        }
    }

    private static void enableCalendarCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Enable[\\s]+Calendar[\\s]+([\\d]+)$").matcher(command);
        matcher.find();
        int calendarId;
        try {
            calendarId = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            System.out.println("there is no calendar with this ID!");
            return;
        }
        if (!Controller.isCalendarExists(calendarId))
            System.out.println("there is no calendar with this ID!");
        else if (!Controller.isCalendarBelongsToUser(username, calendarId))
            System.out.println("you have no calendar with this ID!");
        else {
            System.out.println("calendar enabled successfully!");
            Controller.enableCalendar(username, calendarId);
        }
    }

    private static void openCalendarCommand(String username, String command) {
        Matcher matcher = Pattern.compile("^Open[\\s]+Calendar[\\s]+([\\d]+)$").matcher(command);
        matcher.find();
        int calendarId;
        try {
            calendarId = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            System.out.println("there is no calendar with this ID!");
            return;
        }
        if (!Controller.isCalendarExists(calendarId))
            System.out.println("there is no calendar with this ID!");
        else if (!Controller.isCalendarBelongsToUser(username, calendarId))
            System.out.println("you have no calendar with this ID!");
        else {
            System.out.println("calendar opened successfully!");
            calendarMenu(username, calendarId);
        }
    }

    private static void createANewCalendarCommand(String username, String command) {
        if (!Pattern.matches("^Create[\\s]+New[\\s]+Calendar[\\s]+[\\w]+$", command))
            System.out.println("invalid title!");
        else {
            Matcher matcher = Pattern.compile("^Create[\\s]+New[\\s]+Calendar[\\s]+([\\w]+)$").matcher(command);
            matcher.find();
            System.out.println("calendar created successfully!");
            Controller.createANewCalendar(matcher.group(1), username);
        }
    }

    public static void calendarMenu(String username, int calendarId) {
        String command = scanner.nextLine();
        while (!command.trim().equals("Back")) {
            if (Pattern.matches("^Show[\\s]+Events$", command.trim())) {
                showEventsCommand(calendarId);
            } else if (Pattern.matches("^Show[\\s]+tasks$", command.trim())) {
                showTasksCommand(calendarId);
            } else if (Pattern.matches("^Add[\\s]+Event[\\s]+[\\S]+[\\s]+(?:[0-9]{4}_[0-9]{2}_[0-9]{2})[\\s]+" +
                    "(?:(?:[0-9]{4}_[0-9]{2}_[0-9]{2})|[\\d]+|None)[\\s]+[DWM]?[\\s]+[TF]$", command.trim())) {
                addEventCommand(username, calendarId, command.trim());
            } else if (Pattern.matches("^Add[\\s]+Task[\\s]+[\\S]+[\\s]+[0-9]{2}_[0-9]{2}[\\s]+[0-9]{2}_[0-9]{2}[\\s]+" +
                    "(?:[0-9]{4}_[0-9]{2}_[0-9]{2})[\\s]+(?:(?:[0-9]{4}_[0-9]{2}_[0-9]{2})|[\\d]+|None)[\\s]+[DWM]?[\\s]+[TF]$", command.trim())) {
                addTaskCommand(username, calendarId, command.trim());
            } else if (Pattern.matches("^Edit[\\s]+Event[\\s]+[\\S]+[\\s]+(?:title|repeat|kind[\\s]+of[\\s]+repeat|meet)[\\s]+[\\S]+$", command.trim())) {
                editEventCommand(username, calendarId, command.trim());
            } else if (Pattern.matches("^Edit[\\s]+Task[\\s]+[\\S]+[\\s]+" +
                    "(?:title|repeat|kind[\\s]+of[\\s]+repeat|meet|start[\\s]+time|end[\\s]+time)[\\s]+[\\S]+$", command.trim())) {
                editTaskCommand(username, calendarId, command.trim());
            } else if (Pattern.matches("^Delete[\\s]+Event[\\s]+[\\S]+$", command.trim())) {
                deleteEventCommand(username, calendarId, command.trim());
            } else if (Pattern.matches("^Delete[\\s]+Task[\\s]+[\\S]+$", command.trim())) {
                deleteTaskCommand(username, calendarId, command.trim());
            } else
                System.out.println("invalid command!");
            command = scanner.nextLine();
        }
    }

    private static void deleteTaskCommand(String username, int calendarId, String command) {
        Matcher matcher = Pattern.compile("^Delete[\\s]+Task[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String title = matcher.group(1);

        if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to do this!");
        else if (!Pattern.matches("[\\w]+", title))
            System.out.println("invalid title!");
        else if (!Controller.isTaskExists(title))
            System.out.println("there is no task with this title!");
        else {
            System.out.println("task deleted successfully!");
            Controller.deleteTask(title);
        }
    }

    private static void deleteEventCommand(String username, int calendarId, String command) {
        Matcher matcher = Pattern.compile("^Delete[\\s]+Event[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String title = matcher.group(1);

        if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to do this!");
        else if (!Pattern.matches("[\\w]+", title))
            System.out.println("invalid title!");
        else if (!Controller.isEventExists(title))
            System.out.println("there is no event with this title!");
        else {
            System.out.println("event deleted successfully!");
            Controller.deleteEvent(title);
        }
    }

    private static void editTaskCommand(String username, int calendarId, String command) {
        Matcher matcher = Pattern.compile("^Edit[\\s]+Task[\\s]+([\\S]+)[\\s]+" +
                "((?:title|repeat|kind[\\s]+of[\\s]+repeat|meet|start[\\s]+time|end[\\s]+time))[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String title = matcher.group(1);
        String field = matcher.group(2);
        String newValue = matcher.group(3);

        if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to do this!");
        else if (field.equals("title") && !Pattern.matches("[\\w]+", newValue))
            System.out.println("invalid title!");
        else if (field.equals("repeat") && Pattern.matches("(?:[0-9]{4}_[0-9]{2}_[0-9]{2})", newValue) && Controller.isDateInvalid(newValue))
            System.out.println("invalid end date!");
        else if (Controller.isTaskExists(newValue))
            System.out.println("there is another task with this title!");
        else if (!Controller.isTaskExists(title))
            System.out.println("there is no event with this title!");
        else if (field.equals("title") && Pattern.matches("[\\w]+", newValue)) {
            System.out.println("task edited!");
            Controller.editTask(title, field, newValue);
        } else if (field.equals("repeat") && Pattern.matches("(?:[0-9]{4}_[0-9]{2}_[0-9]{2})|[\\d]+", newValue)) {
            System.out.println("task edited!");
            Controller.editTask(title, field, newValue);
        } else if (Pattern.matches("kind[\\s]+of[\\s]+repeat", field) && Pattern.matches("[DWM]", newValue)) {
            System.out.println("task edited!");
            Controller.editTask(title, field, newValue);
        } else if (field.equals("meet") && Pattern.matches("[TF]", newValue)) {
            System.out.println("task edited!");
            if (newValue.equals("T"))
                scanner.nextLine();
            Controller.editTask(title, field, newValue);
        } else if (Pattern.matches("start[\\s]+time|end[\\s]+time", field) && Pattern.matches("(?:[0-9]{4}_[0-9]{2}_[0-9]{2})", newValue)) {
            System.out.println("task edited!");
            Controller.editTask(title, field, newValue);
        } else System.out.println("invalid command!");
    }

    private static void editEventCommand(String username, int calendarId, String command) {
        Matcher matcher = Pattern.compile("^Edit[\\s]+Event[\\s]+([\\S]+)[\\s]+((?:title|repeat|kind[\\s]+of[\\s]+repeat|meet))[\\s]+([\\S]+)$").matcher(command);
        matcher.find();
        String title = matcher.group(1);
        String field = matcher.group(2);
        String newValue = matcher.group(3);

        if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to do this!");
        else if (field.equals("title") && !Pattern.matches("[\\w]+", newValue))
            System.out.println("invalid title!");
        else if (field.equals("repeat") && Pattern.matches("(?:[0-9]{4}_[0-9]{2}_[0-9]{2})", newValue) && Controller.isDateInvalid(newValue))
            System.out.println("invalid end date!");
        else if (Controller.isEventExists(newValue))
            System.out.println("there is another event with this title!");
        else if (!Controller.isEventExists(title))
            System.out.println("there is no task with this title!");
        else if (field.equals("title") && Pattern.matches("[\\w]+", newValue)) {
            System.out.println("task edited!");
            Controller.editEvent(title, field, newValue);
        } else if (field.equals("repeat") && Pattern.matches("(?:[0-9]{4}_[0-9]{2}_[0-9]{2})|[\\d]+", newValue)) {
            System.out.println("task edited!");
            Controller.editEvent(title, field, newValue);
        } else if (Pattern.matches("kind[\\s]+of[\\s]+repeat", field) && Pattern.matches("[DWM]", newValue)) {
            System.out.println("task edited!");
            Controller.editEvent(title, field, newValue);
        } else if (field.equals("meet") && Pattern.matches("[TF]", newValue)) {
            System.out.println("task edited!");
            if (newValue.equals("T"))
                scanner.nextLine();
            Controller.editEvent(title, field, newValue);
        } else System.out.println("invalid command!");
    }

    private static void addTaskCommand(String username, int calendarId, String command) {
        Matcher matcher = Pattern.compile("^Add[\\s]+Task[\\s]+([\\S]+)[\\s]+([0-9]{2}_[0-9]{2})[\\s]+([0-9]{2}_[0-9]{2})[\\s]+" +
                "((?:[0-9]{4}_[0-9]{2}_[0-9]{2}))[\\s]+((?:(?:[0-9]{4}_[0-9]{2}_[0-9]{2})|[\\d]+|None))[\\s]+([DWM]?)[\\s]+([TF])$").matcher(command);
        matcher.find();
        String title = matcher.group(1);
        String startTimeString = matcher.group(2);
        String endTimeString = matcher.group(3);
        String startDateString = matcher.group(4);
        String repeatString = matcher.group(5);
        String repeatAmountString = matcher.group(6);
        String meetingString = matcher.group(7);
        boolean repeat = true;
        boolean meeting = false;
        if (repeatString.equals("None"))
            repeat = false;
        if (meetingString.equals("T")) {
            meeting = true;
        }
        if (repeat && !repeatAmountString.equals("D") && !repeatAmountString.equals("W") && !repeatAmountString.equals("M"))
            System.out.println("invalid command!");
        else if (!repeat && repeatAmountString.length() != 0)
            System.out.println("invalid command!");
        else if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to do this!");
        else if (!Pattern.matches("[\\w]+", title))
            System.out.println("invalid title!");
        else if (Controller.isDateInvalid(startDateString))
            System.out.println("invalid start date!");
        else if (Pattern.matches("(?:[0-9]{4}_[0-9]{2}_[0-9]{2})", repeatString) && Controller.isDateInvalid(repeatString))
            System.out.println("invalid end date!");
        else if (Controller.isTaskExists(title))
            System.out.println("there is another task with this title!");
        else {
            System.out.println("task added successfully!");
            if (meeting)
                scanner.nextLine();
            Controller.addTask(calendarId, title, startDateString, repeat, repeatString, repeatAmountString, meeting, startTimeString, endTimeString);
        }
    }

    private static void addEventCommand(String username, int calendarId, String command) {
        Matcher matcher = Pattern.compile("^Add[\\s]+Event[\\s]+([\\S]+)[\\s]+((?:[0-9]{4}_[0-9]{2}_[0-9]{2}))[\\s]+((?:(?:[0-9]{4}_[0-9]{2}_[0-9]{2})|[\\d]+|None))" +
                "[\\s]+([DWM]?)[\\s]+([TF])$").matcher(command);
        matcher.find();
        String title = matcher.group(1);
        String startDateString = matcher.group(2);
        String repeatString = matcher.group(3);
        String repeatAmountString = matcher.group(4);
        String meetingString = matcher.group(5);
        boolean repeat = true;
        boolean meeting = false;
        if (repeatString.equals("None"))
            repeat = false;
        if (meetingString.equals("T")) {
            meeting = true;
        }
        if (repeat && !repeatAmountString.equals("D") && !repeatAmountString.equals("W") && !repeatAmountString.equals("M"))
            System.out.println("invalid command!");
        else if (!repeat && repeatAmountString.length() != 0)
            System.out.println("invalid command!");
        else if (!Controller.doesUserOwnThisCalendar(username, calendarId))
            System.out.println("you don't have access to do this!");
        else if (!Pattern.matches("[\\w]+", title))
            System.out.println("invalid title!");
        else if (Controller.isDateInvalid(startDateString))
            System.out.println("invalid start date!");
        else if (Pattern.matches("(?:[0-9]{4}_[0-9]{2}_[0-9]{2})", repeatString) && Controller.isDateInvalid(repeatString))
            System.out.println("invalid end date!");
        else if (Controller.isEventExists(title))
            System.out.println("there is another event with this title!");
        else {
            System.out.println("event added successfully!");
            if (meeting)
                scanner.nextLine();
            Controller.addEvent(calendarId, title, startDateString, repeat, repeatString, repeatAmountString, meeting);
        }
    }

    private static void showTasksCommand(int calendarId) {
        ArrayList<Tasks> calendarsTasks = Objects.requireNonNull(Calendar.getCalendarById(calendarId)).getAllTasks();
        if (calendarsTasks.size() == 0)
            System.out.println("nothing");
        else {
            System.out.println("tasks of this calendar:");

            calendarsTasks.sort(new Comparator<Tasks>() {
                @Override
                public int compare(Tasks o1, Tasks o2) {
                    if (!o1.getStartDate().isEqual(o2.getStartDate()) && o1.getStartDate().isEqual(o2.getStartDate()))
                        return -1;
                    else if (!o1.getStartDate().isEqual(o2.getStartDate()) && o1.getStartDate().isAfter(o2.getStartDate()))
                        return 1;
                    else if (o1.getStartTime().isBefore(o2.getStartTime()))
                        return -1;
                    else if (o1.getStartTime().isAfter(o2.getStartTime()))
                        return 1;
                    else
                        return o1.getTitle().compareTo(o2.getTitle());
                }
            });

            for (Tasks task : calendarsTasks) {
                System.out.println(task);
            }
        }
    }

    private static void showEventsCommand(int calendarId) {
        ArrayList<Events> calendarsEvents = Objects.requireNonNull(Calendar.getCalendarById(calendarId)).getAllEvents();
        if (calendarsEvents.size() == 0)
            System.out.println("nothing");
        else {
            System.out.println("events of this calendar:");

            calendarsEvents.sort(new Comparator<Events>() {
                @Override
                public int compare(Events o1, Events o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });

            for (Events event : calendarsEvents) {
                System.out.println(event);
            }
        }
    }
}
