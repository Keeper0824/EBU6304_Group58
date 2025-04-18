package src.main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserManagementApp extends Application {
    private User user;

    public UserManagementApp(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManagement.fxml"));
        Parent root = loader.load();

        UserManagementController controller = loader.getController();
        controller.setUser(user);

        primaryStage.setTitle("User Management");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}