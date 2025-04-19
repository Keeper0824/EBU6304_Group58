package src.main.java.CashFlowVisualization_story15;

public class Transaction {
    private String type; // "income" or "expense"
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
