// CreditCard.java
package card_management;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreditCard {
    private final StringProperty cardNumber;
    private final StringProperty cardHolder;
    private final StringProperty expiryDate;
    private final StringProperty cvv;

    public CreditCard(String cardNumber, String cardHolder, String expiryDate, String cvv) {
        this.cardNumber = new SimpleStringProperty(formatCardNumber(cardNumber));
        this.cardHolder = new SimpleStringProperty(cardHolder.toUpperCase());
        this.expiryDate = new SimpleStringProperty(expiryDate);
        this.cvv = new SimpleStringProperty(cvv);
    }

    private String formatCardNumber(String number) {
        return number.replaceAll("(.{4})", "$1 ").trim();
    }

    // Getters and property methods
    public String getCardNumber() { return cardNumber.get(); }
    public StringProperty cardNumberProperty() { return cardNumber; }

    public String getCardHolder() { return cardHolder.get(); }
    public StringProperty cardHolderProperty() { return cardHolder; }

    public String getExpiryDate() { return expiryDate.get(); }
    public StringProperty expiryDateProperty() { return expiryDate; }

    public String getCvv() { return cvv.get(); }
    public StringProperty cvvProperty() { return cvv; }
}