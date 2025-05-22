package src.main.java.Category_story6_7;

import src.main.java.AbstractTransaction;

/**
 * This class represents a financial transaction in the Transaction Management System.
 * It encapsulates the transaction details such as transaction name, price, classification, date, and type (Income/Expense).
 * The class provides getter and setter methods for each attribute, as well as a method to get a description of the transaction.
 *
 * @author Wei Chen
 * @version 1.0
 */
public class Transaction extends AbstractTransaction {
    private String id;  // Unique identifier for the transaction
    private String transaction;  // Description of the transaction
    private double price;  // Amount of the transaction
    private String classification;  // Category of the transaction (e.g., Food, Clothing, etc.)
    private String date;  // Date of the transaction in "yyyy-MM-dd" format
    private String IOType;  // Type of the transaction (Income or Expense)

    /**
     * Constructs a new Transaction object with the specified details.
     * @param id The unique identifier for the transaction.
     * @param transaction The description of the transaction.
     * @param price The amount of the transaction.
     * @param classification The category of the transaction.
     * @param date The date of the transaction in "yyyy-MM-dd" format.
     * @param IOType The type of the transaction (Income or Expense).
     */
    public Transaction(String id, String transaction, double price, String classification, String date, String IOType) {
        this.id = id;
        this.transaction = transaction;
        this.price = price;
        this.classification = classification;
        this.date = date;
        this.IOType = IOType;
    }

    /**
     * Gets the transaction description.
     * @return The transaction description.
     */
    public String getTransaction() {
        return transaction;
    }

    /**
     * Sets the transaction description.
     * @param transaction The new transaction description.
     */
    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    /**
     * Gets the transaction price.
     * @return The transaction price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the transaction price.
     * @param price The new transaction price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the transaction classification.
     * @return The transaction classification.
     */
    public String getClassification() {
        return classification;
    }

    /**
     * Sets the transaction classification.
     * @param classification The new transaction classification.
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * Gets the transaction date.
     * @return The transaction date in "yyyy-MM-dd" format.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the transaction date.
     * @param date The new transaction date in "yyyy-MM-dd" format.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the transaction ID.
     * @return The transaction ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the transaction ID.
     * @param id The new transaction ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the transaction type (Income or Expense).
     * @return The transaction type.
     */
    public String getIOType() {
        return IOType;
    }

    /**
     * Sets the transaction type (Income or Expense).
     * @param IOType The new transaction type.
     */
    public void setIOType(String IOType) {
        this.IOType = IOType;
    }

    /**
     * Gets a description of the transaction.
     * @return A string describing the transaction.
     */
    public String getDescription() {
        return transaction;  // Returns the transaction description
    }

    /**
     * Gets the transaction type (Income or Expense).
     * @return The transaction type.
     */
    public String getIoType() {
        return IOType;  // Returns the transaction type (Income or Expense)
    }

    /**
     * Returns a string representation of the transaction.
     * @return A string containing the transaction details.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "transaction='" + transaction + '\'' +
                ", price=" + price +
                ", classification='" + classification + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}