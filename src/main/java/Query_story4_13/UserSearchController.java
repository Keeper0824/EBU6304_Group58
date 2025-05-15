package src.main.java.Query_story4_13;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.main.java.Login_story1_3.LoginApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Title      : UserSearchController.java
 * Description: Controller for handling user search logic in the application.
 *              Supports search by nickname, and listing VIP or normal users.
 *              Loads user data from a CSV file, and supports logout functionality.
 *
 * @author      Kaiyu Liu
 * @version     1.0
 */
public class UserSearchController {

    /**
     * Models a User with basic personal information, VIP status, and expiration date.
     */
    public static class User {
        private final String id;
        private final String nickname;
        private final String email;
        private final String gender;
        private final String dateOfBirth;
        private final boolean isVIP;
        private final String expireDate;

        /**
         * Constructs a new User object.
         *
         * @param id           the user ID
         * @param nickname     the user's nickname
         * @param email        the user's email address
         * @param gender       the user's gender
         * @param dateOfBirth  the user's date of birth
         * @param isVIP        whether the user is a VIP
         * @param expireDate   the VIP expiration date (if applicable)
         */
        public User(String id, String nickname, String email, String gender, String dateOfBirth, boolean isVIP, String expireDate) {
            this.id = id;
            this.nickname = nickname;
            this.email = email;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.isVIP = isVIP;
            this.expireDate = expireDate;
        }

        public String getID() { return id; }
        public String getNickname() { return nickname; }
        public String getEmail() { return email; }
        public String getGender() { return gender; }
        public String getDateOfBirth() { return dateOfBirth; }
        public boolean isVIP() { return isVIP; }
        public String getExpireDate() { return expireDate; }
    }

    @FXML
    private TextField searchField;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<User> vipList = FXCollections.observableArrayList();
    private ObservableList<User> normalList = FXCollections.observableArrayList();

    /**
     * Handles user logout by returning to the login screen.
     */
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

    /**
     * Initializes the controller. Called after the FXML file is loaded.
     * Loads user data from the CSV file.
     */
    @FXML
    private void initialize() {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        loadUsersFromCSV("data/user.csv");
    }

    /**
     * Loads users from the given CSV file into the internal lists.
     *
     * @param filePath the path to the CSV file
     */
    private void loadUsersFromCSV(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
        } else {
            System.out.println("File found: " + file.getAbsolutePath());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 8) {
                    String ID = values[0].trim();
                    String nickname = values[1].trim();
                    String email = values[3].trim();
                    String gender = values[4].trim();
                    String dateOfBirth = values[5].trim();
                    String expireDate = values[7].trim();
                    boolean isVIP = values[6].trim().equalsIgnoreCase("VIP");

                    User user = new User(ID, nickname, email, gender, dateOfBirth, isVIP, expireDate);
                    userList.add(user);
                    if (isVIP) {
                        vipList.add(user);
                    } else {
                        normalList.add(user);
                    }
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Failed to load users from file: " + e.getMessage());
        }
    }

    /**
     * Handles the user search action based on input in the search field.
     * Supports searching by nickname or listing all VIP/Normal users.
     */
    @FXML
    private void handleSearch() {
        String input = searchField.getText();

        if (input == null || input.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a valid nickname.");
            return;
        }

        if (input.trim().equalsIgnoreCase("VIP")) {
            showVIPList();
            return;
        }

        if (input.trim().equalsIgnoreCase("normal")) {
            showNormalList();
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

    /**
     * Displays detailed information of the selected user.
     *
     * @param user the user to display
     */
    private void showUserInfo(User user) {
        String info = "ID: " + user.getID() + "\n"
                + "Nickname: " + user.getNickname() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Gender: " + user.getGender() + "\n"
                + "Date of Birth: " + user.getDateOfBirth();
        if (user.isVIP()) {
            info += "\nVIP Status: Yes";
            info += "\nExpireDate:  " + user.getExpireDate();
        } else {
            info += "\nVIP Status: No";
        }
        showAlert(Alert.AlertType.INFORMATION, "User Information", info);
    }

    /**
     * Displays the list of all VIP users.
     */
    private void showVIPList() {
        StringBuilder vipNames = new StringBuilder("List of VIP Users:\n");
        for (User vipUser : vipList) {
            vipNames.append(vipUser.getNickname()).append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "VIP Users", vipNames.toString());
    }

    /**
     * Displays the list of all Normal users.
     */
    private void showNormalList() {
        StringBuilder normalNames = new StringBuilder("List of Normal Users:\n");
        for (User normalUser : normalList) {
            normalNames.append(normalUser.getNickname()).append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "Normal Users", normalNames.toString());
    }

    /**
     * Displays an alert with specified type, title, and content.
     *
     * @param type    the alert type (e.g., INFORMATION, ERROR)
     * @param title   the title of the alert
     * @param content the content/message of the alert
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
