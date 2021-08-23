package models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Events {
    private String title;
    private final LocalDate startDate;
    private boolean meeting;
    private boolean repeat;
    private ArrayList<LocalDate> repeatDates;
    private final Calendar calendar;
    private String repeatString;

    private static final ArrayList<Events> allEvents;

    static {
        allEvents = new ArrayList<>();
    }

    public Events(String title, LocalDate startDate, boolean meeting, boolean repeat, Calendar calendar, ArrayList<LocalDate> repeatDates, String repeatString) {
        this.title = title;
        this.startDate = startDate;
        this.meeting = meeting;
        this.repeat = repeat;
        this.repeatDates = repeatDates;
        this.calendar = calendar;
        this.repeatString = repeatString;

        allEvents.add(this);
    }

    public ArrayList<LocalDate> getRepeatDates() {
        return repeatDates;
    }

    public void setRepeatDates(ArrayList<LocalDate> newRepeat) {
        this.repeatDates = newRepeat;
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

    public static Events getEventsByTitle(String title) {
        for (Events event : allEvents) {
            if (event.title.equals(title))
                return event;
        }
        return null;
    }

    public static void removeEvent(String title) {
        allEvents.remove(Events.getEventsByTitle(title));
    }

    @Override
    public String toString() {
        if (this.meeting)
            return "Event: " + this.title + " T";
        else
            return "Event: " + this.title + " F";
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
