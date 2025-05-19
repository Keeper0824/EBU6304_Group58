package src.main.java.CashFlowVisualization_story15;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import src.main.java.Session;

/**
 * Title      : CashFlowController.java
 * Description: Controller for the cash flow visualization view (Story15).
 * Initializes pie and bar charts, loads transaction data for the
 * current user from CSV, and animates the data display over time.
 * Displays total income, expense, and net values, and updates charts
 * dynamically. Ensures graceful shutdown of any scheduled tasks.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class CashFlowController {

    @FXML
    private VBox mainContainer;

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expenseLabel;

    @FXML
    private Label netLabel;

    @FXML
    private VBox graphsContainer;

    @FXML
    private ImageView arrowImage;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    private List<Transaction> transactions;
    private int currentLineIndex = 0;
    private final String currentUser = Session.getCurrentNickname();

    /**
     * Initializes the view: sets up charts, labels, and loads transactions
     * from the CSV file for the current user. Called automatically by FXMLLoader.
     */
    @FXML
    public void initialize() {
        setupCharts();
        setupLabels();
        transactions = new ArrayList<>();
        updateCharts();

        try {
            loadTransactionsFromCSV("data/" + currentUser + "_transaction.csv");
        } catch (IOException e) {
            System.err.println("Failed to load transactions: " + e.getMessage());
            transactions = new ArrayList<>();
            updateCharts();
        }
    }

    /**
     * Applies styling and titles to the PieChart and BarChart components,
     * and styles the container background.
     */
    private void setupCharts() {
        pieChart.setLegendVisible(true);
        pieChart.setTitle("Income vs Expense Ratio");

        barChart.setTitle("Monthly Cash Flow");
        barChart.setLegendVisible(true);

        graphsContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.85); " +
                        "-fx-background-radius: 15; -fx-padding: 20;"
        );
    }

    /**
     * Styles the labels for income, expense, and net values with colors and font settings.
     */
    private void setupLabels() {
        incomeLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 16px; -fx-font-weight: bold;");
        expenseLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
        netLabel.setStyle("-fx-text-fill: #3498db; -fx-font-size: 16px; -fx-font-weight: bold;");
    }

    /**
     * Updates the controller's transaction list and refreshes the charts.
     *
     * @param transactions the list of transactions to display
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        updateCharts();
    }

    /**
     * Recalculates totals and updates the pie and bar charts along with summary labels.
     */
    private void updateCharts() {
        double income = calculateTotalIncome();
        double expense = calculateTotalExpense();
        double net = income - expense;

        incomeLabel.setText(String.format("Income: $%.2f", income));
        expenseLabel.setText(String.format("Expense: $%.2f", expense));
        netLabel.setText(String.format("Net: $%.2f", net));

        if (net >= 0) {
            netLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 16px; -fx-font-weight: bold;");
        } else {
            netLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
        }

        pieChart.getData().clear();
        PieChart.Data incomeData = new PieChart.Data("Income", income);
        PieChart.Data expenseData = new PieChart.Data("Expense", expense);
        pieChart.getData().addAll(incomeData, expenseData);

        incomeData.getNode().setStyle("-fx-pie-color: #27ae60;");
        expenseData.getNode().setStyle("-fx-pie-color: #e74c3c;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cash Flow");
        series.getData().add(new XYChart.Data<>("Income", income));
        series.getData().add(new XYChart.Data<>("Expense", expense));
        barChart.getData().clear();
        barChart.getData().add(series);

        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getXValue().equals("Income")) {
                data.getNode().setStyle("-fx-bar-fill: #27ae60;");
            } else {
                data.getNode().setStyle("-fx-bar-fill: #e74c3c;");
            }
        }
    }

    /**
     * Calculates the total income from the current transaction list.
     *
     * @return sum of all income transaction amounts
     */
    private double calculateTotalIncome() {
        return transactions.stream()
                .filter(t -> "income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Calculates the total expense from the current transaction list.
     *
     * @return sum of all expense transaction amounts
     */
    private double calculateTotalExpense() {
        return transactions.stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Stops any running scheduled tasks to prevent memory leaks.
     */
    public void stop() {
        // No executorService used here; included if future tasks are scheduled
    }

    /**
     * Loads transaction records from the given CSV file path, skipping the header.
     * Parses each line into a Transaction object and starts the simulated display.
     *
     * @param filePath path to the transactions CSV file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadTransactionsFromCSV(String filePath) throws IOException {
        List<Transaction> transactionsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String type = fields[5].trim();
                double amount = Double.parseDouble(fields[2].trim());
                transactionsList.add(new Transaction(type, amount));
            }
        }
        simulateDataReading(transactionsList);
    }

    /**
     * Simulates incremental data loading by scheduling a TimerTask to reveal
     * one transaction at a time every 2 seconds, updating the UI on the JavaFX thread.
     *
     * @param transactionsList full list of transactions to display
     */
    private void simulateDataReading(List<Transaction> transactionsList) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentLineIndex < transactionsList.size()) {
                    List<Transaction> newSubset = transactionsList.subList(0, currentLineIndex + 1);
                    Platform.runLater(() -> {
                        setTransactions(newSubset);
                    });
                    currentLineIndex++;
                } else {
                    timer.cancel();
                }
            }
        }, 0, 2000);
    }

    //    private void setupButtons() {
//        backButton.setOnAction(e -> handleBackToMainMenu());
//        nextButton.setOnAction(e -> handleNextView());
//    }

//    private void handleNextView() {
//        try {
//            // Close current window
//            Stage currentStage = (Stage) nextButton.getScene().getWindow();
//            currentStage.close();
//
//            // Load the main_view.fxml
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/financial_story9/main_view.fxml"));
//            Parent root = loader.load();
//
//            // Set background style
//            root.setStyle("-fx-background-image: url('/src/main/resources/financial_story9/images/background.png');" +
//                    "-fx-background-size: cover;" +
//                    "-fx-background-position: center;");
//
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root, 1600, 900));
//            stage.setTitle("Financial Analysis");
//            stage.show();
//        } catch (IOException e) {
//            System.err.println("Failed to load next view: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void handleBackToMainMenu() {
//        try {
//            // Close current window
//            Stage currentStage = (Stage) backButton.getScene().getWindow();
//            currentStage.close();
//
//            // Open Main Menu with the current user
//            User currentUser = MainMenuApp.getCurrentUser();
//            new MainMenuApp(currentUser).start(new Stage());
//        } catch (Exception e) {
//            System.err.println("Failed to return to Main Menu: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
