package src.test.AI_story11_21_22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import src.main.java.AI_story11_21_22.DataPreprocessor;
import src.main.java.AI_story11_21_22.Transaction;
import src.main.java.Session; // 假设Session类在这里

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Title      : DataPreprocessorTest.java
 * Description: Unit tests for the DataPreprocessor class.
 *              Verifies file loading, error handling, and data parsing with mocked session.
 *
 * @author Wei Muchi
 * @version 1.0
 */
public class DataPreprocessorTest {

    @BeforeEach
    public void setUp() {
        // 重置Session状态，确保每次测试前都是已知状态
        Session.setCurrentNickname("test_user"); // 假设Session提供了这个静态方法
    }

    /**
     * Tests successful loading of transaction data from a valid CSV file.
     * Uses a mock Session to ensure the correct user nickname is used.
     *
     * @throws IOException if an I/O error occurs (should not happen with valid test file).
     */
    @Test
    public void testLoadTransactions() throws IOException {
        // Arrange
        String validFilePath = "src/test/resources/test_user_transaction.csv";

        // 使用Mockito模拟Session类（如果Session是final类，可能需要PowerMockito）
        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            mockedSession.when(Session::getCurrentNickname).thenReturn("test_user");

            // Act
            List<Transaction> transactions = DataPreprocessor.loadTransactions(validFilePath);

            // Assert
            assertNotNull(transactions, "Returned transaction list should not be null");
            assertFalse(transactions.isEmpty(), "Transaction list should contain at least one item");
            // 可以添加更多断言来验证具体的交易数据
        }
    }

    /**
     * Tests that an IOException is thrown when the user data file is missing.
     */
    @Test
    public void testLoadTransactions_FileNotFound() {
        // Arrange
        String invalidFilePath = "src/test/resources/non_existent_user_transaction.csv";

        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            mockedSession.when(Session::getCurrentNickname).thenReturn("non_existent_user");

            // Act & Assert
            Exception exception = assertThrows(IOException.class, () -> {
                DataPreprocessor.loadTransactions(invalidFilePath);
            });

            assertTrue(exception.getMessage().contains("文件不存在"),
                    "Exception should indicate file not found");
        }
    }
}