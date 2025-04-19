package src.main.java.financial_story9;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Map<YearMonth, Double> monthlyIncome = new HashMap<>();
    private final Map<YearMonth, Double> monthlyExpense = new HashMap<>();
    private final Map<String, Double> categoryExpenses = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load data
            loadData("data/transactions.csv");

            // Create main layout
            VBox root = new VBox(20);

            // Create charts
            PieChart pieChart = createPieChart();
            BarChart<String, Number> barChart = createBarChart();

            root.getChildren().addAll(pieChart, barChart);

            // Set up scene with 1600x900 resolution
            Scene scene = new Scene(root, 1600, 900);
            primaryStage.setTitle("Financial Analysis System");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            showErrorDialog("Data Load Error", e.getMessage());
        }
    }

    private void loadData(String filePath) throws IOException {
        Path path = Path.of(filePath);
        Files.readAllLines(path).forEach(line -> {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                try {
                    LocalDate date = LocalDate.parse(parts[0].trim(), DATE_FORMATTER);
                    double amount = Double.parseDouble(parts[1].trim());
                    String category = parts[2].trim();
                    YearMonth month = YearMonth.from(date);

                    if (amount > 0) {
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

    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Expense Category Distribution");
        
        categoryExpenses.forEach((category, amount) ->
            pieChart.getData().add(new PieChart.Data(category + " (" + amount + ")", amount))
        );
        
        return pieChart;
    }

    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");
        
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Monthly Income vs. Expense Comparison");
        
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");
        
        monthlyIncome.keySet().stream()
            .sorted()
            .forEach(month -> {
                String monthLabel = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                incomeSeries.getData().add(new XYChart.Data<>(monthLabel, monthlyIncome.get(month)));
                expenseSeries.getData().add(new XYChart.Data<>(monthLabel, monthlyExpense.getOrDefault(month, 0.0)));
            });
        
        barChart.getData().addAll(incomeSeries, expenseSeries);
        return barChart;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}