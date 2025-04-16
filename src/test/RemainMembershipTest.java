package src.test;
import java.time.LocalDate;
import src.main.java.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RemainMembershipTest {
    @Test
    void testIsMembershipActive_True() {
        User user = new User("testuser", LocalDate.now().plusDays(5));
        assertTrue(user.isMembershipActive(), "会员应为激活状态");
    }

    @Test
    void testIsMembershipActive_False() {
        User user = new User("testuser", LocalDate.now().minusDays(-1));
        assertFalse(user.isMembershipActive(), "会员应为过期状态");
    }

    @Test
    void testGetRemainingDays_Positive() {
        User user = new User("testuser", LocalDate.now().plusDays(10));
        assertEquals(10, user.getRemainingDays(), "剩余天数应为 10 天");
    }

    @Test
    void testGetRemainingDays_Negative() {
        User user = new User("testuser", LocalDate.now().minusDays(-2));
        assertEquals(-2, user.getRemainingDays(), "应返回负数表示已过期");
    }
}
