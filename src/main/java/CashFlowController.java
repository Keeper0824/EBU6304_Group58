package src.main.java;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CashFlowController {

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private ImageView backgroundImage;
    @FXML private Label incomeLabel;
    @FXML private Label expenseLabel;
    @FXML private Label netLabel;
    @FXML private VBox graphsContainer;

    private List<Transaction> transactions;
    private final Random random = new Random();
    private ScheduledExecutorService executorService;

    @FXML
    public void initialize() {
        setupBackground();
        setupCharts();
        setupLabels();
        startDataSimulation();
    }

    private void setupBackground() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/src/main/resources/images/background.png"));
            backgroundImage.setImage(image);
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

    private void startDataSimulation() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            // Simulate new transactions
            if (transactions != null) {
                double incomeChange = random.nextDouble() * 500 - 100;
                double expenseChange = random.nextDouble() * 300 - 50;

                transactions.add(new Transaction("income", Math.max(incomeChange, 0)));
                transactions.add(new Transaction("expense", Math.max(expenseChange, 0)));

                javafx.application.Platform.runLater(this::updateCharts);
            }
        }, 0, 2, TimeUnit.SECONDS);
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
}