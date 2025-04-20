package src.main.java.financial_story9;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
        import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
            // Load transaction data
            loadData("data/transaction.csv");

            // Create main layout with background
            VBox root = new VBox(20);
            setBackground(root);

            // Create and configure charts
            PieChart pieChart = createPieChart();
            BarChart<String, Number> barChart = createBarChart();

            // Add charts to layout
            root.getChildren().addAll(pieChart, barChart);

            // Configure scene and stage
            Scene scene = new Scene(root, 1600, 900);
            primaryStage.setTitle("Financial Analysis System");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            showErrorDialog("Data Load Error", e.getMessage());
        }
    }

    private void setBackground(VBox root) {
        try {
            // Load background image (ensure correct path)
            Image backgroundImage = new Image(getClass().getResourceAsStream("background.png"));

            // Configure background sizing
            BackgroundSize backgroundSize = new BackgroundSize(
                    100, 100, true, true, true, true);

            // Create background image
            BackgroundImage background = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    backgroundSize
            );

            // Apply background to root pane
            root.setBackground(new Background(background));

            // Set chart transparency
            root.setStyle("-fx-background-color: transparent;");

        } catch (Exception e) {
            showErrorDialog("Background Error", "Failed to load background: " + e.getMessage());
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
                    String IOType = parts[5].trim();
                    YearMonth month = YearMonth.from(date);

                    if (IOType.equals("income")) {
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
        pieChart.setStyle("-fx-background-color: rgba(255,255,255,0.7);");

        categoryExpenses.forEach((category, amount) ->
                pieChart.getData().add(new PieChart.Data(
                        String.format("%s (¥%.2f)", category, amount),
                        amount))
        );

        return pieChart;
    }

    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (CNY)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Monthly Income vs. Expense Comparison");
        barChart.setStyle("-fx-background-color: rgba(255,255,255,0.7);");

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