package src.main.java.Login_story1_3;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import src.main.java.Query_story4_13.UserSearchApp;
import src.main.java.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

/**
 * Title      : LoginController.java
 * Description: Controller for the login view.
 *              It handles user authentication with email/password and CAPTCHA,
 *              supports an admin backdoor, opens the appropriate next window,
 *              and ensures VIP status is updated if expired.
 *
 * @author Zhengxuan Han
 * @version 1.0
 * @author Haoran Sun
 * @version 1.1
 */
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField captchaField;
    @FXML private Label captchaLabel;
    @FXML private Button refreshCaptchaBtn;

    private String currentCaptcha;

    /**
     * Initializes the login form by generating the first CAPTCHA
     * and wiring the refresh button to regenerate it.
     */
    @FXML
    public void initialize() {
        generateCaptcha();
        refreshCaptchaBtn.setOnAction(event -> generateCaptcha());
    }

    /**
     * Handles the login button press:
     * 1) Verifies the CAPTCHA.
     * 2) Allows admin login for "Administration@qq.com"/"000000".
     * 3) Validates the user against CSV credentials.
     * 4) Updates session nickname and opens the main menu on success.
     * 5) Checks and updates VIP expiry status after login.
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String userCaptcha = captchaField.getText();

        // 0. Email format check
        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format!");
            generateCaptcha();
            return;
        }

        // 1. CAPTCHA check
        if (!userCaptcha.equalsIgnoreCase(currentCaptcha)) {
            showAlert("Error", "Invalid verification code!");
            generateCaptcha();
            return;
        }

        // 2. Admin backdoor
        if (email.equals("Administration@qq.com") && password.equals("000000")) {
            try {
                new UserSearchApp().start(new Stage());
                ((Stage) emailField.getScene().getWindow()).close();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open user search window.");
                return;
            }
        }

        // 3. User credential validation
        User user = validateUser(email, password);
        if (user != null) {
            Session.setCurrentNickname(user.getNickname());
            showAlert("Success", "Login successful.");
            try {
                // Close login window
                Stage currentStage = (Stage) emailField.getScene().getWindow();
                currentStage.close();

                // Open main menu
                Main mainApp = new Main();
                Stage mainStage = new Stage();
                mainApp.start(mainStage);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open main menu window.");
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.");
            generateCaptcha();
            return;
        }

        // 4. Post-login VIP expiry check and update
        if ("VIP".equalsIgnoreCase(user.getMembershipType())) {
            String exp = user.getExpiryDate(); // e.g. "2025-05-01" or "null"
            if (exp != null && !exp.equalsIgnoreCase("null")) {
                LocalDate expireDate = LocalDate.parse(exp, DateTimeFormatter.ISO_DATE);
                if (expireDate.isBefore(LocalDate.now())) {
                    user.setMembershipType("Normal");                       // update in memory
                    updateVIPStatusInCSV(user.getID(), "Normal");           // persist update
                }
            }
        }
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Encrypts a plaintext password using SHA-256 and Base64 encoding.
     *
     * @param password the raw password to encrypt
     * @return the Base64-encoded SHA-256 hash
     * @throws NoSuchAlgorithmException if SHA-256 is not available
     */
    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Validates the user's email and password against records in data/user.csv.
     *
     * @param email    the user's email
     * @param password the plaintext password
     * @return a User object if credentials match; null otherwise
     */

    public User validateUser(String email, String password) {
        List<User> users = loadUsersFromCSV();
        try {
            String encryptedPassword = encryptPassword(password);
            for (User user : users) {
                if (user.getEmail().equals(email) &&
                        user.getPassword().equals(encryptedPassword)) {
                    return user;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            showAlert("Error", "System error during login.");
        }
        return null;
    }

    /**
     * Updates the VIP status field in the CSV for the given user ID.
     *
     * @param userId    the ID of the user to update
     * @param newStatus the new VIP status ("VIP" or "Normal")
     */
    private void updateVIPStatusInCSV(String userId, String newStatus) {
        Path path = Paths.get("data/user.csv");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<String> updated = lines.stream()
                    .map(line -> {
                        String[] f = line.split(",");
                        if (f.length >= 7 && f[0].equals(userId)) {
                            f[6] = newStatus;  // update isVIP column
                            // leave ExpiryDate (column 8) unchanged
                            return String.join(",", f);
                        }
                        return line;
                    })
                    .collect(Collectors.toList());
            Files.write(path, updated, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to update VIP status in CSV.");
        }
    }

    /**
     * Generates a new 4-character alphanumeric CAPTCHA,
     * displays it in the label, and clears the input field.
     */
    private void generateCaptcha() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        currentCaptcha = captcha.toString();
        captchaLabel.setText(currentCaptcha);
        captchaField.clear();
    }

    /**
     * Opens the registration window and closes the login form.
     */
    @FXML
    private void handleRegister() {
        try {
            new RegistrationApp().start(new Stage());
            ((Stage) emailField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open registration window.");
        }
    }

    /**
     * Loads all users from data/user.csv into a List.
     *
     * @return a List of User objects parsed from the CSV
     */
    public List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/user.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    users.add(new User(data[0], data[1], data[2], data[3],
                            data[4], data[5], data[6], data[7]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load users from CSV.");
        }
        return users;
    }

    /**
     * Displays an information alert with the given title and message.
     *
     * @param title   the alert title
     * @param message the alert message text
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
