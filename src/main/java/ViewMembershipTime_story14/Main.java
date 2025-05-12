package src.main.java.ViewMembershipTime_story14;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Title      : Main.java
 * Description: This program is a JavaFX application that displays the VIP Membership Status UI.
 * It loads the FXML layout, initializes the controller, applies the CSS stylesheet,
 * and shows the primary stage with a fixed size window.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * Loads the FXML, initializes the controller, applies styling, and displays the stage.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/ViewMembershipTime_story14/ui.fxml"));
        Scene scene = new Scene(loader.load(), 1600, 900);  // Set fixed size

        // Get the controller instance and initialize it
        UserController controller = loader.getController();
        controller.initialize();

        // Apply the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/ViewMembershipTime_story14/style.css").toExternalForm());

        primaryStage.setTitle("VIP Membership Status");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
