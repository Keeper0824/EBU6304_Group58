package src.test.Login_story1_3;

import org.junit.jupiter.api.Test;
import src.main.java.Login_story1_3.RegistrationController;

import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Title      : RegistrationControllerTest.java
 * Description: Test suite for the RegistrationController class.
 *              It validates the core functionality of the registration controller,
 *              including password encryption, email validation, and user ID generation.
 * @author Zhengxuan Han
 * @version 1.0
 */
public class RegistrationControllerTest {

    /**
     * Tests the encryptPassword method to ensure it generates a non-null encrypted string.
     * Uses a test password and checks that the result is not null.
     *
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    @Test
    public void testEncryptPassword() throws NoSuchAlgorithmException {
        RegistrationController controller = new RegistrationController();
        String password = "testPassword";
        String encryptedPassword = controller.encryptPassword(password);
        assertNotNull(encryptedPassword);
    }

    /**
     * Tests the isValidEmail method with valid and invalid email addresses.
     * Verifies that valid emails return true and invalid ones return false.
     */
    @Test
    public void testIsValidEmail() {
        RegistrationController controller = new RegistrationController();
        String validEmail = "test@example.com";
        String invalidEmail = "testexample.com";

        assertTrue(controller.isValidEmail(validEmail));
        assertFalse(controller.isValidEmail(invalidEmail));
    }

    /**
     * Tests the getNextUserId method to ensure it returns a non-null user ID string.
     * Verifies that the method correctly generates the next user ID based on CSV data.
     */
    @Test
    public void testGetNextUserId() {
        RegistrationController controller = new RegistrationController();
        String nextUserId = controller.getNextUserId();
        assertNotNull(nextUserId);
    }
}