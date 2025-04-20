package src.main.java.Category_story6_7;

public class Transaction {
    private String transaction;
    private double price;
    private String classification;
    private String date;

    // 构造函数、getter 和 setter
    public Transaction(String transaction, double price, String classification, String date) {
        this.transaction = transaction;
        this.price = price;
        this.classification = classification;
        this.date = date;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "transaction.csv='" + transaction + '\'' +
                ", price=" + price +
                ", classification='" + classification + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}