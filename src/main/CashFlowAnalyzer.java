package src.main;
import java.util.List;

public class CashFlowAnalyzer {
    private final List<Transaction> transactions;

    public CashFlowAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}

