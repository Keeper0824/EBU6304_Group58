package src.main.java.CashFlowVisualization_story15;

/**
 * Title      : Transaction.java
 * Description: Represents a single financial transaction for the cash flow visualization feature.
 *              Each transaction has a type ("income" or "expense") and an associated amount.
 *              Used by CashFlowController to calculate totals and update charts dynamically.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class Transaction {
    /**
     * The type of the transaction: either "income" or "expense".
     */
    private String type;

    /**
     * The monetary amount of the transaction.
     */
    private double amount;

    /**
     * Constructs a Transaction with the specified type and amount.
     *
     * @param type   the type of transaction, expected values: "income" or "expense"
     * @param amount the monetary value of the transaction
     */
    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Returns the type of this transaction.
     *
     * @return the transaction type, either "income" or "expense"
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the amount of this transaction.
     *
     * @return the monetary amount of the transaction
     */
    public double getAmount() {
        return amount;
    }
}