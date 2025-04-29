package src.main.java.Login_story1_3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty ID;
    private final StringProperty nickname;
    private final StringProperty password;
    private final StringProperty email;
    private final StringProperty gender;
    private final StringProperty dateOfBirth;
    private final StringProperty membershipType;
    private final StringProperty expiryDate;


    public User(String ID,String nickname, String password, String email, String gender, String dateOfBirth, String membershipType, String expiryDate) {
        this.ID = new SimpleStringProperty(ID);
        this.nickname = new SimpleStringProperty(nickname);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
        this.membershipType = new SimpleStringProperty(membershipType);
        this.expiryDate = new SimpleStringProperty(expiryDate);
    }

    public String getID() {
        return ID.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getMembershipType() {
        return membershipType.get();
    }

    public void setMembershipType(String membershipType) {}

    public String getExpiryDate() {
        return expiryDate.get();
    }

    public void setExpiryDate(String expiryDate) {}

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

    public StringProperty IDProperty() {
        return ID;
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

    public StringProperty expiryDateProperty() {
        return expiryDate;
    }

    public StringProperty membershipTypeProperty() {
        return membershipType;
    }
}