package src.main.java.Reminder_story_19_20;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import src.main.java.Session;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Title      : ScheduleListController.java
 * Description: Controller for the reminder view.
 *              It loads user-defined reminders from CSV, auto-adds today's holiday reminder,
 *              polls VIP expiry status every 5 seconds, and displays up to 10 recent reminders
 *              in a styled ListView.
 *
 * @author Haoran Sun
 * @version 1.0
 * @author Yudian Wang
 * @version 1.1
 */
public class ScheduleListController {

    private final String currentUser = Session.getCurrentNickname();

    @FXML private ListView<ScheduleItem> scheduleList;

    private final ObservableList<ScheduleItem> reminders = FXCollections.observableArrayList();

    /** Used to prevent duplicate reminders (key = date#category) */
    private final Set<String> uniqueKeys = new HashSet<>();

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;
    private final Path CSV_PATH = Paths.get("data/" + currentUser + "_reminders.csv");

    /** Mapping of fixed holidays by month and day */
    private static final Map<MonthDay, String> HOLIDAYS = Map.ofEntries(
            Map.entry(MonthDay.of(1,  1),   "New Year's Day"),
            Map.entry(MonthDay.of(2, 14),   "Valentine's Day"),
            Map.entry(MonthDay.of(2, 17),   "Spring Festival"),
            Map.entry(MonthDay.of(3,  8),   "International Women's Day"),
            Map.entry(MonthDay.of(4,  1),   "April Fool's Day"),
            Map.entry(MonthDay.of(4,  4),   "Tomb-Sweeping Day"),
            Map.entry(MonthDay.of(5,  12),   "Test Day"),
            Map.entry(MonthDay.of(5,  1),   "Labour Day"),
            Map.entry(MonthDay.of(6,  1),   "Children's Day"),
            Map.entry(MonthDay.of(9, 10),   "Teacher's Day"),
            Map.entry(MonthDay.of(10, 1),   "National Day"),
            Map.entry(MonthDay.of(10,31),   "Halloween"),
            Map.entry(MonthDay.of(12,25),   "Christmas Day"),
            Map.entry(MonthDay.of(12,31),   "New Year's Eve")
    );

    /**
     * Called after FXML fields are injected.
     * Loads CSV and holiday data, configures the ListView cell factory,
     * displays the latest reminders, and starts the VIP expiry polling task.
     */
    @FXML
    public void initialize() {
        loadCsvAndHoliday();
        initListView();
        updateScheduleList();
        startScheduledTask();
    }

    /**
     * Appends a new reminder entry to the user's CSV file.
     *
     * @param date     the date of the reminder
     * @param category the category ("Holiday" or "VIP")
     * @param detail   the reminder detail text
     */
    private void appendCsv(LocalDate date, String category, String detail) {
        try (BufferedWriter bw = Files.newBufferedWriter(
                CSV_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(date.format(DTF) + "," + category + "," + detail);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads existing reminders from CSV and adds today's holiday reminder if applicable.
     */
    private void loadCsvAndHoliday() {
        // Load from CSV
        if (Files.exists(CSV_PATH)) {
            try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",", 3);
                    if (p.length < 3) {
                        p = new String[]{ p[0], "Custom", p[1] };
                    }
                    LocalDate d = LocalDate.parse(p[0], DTF);
                    String category = p[1];
                    String msg = p[2];

                    String key = d + "#" + category;
                    if (uniqueKeys.add(key)) {
                        String title = "VIP".equals(category)
                                ? "VIP Expiry Reminder"
                                : "Holiday".equals(category)
                                ? "Holiday Reminder"
                                : category;
                        reminders.add(new ScheduleItem(d, title, List.of(msg)));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Add today's holiday
        LocalDate today = LocalDate.now();
        MonthDay md = MonthDay.from(today);
        if (HOLIDAYS.containsKey(md)) {
            String key = today + "#Holiday";
            if (uniqueKeys.add(key)) {
                String festName = HOLIDAYS.get(md);
                String detail = "Today is " + festName + ". Remember to arrange the budget reasonably.";
                reminders.add(new ScheduleItem(today, "Holiday Reminder", List.of(detail)));
                appendCsv(today, "Holiday", detail);
            }
        }
    }

    /**
     * Starts a Timeline that runs every 5 seconds to check VIP expiry status.
     */
    private void startScheduledTask() {
        checkVipAndNotify();

        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(5),
                        e -> checkVipAndNotify())
        );
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }


    /**
     * Checks the current user's VIP expiry date; if expiring within 7 days,
     * creates a reminder, appends it to CSV, and updates the list view.
     */
    private void checkVipAndNotify() {
        Path userCsv = Paths.get("data/user.csv");
        if (!Files.exists(userCsv)) return;

        String expireStr = null;
        try (BufferedReader br = Files.newBufferedReader(userCsv)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 8
                        && p[1].trim().equals(currentUser)
                        && "VIP".equals(p[6])) {
                    expireStr = p[7];
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (expireStr == null || expireStr.isBlank()) return;

        LocalDate today = LocalDate.now();
        LocalDate expire;
        try {
            expire = LocalDate.parse(expireStr, DTF);
        } catch (DateTimeParseException ex) {
            return;
        }
        long daysLeft = ChronoUnit.DAYS.between(today, expire);
        if (daysLeft < 0 || daysLeft > 7) return;

        String key = today + "#VIP";
        if (uniqueKeys.add(key)) {
            String msg = "Your VIP membership will expire in " + daysLeft
                    + " day" + (daysLeft == 1 ? "" : "s")
                    + ". Please renew it as soon as possible.";
            reminders.add(new ScheduleItem(today, "VIP Expiry Reminder", List.of(msg)));
            appendCsv(today, "VIP", msg);
            updateScheduleList();
        }
    }

    /**
     * Configures the ListView to render ScheduleItem objects as styled cards
     * with header and body labels.
     */
    private void initListView() {
        scheduleList.getStyleClass().add("card-list");
        scheduleList.setCellFactory(lv -> new ListCell<>() {
            private final Label header = new Label();
            private final Label body   = new Label();
            private final VBox card    = new VBox(4, header, body);
            {
                card.getStyleClass().add("notification-card");
                header.getStyleClass().add("notification-header");
                body.getStyleClass().add("notification-body");
                card.setMaxWidth(Double.MAX_VALUE);
            }
            @Override
            protected void updateItem(ScheduleItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    String dateStr = item.getDate()
                            .format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH));
                    header.setText(dateStr + " " + item.getTitle());
                    String detail = item.getDetails().isEmpty() ? "" : item.getDetails().get(0);
                    body.setText("Remind: " + detail);
                    setGraphic(card);
                }
            }
        });
    }

    /**
     * Sorts reminders (VIP first, then by date descending),
     * selects up to the latest 10, and updates the ListView.
     */
    private void updateScheduleList() {
        reminders.sort(
                Comparator.comparing(ScheduleItem::getDate)
                        .reversed()
                        .thenComparing(ScheduleItem::getTitle)
        );

        List<ScheduleItem> latest10 = reminders.size() > 10
                ? reminders.subList(0, 10)
                : new ArrayList<>(reminders);

        scheduleList.setItems(FXCollections.observableArrayList(latest10));
    }

}
