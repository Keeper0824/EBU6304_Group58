package src.main.java.ViewMembershipTime_story14;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class User {
    private String username;
    private LocalDate membershipExpiryDate;

    public User(String username, LocalDate membershipExpiryDate) {
        this.username = username;
        this.membershipExpiryDate = membershipExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    public boolean isMembershipActive() {
        return LocalDate.now().isBefore(membershipExpiryDate);
    }

    public long getRemainingDays() {
        return ChronoUnit.DAYS.between(LocalDate.now(), membershipExpiryDate);
    }
}

