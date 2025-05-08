package src.main.java.AI_story_11_21_22;

public class Transaction {
    private String id;
    private String description;
    private double price;
    private String classification;
    private String date;
    private String ioType;

    // 无参构造函数，用于从CSV文件解析数据
    public Transaction() {
    }

    // 带参构造函数，用于创建Transaction对象
    public Transaction(String id, String description, double price, String classification, String date, String ioType) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.classification = classification;
        this.date = date;
        this.ioType = ioType;
    }

    // Getters and Setters
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