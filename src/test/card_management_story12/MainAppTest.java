/**
 * Title      : MainAppTest.java
 * Description: Unit test for the MainApp class.
 *              Verifies that the main method of the MainApp JavaFX application runs
 *              without throwing any exceptions. It ensures that the application
 *              can be initialized correctly even in a headless environment.
 *
 * @author Yudian Wang
 * @version 1.0
 */

package src.test.card_management_story12;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import src.main.java.card_management_story12.MainApp;

public class MainAppTest {

    /**
     * Sets up the JavaFX environment in headless mode before each test.
     * This ensures that the tests can run in an environment without a graphical interface.
     */
    @BeforeEach
    public void setup() {
        // Set the JavaFX environment to headless mode to ensure it runs without a GUI
        System.setProperty("javafx.platform", "headless");
    }

    /**
     * Test method for the main() method of the MainApp class.
     * This test ensures that the main method can run without throwing any exceptions.
     * It verifies that the JavaFX application can be initialized and executed in a headless environment.
     */
    @Test
    public void testMain() {
        // Test that the main method runs without throwing any exceptions
        Assertions.assertDoesNotThrow(() -> {
            // Run the main method of the MainApp
            MainApp.main(new String[]{});
        }, "Main method should run without exceptions.");
    }
}
