package src.main.java.Login_story1_3;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField captchaField;
    @FXML
    private Label captchaLabel;
    @FXML
    private Button refreshCaptchaBtn;

    private String currentCaptcha;

    @FXML
    public void initialize() {
        generateCaptcha();
        refreshCaptchaBtn.setOnAction(event -> generateCaptcha());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String userCaptcha = captchaField.getText();

        if (!userCaptcha.equalsIgnoreCase(currentCaptcha)) {
            showAlert("Error", "Invalid verification code!");
            generateCaptcha();
            return;
        }

        User user = validateUser(email, password);

        if (user != null) {
            showAlert("Success", "Login successful.");
            try {
                new MainMenuApp(user).start(new Stage());
                ((Stage) emailField.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open main menu window.");
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.");
            generateCaptcha();
        }
    }

    // 密码加密方法（与注册时使用的相同）
    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private User validateUser(String email, String password) {
        List<User> users = loadUsersFromCSV();
        try {
            String encryptedPassword = encryptPassword(password);
            for (User user : users) {
                if (user.getEmail().equals(email) && user.getPassword().equals(encryptedPassword)) {
                    return user;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            showAlert("Error", "System error during login.");
        }
        return null;
    }

    // 其他方法保持不变...
    private void generateCaptcha() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(chars.length());
            captcha.append(chars.charAt(index));
        }

        currentCaptcha = captcha.toString();
        captchaLabel.setText(currentCaptcha);
        captchaField.clear();
    }

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

    private List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/user.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    User user = new User(data[0], data[1], data[2], data[3], data[4],data[5],data[6],data[7]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load users from CSV.");
        }
        return users;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
