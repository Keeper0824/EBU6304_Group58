package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Title      : LoginApp.java
 * Description: JavaFX application class for the login view.
 *              It loads the FXML file, sets up the scene, and displays the window.
 *
 * @author Zhengxuan Han
 * @version 1.0
 */

public class LoginApp extends Application {
    /**
     * The start method is the entry point for JavaFX applications.
     * It sets up the login window with the specified FXML file, scene size, position, and minimum dimensions.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If there is an error loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/Login.fxml"));
        Parent root = loader.load();

        // Set the scene size to the screen ratio (1600x900)
        Scene scene = new Scene(root, 1600, 900);

        // Set the initial position of the window to be centered on the screen
        primaryStage.centerOnScreen();

        // Set the minimum dimensions of the window
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);

        // Set the title of the window
        primaryStage.setTitle("Login System");
        // Set the scene for the primary stage
        primaryStage.setScene(scene);
        // Display the window
        primaryStage.show();
    }
}