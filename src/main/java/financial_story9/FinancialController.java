package src.main.java.financial_story9;


import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import src.main.java.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FinancialController {
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private final static String currentUser = Session.getCurrentNickname();
    private final static String currentUser ="Keeper";
    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private final Map<YearMonth, Double> monthlyIncome = new HashMap<>();
    private final Map<YearMonth, Double> monthlyExpense = new HashMap<>();
    private final Map<String, Double> categoryExpenses = new HashMap<>();

    @FXML
    public void initialize() {
        try {
            loadData("data/"+currentUser+"_transaction.csv");
            populateCharts();
        } catch (IOException e) {
            showErrorDialog("Data Load Error", e.getMessage());
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
        // 填充饼图
        categoryExpenses.forEach((category, amount) ->
                pieChart.getData().add(new PieChart.Data(category + " (" + amount + ")", amount))
        );

        // 配置柱状图
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        monthlyIncome.keySet().stream()
                .sorted()
                .forEach(month -> {
                    String monthLabel = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    incomeSeries.getData().add(new XYChart.Data<>(monthLabel, monthlyIncome.get(month)));
                    expenseSeries.getData().add(new XYChart.Data<>(monthLabel,
                            monthlyExpense.getOrDefault(month, 0.0)));
                });

        barChart.getData().addAll(incomeSeries, expenseSeries);
        yAxis.setAutoRanging(true); // 启用自动范围调整
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}