package src.main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoginApp extends Application {

    private static final String CSV_FILE = "users.csv";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label userLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        grid.add(userLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            User user = validateUser(email, password);
            if (user != null) {
                showAlert("Success", "Login successful.");
                // 打开用户管理界面，并且只显示当前用户的信息
                new UserManagementApp(user).start(primaryStage);
            } else {
                showAlert("Login Failed", "Invalid email or password.");
            }
        });

        registerButton.setOnAction(e -> {
            // 打开注册界面
            new RegistrationApp().start(primaryStage);
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
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