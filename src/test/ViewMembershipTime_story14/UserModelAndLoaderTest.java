package src.test.ViewMembershipTime_story14;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import src.main.java.Session;
import src.main.java.ViewMembershipTime_story14.User;
import src.main.java.ViewMembershipTime_story14.UserLoader;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Title      : UserModelAndLoaderTest.java
 * Description: Unit tests for the User model and UserLoader utility in the
 *              ViewMembershipTime_story14 package. Verifies:
 *              - VIP users with valid expiry dates are loaded correctly,
 *              - Non-VIP users yield a null expiry date,
 *              - Behavior when no session is set or nickname does not match.
 *
 * @author Haoran Sun
 * @version 1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserModelAndLoaderTest {

    @TempDir
    Path tempDir;

    /**
     * Initializes the JavaFX toolkit to avoid headless errors
     * when UserLoader or other JavaFX-dependent code runs.
     */
    @BeforeAll
    public void initJavaFx() {
        new javafx.embed.swing.JFXPanel();
    }

    /**
     * Clears any existing session nickname before each test
     * to ensure a clean test environment.
     */
    @BeforeEach
    public void clearSession() {
        Session.setCurrentNickname(null);
    }

    /**
     * Tests that a VIP user record in CSV with a valid future expiry date
     * is loaded correctly, with the expiry parsed into a LocalDate.
     */
    @Test
    public void testLoadUserFromCSV_VIP() throws IOException {
        // Prepare temporary CSV file
        Path csv = tempDir.resolve("u.csv");
        Files.writeString(csv,
                "ID,Nickname,Pwd,Email,Gender,DOB,isVIP,Expiry\n" +
                        "1,carol,xxx,carol@e,g,F,VIP,2050-12-31\n"
        );
        // Set current session user
        Session.setCurrentNickname("carol");
        // Act
        User u = UserLoader.loadUserFromCSV(csv.toString());
        // Verify
        assertNotNull(u, "VIP user should be loaded");
        assertEquals("carol", u.getUsername(), "Username should match CSV");
        assertEquals(LocalDate.of(2050, 12, 31), u.getMembershipExpiryDate(),
                "Expiry date should be parsed correctly");
    }

    /**
     * Tests that a non-VIP user record in CSV results in a User
     * with a null expiry date.
     */
    @Test
    public void testLoadUserFromCSV_NonVIP() throws IOException {
        Path csv = tempDir.resolve("u2.csv");
        Files.writeString(csv,
                "ID,Nickname,Pwd,Email,Gender,DOB,isVIP,Expiry\n" +
                        "2,dave,yyy,dave@e,m,M,Normal,null\n"
        );
        Session.setCurrentNickname("dave");
        User u = UserLoader.loadUserFromCSV(csv.toString());
        assertNotNull(u, "Non-VIP user should be loaded");
        assertEquals("dave", u.getUsername(), "Username should match CSV");
        assertNull(u.getMembershipExpiryDate(), "Non-VIP user should have null expiryDate");
    }

    /**
     * Tests behavior when no session nickname is set
     * or when the session user does not match any CSV record.
     */
    @Test
    public void testLoadUserFromCSV_NoMatchOrNoSession() throws IOException {
        Path csv = tempDir.resolve("u3.csv");
        Files.writeString(csv,
                "header\n" +
                        "3,ellen,zzz,e@e,f,F,VIP,2025-01-01\n"
        );
        // No session set
        assertNull(UserLoader.loadUserFromCSV(csv.toString()),
                "Without a session user, loader should return null");
        // Session set but nickname does not match
        Session.setCurrentNickname("frank");
        assertNull(UserLoader.loadUserFromCSV(csv.toString()),
                "With non-matching nickname, loader should return null");
    }
}
