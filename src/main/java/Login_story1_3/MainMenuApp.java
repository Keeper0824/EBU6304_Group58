package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuApp extends Application {
    private static User currentUser;  // Make static to maintain user across instances

    // Constructor with user
    public MainMenuApp(User user) {
        currentUser = user;
    }

    // Default constructor
    public MainMenuApp() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/MainMenu.fxml"));

        // Set controller factory to pass the current user
        loader.setControllerFactory(param -> {
            if (currentUser != null) {
                return new MainMenuController(currentUser);
            }
            return new MainMenuController(); // Fallback if no user
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 1600, 900);

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Static method to get current user
    public static User getCurrentUser() {
        return currentUser;
    }

    // Static method to set current user
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}