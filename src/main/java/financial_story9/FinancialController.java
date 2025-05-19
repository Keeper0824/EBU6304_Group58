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

/**
 * Title      : FinancialController.java
 * Description: Controller class for handling financial data visualization.
 *              Loads transaction data, processes it, and populates charts (pie and bar) with the data.
 *              Also beautifies the user interface and handles errors.
 *
 * @author Yudian Wang
 * @version 1.0
 */
public class FinancialController {
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String currentUser = Session.getCurrentNickname();

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private AnchorPane rootPane;

    private final Map<YearMonth, Double> monthlyIncome = new HashMap<>();
    private final Map<YearMonth, Double> monthlyExpense = new HashMap<>();
    private final Map<String, Double> categoryExpenses = new HashMap<>();

    /**
     * Initializes the controller by loading data and populating the charts.
     * Also beautifies the UI by setting a gradient background.
     */
    @FXML
    public void initialize() {
        try {
            loadData("data/" + currentUser + "_transaction.csv"); // Load transaction data from a CSV file
            populateCharts(); // Populate the pie and bar charts with data
            beautifyUI(); // Beautify the user interface with a gradient background
        } catch (IOException e) {
            showErrorDialog("Data Loading Error", e.getMessage()); // Show error dialog if data loading fails
            e.printStackTrace();
        }
    }

    /**
     * Loads transaction data from a CSV file and processes it.
     * The data includes income, expense, category, and date information.
     *
     * @param filePath The file path of the CSV data.
     * @throws IOException If an error occurs while reading the file.
     */
    private void loadData(String filePath) throws IOException {
        Path path = Path.of(filePath);
        Files.readAllLines(path).forEach(line -> {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                try {
                    // Parse the transaction data
                    LocalDate date = LocalDate.parse(parts[4].trim(), DATE_FORMATTER);
                    double amount = Double.parseDouble(parts[2].trim());
                    String category = parts[3].trim();
                    YearMonth month = YearMonth.from(date);

                    // Process income and expense data
                    if (parts[5].trim().equals("income")) {
                        monthlyIncome.merge(month, amount, Double::sum); // Sum income for each month
                    } else {
                        double expense = Math.abs(amount);
                        monthlyExpense.merge(month, expense, Double::sum); // Sum expense for each month
                        categoryExpenses.merge(category, expense, Double::sum); // Sum expenses by category
                    }
                } catch (Exception e) {
                    System.err.println("Invalid data line: " + line); // Handle invalid data lines
                }
            }
        });
    }

    /**
     * Populates the pie chart and bar chart with processed data.
     * The pie chart shows category-wise expenses, while the bar chart shows monthly income and expenses.
     */
    private void populateCharts() {
        pieChart.getData().clear(); // Clear previous data
        barChart.getData().clear(); // Clear previous data

        // Set chart font size and style
        pieChart.setStyle("-fx-font-size: 16px; -fx-font-weight: bold");
        barChart.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 -20 0 0;");

        // Populate pie chart with category expenses
        categoryExpenses.forEach((category, amount) ->
                pieChart.getData().add(new PieChart.Data(category + " (" + amount + ")", amount))
        );

        // Prepare data series for bar chart (Income and Expense)
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        // Add monthly data to the bar chart
        monthlyIncome.keySet().stream().sorted().forEach(month -> {
            String label = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            incomeSeries.getData().add(new XYChart.Data<>(label, monthlyIncome.get(month)));
            expenseSeries.getData().add(new XYChart.Data<>(label, monthlyExpense.getOrDefault(month, 0.0)));
        });

        barChart.getData().addAll(incomeSeries, expenseSeries); // Add both income and expense series to the bar chart
        yAxis.setAutoRanging(true); // Automatically adjust the Y-axis range

        // Set axis font size and label padding
        xAxis.setStyle("-fx-tick-label-font-size: 8px; -fx-label-padding: 10;");
        yAxis.setStyle("-fx-tick-label-font-size: 8px; -fx-label-padding: 10;");

        // Set legend font size for both charts
        barChart.lookupAll(".chart-legend").forEach(legend ->
                legend.setStyle("-fx-font-size: 12px;")
        );
        pieChart.lookupAll(".chart-legend").forEach(legend ->
                legend.setStyle("-fx-font-size: 12px;")
        );

        // Adjust bar spacing for the bar chart
        barChart.setCategoryGap(20);  // Gap between groups of bars
        barChart.setBarGap(5);        // Gap between bars in the same group
    }

    /**
     * Beautifies the user interface by setting a gradient background for the root pane.
     */
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

    /**
     * Displays an error dialog with the given title and message.
     *
     * @param title The title of the error dialog.
     * @param message The content message to be displayed.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // Show the error dialog and wait for user response
    }
}
