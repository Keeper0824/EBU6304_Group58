package src.main.java.card_management_story12;

/**
 * Title      : CreditCard.java
 * Description: Represents a credit card entry in the card management system (Story 12).
 *              Stores the card number, card holder's name, expiry date, and CVV.
 *              Used by BankCardController to display and persist card data.
 *
 * @author Yudian Wang
 * @version 1.0
 * @author Haoran Sun
 * @version 1.1
 */
public class CreditCard {
    private final String cardNumber;
    private final String cardHolder;
    private final String expiryDate;
    private final String cvv;

    /**
     * Constructs a new CreditCard with the specified details.
     *
     * @param num    the 16-digit card number (no spaces)
     * @param holder the full name of the card holder
     * @param date   the expiry date in MM/YY format
     * @param cvv    the 3-digit security code
     */
    public CreditCard(String num, String holder, String date, String cvv) {
        this.cardNumber = num;
        this.cardHolder = holder;
        this.expiryDate = date;
        this.cvv = cvv;
    }

    /**
     * Gets the card number.
     * Must match the PropertyValueFactory column identifier in the TableView.
     *
     * @return the card number string
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Gets the card holder's name.
     *
     * @return the card holder name string
     */
    public String getCardHolder() {
        return cardHolder;
    }

    /**
     * Gets the card's expiry date.
     *
     * @return the expiry date string
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Gets the card's CVV code.
     *
     * @return the CVV string
     */
    public String getCvv() {
        return cvv;
    }
}
