package src.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserManagementApp extends Application {

    private static final String CSV_FILE = "users.csv";

    private TextField modifyNicknameField;
    private TextField modifyEmailField;
    private ChoiceBox<String> modifyGenderChoiceBox;
    private DatePicker modifyDateOfBirthPicker;

    private TableView<User> tableView;

    public UserManagementApp(User user) {
        // 初始化表格并只显示当前用户的信息
        tableView = new TableView<>();
        tableView.getItems().add(user);

        TableColumn<User, String> nicknameColumn = new TableColumn<>("Nickname");
        nicknameColumn.setCellValueFactory(cellData -> cellData.getValue().nicknameProperty());
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        TableColumn<User, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        TableColumn<User, String> dateOfBirthColumn = new TableColumn<>("Date of Birth");
        dateOfBirthColumn.setCellValueFactory(cellData -> cellData.getValue().dateOfBirthProperty());

        tableView.getColumns().addAll(nicknameColumn, emailColumn, genderColumn, dateOfBirthColumn);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Management");

        // Create modify user information form
        GridPane modifyForm = new GridPane();
        modifyForm.setAlignment(Pos.CENTER);
        modifyForm.setHgap(10);
        modifyForm.setVgap(10);
        modifyForm.setPadding(new Insets(25, 25, 25, 25));

        Label modifyNicknameLabel = new Label("Nickname:");
        modifyNicknameField = new TextField();
        modifyForm.add(modifyNicknameLabel, 0, 0);
        modifyForm.add(modifyNicknameField, 1, 0);

        Label modifyEmailLabel = new Label("Email:");
        modifyEmailField = new TextField();
        modifyEmailField.setEditable(false); // 邮箱字段不可编辑
        modifyForm.add(modifyEmailLabel, 0, 1);
        modifyForm.add(modifyEmailField, 1, 1);

        Label modifyGenderLabel = new Label("Gender:");
        modifyGenderChoiceBox = new ChoiceBox<>();
        modifyGenderChoiceBox.getItems().addAll("Male", "Female", "Other");
        modifyForm.add(modifyGenderLabel, 0, 2);
        modifyForm.add(modifyGenderChoiceBox, 1, 2);

        Label modifyDateOfBirthLabel = new Label("Date of Birth:");
        modifyDateOfBirthPicker = new DatePicker();
        modifyForm.add(modifyDateOfBirthLabel, 0, 3);
        modifyForm.add(modifyDateOfBirthPicker, 1, 3);

        Button modifyButton = new Button("Modify");
        modifyButton.setOnAction(e -> modifyUser());
        modifyForm.add(modifyButton, 1, 4);

        // Create main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(tableView, modifyForm);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 初始化修改表单的值
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            modifyNicknameField.setText(selectedUser.getNickname());
            modifyEmailField.setText(selectedUser.getEmail());
            modifyGenderChoiceBox.setValue(selectedUser.getGender());
            modifyDateOfBirthPicker.setValue(LocalDate.parse(selectedUser.getDateOfBirth()));
        }
    }

    private void modifyUser() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to modify.");
            return;
        }

        String nickname = modifyNicknameField.getText();
        String email = modifyEmailField.getText();
        String gender = modifyGenderChoiceBox.getValue();
        String dateOfBirth = modifyDateOfBirthPicker.getValue().toString();

        selectedUser.setNickname(nickname);
        selectedUser.setGender(gender);
        selectedUser.setDateOfBirth(dateOfBirth);

        saveUsers();
        showAlert("Success", "User information modified successfully.");
    }

    private void saveUsers() {
        List<User> users = loadUsersFromCSV();
        // 更新当前用户的信息
        for (User user : users) {
            if (user.getEmail().equals(modifyEmailField.getText())) {
                user.setNickname(modifyNicknameField.getText());
                user.setGender(modifyGenderChoiceBox.getValue());
                user.setDateOfBirth(modifyDateOfBirthPicker.getValue().toString());
                break;
            }
        }
        saveUsersToCSV(users);
    }

    private List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    users.add(new User(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void saveUsersToCSV(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            bw.write("Nickname,Password,Email,Gender,DateOfBirth");
            bw.newLine();
            for (User user : users) {
                bw.write(user.getNickname() + "," + user.getPassword() + "," + user.getEmail() + "," + user.getGender() + "," + user.getDateOfBirth());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}