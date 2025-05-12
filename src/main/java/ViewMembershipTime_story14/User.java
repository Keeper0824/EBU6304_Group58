package src.main.java.ViewMembershipTime_story14;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Title      : User.java
 * Description: This class models a user with a VIP membership. It stores the username
 * and the membership expiry date, and provides methods to check if the membership is
 * still active as well as to calculate the remaining days until expiration.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class User {
    private String username;
    private LocalDate membershipExpiryDate;

    /**
     * Constructs a new User with the given username and membership expiry date.
     *
     * @param username               the user's name or identifier
     * @param membershipExpiryDate   the date when the VIP membership expires
     */
    public User(String username, LocalDate membershipExpiryDate) {
        this.username = username;
        this.membershipExpiryDate = membershipExpiryDate;
    }

    /**
     * Gets the username of this user.
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the expiry date of this user's VIP membership.
     *
     * @return the membership expiry date
     */
    public LocalDate getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    /**
     * Checks whether the user's VIP membership is currently active.
     *
     * @return {@code true} if today is before the expiry date; {@code false} otherwise
     */
    public boolean isMembershipActive() {
        return LocalDate.now().isBefore(membershipExpiryDate);
    }

    /**
     * Calculates the number of days remaining until the membership expires.
     *
     * @return the number of days from today until expiry (may be negative if already expired)
     */
    public long getRemainingDays() {
        return ChronoUnit.DAYS.between(LocalDate.now(), membershipExpiryDate);
    }
}
