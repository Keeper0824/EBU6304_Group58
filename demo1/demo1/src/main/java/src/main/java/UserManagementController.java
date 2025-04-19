package src.main.java;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class UserManagementController {
    @FXML private TableView<User> userTable;
    @FXML private TextField nicknameField;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private DatePicker dobPicker;

    private User currentUser;

    // 初始化方法
    @FXML
    private void initialize() {
        // 初始化性别选择框
        genderChoiceBox.getItems().addAll("Male", "Female", "Other");

        // 设置表格选择监听器
        userTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        Platform.runLater(this::updateFormFields);
                    }
                });
    }

    // 设置当前用户
    public void setUser(User user) {
        this.currentUser = user;
        Platform.runLater(() -> {
            userTable.getItems().clear();
            userTable.getItems().add(user);
            updateFormFields();
        });
    }

    // 更新表单字段
    private void updateFormFields() {
        try {
            if (currentUser != null) {
                nicknameField.setText(currentUser.getNickname());
                emailField.setText(currentUser.getEmail());
                genderChoiceBox.setValue(currentUser.getGender());

                // 安全解析日期
                if (currentUser.getDateOfBirth() != null && !currentUser.getDateOfBirth().isEmpty()) {
                    dobPicker.setValue(LocalDate.parse(currentUser.getDateOfBirth()));
                }
            }
        } catch (DateTimeParseException e) {
            showAlert("Error", "Invalid date format: " + currentUser.getDateOfBirth());
            dobPicker.setValue(null);
        }
    }

    // 处理更新操作
    @FXML
    private void handleUpdate() {
        try {
            // 验证字段
            if (nicknameField.getText().isEmpty() ||
                    genderChoiceBox.getValue() == null ||
                    dobPicker.getValue() == null) {
                showAlert("Error", "All fields are required!");
                return;
            }

            // 更新用户信息
            currentUser.setNickname(nicknameField.getText());
            currentUser.setGender(genderChoiceBox.getValue());
            currentUser.setDateOfBirth(dobPicker.getValue().toString());

            // 保存更改
            saveUserChanges(currentUser);

            // 刷新表格
            Platform.runLater(() -> {
                userTable.refresh();
                showAlert("Success", "User updated successfully!", () -> {
                    // 更新成功后的操作（可选）
                });
            });

        } catch (Exception e) {
            showAlert("Error", "Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 保存用户更改
    private void saveUserChanges(User user) {
        // 实现你的保存逻辑（如写入CSV或数据库）
        try {
            List<User> users = loadUsersFromCSV();
            boolean found = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getEmail().equals(user.getEmail())) {
                    users.set(i, user);
                    found = true;
                    break;
                }
            }

            if (!found) {
                users.add(user);
            }

            saveUsersToCSV(users);

            System.out.println("User changes saved successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user changes", e);
        }
    }

    // 从CSV文件加载用户
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

    // 将用户信息保存到CSV文件
    private void saveUsersToCSV(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("user.csv"))) {
            for (User user : users) {
                bw.write(String.join(",", user.getNickname(), user.getPassword(), user.getEmail(), user.getGender(), user.getDateOfBirth()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save users to CSV.");
        }
    }

    // 显示弹窗（线程安全版本）
    private void showAlert(String title, String message) {
        showAlert(title, message, null);
    }

    private void showAlert(String title, String message, Runnable postAction) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            // 确保在UI线程执行showAndWait
            alert.showAndWait().ifPresent(response -> {
                if (postAction != null) {
                    postAction.run();
                }
            });
        });
    }
}