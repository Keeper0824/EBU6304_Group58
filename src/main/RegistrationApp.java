package src.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistrationApp extends Application {

    private static final String CSV_FILE = "users.csv";
    private Stage primaryStage;

    private TextField nicknameField;
    private PasswordField passwordField;
    private TextField emailField;
    private ChoiceBox<String> genderChoiceBox;
    private DatePicker dateOfBirthPicker;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Registration");

        GridPane registrationForm = new GridPane();
        registrationForm.setAlignment(Pos.CENTER);
        registrationForm.setHgap(10);
        registrationForm.setVgap(10);
        registrationForm.setPadding(new Insets(25, 25, 25, 25));

        Label nicknameLabel = new Label("Nickname:");
        nicknameField = new TextField();
        registrationForm.add(nicknameLabel, 0, 0);
        registrationForm.add(nicknameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        registrationForm.add(passwordLabel, 0, 1);
        registrationForm.add(passwordField, 1, 1);

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        registrationForm.add(emailLabel, 0, 2);
        registrationForm.add(emailField, 1, 2);

        Label genderLabel = new Label("Gender:");
        genderChoiceBox = new ChoiceBox<>();
        genderChoiceBox.getItems().addAll("Male", "Female", "Other");
        registrationForm.add(genderLabel, 0, 3);
        registrationForm.add(genderChoiceBox, 1, 3);

        Label dateOfBirthLabel = new Label("Date of Birth:");
        dateOfBirthPicker = new DatePicker();
        registrationForm.add(dateOfBirthLabel, 0, 4);
        registrationForm.add(dateOfBirthPicker, 1, 4);

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> registerUser());
        registrationForm.add(registerButton, 1, 5);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new LoginApp().start(primaryStage); // 返回登录界面
        });
        registrationForm.add(backButton, 0, 5);

        Scene scene = new Scene(registrationForm, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void registerUser() {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String gender = genderChoiceBox.getValue();
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        String dateOfBirthStr = dateOfBirth != null ? dateOfBirth.toString() : "";

        if (nickname.isEmpty() || password.isEmpty() || email.isEmpty() || gender == null || dateOfBirthStr.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        User newUser = new User(nickname, password, email, gender, dateOfBirthStr);
        saveUser(newUser);

        showAlert("Success", "User registered successfully.");
        primaryStage.close(); // 关闭注册界面
        new LoginApp().start(new Stage()); // 打开登录界面
    }

    private void saveUser(User user) {
        List<User> users = loadUsersFromCSV();
        users.add(user);
        saveUsersToCSV(users);
    }

    private List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        try {
            if (!Files.exists(Paths.get(CSV_FILE))) {
                createCSVFile();
            }
            try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
                String line;
                br.readLine(); // Skip header
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 5) {
                        users.add(new User(data[0], data[1], data[2], data[3], data[4]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void createCSVFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            bw.write("Nickname,Password,Email,Gender,DateOfBirth");
        } catch (IOException e) {
            e.printStackTrace();
        }
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