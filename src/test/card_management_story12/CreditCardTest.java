/**
 * Title      : CreditCardTest.java
 * Description: Unit test for the CreditCard class.
 *              Verifies that the constructor and getter methods of the CreditCard class
 *              correctly initialize and retrieve the credit card information.
 *              This test ensures that the credit card details, including the card number,
 *              holder's name, expiry date, and CVV, are correctly assigned and accessed.
 *
 * @author Yudian Wang
 * @version 1.0
 */

package src.test.card_management_story12;

import org.junit.jupiter.api.Test;
import src.main.java.card_management_story12.CreditCard;

import static org.junit.jupiter.api.Assertions.*;

public class CreditCardTest {

    /**
     * Test method for the constructor and getter methods of the CreditCard class.
     * This test checks that the CreditCard constructor correctly initializes the card's
     * attributes and that the getter methods return the correct values for card number,
     * card holder, expiry date, and CVV.
     */
    @Test
    public void testConstructorAndGetters() {
        // Initialize a CreditCard object
        CreditCard card = new CreditCard("1234567890123456", "John Doe", "12/23", "123");

        // Test getters for proper data retrieval
        assertEquals("1234567890123456", card.getCardNumber(), "Card number should match.");
        assertEquals("John Doe", card.getCardHolder(), "Card holder's name should match.");
        assertEquals("12/23", card.getExpiryDate(), "Card expiry date should match.");
        assertEquals("123", card.getCvv(), "Card CVV should match.");
    }
}
