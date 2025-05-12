package src.main.java.Reminder_story_19_20;

import java.time.LocalDate;
import java.util.List;

/**
 * Title      : ScheduleItem.java
 * Description: Models a reminder item in the schedule list. Each item has a date,
 *              a title, optional details lines, and a completion status.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class ScheduleItem {
    private final LocalDate date;
    private final String title;
    private final List<String> details;
    private boolean done;

    /**
     * Constructs a new ScheduleItem.
     *
     * @param date    the date of the reminder
     * @param title   the title or summary of the reminder
     * @param details a list of detail strings for the reminder
     */
    public ScheduleItem(LocalDate date, String title, List<String> details) {
        this.date = date;
        this.title = title;
        this.details = details;
        this.done = false;
    }

    /**
     * Gets the date of this reminder item.
     *
     * @return the LocalDate when this item is scheduled
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the title of this reminder item.
     *
     * @return the title string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the detail lines associated with this reminder item.
     *
     * @return a List of detail strings
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * Checks whether this reminder item has been marked as done.
     *
     * @return true if completed; false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Sets the completion status of this reminder item.
     *
     * @param done true to mark as completed; false to mark as pending
     */
    public void setDone(boolean done) {
        this.done = done;
    }
}
