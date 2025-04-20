package src.main.java.financial_story9;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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
    private final static String currentUser = Session.getCurrentNickname();

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private Button returnButton;

    private final Map<YearMonth, Double> monthlyIncome = new HashMap<>();
    private final Map<YearMonth, Double> monthlyExpense = new HashMap<>();
    private final Map<String, Double> categoryExpenses = new HashMap<>();

    @FXML
    public void initialize() {
        try {
            loadData("data/" + currentUser + "_transaction.csv");
            populateCharts();
            setupReturnButton();
        } catch (IOException e) {
            showErrorDialog("Data Load Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupReturnButton() {
        returnButton.setOnAction(e -> {
            try {
                // Close current window
                Stage currentStage = (Stage) returnButton.getScene().getWindow();
                currentStage.close();

                // Load the cash flow view (ui.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/CashFlowVisualization_story15/ui.fxml"));
                Parent root = loader.load();

                // Create new stage for cash flow view
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1600, 1100));
                stage.setTitle("Cash Flow Visualization");
                stage.show();
            } catch (IOException ex) {
                showErrorDialog("Navigation Error", "Failed to load cash flow view: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
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
        // Clear existing data
        pieChart.getData().clear();
        barChart.getData().clear();

        // Populate pie chart with expense categories
        categoryExpenses.forEach((category, amount) ->
                pieChart.getData().add(new PieChart.Data(category + " (" + amount + ")", amount))
        );

        // Configure bar chart series
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        // Add data to bar chart series
        monthlyIncome.keySet().stream()
                .sorted()
                .forEach(month -> {
                    String monthLabel = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    incomeSeries.getData().add(new XYChart.Data<>(monthLabel, monthlyIncome.get(month)));
                    expenseSeries.getData().add(new XYChart.Data<>(monthLabel,
                            monthlyExpense.getOrDefault(month, 0.0)));
                });

        // Add series to bar chart
        barChart.getData().addAll(incomeSeries, expenseSeries);
        yAxis.setAutoRanging(true);
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}