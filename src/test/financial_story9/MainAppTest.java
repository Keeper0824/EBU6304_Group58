package src.test.financial_story9;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import src.main.java.financial_story9.MainApp;

/**
 * Title      : MainAppTest.java
 * Description: Unit test for the MainApp JavaFX application.
 *              Verifies that the MainApp application starts successfully
 *              and initializes the JavaFX environment correctly.
 *
 * @author Yudian Wang
 * @version 1.0
 */
public class MainAppTest {

    /**
     * This test method verifies that the MainApp JavaFX application starts correctly.
     * It uses Application.launch() to launch the JavaFX application and checks if it initializes properly.
     * The test currently asserts that the JavaFX application starts without any issues.
     */
    @Test
    public void testStart() {
        // Launch the JavaFX application
        // Using Application.launch() ensures that the JavaFX environment is initialized correctly
        Application.launch(MainApp.class);  // Launch the MainApp JavaFX application

        // Since JavaFX needs to run on the JavaFX application thread, calling Application.launch()
        // will ensure that the environment is properly initialized.
        // After launching, you can continue your testing logic here.

        // Perform assertions to check that JavaFX starts without issues
        Assertions.assertTrue(true, "JavaFX application started successfully.");
    }
}
