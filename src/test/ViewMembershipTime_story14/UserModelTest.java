package src.test.ViewMembershipTime_story14;

import org.junit.jupiter.api.Test;
import src.main.java.ViewMembershipTime_story14.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Title      : UserModelTest.java
 * Description: Unit tests for the User model class in the ViewMembershipTime_story14 package.
 *              Verifies that the constructor, getters, isMembershipActive(), and getRemainingDays()
 *              behave correctly for both future and past expiry dates.
 *
 * @author Haoran
 * @version 1.0
 */
public class UserModelTest {

    /**
     * Tests that:
     * 1) getUsername() and getMembershipExpiryDate() return the values passed to the constructor,
     * 2) isMembershipActive() returns true when expiry is in the future and false when in the past,
     * 3) getRemainingDays() returns the correct positive or negative day difference.
     */
    @Test
    public void testGettersAndFlags() {
        LocalDate today = LocalDate.now();

        // Case 1: expiry 2 days in the future
        User u = new User("sun", today.plusDays(2));
        assertEquals("sun", u.getUsername(), "Username getter should return the constructor value");
        assertEquals(today.plusDays(2), u.getMembershipExpiryDate(),
                "Expiry date getter should return the constructor value");
        assertTrue(u.isMembershipActive(),
                "Membership should be active when expiry is in the future");
        assertEquals(2, u.getRemainingDays(),
                "getRemainingDays() should return positive days until expiry");

        // Case 2: expiry 1 day in the past
        u = new User("sun", today.minusDays(1));
        assertFalse(u.isMembershipActive(),
                "Membership should be inactive when expiry is in the past");
        assertEquals(-1, u.getRemainingDays(),
                "getRemainingDays() should return negative days when already expired");
    }
}
