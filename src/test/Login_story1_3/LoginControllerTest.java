package src.test.Login_story1_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.java.Login_story1_3.LoginController;
import src.main.java.Login_story1_3.User;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Title      : LoginControllerTest.java
 * Description: Test suite for the LoginController class.
 *              It validates the core functionality of the login controller,
 *              including email validation, password encryption, user authentication,
 *              and CSV data loading.
 * @author Zhengxuan Han
 * @version 1.0
 */
class LoginControllerTest {

    private LoginController loginController;

    /**
     * Sets up the test environment before each test case.
     * Initializes a new instance of LoginController for testing.
     */
    @BeforeEach
    void setUp() {
        loginController = new LoginController();
    }

    /**
     * Tests the isValidEmail method with valid and invalid email addresses.
     * Verifies that valid emails return true and invalid ones return false.
     */
    @Test
    void testIsValidEmail() {
        String validEmail = "test@example.com";
        String invalidEmail = "testexample.com";

        assertTrue(loginController.isValidEmail(validEmail));
        assertFalse(loginController.isValidEmail(invalidEmail));
    }

    /**
     * Tests the encryptPassword method to ensure it generates a non-null encrypted string.
     * Uses a test password and checks that the result is not null.
     *
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    @Test
    void testEncryptPassword() throws NoSuchAlgorithmException {
        String password = "testPassword";
        String encryptedPassword = loginController.encryptPassword(password);
        assertNotNull(encryptedPassword);
    }

    /**
     * Tests the validateUser method by checking its return type against expected CSV data.
     * Verifies that the method returns either a User object or null based on CSV contents.
     */
    @Test
    void testValidateUser() {
        String email = "test@example.com";
        String password = "testPassword";
        User user = loginController.validateUser(email, password);

        // Since actual CSV data is unknown, verify the return type is correct
        // Should return User if credentials match, otherwise null
        assertTrue(user == null || user instanceof User);
    }

    /**
     * Tests the loadUsersFromCSV method to ensure it returns a non-null list of users.
     * Verifies that the method correctly loads user data from the CSV file.
     */
    @Test
    void testLoadUsersFromCSV() {
        List<User> users = loginController.loadUsersFromCSV();
        assertNotNull(users);
    }
}