package src.main.java;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.input.MouseEvent;

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
    private ImageView arrowImage; // Arrow Image

    private List<Transaction> transactions;
    private ScheduledExecutorService executorService;
    private int currentLineIndex = 0;  // Declare and initialize currentLineIndex

    @FXML
    public void initialize() {
        setupBackground();
        setupCharts();
        setupLabels();
    }

    @FXML
    public void goToPreviousPage(MouseEvent event) {
        // Add logic to navigate to the previous page (for now, just print to console)
        System.out.println("Navigating to previous page...");
        // Implement your page switch logic here (e.g., using SceneManager or Stage switching)
    }

    private void setupBackground() {
        try {
            backgroundImage.setImage(new javafx.scene.image.Image("/src/main/resources/images/background.png"));
            arrowImage.setImage(new javafx.scene.image.Image("/src/main/resources/images/arrow.png"));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundImage.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
        }
    }

    private void setupCharts() {
        // Pie Chart Styling
        pieChart.setLegendVisible(true);
        pieChart.setTitle("Income vs Expense Ratio");

        // Bar Chart Styling
        barChart.setTitle("Monthly Cash Flow");
        barChart.setLegendVisible(true);

        // Container Styling
        graphsContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-background-radius: 15; -fx-padding: 20;");
    }

    private void setupLabels() {
        incomeLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 16px; -fx-font-weight: bold;");
        expenseLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
        netLabel.setStyle("-fx-text-fill: #3498db; -fx-font-size: 16px; -fx-font-weight: bold;");
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        updateCharts();
    }

    private void updateCharts() {
        double income = calculateTotalIncome();
        double expense = calculateTotalExpense();
        double net = income - expense;

        // Update Labels
        incomeLabel.setText(String.format("Income: $%.2f", income));
        expenseLabel.setText(String.format("Expense: $%.2f", expense));
        netLabel.setText(String.format("Net: $%.2f", net));

        // Set net label color based on value
        if (net >= 0) {
            netLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 16px; -fx-font-weight: bold;");
        } else {
            netLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
        }

        // Update PieChart
        pieChart.getData().clear();
        PieChart.Data incomeData = new PieChart.Data("Income", income);
        PieChart.Data expenseData = new PieChart.Data("Expense", expense);
        pieChart.getData().addAll(incomeData, expenseData);

        // Set colors for pie chart slices
        incomeData.getNode().setStyle("-fx-pie-color: #27ae60;");
        expenseData.getNode().setStyle("-fx-pie-color: #e74c3c;");

        // Update BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cash Flow");
        series.getData().add(new XYChart.Data<>("Income", income));
        series.getData().add(new XYChart.Data<>("Expense", expense));
        barChart.getData().clear();
        barChart.getData().add(series);

        // Set colors for bar chart
        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getXValue().equals("Income")) {
                data.getNode().setStyle("-fx-bar-fill: #27ae60;");
            } else {
                data.getNode().setStyle("-fx-bar-fill: #e74c3c;");
            }
        }
    }

    private double calculateTotalIncome() {
        return transactions.stream()
                .filter(t -> "income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private double calculateTotalExpense() {
        return transactions.stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public void stop() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    public void loadTransactionsFromCSV(String filePath) throws IOException {
        List<Transaction> transactionsList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip header if present
        reader.readLine();

        // 读取所有数据到 transactionsList
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            String type = fields[0].trim();  // 'income' or 'expense'
            double amount = Double.parseDouble(fields[1].trim());  // Amount in the second column

            transactionsList.add(new Transaction(type, amount));
        }
        reader.close();

        // Start the "animation" by simulating the gradual display of data
        simulateDataReading(transactionsList);
    }

    private void simulateDataReading(List<Transaction> transactionsList) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentLineIndex < transactionsList.size()) {
                    // 每次更新图表显示新的交易记录
                    List<Transaction> newTransactionList = transactionsList.subList(0, currentLineIndex + 1);

                    // 使用 Platform.runLater 确保图表更新发生在 JavaFX 主线程
                    Platform.runLater(() -> {
                        setTransactions(newTransactionList);  // 更新交易列表
                        updateCharts();  // 更新图表
                    });

                    currentLineIndex++;
                } else {
                    timer.cancel();  // 所有数据已显示完毕，停止定时器
                }
            }
        }, 0, 2000);  // 每 500 毫秒更新一次图表
    }
}
