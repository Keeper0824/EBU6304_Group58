package src.main.java.AI_story11_21_22;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Title      : MainApp.java
 * Description: This is the main application class that extends JavaFX Application.
 *              It loads the FXML file and sets up the main window of the application.
 *
 * @author Wei Muchi
 * @version 1.0
 */
public class MainApp extends Application {

    /**
     * Starts the JavaFX application.
     * It loads the FXML file, creates a scene, and sets it on the primary stage.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during the loading of the FXML file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/AI_story11_21_22/ConsumptionForecast.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1600, 900));
        primaryStage.setTitle("AI Empowered Personal Finance Tracker");
        primaryStage.show();
    }

    /**
     * The main entry point for the application.
     * It launches the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}