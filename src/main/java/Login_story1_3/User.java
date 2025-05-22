package src.main.java.Login_story1_3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import src.main.java.AbstractUser;

/**
 * Title      : User.java
 * Description: Represents a user in the login system with JavaFX properties.
 *              Stores ID, nickname, encrypted password, email, gender, date of birth,
 *              membership type (e.g., VIP or Normal), and expiry date.
 *              Provides getters, setters, and property accessors for data binding.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class User extends AbstractUser {
    private final StringProperty ID;
    private final StringProperty nickname;
    private final StringProperty password;
    private final StringProperty email;
    private final StringProperty gender;
    private final StringProperty dateOfBirth;
    private final StringProperty membershipType;
    private final StringProperty expiryDate;

    /**
     * Constructs a new User with the specified values.
     *
     * @param ID             unique identifier for the user
     * @param nickname       display name or nickname
     * @param password       SHA-256 hashed and Base64 encoded password
     * @param email          user's email address
     * @param gender         user's gender
     * @param dateOfBirth    date of birth in ISO format (yyyy-MM-dd)
     * @param membershipType "VIP" or "Normal" membership status
     * @param expiryDate     membership expiry date in ISO format, or "null"
     */
    public User(String ID, String nickname, String password, String email,
                String gender, String dateOfBirth, String membershipType,
                String expiryDate) {
        super(nickname);
        this.ID = new SimpleStringProperty(ID);
        this.nickname = new SimpleStringProperty(nickname);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
        this.membershipType = new SimpleStringProperty(membershipType);
        this.expiryDate = new SimpleStringProperty(expiryDate);
    }

    /**
     * Gets the user ID.
     *
     * @return the ID string
     */
    public String getID() {
        return ID.get();
    }

    /**
     * Sets the user ID.
     *
     * @param ID the new ID to set
     */
    public void setID(String ID) {
        this.ID.set(ID);
    }

    /**
     * Gets the user's membership type.
     *
     * @return the membership type ("VIP" or "Normal")
     */
    public String getMembershipType() {
        return membershipType.get();
    }

    /**
     * Sets the user's membership type.
     *
     * @param membershipType the new membership type to set
     */
    public void setMembershipType(String membershipType) {
        this.membershipType.set(membershipType);
    }

    /**
     * Gets the membership expiry date.
     *
     * @return the expiry date string, or "null" if none
     */
    public String getExpiryDate() {
        return expiryDate.get();
    }

    /**
     * Sets the membership expiry date.
     *
     * @param expiryDate the expiry date string to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate.set(expiryDate);
    }

    /**
     * Gets the user's nickname.
     *
     * @return the nickname string
     */
    public String getNickname() {
        return nickname.get();
    }

    /**
     * Sets the user's nickname.
     *
     * @param nickname the new nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    /**
     * Gets the user's encrypted password.
     *
     * @return the password string
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Sets the user's password.
     *
     * @param password the new encrypted password to set
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * Gets the user's email address.
     *
     * @return the email string
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Sets the user's email address.
     *
     * @param email the new email to set
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * Gets the user's gender.
     *
     * @return the gender string
     */
    public String getGender() {
        return gender.get();
    }

    /**
     * Sets the user's gender.
     *
     * @param gender the new gender to set
     */
    public void setGender(String gender) {
        this.gender.set(gender);
    }

    /**
     * Gets the user's date of birth.
     *
     * @return the date of birth string
     */
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    /**
     * Sets the user's date of birth.
     *
     * @param dateOfBirth the new date of birth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    /**
     * Provides the StringProperty for ID for data binding.
     *
     * @return the ID property
     */
    public StringProperty IDProperty() {
        return ID;
    }

    /**
     * Provides the StringProperty for nickname for data binding.
     *
     * @return the nickname property
     */
    public StringProperty nicknameProperty() {
        return nickname;
    }

    /**
     * Provides the StringProperty for password for data binding.
     *
     * @return the password property
     */
    public StringProperty passwordProperty() {
        return password;
    }

    /**
     * Provides the StringProperty for email for data binding.
     *
     * @return the email property
     */
    public StringProperty emailProperty() {
        return email;
    }

    /**
     * Provides the StringProperty for gender for data binding.
     *
     * @return the gender property
     */
    public StringProperty genderProperty() {
        return gender;
    }

    /**
     * Provides the StringProperty for dateOfBirth for data binding.
     *
     * @return the dateOfBirth property
     */
    public StringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    /**
     * Provides the StringProperty for membershipType for data binding.
     *
     * @return the membershipType property
     */
    public StringProperty membershipTypeProperty() {
        return membershipType;
    }

    /**
     * Provides the StringProperty for expiryDate for data binding.
     *
     * @return the expiryDate property
     */
    public StringProperty expiryDateProperty() {
        return expiryDate;
    }
}
