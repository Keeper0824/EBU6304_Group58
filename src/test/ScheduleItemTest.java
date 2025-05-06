package src.test;

import org.junit.jupiter.api.Test;
import src.main.java.Reminder_story_19_20.ScheduleItem;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class ScheduleItemTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 5, 1);
        String title = "Labour Day";
        List<String> details = Arrays.asList("One day off", "Buy gifts");

        // Act
        ScheduleItem item = new ScheduleItem(date, title, details);

        // Assert
        assertEquals(date, item.getDate(),    "getDate() should return the date passed to the constructor");
        assertEquals(title, item.getTitle(),  "getTitle() should return the title passed to the constructor");
        assertEquals(details, item.getDetails(), "getDetails() should return the details list passed to the constructor");
        assertFalse(item.isDone(),             "Newly created ScheduleItem should not be marked done");
    }

    @Test
    void testSetDone() {
        // Arrange
        ScheduleItem item = new ScheduleItem(
                LocalDate.now(),
                "Test Event",
                List.of("Detail")
        );

        // Precondition
        assertFalse(item.isDone(), "Initially, isDone() should be false");

        // Act & Assert
        item.setDone(true);
        assertTrue(item.isDone(),  "After setDone(true), isDone() should be true");

        item.setDone(false);
        assertFalse(item.isDone(), "After setDone(false), isDone() should revert to false");
    }
}

