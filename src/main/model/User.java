package main.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {
    private String username;
    private String password;
    private LocalDateTime registrationTime;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.registrationTime = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRegistrationTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return registrationTime.format(formatter);
    }

    public String toCSV() {
        return String.format("\"%s\",\"%s\",\"%s\"", 
                            username, password, getRegistrationTime());
    }
}