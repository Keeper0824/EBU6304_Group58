package src.main.java.Login_story1_3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.*;

/**
 * Title      : RegistrationController.java
 * Description: Controller for the registration view.
 *              It handles user registration, validates fields, encrypts passwords,
 *              and saves user information to a CSV file.
 *
 * @author Zhengxuan Han
 * @version 1.0
 */

public class RegistrationController {
    @FXML private TextField nicknameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private DatePicker dateOfBirthPicker;

    /**
     * Initializes the registration form.
     * Sets the date picker to disable future dates.
     */
    @FXML
    private void initialize() {
        // Set the date picker to disable future dates
        dateOfBirthPicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now())); // Disable selecting future dates
            }
        });
    }

    /**
     * Handles the register button click event.
     * Validates the form fields, encrypts the password, generates a new user ID,
     * creates a user object, saves the user to the CSV file, and shows a success or error alert.
     * If registration is successful, it switches to the login screen.
     */
    @FXML
    private void handleRegister() {
        try {
            // Validate fields
            if (!validateFields()) return;

            // Encrypt the password
            String encryptedPassword = encryptPassword(passwordField.getText());

            // Generate a new user ID
            String newId = getNextUserId();

            // Create a new user object
            User newUser = new User(
                    newId,
                    nicknameField.getText(),
                    encryptedPassword, // Store the encrypted password
                    emailField.getText(),
                    genderChoiceBox.getValue(),
                    dateOfBirthPicker.getValue().toString(),
                    "Normal",
                    "null"
            );

            // Save the user
            saveUser(newUser);

            // Show a success alert and switch to the login screen after the alert is closed
            showAlert("Success", "Registration successful!", () -> {
                try {
                    switchToLoginScreen(); // Switch to the login screen after successful registration
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            showAlert("Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Encrypts the given password using the SHA-256 algorithm and Base64 encoding.
     *
     * @param password The plaintext password to be encrypted.
     * @return The Base64-encoded SHA-256 hash of the password.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Handles the back button click event.
     * Switches to the login screen.
     */
    @FXML
    private void handleBack() {
        switchToLoginScreen();
    }

    /**
     * Validates the form fields.
     * Checks if all required fields are filled and if the email format is valid.
     * Shows an error alert if validation fails.
     *
     * @return true if all fields are valid; false otherwise.
     */
    private boolean validateFields() {
        if (nicknameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                genderChoiceBox.getValue() == null ||
                dateOfBirthPicker.getValue() == null) {

            showAlert("Error", "All fields are required!");
            return false;
        }

        if (!isValidEmail(emailField.getText())) {
            showAlert("Error", "Invalid email format!");
            return false;
        }

        return true;
    }

    /**
     * Checks if the given email address is in a valid format.
     *
     * @param email The email address to be checked.
     * @return true if the email is valid; false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Saves the user information to the CSV file.
     * Converts the user object to a CSV line and appends it to the file.
     *
     * @param user The user object to be saved.
     */
    private void saveUser(User user) {
        System.out.println("Saving user: " + user.getNickname());
        try {
            List<String> userData = new ArrayList<>();
            userData.add(user.getID());
            userData.add(user.getNickname());
            userData.add(user.getPassword()); // Store the encrypted password
            userData.add(user.getEmail());
            userData.add(user.getGender());
            userData.add(user.getDateOfBirth());
            userData.add("Normal");
            userData.add("null");

            String[] userArray = userData.toArray(new String[0]);
            String csvContent = String.join(",", userArray);

            String filePath = "data/user.csv";
            FileWriter writer = new FileWriter(filePath, true);
            writer.append(csvContent);
            writer.append("\n");
            writer.flush();
            writer.close();

            System.out.println("User data saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save user data: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save user", e);
        }
    }

    /**
     * Switches to the login screen.
     * Closes the current registration window and opens the login window.
     */
    private void switchToLoginScreen() {
        try {
            Platform.runLater(() -> {
                try {
                    new LoginApp().start(new Stage());
                    ((Stage) nicknameField.getScene().getWindow()).close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert with the given title and message.
     * Calls the overloaded showAlert method with a null post-action.
     *
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(String title, String message) {
        showAlert(title, message, null);
    }

    /**
     * Displays an alert with the given title, message, and an optional post-action.
     * Runs on the JavaFX application thread.
     *
     * @param title The title of the alert.
     * @param message The message of the alert.
     * @param postAction An optional callback function to be executed after the alert is closed.
     */
    private void showAlert(String title, String message, Runnable postAction) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.showAndWait().ifPresent(response -> {
                if (postAction != null) {
                    postAction.run();
                }
            });
        });
    }

    /**
     * Gets the next user ID.
     * Reads the last line of the user.csv file, extracts the last user ID, and increments it by 1.
     * If the file is empty or an error occurs, returns "1".
     *
     * @return The next user ID as a string.
     */
    private String getNextUserId() {
        String lastLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader("data/user.csv"))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lastLine = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!lastLine.isEmpty()) {
            String[] parts = lastLine.split(",");
            try {
                int lastId = Integer.parseInt(parts[0].trim());
                return String.valueOf(lastId + 1);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return "1";
    }
}