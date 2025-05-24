package src.main.java;

public abstract class AbstractTransaction {
    protected String id;
    protected String description;
    protected double price;
    protected String classification;
    protected String date;
    protected String ioType;

    public AbstractTransaction() {
    }

    public AbstractTransaction(String id,
                                   String description,
                                   double price,
                                   String classification,
                                   String date,
                                   String ioType) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.classification = classification;
        this.date = date;
        this.ioType = ioType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getIoType() { return ioType; }
    public void setIoType(String ioType) { this.ioType = ioType; }
}