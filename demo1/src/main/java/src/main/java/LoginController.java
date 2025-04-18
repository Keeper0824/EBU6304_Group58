package src.main.java;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() throws Exception {
        String email = emailField.getText();
        String password = passwordField.getText();
        User user = validateUser(email, password);

        if (user != null) {
            showAlert("Success", "Login successful.");
            // 打开用户管理界面
            new UserManagementApp(user).start(new Stage());
            // 关闭当前窗口
            ((Stage) emailField.getScene().getWindow()).close();
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }


    @FXML
    private void handleRegister() throws Exception {
        new RegistrationApp().start(new Stage());
        ((Stage) emailField.getScene().getWindow()).close();
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
        // 实现与之前相同
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}