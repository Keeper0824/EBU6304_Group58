package src.main.java.AI_story11_21_22;

/**
 * Title      : Transaction.java
 * Description: This class models a transaction with attributes such as ID, description, price,
 *              classification, date, and input/output type.
 *
 * @author Wei Muchi
 * @version 1.0
 */
public class Transaction {
    private String id;
    private String description;
    private double price;
    private String classification;
    private String date;
    private String ioType;

    /**
     * Default constructor for creating a Transaction object from a CSV file.
     */
    public Transaction() {
    }

    /**
     * Constructs a new Transaction object with the given attributes.
     *
     * @param id             the ID of the transaction
     * @param description    the description or summary of the transaction
     * @param price          the price or amount of the transaction
     * @param classification the classification of the transaction
     * @param date           the date of the transaction
     * @param ioType         the input/output type of the transaction
     */
    public Transaction(String id, String description, double price, String classification, String date, String ioType) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.classification = classification;
        this.date = date;
        this.ioType = ioType;
    }

    /**
     * Gets the ID of this transaction.
     *
     * @return the ID string
     */
    public String getId() { return id; }

    /**
     * Sets the ID of this transaction.
     *
     * @param id the new ID string
     */
    public void setId(String id) { this.id = id; }

    /**
     * Gets the description of this transaction.
     *
     * @return the description string
     */
    public String getDescription() { return description; }

    /**
     * Sets the description of this transaction.
     *
     * @param description the new description string
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets the price of this transaction.
     *
     * @return the price as a double
     */
    public double getPrice() { return price; }

    /**
     * Sets the price of this transaction.
     *
     * @param price the new price as a double
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * Gets the classification of this transaction.
     *
     * @return the classification string
     */
    public String getClassification() { return classification; }

    /**
     * Sets the classification of this transaction.
     *
     * @param classification the new classification string
     */
    public void setClassification(String classification) { this.classification = classification; }

    /**
     * Gets the date of this transaction.
     *
     * @return the date string
     */
    public String getDate() { return date; }

    /**
     * Sets the date of this transaction.
     *
     * @param date the new date string
     */
    public void setDate(String date) { this.date = date; }

    /**
     * Gets the input/output type of this transaction.
     *
     * @return the input/output type string
     */
    public String getIoType() { return ioType; }

    /**
     * Sets the input/output type of this transaction.
     *
     * @param ioType the new input/output type string
     */
    public void setIoType(String ioType) { this.ioType = ioType; }
}