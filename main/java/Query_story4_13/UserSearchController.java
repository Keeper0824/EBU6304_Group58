package src.main.java.Query_story4_13;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.stage.Stage;
import src.main.java.Login_story1_3.LoginApp;

public class UserSearchController {

    public static class User {
        private final String id;
        private final String nickname;
        private final String email;
        private final String gender;
        private final String dateOfBirth;
        private final boolean isVIP; // VIP 状态字段

        public User(String id, String nickname, String email, String gender, String dateOfBirth, boolean isVIP) {
            this.id = id;
            this.nickname = nickname;
            this.email = email;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.isVIP = isVIP;
        }

        public String getID() {return id;}
        public String getNickname() { return nickname; }
        public String getEmail() { return email; }
        public String getGender() { return gender; }
        public String getDateOfBirth() { return dateOfBirth; }
        public boolean isVIP() { return isVIP; } // 获取VIP状态
    }

    @FXML
    private TextField searchField;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<User> vipList = FXCollections.observableArrayList(); // 存储VIP用户
    private ObservableList<User> normalList = FXCollections.observableArrayList(); // 存储Normal用户

    // In UserSearchController.java, add this method
    @FXML
    private void handleLogout() {
        try {
            new LoginApp().start(new Stage());
            ((Stage) searchField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to login screen.");
        }
    }

    @FXML
    private void initialize() {
        // 打印当前工作目录，用于调试
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // 使用你提供的路径加载用户数据
        loadUsersFromCSV("data/user.csv");  // 使用上传的文件路径
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
                if (values.length >= 8) {  // 确保CSV格式正确

                    String ID = values[0].trim();
                    String nickname = values[1].trim();  // Nickname is a string
                    String email = values[3].trim();
                    String gender = values[4].trim();
                    String dateOfBirth = values[5].trim();

                    // 根据CSV中的VIP状态判断用户是否是VIP
                    boolean isVIP = values[6].trim().equalsIgnoreCase("VIP");

                    User user = new User(ID, nickname, email, gender, dateOfBirth, isVIP);
                    userList.add(user);
                    if (isVIP) {
                        vipList.add(user); // 如果是VIP用户，则加入VIP列表
                    } else {
                        normalList.add(user); // 如果是Normal用户，则加入Normal列表
                    }
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

        if (input.trim().equalsIgnoreCase("VIP")) {
            // 如果输入是 "VIP"，显示所有 VIP 用户
            showVIPList();
            return;
        }

        if (input.trim().equalsIgnoreCase("normal")) {
            // 如果输入是 "normal"，显示所有 Normal 用户
            showNormalList();
            return;
        }

        // 如果输入不是VIP或者normal，查询单个用户
        for (User user : userList) {
            if (user.getNickname().equalsIgnoreCase(input)) {
                showUserInfo(user);
                return;
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Not Found", "User not found.");
    }

    private void showUserInfo(User user) {
        String info = "ID: " + user.getID() + "\n"
                + "Nickname: " + user.getNickname() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Gender: " + user.getGender() + "\n"
                + "Date of Birth: " + user.getDateOfBirth();
        if (user.isVIP()) {
            info += "\nVIP Status: Yes";
        }
        showAlert(Alert.AlertType.INFORMATION, "User Information", info);
    }

    private void showVIPList() {
        StringBuilder vipNames = new StringBuilder("List of VIP Users:\n");
        for (User vipUser : vipList) {
            vipNames.append(vipUser.getNickname()).append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "VIP Users", vipNames.toString());
    }

    private void showNormalList() {
        StringBuilder normalNames = new StringBuilder("List of Normal Users:\n");
        for (User normalUser : normalList) {
            normalNames.append(normalUser.getNickname()).append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "Normal Users", normalNames.toString());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
