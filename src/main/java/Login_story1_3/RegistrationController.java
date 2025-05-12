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

public class RegistrationController {
    @FXML private TextField nicknameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private DatePicker dateOfBirthPicker;

    @FXML
    private void initialize() {
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

            // 加密密码
            String encryptedPassword = encryptPassword(passwordField.getText());

            // 自动计算新 ID
            String newId = getNextUserId();

            // 创建用户对象
            User newUser = new User(
                    newId,
                    nicknameField.getText(),
                    encryptedPassword, // 存储加密后的密码
                    emailField.getText(),
                    genderChoiceBox.getValue(),
                    dateOfBirthPicker.getValue().toString(),
                    "Normal",
                    "null"
            );

            // 保存用户
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

    // 密码加密方法
    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
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

        if (!isValidEmail(emailField.getText())) {
            showAlert("Error", "Invalid email format!");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    private void saveUser(User user) {
        System.out.println("Saving user: " + user.getNickname());
        try {
            List<String> userData = new ArrayList<>();
            userData.add(user.getID());
            userData.add(user.getNickname());
            userData.add(user.getPassword()); // 存储的是加密后的密码
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

    /**
     * 从 user.csv 读取最后一个 ID，然后返回 lastId+1，
     * 如果文件只有表头或出错，就返回 "1"
     */
    private String getNextUserId() {
        String lastLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader("data/user.csv"))) {
            String line;
            br.readLine(); // 跳过表头
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