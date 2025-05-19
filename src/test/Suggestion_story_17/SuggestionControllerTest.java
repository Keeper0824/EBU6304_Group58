package src.test.Suggestion_story_17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.java.Category_story6_7.Transaction;
import src.main.java.Suggestion_story_17.SuggestionController;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Title      : SuggestionControllerTest.java
 * Description: Unit test class for SuggestionController (Story 4–13).
 *              Verifies correct behavior of AI advice prompt generation
 *              and CSV transaction data parsing.
 *              Includes test cases for expense-only filtering,
 *              valid and invalid CSV input formats.
 *
 * @author      Kaiyu Liu
 * @version     1.0
 */
public class SuggestionControllerTest {

    /** Instance of SuggestionController used for each test. */
    private SuggestionController controller;

    /**
     * Initializes a fresh SuggestionController before each test case.
     */
    @BeforeEach
    public void setup() {
        controller = new SuggestionController();
    }

    /**
     * Tests whether generateAdvicePrompt only includes transactions
     * with IOType "expense", and excludes "income" entries.
     * Also verifies that instructional content is present in the prompt.
     */
    @Test
    public void testGenerateAdvicePrompt_onlyIncludesExpenses() {
        // Arrange
        Transaction t1 = new Transaction("2024-01-01", "Coffee", 25.0, "Food", "Personal", "expense");
        Transaction t2 = new Transaction("2024-01-02", "Salary", 10000.0, "Income", "Work", "income");
        Transaction t3 = new Transaction("2024-01-03", "Movie", 80.0, "Entertainment", "Fun", "expense");
        List<Transaction> txs = List.of(t1, t2, t3);

        // Act
        String prompt = controller.generateAdvicePrompt(txs);

        // Assert
        assertTrue(prompt.contains("Coffee"));      // Expense
        assertTrue(prompt.contains("Movie"));       // Expense
        assertFalse(prompt.contains("Salary"));     // Income
        assertTrue(prompt.contains("Based on the following transaction data"));
        assertTrue(prompt.contains("Please provide specific suggestions"));
    }

    /**
     * Tests that a properly formatted CSV file is read correctly.
     * Verifies that all fields are parsed into Transaction objects.
     *
     * @throws IOException if temporary file operations fail
     */
    @Test
    public void testReadTransactionsFromCSV_validFile() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("test_transactions", ".csv");
        String content = "date,transaction,price,classification,comment,IOType\n" +
                "2024-01-01,Coffee,25.0,Food,Personal,expense\n" +
                "2024-01-02,Salary,10000.0,Income,Work,income\n";
        Files.write(tempFile.toPath(), content.getBytes());

        // Act
        List<Transaction> transactions = controller.readTransactionsFromCSV(tempFile.getAbsolutePath());

        // Assert
        assertEquals(2, transactions.size());
        assertEquals("Coffee", transactions.get(0).getTransaction());
        assertEquals("Salary", transactions.get(1).getTransaction());

        // Cleanup
        tempFile.deleteOnExit();
    }

    /**
     * Tests behavior when the CSV file contains a malformed row.
     * Ensures invalid lines are skipped, and valid lines are still parsed.
     *
     * @throws IOException if temporary file operations fail
     */
    @Test
    public void testReadTransactionsFromCSV_invalidFormat_skipsBadLine() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("test_bad_format", ".csv");
        String content = "date,transaction,price,classification,comment,IOType\n" +
                "2024-01-01,Coffee,25.0,Food,Personal,expense\n" +
                "InvalidLineWithoutEnoughFields\n";  // Malformed
        Files.write(tempFile.toPath(), content.getBytes());

        // Act
        List<Transaction> transactions = controller.readTransactionsFromCSV(tempFile.getAbsolutePath());

        // Assert
        assertEquals(1, transactions.size());
        assertEquals("Coffee", transactions.get(0).getTransaction());

        // Cleanup
        tempFile.deleteOnExit();
    }
}
