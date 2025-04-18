package com.example.userstory4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UserSearchController {

    public static class User {
        private final String nickname;
        private final String email;
        private final String gender;
        private final String dateOfBirth;

        public User(String nickname, String email, String gender, String dateOfBirth) {
            this.nickname = nickname;
            this.email = email;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
        }

        public String getNickname() { return nickname; }
        public String getEmail() { return email; }
        public String getGender() { return gender; }
        public String getDateOfBirth() { return dateOfBirth; }
    }

    @FXML
    private TextField searchField;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 打印当前工作目录，用于调试
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // 使用相对路径加载 CSV 文件
        loadUsersFromCSV("AdminDashboard/users.csv");  // 相对路径
    }

    private void loadUsersFromCSV(String filePath) {
        // 检查文件是否存在，调试用
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
        } else {
            System.out.println("File found: " + file.getAbsolutePath());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 5) {
                    String nickname = values[0].trim();  // Nickname is a string
                    String email = values[2].trim();
                    String gender = values[3].trim();
                    String dateOfBirth = values[4].trim();

                    User user = new User(nickname, email, gender, dateOfBirth);
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Failed to load users from file: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String input = searchField.getText();

        if (input == null || input.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a valid nickname.");
            return;
        }

        for (User user : userList) {
            if (user.getNickname().equalsIgnoreCase(input)) {
                showUserInfo(user);
                return;
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Not Found", "User not found.");
    }

    private void showUserInfo(User user) {
        String info = "Nickname: " + user.getNickname() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Gender: " + user.getGender() + "\n"
                + "Date of Birth: " + user.getDateOfBirth();
        showAlert(Alert.AlertType.INFORMATION, "User Information", info);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
