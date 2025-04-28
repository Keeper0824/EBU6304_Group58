package src.main.java.Reminder_story_19_20;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.main.java.ViewMembershipTime_story14.UserController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/src/main/resources/Reminder_story_19_20/ReminderView.fxml"));
        Scene scene = new Scene(root, 1600, 900);
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/Reminder_story_19_20/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Notifications");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
