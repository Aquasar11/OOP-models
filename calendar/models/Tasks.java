package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Tasks {
    private String title;
    private final LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean meeting;
    private boolean repeat;
    private ArrayList<LocalDate> repeatDates;
    private final Calendar calendar;
    private String repeatString;

    private static final ArrayList<Tasks> allTasks;

    static {
        allTasks = new ArrayList<>();
    }

    public Tasks(String title, LocalDate startDate, boolean meeting, boolean repeat, LocalTime startTime,
                 LocalTime endTime, Calendar calendar, ArrayList<LocalDate> repeatDates, String repeatString) {
        this.title = title;
        this.startDate = startDate;
        this.meeting = meeting;
        this.repeat = repeat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatDates = repeatDates;
        this.calendar = calendar;
        this.repeatString = repeatString;

        allTasks.add(this);
    }

    public ArrayList<LocalDate> getRepeatDates() {
        return repeatDates;
    }

    public void setRepeatDates(ArrayList<LocalDate> newRepeatDates) {
        this.repeatDates = newRepeatDates;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isMeeting() {
        return meeting;
    }

    public void setMeeting(boolean meeting) {
        this.meeting = meeting;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Tasks getTasksByTitle(String title) {
        for (Tasks task : allTasks) {
            if (task.title.equals(title))
                return task;
        }
        return null;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public static void removeTask(String title) {
        allTasks.remove(Tasks.getTasksByTitle(title));
    }

    private String showTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH_mm");
        return time.format(formatter);
    }

    @Override
    public String toString() {
        if (this.meeting)
            return "Task: " + this.title + " T " + this.showTime(startTime) + " " + this.showTime(endTime);
        else
            return "Task: " + this.title + " F " + this.showTime(startTime) + " " + this.showTime(endTime);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getRepeatString() {
        return repeatString;
    }

    public void setRepeatString(String repeatString) {
        this.repeatString = repeatString;
    }
}
