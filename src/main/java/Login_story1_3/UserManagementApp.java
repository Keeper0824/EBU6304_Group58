package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserManagementApp extends Application {
    private User user;

    public UserManagementApp() {
        // 无参构造函数
    }

    public UserManagementApp(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/UserManagement.fxml"));
        Parent root = loader.load();

        UserManagementController controller = loader.getController();
        if (user != null) {
            controller.setUser(user);
        }

        primaryStage.setTitle("User Management");
        primaryStage.setScene(new Scene(root, 1200, 590));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}