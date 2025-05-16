/**
 * Title      : CSVUtilsTest.java
 * Description: Unit test for the CSVUtils class.
 *              Verifies that the saveCards() and loadCards() methods
 *              correctly save and load credit card data from CSV files.
 *              The test checks if the data is properly written to and read from
 *              the CSV file, ensuring that the card details match after loading.
 *
 * @author Yudian Wang
 * @version 1.0
 */

package src.test.card_management_story12;

import org.junit.jupiter.api.Test;
import src.main.java.Session;
import src.main.java.card_management_story12.CSVUtils;
import src.main.java.card_management_story12.CreditCard;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CSVUtilsTest {

    /**
     * Test method for saving and loading credit card data to/from CSV.
     * This method verifies that the saveCards() method correctly saves credit card
     * data to a CSV file, and the loadCards() method loads the data back correctly.
     * The test checks if the saved card's data matches the loaded card's data.
     *
     * @throws IOException if there is an issue with file operations
     */
    @Test
    public void testSaveAndLoadCards() throws IOException {
        // Create a sample CreditCard object
        CreditCard card = new CreditCard("1234567890123456", "John Doe", "12/23", "123");

        // Save the card to the CSV
        CSVUtils.saveCards(List.of(card));

        // Verify if the file exists and contains the saved card
        File testFile = new File("data/" + Session.getCurrentNickname() + "_cards.csv");
        assertTrue(testFile.exists(), "CSV file should exist after saving cards.");

        // Load the cards from the CSV
        List<CreditCard> loadedCards = CSVUtils.loadCards();

        // Test if the loaded card data matches the saved data
        assertFalse(loadedCards.isEmpty(), "The list of loaded cards should not be empty.");
        assertEquals("1234567890123456", loadedCards.get(0).getCardNumber(), "Card number should match.");
        assertEquals("John Doe", loadedCards.get(0).getCardHolder(), "Card holder's name should match.");
        assertEquals("12/23", loadedCards.get(0).getExpiryDate(), "Card expiry date should match.");
        assertEquals("123", loadedCards.get(0).getCvv(), "Card CVV should match.");

        // Clean up the created file after testing
        testFile.delete();
    }
}
