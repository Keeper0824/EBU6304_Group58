package src.main.java.Reminder_story_19_20;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Title      : Main.java
 * Description: Entry point for the Reminder feature (Story 19-20).
 *              This JavaFX application loads the reminder view from FXML,
 *              applies the stylesheet, and displays the notifications window.
 *              Serves as the launcher for the schedule reminders UI.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class Main extends Application {

    /**
     * The main entry point for the JavaFX application.
     * Loads the ReminderView FXML layout, attaches the CSS stylesheet,
     * sets the scene on the primary stage, and displays it.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(
                "/src/main/resources/Reminder_story_19_20/ReminderView.fxml"
        ));
        Scene scene = new Scene(root, 1600, 900);
        scene.getStylesheets().add(getClass().getResource(
                "/src/main/resources/Reminder_story_19_20/style.css"
        ).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Notifications");
        primaryStage.show();
    }

    /**
     * Launches the Reminder application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
