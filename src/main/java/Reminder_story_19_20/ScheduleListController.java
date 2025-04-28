package src.main.java.Reminder_story_19_20;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import src.main.java.Session;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class ScheduleListController {

    private final String currentUser = Session.getCurrentNickname();
    @FXML
    private ListView<ScheduleItem> scheduleList;

    private final ObservableList<ScheduleItem> reminders = FXCollections.observableArrayList();
    private final Path CSV_PATH = Paths.get("data/" + currentUser + "_reminders.csv");
    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;

    @FXML
    public void initialize() {
        // 1. 法定假日映射
        Map<MonthDay, String> holidays = Map.ofEntries(
                Map.entry(MonthDay.of(1,  1),  "New Year's Day"),           // 元旦
                Map.entry(MonthDay.of(2, 17),  "Spring Festival"),      // 春节
                Map.entry(MonthDay.of(2, 14),  "Valentine's Day"),          // 情人节
                Map.entry(MonthDay.of(3,  8),  "International Women's Day"),// 妇女节
                Map.entry(MonthDay.of(4,  1),  "April Fool's Day"),         // 愚人节
                Map.entry(MonthDay.of(4, 4),  "Tomb-Sweeping Day"),                // 清明节
                Map.entry(MonthDay.of(4, 28),  "Test Day"),                // 测试节
                Map.entry(MonthDay.of(5,  1),  "Labour Day"),                  // 国际劳动节
                Map.entry(MonthDay.of(6,  1),  "Children's Day"),            // 儿童节
                Map.entry(MonthDay.of(9, 10),  "Teacher's Day"),             // 教师节
                Map.entry(MonthDay.of(10,31),  "Halloween"),                 // 万圣节
                Map.entry(MonthDay.of(10, 1),  "National Day"),              // 国庆节
                Map.entry(MonthDay.of(12,25),  "Christmas Day"),             // 圣诞节
                Map.entry(MonthDay.of(12,31),  "New Year's Eve")
        );

        // 2. 先从 CSV 里读“自定义提醒” —— CSV 格式：日期,消息
        if (Files.exists(CSV_PATH)) {
            try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length < 2) continue;
                    LocalDate date = LocalDate.parse(parts[0], DTF);
                    String detailMsg = parts[1];
                    String festival = holidays.getOrDefault(MonthDay.from(date), "");
                    reminders.add(new ScheduleItem(
                            date,
                            festival,
                            List.of(detailMsg)
                    ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 3. 如果今天是法定假日，而且列表里还没这条，就自动追加，并写进 CSV
        LocalDate today = LocalDate.now();
        MonthDay md = MonthDay.from(today);
        if (holidays.containsKey(md)
                && reminders.stream().noneMatch(it -> it.getDate().equals(today))) {
            String festName = holidays.get(md);
            String detail = String.format("Today is %s. Remember to arrange the budget reasonably.", festName);
            // 写入集合
            reminders.add(new ScheduleItem(
                    today,
                    festName,
                    List.of(detail)
            ));
            // 追加写到 CSV
            try (BufferedWriter bw = Files.newBufferedWriter(
                    CSV_PATH,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                bw.write(today.format(DTF) + "," + detail);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 4. 按日期倒序、取最新 10 条
        reminders.sort(Comparator.comparing(ScheduleItem::getDate).reversed());
        List<ScheduleItem> recent10 = reminders.size() > 10
                ? reminders.subList(0, 10)
                : new ArrayList<>(reminders);

        // 5. 绑定到 ListView
        scheduleList.setItems(FXCollections.observableArrayList(recent10));

        scheduleList.getStyleClass().add("card-list");  // ① 确保 ListView 有这个 styleClass

        scheduleList.setCellFactory(lv -> new ListCell<ScheduleItem>() {
            private final Label headerLbl = new Label();
            private final Label bodyLbl = new Label();
            private final VBox card = new VBox(4, headerLbl, bodyLbl);

            {
                // 要用我们 CSS 里定义的类名
                card.getStyleClass().add("notification-card");
                headerLbl.getStyleClass().add("notification-header");
                bodyLbl.getStyleClass().add("notification-body");
                card.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(ScheduleItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    // —— 只拿节日名作为 title，不要把消息放这里
                    String fest = item.getTitle();  // “Labor Day”
                    // —— 短日期，不要年份，英文缩写月
                    String dateStr = item.getDate()
                            .format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH));
                    // 1) 第一行：Apr 28 Labor Day
                    headerLbl.setText(dateStr + " " + fest);

                    // 2) 第二行：Remind: 后面跟 details（List 里应该只放「消息正文」）
                    String detail = item.getDetails().isEmpty()
                            ? ""
                            : item.getDetails().get(0);
                    bodyLbl.setText("Remind: " + detail);

                    setGraphic(card);
                }
            }
        });

    }
}
