package src.main.java.card_management_story12;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

/**
 * Title      : MainApp.java
 * Description: Entry point for the Card Management module (Story 12).
 *              It launches a JavaFX application, loads the card_manager.fxml layout,
 *              sets the window size to 1600×1000, and displays the primary stage.
 *
 * @author Yudian Wang
 * @version 1.0
 * @author Haoran Sun
 * @version 1.1
 */
public class MainApp extends Application {

    /**
     * Initializes and shows the primary stage for card management.
     * Loads the FXML layout, applies it to a new Scene, sets the window dimensions,
     * and displays the stage with the title "Card Management".
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/src/main/resources/card_management_story12/card_manager.fxml")
            );
            Scene scene = new Scene(loader.load());

            primaryStage.setWidth(1600);
            primaryStage.setHeight(1000);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Card Management");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the Card Management JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
