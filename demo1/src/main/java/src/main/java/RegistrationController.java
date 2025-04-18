package src.main.java;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class RegistrationController {
    @FXML private TextField nicknameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private DatePicker dateOfBirthPicker;

    @FXML
    private void initialize() {
        // 初始化性别选择框
        genderChoiceBox.getItems().addAll("Male", "Female", "Other");

        // 设置日期选择器的默认限制（可选）
        dateOfBirthPicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now())); // 禁止选择未来日期
            }
        });
    }

    @FXML
    private void handleRegister() {
        try {
            // 验证字段
            if (!validateFields()) return;

            // 创建用户对象
            User newUser = new User(
                    nicknameField.getText(),
                    passwordField.getText(),
                    emailField.getText(),
                    genderChoiceBox.getValue(),
                    dateOfBirthPicker.getValue().toString()
            );

            // 保存用户（这里应该实现您的保存逻辑）
            saveUser(newUser);

            // 显示成功提示
            showAlert("Success", "Registration successful!", () -> {
                try {
                    switchToLoginScreen(); // 注册成功后跳转登录界面
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            showAlert("Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        switchToLoginScreen();
    }

    private boolean validateFields() {
        if (nicknameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                genderChoiceBox.getValue() == null ||
                dateOfBirthPicker.getValue() == null) {

            showAlert("Error", "All fields are required!");
            return false;
        }

        // 可以添加更多验证逻辑，例如邮箱格式验证
        if (!emailField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Error", "Invalid email format!");
            return false;
        }

        return true;
    }

    private void saveUser(User user) {
        // 实现您的用户保存逻辑
        // 例如：保存到数据库或文件
        try {
            // 这里应该是您的保存逻辑
            System.out.println("Saving user: " + user.getNickname());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

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

    private void showAlert(String title, String message) {
        showAlert(title, message, null);
    }

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
}