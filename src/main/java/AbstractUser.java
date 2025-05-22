package src.main.java;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 */
public abstract class AbstractUser {
    protected String username;

    public AbstractUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}


