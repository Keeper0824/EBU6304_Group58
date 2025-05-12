package src.main.java.Category_story6_7;

public class Transaction {
    private String id;
    private String transaction;
    private double price;
    private String classification;
    private String date;
    private String IOType;

    // 构造函数、getter 和 setter
    public Transaction(String id, String transaction, double price, String classification, String date, String IOType) {
        this.id = id;
        this.transaction = transaction;
        this.price = price;
        this.classification = classification;
        this.date = date;
        this.IOType = IOType;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIOType() {
        return IOType;
    }

    public void setIOType(String IOType) {
        this.IOType = IOType;
    }

    // 新增方法：获取描述
    public String getDescription() {
        return transaction;  // 返回交易描述
    }

    // 新增方法：获取交易类型
    public String getIoType() {
        return IOType;  // 返回交易类型（收入或支出）
    }

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
