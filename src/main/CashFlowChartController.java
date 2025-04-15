package src.main;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import java.util.List;

public class CashFlowChartController {
    private final List<Transaction> transactions;

    public CashFlowChartController(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * 计算收入总额
     */
    private double calculateTotalIncome() {
        return transactions.stream()
                .filter(t -> "income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * 计算支出总额
     */
    private double calculateTotalExpense() {
        return transactions.stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * 生成饼图数据：显示收入与支出比例
     */
    public PieChart generatePieChart() {
        double income = calculateTotalIncome();
        double expense = calculateTotalExpense();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Income", income),
                new PieChart.Data("Expense", expense)
        );

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Monthly Cash Flow");
        return pieChart;
    }

    /**
     * 生成柱状图数据：展示收入和支出数量对比
     */
    public BarChart<String, Number> generateBarChart() {
        double income = calculateTotalIncome();
        double expense = calculateTotalExpense();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cash Flow");

        series.getData().add(new XYChart.Data<>("Income", income));
        series.getData().add(new XYChart.Data<>("Expense", expense));

        BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart.getData().add(series);
        barChart.setTitle("Monthly Income vs Expense");
        return barChart;
    }
}
