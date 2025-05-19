package src.test.AI_story11_21_22;

import org.junit.jupiter.api.Test;
import src.main.java.AI_story11_21_22.Transaction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Title      : TransactionTest.java
 * Description: Unit tests for the Transaction model class.
 *              Verifies correct initialization and behavior of getters/setters.
 *
 * @author Wei Muchi
 * @version 1.0
 */
public class TransactionTest {

    /**
     * Tests that the Transaction constructor and getters correctly initialize and retrieve fields.
     */
    @Test
    public void testConstructorAndGetters() {
        // Arrange & Act
        Transaction transaction = new Transaction(
                "T123",
                "Monthly Rent",
                1500.0,
                "Housing",
                "2024-01-15",
                "expense"
        );

        // Assert
        assertEquals("T123", transaction.getId(), "ID should match the constructor value");
        assertEquals("Monthly Rent", transaction.getDescription(), "Description should match the constructor value");
        assertEquals(1500.0, transaction.getPrice(), 0.001, "Price should match the constructor value");
        assertEquals("Housing", transaction.getClassification(), "Classification should match the constructor value");
        assertEquals("2024-01-15", transaction.getDate(), "Date should match the constructor value");
        assertEquals("expense", transaction.getIoType(), "IO Type should match the constructor value");
    }

    /**
     * Tests that the Transaction setters correctly update fields.
     */
    @Test
    public void testSetters() {
        // Arrange
        Transaction transaction = new Transaction();

        // Act
        transaction.setId("T456");
        transaction.setDescription("Grocery Shopping");
        transaction.setPrice(85.50);
        transaction.setClassification("Food");
        transaction.setDate("2024-01-20");
        transaction.setIoType("expense");

        // Assert
        assertEquals("T456", transaction.getId(), "ID should be updated by setter");
        assertEquals("Grocery Shopping", transaction.getDescription(), "Description should be updated by setter");
        assertEquals(85.50, transaction.getPrice(), 0.001, "Price should be updated by setter");
        assertEquals("Food", transaction.getClassification(), "Classification should be updated by setter");
        assertEquals("2024-01-20", transaction.getDate(), "Date should be updated by setter");
        assertEquals("expense", transaction.getIoType(), "IO Type should be updated by setter");
    }
}