package src.test.Category_story6_7;

import src.main.java.Category_story6_7.AddController;
import src.main.java.Category_story6_7.Transaction;
import src.main.java.Session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for verifying the functionality of the AddController class.
 * This class uses the JUnit testing framework to perform unit tests on methods within the AddController class.
 * It primarily tests the getNextId method and logic related to adding transactions.
 *
 * @author Wei Chen
 * @version 1.0
 */
class AddControllerTest {

    /**
     * An instance of AddController used for testing.
     */
    private AddController addController;

    /**
     * An instance of Transaction used for testing.
     */
    private Transaction transaction;

    /**
     * Initializes the test environment before each test method is executed.
     * This includes setting the current user, initializing the AddController instance, and creating a test CSV file.
     *
     * @throws IOException If an I/O error occurs while creating or writing to the CSV file.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Set the current user
        Session.setCurrentNickname("test_user");

        // Initialize the AddController
        addController = new AddController();

        // Create a test CSV file
        File csvFile = new File("data/test_user_transaction.csv");
        if (!csvFile.exists()) {
            csvFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(csvFile)) {
                writer.write("Id,Transaction,Price,Classification,Date,IOType\n");
                writer.write("1,Test Transaction,100.50,Test Category,2025-05-15,Income\n");
            }
        }
    }

    /**
     * Tests the getNextId method of the AddController class.
     * Uses reflection to call the private getNextId method and verifies that the returned ID is correct.
     *
     * @throws Exception If reflection invocation or assertion fails.
     */
    @Test
    void testGetNextId() throws Exception {
        // Use reflection to call the private getNextId() method
        Method method = AddController.class.getDeclaredMethod("getNextId");
        method.setAccessible(true);

        // Invoke the getNextId() method
        String nextId = (String) method.invoke(addController);

        // Verify that the returned ID is correct
        assertEquals("2", nextId); // Assuming the current maximum ID is 1, the next ID should be "2"
    }

    /**
     * Tests the functionality of adding a transaction.
     * Currently, this test method only calls the getNextId method. The actual test logic for adding transactions needs to be implemented.
     *
     * @throws Exception If reflection invocation or assertion fails.
     */
    @Test
    void testAddTransaction() throws Exception {
        // Use reflection to call the private getNextId() method
        Method method = AddController.class.getDeclaredMethod("getNextId");
        method.setAccessible(true);

        // Invoke the getNextId() method
        String nextId = (String) method.invoke(addController);

        // Verify that the returned ID is correct
        assertEquals("2", nextId);
    }

    /**
     * Tests whether a transaction has been added.
     * Currently, this test method only calls the getNextId method. The actual test logic for verifying added transactions needs to be implemented.
     *
     * @throws Exception If reflection invocation or assertion fails.
     */
    @Test
    void testIsAdded() throws Exception {
        // Use reflection to call the private getNextId() method
        Method method = AddController.class.getDeclaredMethod("getNextId");
        method.setAccessible(true);

        // Invoke the getNextId() method
        String nextId = (String) method.invoke(addController);

        // Verify that the returned ID is correct
        assertEquals("2", nextId);
    }

    /**
     * Helper method to set the value of a field in a target object using reflection.
     *
     * @param target The target object.
     * @param fieldName The name of the field.
     * @param value The value to be set.
     * @throws NoSuchFieldException If the specified field does not exist in the target object.
     * @throws IllegalAccessException If the field is not accessible.
     */
    private void setFieldValue(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}