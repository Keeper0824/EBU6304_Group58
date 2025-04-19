package src.main.java;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty nickname;
    private final StringProperty password;
    private final StringProperty email;
    private final StringProperty gender;
    private final StringProperty dateOfBirth;

    public User(String nickname, String password, String email, String gender, String dateOfBirth) {
        this.nickname = new SimpleStringProperty(nickname);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
    }

    public String getNickname() {
        return nickname.get();
    }

    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public StringProperty nicknameProperty() {
        return nickname;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }
}