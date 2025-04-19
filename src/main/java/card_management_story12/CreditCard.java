package src.main.java.card_management_story12;


public class CreditCard {
    private final String cardNumber;
    private final String cardHolder;
    private final String expiryDate;
    private final String cvv;

    public CreditCard(String num, String holder, String date, String cvv) {
        this.cardNumber = num;
        this.cardHolder = holder;
        this.expiryDate = date;
        this.cvv = cvv;
    }

    // Getter方法需要与TableView的PropertyValueFactory对应
    public String getCardNumber() { return cardNumber; }
    public String getCardHolder() { return cardHolder; }
    public String getExpiryDate() { return expiryDate; }
    public String getCvv() { return cvv; }
}