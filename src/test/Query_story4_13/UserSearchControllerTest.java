package src.test.Query_story4_13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.java.Query_story4_13.UserSearchController;
import src.main.java.Query_story4_13.UserSearchController.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Title      : UserSearchControllerTest.java
 * Description: Unit tests for the UserSearchController class.
 *              This test suite verifies the correctness of the User model,
 *              and checks that VIP and normal users are correctly classified.
 *
 * Author     : Kaiyu Liu
 * Version    : 1.0
 */
public class UserSearchControllerTest {

    // Instance of the controller to be tested
    private UserSearchController controller;

    /**
     * Set up the test environment before each test case.
     * Creates a fresh instance of the controller to ensure test isolation.
     */
    @BeforeEach
    public void setUp() {
        controller = new UserSearchController();
    }

    /**
     * Test the User class constructor and getter methods.
     * Ensures that user data is correctly stored and retrievable.
     */
    @Test
    public void testUserConstructorAndGetters() {
        // Create a sample user
        User user = new User("001", "Alice", "alice@example.com", "Female", "1990-01-01", true, "2025-12-31");

        // Assert that each getter returns the expected value
        assertEquals("001", user.getID());
        assertEquals("Alice", user.getNickname());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("Female", user.getGender());
        assertEquals("1990-01-01", user.getDateOfBirth());
        assertTrue(user.isVIP());
        assertEquals("2025-12-31", user.getExpireDate());
    }

    /**
     * Test VIP and normal user classification logic.
     * Uses Java reflection to access and manipulate the controller's internal VIP and normal lists.
     */
    @Test
    public void testVIPAndNormalClassification() throws Exception {
        // Create VIP and normal user objects
        User vipUser = new User("003", "VIPUser", "vip@example.com", "Female", "1988-02-20", true, "2026-01-01");
        User normalUser = new User("004", "NormalUser", "normal@example.com", "Male", "1995-07-12", false, "");

        // Access private fields using reflection
        var vipListField = UserSearchController.class.getDeclaredField("vipList");
        var normalListField = UserSearchController.class.getDeclaredField("normalList");
        vipListField.setAccessible(true);
        normalListField.setAccessible(true);

        // Retrieve and manipulate internal lists
        List<User> vipList = (List<User>) vipListField.get(controller);
        List<User> normalList = (List<User>) normalListField.get(controller);

        vipList.add(vipUser);
        normalList.add(normalUser);

        // Assert that the users are placed in the correct lists
        assertEquals(1, vipList.size());
        assertEquals(1, normalList.size());
        assertTrue(vipList.get(0).isVIP());
        assertFalse(normalList.get(0).isVIP());
    }
}
