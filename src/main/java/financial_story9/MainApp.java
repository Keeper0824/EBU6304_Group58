package src.main.java.financial_story9;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Title      : MainApp.java
 * Description: The main entry point for launching the Financial Analysis System application.
 *              It loads the main view using FXML, sets the background image,
 *              and displays the application window with the specified size and title.
 *
 * @author Yudian Wang
 * @version 1.0
 */
public class MainApp extends Application {

    /**
     * This method is called when the application starts.
     * It loads the main FXML view, sets the background image, and configures the scene and stage.
     *
     * @param primaryStage The primary stage for this application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the main view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/financial_story9/main_view.fxml"));
        Parent root = loader.load();

        // Set the background image for the root layout
        root.setStyle("-fx-background-image: url('/src/main/resources/financial_story9/images/background.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center;");

        // Create a scene with the root layout and set the primary stage
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setTitle("Financial Analysis System"); // Set the window title
        primaryStage.setScene(scene); // Set the scene
        primaryStage.setMinWidth(800); // Set minimum window width
        primaryStage.setMinHeight(600); // Set minimum window height
        primaryStage.show(); // Display the window
    }

    /**
     * The main method to launch the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
