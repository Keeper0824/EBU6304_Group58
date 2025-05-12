package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Title      : RegistrationApp.java
 * Description: JavaFX application class for the registration view.
 *              It loads the FXML file, sets up the scene, and displays the window.
 *
 * @author Zhengxuan Han
 * @version 1.0
 */


public class RegistrationApp extends Application {
    /**
     * The start method is the entry point for JavaFX applications.
     * It sets up the registration window with the specified FXML file, scene size, position, and displays the window.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception If there is an error loading the FXML file or other exceptions occur.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the registration screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/Registration.fxml"));
        Parent root = loader.load();

        // Set the scene size to 1600x900
        Scene scene = new Scene(root, 1600, 900);

        // Center the window on the screen
        primaryStage.centerOnScreen();
        // Set the title of the window
        primaryStage.setTitle("Registration");
        // Set the scene for the primary stage
        primaryStage.setScene(scene);
        // Display the window
        primaryStage.show();
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}