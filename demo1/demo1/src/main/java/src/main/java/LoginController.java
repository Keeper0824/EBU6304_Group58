package src.main.java;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        // 设置刷新验证码按钮的事件处理
        refreshCaptchaBtn.setOnAction(event -> generateCaptcha());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String userCaptcha = captchaField.getText();

        // 首先验证验证码
        if (!userCaptcha.equalsIgnoreCase(currentCaptcha)) {
            showAlert("Error", "Invalid verification code!");
            generateCaptcha(); // 重新生成验证码
            return;
        }

        User user = validateUser(email, password);

        if (user != null) {
            showAlert("Success", "Login successful.");
            try {
                // 打开主菜单界面并传递用户对象
                new MainMenuApp(user).start(new Stage());

                // 关闭当前窗口
                ((Stage) emailField.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open main menu window.");
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.");
            generateCaptcha(); // 登录失败重新生成验证码
        }
    }

    // 生成4位数字字母混合的验证码
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

    private User validateUser(String email, String password) {
        List<User> users = loadUsersFromCSV();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("user.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    User user = new User(data[0], data[1], data[2], data[3], data[4]);
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
