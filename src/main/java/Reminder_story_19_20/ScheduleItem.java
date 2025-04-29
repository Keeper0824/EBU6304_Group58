package src.main.java.Reminder_story_19_20;

import java.time.LocalDate;
import java.util.List;

public class ScheduleItem {
    private final LocalDate date;
    private final String title;
    private final List<String> details;
    private boolean done;

    public ScheduleItem(LocalDate date, String title, List<String> details) {
        this.date = date;
        this.title = title;
        this.details = details;
        this.done = false;
    }

    public LocalDate getDate() { return date; }
    public String getTitle() { return title; }
    public List<String> getDetails() { return details; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}

