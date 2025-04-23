package src.main.java.financial_story9;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import src.main.java.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;

public class FinancialController {
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static String currentUser = Session.getCurrentNickname();

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private AnchorPane rootPane;

    private final Map<YearMonth, Double> monthlyIncome = new HashMap<>();
    private final Map<YearMonth, Double> monthlyExpense = new HashMap<>();
    private final Map<String, Double> categoryExpenses = new HashMap<>();

    @FXML
    public void initialize() {
        try {
            loadData("data/" + currentUser + "_transaction.csv");
            populateCharts();
            beautifyUI();
        } catch (IOException e) {
            showErrorDialog("数据加载错误", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadData(String filePath) throws IOException {
        Path path = Path.of(filePath);
        Files.readAllLines(path).forEach(line -> {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                try {
                    LocalDate date = LocalDate.parse(parts[4].trim(), DATE_FORMATTER);
                    double amount = Double.parseDouble(parts[2].trim());
                    String category = parts[3].trim();
                    YearMonth month = YearMonth.from(date);

                    if (parts[5].trim().equals("income")) {
                        monthlyIncome.merge(month, amount, Double::sum);
                    } else {
                        double expense = Math.abs(amount);
                        monthlyExpense.merge(month, expense, Double::sum);
                        categoryExpenses.merge(category, expense, Double::sum);
                    }
                } catch (Exception e) {
                    System.err.println("Invalid data line: " + line);
                }
            }
        });
    }

    private void populateCharts() {
        pieChart.getData().clear();
        barChart.getData().clear();

        // 设置图表字体
        pieChart.setStyle("-fx-font-size: 20px;");
        barChart.setStyle("-fx-font-size: 20px;");

        // 饼图数据
        categoryExpenses.forEach((category, amount) ->
                pieChart.getData().add(new PieChart.Data(category + " (" + amount + ")", amount))
        );

        // 柱状图数据系列
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        monthlyIncome.keySet().stream().sorted().forEach(month -> {
            String label = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            incomeSeries.getData().add(new XYChart.Data<>(label, monthlyIncome.get(month)));
            expenseSeries.getData().add(new XYChart.Data<>(label, monthlyExpense.getOrDefault(month, 0.0)));
        });

        barChart.getData().addAll(incomeSeries, expenseSeries);
        yAxis.setAutoRanging(true);

        // 设置坐标轴字体
        xAxis.setStyle("-fx-tick-label-font-size: 16px; -fx-label-padding: 10;");
        yAxis.setStyle("-fx-tick-label-font-size: 16px; -fx-label-padding: 10;");

        // 设置图例字体
        barChart.lookupAll(".chart-legend").forEach(legend ->
                legend.setStyle("-fx-font-size: 16px;")
        );
        pieChart.lookupAll(".chart-legend").forEach(legend ->
                legend.setStyle("-fx-font-size: 16px;")
        );

        // ✅ 设置柱子间距，使其居中对齐
        barChart.setCategoryGap(20);  // 分组之间的间距
        barChart.setBarGap(5);        // 同组柱子之间的间距
    }

    private void beautifyUI() {
        if (rootPane != null) {
            Stop[] stops = new Stop[] {
                    new Stop(0, Color.web("#d0eaff")),
                    new Stop(1, Color.web("#f3e5f5"))
            };
            LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);

            rootPane.setBackground(new Background(new BackgroundFill(
                    gradient, CornerRadii.EMPTY, Insets.EMPTY
            )));
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
