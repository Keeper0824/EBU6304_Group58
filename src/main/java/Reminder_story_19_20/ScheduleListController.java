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

public class ScheduleListController {

    /* ---------- 基本字段 ---------- */

    private final String currentUser = Session.getCurrentNickname();
    @FXML private ListView<ScheduleItem> scheduleList;

    private final ObservableList<ScheduleItem> reminders = FXCollections.observableArrayList();

    /** 用于去重：key = date#category */
    private final Set<String> uniqueKeys = new HashSet<>();

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;
    private final Path CSV_PATH = Paths.get("data/" + currentUser + "_reminders.csv");

    /** 节日映射 */
    private static final Map<MonthDay, String> HOLIDAYS = Map.ofEntries(
            Map.entry(MonthDay.of(1,  1),   "New Year's Day"),
            Map.entry(MonthDay.of(2, 14),   "Valentine's Day"),
            Map.entry(MonthDay.of(2, 17),   "Spring Festival"),
            Map.entry(MonthDay.of(3,  8),   "International Women's Day"),
            Map.entry(MonthDay.of(4,  1),   "April Fool's Day"),
            Map.entry(MonthDay.of(4,  4),   "Tomb-Sweeping Day"),
            Map.entry(MonthDay.of(5,  9),   "Test Day"),
            Map.entry(MonthDay.of(5,  1),   "Labour Day"),
            Map.entry(MonthDay.of(6,  1),   "Children's Day"),
            Map.entry(MonthDay.of(9, 10),   "Teacher's Day"),
            Map.entry(MonthDay.of(10, 1),   "National Day"),
            Map.entry(MonthDay.of(10,31),   "Halloween"),
            Map.entry(MonthDay.of(12,25),   "Christmas Day"),
            Map.entry(MonthDay.of(12,31),   "New Year's Eve")
    );

    /* ---------- 生命周期 ---------- */

    @FXML
    public void initialize() {
        loadCsvAndHoliday();   // 一次性加载历史 + 今日节日
        initListView();        // 配置 CellFactory
        updateScheduleList();  // 显示前 10 条
        startScheduledTask();  // 5s 轮询 VIP 到期
    }

    /* ---------- CSV 读写 ---------- */

    /** 追加一条到 CSV（三列：date,category,detail） */
    private void appendCsv(LocalDate date, String category, String detail) {
        try (BufferedWriter bw = Files.newBufferedWriter(
                CSV_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(date.format(DTF) + "," + category + "," + detail);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 初始读取 CSV & 补今天节日 */
    private void loadCsvAndHoliday() {
        // 1. 读取现有 CSV
        if (Files.exists(CSV_PATH)) {
            try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",", 3);  // 三列：date,category,detail
                    if (p.length < 3) {               // 两列格式兼容
                        p = new String[]{ p[0], "Custom", p[1] };
                    }
                    LocalDate d      = LocalDate.parse(p[0], DTF);
                    String   category = p[1];
                    String   msg      = p[2];

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

        // 2. 补今天节日（标题固定“Holiday Reminder”）
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

    /* ---------- VIP 到期轮询 ---------- */

    private void startScheduledTask() {
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(5), e -> checkVipAndNotify()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

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

    /* ---------- ListView 渲染 ---------- */

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

    /* ---------- 排序 & 刷新 ---------- */

    private void updateScheduleList() {
        reminders.sort(
                Comparator.<ScheduleItem, Boolean>comparing(it -> !"VIP Expiry Reminder".equals(it.getTitle()))
                        .thenComparing(ScheduleItem::getDate, Comparator.reverseOrder())
        );
        List<ScheduleItem> latest10 = reminders.size() > 10
                ? reminders.subList(0, 10)
                : new ArrayList<>(reminders);
        scheduleList.setItems(FXCollections.observableArrayList(latest10));
    }
}



//package src.main.java.Reminder_story_19_20;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.collections.*;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.util.Duration;
//import src.main.java.Session;
//
//import java.io.*;
//import java.nio.file.*;
//import java.time.*;
//import java.time.format.*;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//
//public class ScheduleListController {
//
//    /* ---------- 常量 & 字段 ---------- */
//
//    private final String currentUser = Session.getCurrentNickname();
//
//    @FXML
//    private ListView<ScheduleItem> scheduleList;
//
//    private final ObservableList<ScheduleItem> reminders = FXCollections.observableArrayList();
//    private final Set<String> pushedReminders = new HashSet<>();          // 防重复
//    private final Path CSV_PATH = Paths.get("data/" + currentUser + "_reminders.csv");
//    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;
//
//    /** 法定假日映射 */
//    private static final Map<MonthDay, String> HOLIDAYS = Map.ofEntries(
//            Map.entry(MonthDay.of(1, 1),   "New Year's Day"),
//            Map.entry(MonthDay.of(2, 14),  "Valentine's Day"),
//            Map.entry(MonthDay.of(2, 17),  "Spring Festival"),
//            Map.entry(MonthDay.of(3, 8),   "International Women's Day"),
//            Map.entry(MonthDay.of(4, 1),   "April Fool's Day"),
//            Map.entry(MonthDay.of(4, 4),   "Tomb‑Sweeping Day"),
//            Map.entry(MonthDay.of(5, 9),  "Test Day"),
//            Map.entry(MonthDay.of(5, 1),   "Labour Day"),
//            Map.entry(MonthDay.of(6, 1),   "Children's Day"),
//            Map.entry(MonthDay.of(9, 10),  "Teacher's Day"),
//            Map.entry(MonthDay.of(10, 1),  "National Day"),
//            Map.entry(MonthDay.of(10, 31), "Halloween"),
//            Map.entry(MonthDay.of(12, 25), "Christmas Day"),
//            Map.entry(MonthDay.of(12, 31), "New Year's Eve")
//    );
//
//    /* ---------- 生命周期 ---------- */
//
//    @FXML
//    public void initialize() {
//        loadHolidayAndCustomReminders();   // 第一次渲染
//        initListView();                    // 配置 cellFactory
//        updateScheduleList();              // 刷到界面
//        startScheduledTask();              // 开始 5 s 巡逻
//    }
//
//    /* ---------- 初始化数据 ---------- */
//
//    /** 读取 CSV＋补今天的节日提醒 */
//    private void loadHolidayAndCustomReminders() {
//
//        /* 1. 读取 CSV（用户自定义提醒） */
//        if (Files.exists(CSV_PATH)) {
//            try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    String[] parts = line.split(",", 2);
//                    if (parts.length < 2) continue;
//                    LocalDate date = LocalDate.parse(parts[0], DTF);
//                    String detailMsg = parts[1];
//                    String fest = HOLIDAYS.getOrDefault(MonthDay.from(date), "");
//                    reminders.add(new ScheduleItem(date, fest, List.of(detailMsg)));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        /* 2. 如果今天是节日且列表里没这条，则追加并写回 CSV */
//        LocalDate today = LocalDate.now();
//        MonthDay md = MonthDay.from(today);
//        if (HOLIDAYS.containsKey(md)
//                && reminders.stream().noneMatch(it -> it.getDate().equals(today) &&
//                "Holiday Reminder".equals(it.getTitle()))) {
//
//            String festName = HOLIDAYS.get(md);
//            String detail = "Today is " + festName + ". Remember to arrange the budget reasonably.";
//
//            reminders.add(new ScheduleItem(today, "Holiday Reminder", List.of(detail)));
//
//            try (BufferedWriter bw = Files.newBufferedWriter(
//                    CSV_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
//                bw.write(today.format(DTF) + "," + detail);
//                bw.newLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /* ---------- 定时检查 VIP ---------- */
//
//    /** 每 5 s 扫一遍 VIP 到期 */
//    private void startScheduledTask() {
//        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(5), e -> checkVIPStatusAndNotify()));
//        tl.setCycleCount(Timeline.INDEFINITE);
//        tl.play();
//    }
//
//    private void checkVIPStatusAndNotify() {
//
//        Path userCSV = Paths.get("data/user.csv");
//        if (!Files.exists(userCSV)) return;
//
//        LocalDate today = LocalDate.now();
//        boolean isVIP = false;
//        String expireDateStr = "";
//
//        try (BufferedReader br = Files.newBufferedReader(userCSV)) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] p = line.split(",");
//                if (p.length >= 8 && p[1].trim().equals(currentUser)) {  // 昵称在第 2 列
//                    if ("VIP".equals(p[6])) {                            // 状态在第 7 列
//                        isVIP = true;
//                        expireDateStr = p[7];                            // 到期在第 8 列
//                    }
//                    break;
//                }
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        if (isVIP && !expireDateStr.isBlank()) {
//            try {
//                LocalDate expire = LocalDate.parse(expireDateStr, DTF);
//                long daysLeft = ChronoUnit.DAYS.between(today, expire);
//
//                if (daysLeft >= 0 && daysLeft <= 7) {
//                    String vipMsg = "Your VIP membership will expire in " + daysLeft +
//                            " day" + (daysLeft == 1 ? "" : "s") +
//                            ". Please renew it as soon as possible.";
//                    String id = currentUser + "-VIP-" + expireDateStr;
//
//                    if (pushedReminders.add(id)) {                      // 保证只推一次
//                        reminders.add(new ScheduleItem(today, "VIP Expiry Reminder", List.of(vipMsg)));
//
//                        try (BufferedWriter bw = Files.newBufferedWriter(
//                                CSV_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
//                            bw.write(today.format(DTF) + "," + vipMsg);
//                            bw.newLine();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        updateScheduleList(); // 立即刷新
//                    }
//                }
//            } catch (DateTimeParseException ignored) { }
//        }
//    }
//
//    /* ---------- ListView 渲染 ---------- */
//
//    private void initListView() {
//        scheduleList.getStyleClass().add("card-list");
//
//        scheduleList.setCellFactory(lv -> new ListCell<>() {
//
//            private final Label header = new Label();
//            private final Label body   = new Label();
//            private final VBox card    = new VBox(4, header, body);
//
//            {
//                card.getStyleClass().add("notification-card");
//                header.getStyleClass().add("notification-header");
//                body.getStyleClass().add("notification-body");
//                card.setMaxWidth(Double.MAX_VALUE);
//            }
//
//            @Override
//            protected void updateItem(ScheduleItem item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setGraphic(null);
//                } else {
//                    String dateStr = item.getDate()
//                            .format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH));
//                    header.setText(dateStr + " " + item.getTitle());
//
//                    String detail = item.getDetails().isEmpty() ? "" : item.getDetails().get(0);
//                    body.setText("Remind: " + detail);
//
//                    setGraphic(card);
//                }
//            }
//        });
//    }
//
//    /** 刷新并取最近 10 条，VIP 排首，日期倒序 */
//    private void updateScheduleList() {
//        reminders.sort(
//                Comparator.<ScheduleItem, Boolean>comparing(it -> !"VIP Expiry Reminder".equals(it.getTitle()))
//                        .thenComparing(ScheduleItem::getDate, Comparator.reverseOrder())
//        );
//
//        List<ScheduleItem> latest10 = reminders.size() > 10
//                ? reminders.subList(0, 10)
//                : new ArrayList<>(reminders);
//
//        scheduleList.setItems(FXCollections.observableArrayList(latest10));
//    }
//}


//package src.main.java.Reminder_story_19_20;
//
//import javafx.collections.*;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import src.main.java.Session;
//
//import java.io.*;
//import java.nio.file.*;
//import java.time.*;
//import java.time.format.*;
//import java.util.*;
//
//public class ScheduleListController {
//
//    private final String currentUser = Session.getCurrentNickname();
//    @FXML
//    private ListView<ScheduleItem> scheduleList;
//
//    private final ObservableList<ScheduleItem> reminders = FXCollections.observableArrayList();
//    private final Path CSV_PATH = Paths.get("data/" + currentUser + "_reminders.csv");
//    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;
//
//    @FXML
//    public void initialize() {
//        // 1. 法定假日映射
//        Map<MonthDay, String> holidays = Map.ofEntries(
//                Map.entry(MonthDay.of(1,  1),  "New Year's Day"),           // 元旦
//                Map.entry(MonthDay.of(2, 17),  "Spring Festival"),      // 春节
//                Map.entry(MonthDay.of(2, 14),  "Valentine's Day"),          // 情人节
//                Map.entry(MonthDay.of(3,  8),  "International Women's Day"),// 妇女节
//                Map.entry(MonthDay.of(4,  1),  "April Fool's Day"),         // 愚人节
//                Map.entry(MonthDay.of(4, 4),  "Tomb-Sweeping Day"),                // 清明节
//                Map.entry(MonthDay.of(4, 29),  "Test Day"),                // 测试节
//                Map.entry(MonthDay.of(5,  1),  "Labour Day"),                  // 国际劳动节
//                Map.entry(MonthDay.of(6,  1),  "Children's Day"),            // 儿童节
//                Map.entry(MonthDay.of(9, 10),  "Teacher's Day"),             // 教师节
//                Map.entry(MonthDay.of(10,31),  "Halloween"),                 // 万圣节
//                Map.entry(MonthDay.of(10, 1),  "National Day"),              // 国庆节
//                Map.entry(MonthDay.of(12,25),  "Christmas Day"),             // 圣诞节
//                Map.entry(MonthDay.of(12,31),  "New Year's Eve")
//        );
//
//        // 2. 先从 CSV 里读“自定义提醒” —— CSV 格式：日期,消息
//        if (Files.exists(CSV_PATH)) {
//            try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    String[] parts = line.split(",", 2);
//                    if (parts.length < 2) continue;
//                    LocalDate date = LocalDate.parse(parts[0], DTF);
//                    String detailMsg = parts[1];
//                    String festival = holidays.getOrDefault(MonthDay.from(date), "");
//                    reminders.add(new ScheduleItem(
//                            date,
//                            festival,
//                            List.of(detailMsg)
//                    ));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 3. 如果今天是法定假日，而且列表里还没这条，就自动追加，并写进 CSV
//        LocalDate today = LocalDate.now();
//        MonthDay md = MonthDay.from(today);
//        if (holidays.containsKey(md)
//                && reminders.stream().noneMatch(it -> it.getDate().equals(today))) {
//            String festName = holidays.get(md);
//            String detail = String.format("Today is %s. Remember to arrange the budget reasonably.", festName);
//            // 写入集合
//            reminders.add(new ScheduleItem(
//                    today,
//                    festName,
//                    List.of(detail)
//            ));
//            // 追加写到 CSV
//            try (BufferedWriter bw = Files.newBufferedWriter(
//                    CSV_PATH,
//                    StandardOpenOption.CREATE,
//                    StandardOpenOption.APPEND)) {
//                bw.write(today.format(DTF) + "," + detail);
//                bw.newLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 4. 按日期倒序、取最新 10 条
//        reminders.sort(Comparator.comparing(ScheduleItem::getDate).reversed());
//        List<ScheduleItem> recent10 = reminders.size() > 10
//                ? reminders.subList(0, 10)
//                : new ArrayList<>(reminders);
//
//        // 5. 绑定到 ListView
//        scheduleList.setItems(FXCollections.observableArrayList(recent10));
//
//        scheduleList.getStyleClass().add("card-list");  // ① 确保 ListView 有这个 styleClass
//
//        scheduleList.setCellFactory(lv -> new ListCell<ScheduleItem>() {
//            private final Label headerLbl = new Label();
//            private final Label bodyLbl = new Label();
//            private final VBox card = new VBox(4, headerLbl, bodyLbl);
//
//            {
//                // 要用我们 CSS 里定义的类名
//                card.getStyleClass().add("notification-card");
//                headerLbl.getStyleClass().add("notification-header");
//                bodyLbl.getStyleClass().add("notification-body");
//                card.setMaxWidth(Double.MAX_VALUE);
//            }
//
//            @Override
//            protected void updateItem(ScheduleItem item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setGraphic(null);
//                } else {
//                    // —— 只拿节日名作为 title，不要把消息放这里
//                    String fest = item.getTitle();  // “Labor Day”
//                    // —— 短日期，不要年份，英文缩写月
//                    String dateStr = item.getDate()
//                            .format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH));
//                    // 1) 第一行：Apr 28 Labor Day
//                    headerLbl.setText(dateStr + " " + fest);
//
//                    // 2) 第二行：Remind: 后面跟 details（List 里应该只放「消息正文」）
//                    String detail = item.getDetails().isEmpty()
//                            ? ""
//                            : item.getDetails().get(0);
//                    bodyLbl.setText("Remind: " + detail);
//
//                    setGraphic(card);
//                }
//            }
//        });
//
//    }
//}
