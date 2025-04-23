//package src.main.java.Login_story1_3;
//
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.stage.Stage;
//import src.main.java.Session;
//
//import java.io.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeParseException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserManagementController {
//    @FXML private TableView<User> userTable;
//    @FXML private TextField nicknameField;
//    @FXML private TextField emailField;
//    @FXML private ChoiceBox<String> genderChoiceBox;
//    @FXML private DatePicker dobPicker;
//
//    private User currentUser2;
////    private final static String currentUser = Session.getCurrentNickname();
//
//    // 初始化方法
//    @FXML
//    private void initialize() {
//        // 初始化性别选择框
//        genderChoiceBox.getItems().addAll("Male", "Female", "Other");
//
//        // 设置表格选择监听器
//        userTable.getSelectionModel().selectedItemProperty().addListener(
//                (obs, oldSelection, newSelection) -> {
//                    if (newSelection != null) {
//                        Platform.runLater(this::updateFormFields);
//                    }
//                });
//    }
//
//    // 添加回调接口
//    private Runnable returnToMainMenuCallback;
//
//    public void setReturnToMainMenuCallback(Runnable callback) {
//        this.returnToMainMenuCallback = callback;
//    }
//
//    @FXML
//    private void handleBack() {
//        if (returnToMainMenuCallback != null) {
//            // 关闭当前窗口
//            Stage stage = (Stage) userTable.getScene().getWindow();
//            stage.close();
//
//            // 执行回调打开主菜单
//            returnToMainMenuCallback.run();
//        }
//    }
//
//    // 设置当前用户
//    public void setUser(User user) {
//        this.currentUser2 = user;
//        Platform.runLater(() -> {
//            userTable.getItems().clear();
//            userTable.getItems().add(user);
//            updateFormFields();
//        });
//    }
//
//    // 更新表单字段
//    private void updateFormFields() {
//        try {
//            if (currentUser2 != null) {
//                nicknameField.setText(currentUser2.getNickname());
//                emailField.setText(currentUser2.getEmail());
//                genderChoiceBox.setValue(currentUser2.getGender());
//
//                // 安全解析日期
//                if (currentUser2.getDateOfBirth() != null && !currentUser2.getDateOfBirth().isEmpty()) {
//                    dobPicker.setValue(LocalDate.parse(currentUser2.getDateOfBirth()));
//                }
//            }
//        } catch (DateTimeParseException e) {
//            showAlert("Error", "Invalid date format: " + currentUser2.getDateOfBirth());
//            dobPicker.setValue(null);
//        }
//    }
//
//    // 处理更新操作
//    @FXML
//    private void handleUpdate() {
//        try {
//            // 验证字段
//            if (nicknameField.getText().isEmpty() ||
//                    genderChoiceBox.getValue() == null ||
//                    dobPicker.getValue() == null) {
//                showAlert("Error", "All fields are required!");
//                return;
//            }
//
//            // 更新用户信息
//            currentUser2.setNickname(nicknameField.getText());
//            currentUser2.setGender(genderChoiceBox.getValue());
//            currentUser2.setDateOfBirth(dobPicker.getValue().toString());
//
//            // 保存更改
//            saveUserChanges(currentUser2);
//
//            // 刷新表格
//            Platform.runLater(() -> {
//                userTable.refresh();
//                showAlert("Success", "User updated successfully!", () -> {
//                    // 更新成功后的操作（可选）
//                });
//            });
//
//        } catch (Exception e) {
//            showAlert("Error", "Update failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // 保存用户更改
//    private void saveUserChanges(User user) {
//        // 实现你的保存逻辑（如写入CSV或数据库）
//        try {
//            List<User> users = loadUsersFromCSV();
//            boolean found = false;
//            for (int i = 0; i < users.size(); i++) {
//                if (users.get(i).getEmail().equals(user.getEmail())) {
//                    users.set(i, user);
//                    found = true;
//                    break;
//                }
//            }
//
//            if (!found) {
//                users.add(user);
//            }
//
//            saveUsersToCSV(users);
//
//            System.out.println("User changes saved successfully");
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to save user changes", e);
//        }
//    }
//
//    // 从CSV文件加载用户
//    private List<User> loadUsersFromCSV() {
//        List<User> users = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader("data/user.csv"))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] data = line.split(",");
//                if (data.length == 8) {
//                    User user = new User(data[0], data[1], data[2], data[3], data[4],data[5],data[6],data[7]);
//                    users.add(user);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error", "Failed to load users from CSV.");
//        }
//        return users;
//    }
//
//    // 将用户信息保存到CSV文件
//    private void saveUsersToCSV(List<User> users) {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/user.csv"))) {
//            for (User user : users) {
//                bw.write(String.join(",", user.getID(),user.getNickname(), user.getPassword(), user.getEmail(), user.getGender(), user.getDateOfBirth(),
//                        user.getMembershipType(), user.getExpiryDate()));
//                bw.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error", "Failed to save users to CSV.");
//        }
//    }
//
//    // 显示弹窗（线程安全版本）
//    private void showAlert(String title, String message) {
//        showAlert(title, message, null);
//    }
//
//    private void showAlert(String title, String message, Runnable postAction) {
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle(title);
//            alert.setHeaderText(null);
//            alert.setContentText(message);
//
//            // 确保在UI线程执行showAndWait
//            alert.showAndWait().ifPresent(response -> {
//                if (postAction != null) {
//                    postAction.run();
//                }
//            });
//        });
//    }
//}

package src.main.java.Login_story1_3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.main.java.Session;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class UserManagementController {
    @FXML private TableView<User> userTable;
    @FXML private TextField nicknameField;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private DatePicker dobPicker;

    // 拿到 Session 里存的昵称
    private static final String sessionNickname = Session.getCurrentNickname();
    private User currentUser;

    @FXML
    private void initialize() {
        // 1. 初始化性别选项 & 表格列绑定（确保你的 FXML 里已经定义了 TableColumn）
        genderChoiceBox.getItems().addAll("Male", "Female", "Other");

        // 2. 自加载：根据 sessionNickname 从 CSV 里找出对应的 User
        List<User> all = loadUsersFromCSV();
        for (User u : all) {
            if (sessionNickname.equals(u.getNickname())) {
                currentUser = u;
                break;
            }
        }

        // 3. 如果找到了，就填数据
        if (currentUser != null) {
            userTable.getItems().add(currentUser);
            updateFormFields();
        } else {
            showAlert("Error", "No user found for: " + sessionNickname);
        }

        // 4. 可选：继续设置选中行监听，或留给后续更新用
        userTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldU, newU) -> {
                    if (newU != null) {
                        currentUser = newU;
                        Platform.runLater(this::updateFormFields);
                    }
                });
    }

    // 添加回调接口
    private Runnable returnToMainMenuCallback;

    public void setReturnToMainMenuCallback(Runnable callback) {
        this.returnToMainMenuCallback = callback;
    }

    @FXML
    private void handleBack() {
        if (returnToMainMenuCallback != null) {
            // 关闭当前窗口
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.close();

            // 执行回调打开主菜单
            returnToMainMenuCallback.run();
        }
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

    // 将用户信息保存到CSV文件
    private void saveUsersToCSV(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/user.csv"))) {
            for (User user : users) {
                bw.write(String.join(",", user.getID(),user.getNickname(), user.getPassword(), user.getEmail(), user.getGender(), user.getDateOfBirth(),
                        user.getMembershipType(), user.getExpiryDate()));
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