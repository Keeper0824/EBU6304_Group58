package src.main.java.Category_story6_7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Title      : MainApp.java
 * Description: This class is the entry point of the JavaFX application for the Transaction Management System.
 * It initializes the primary stage, loads the main FXML layout, and sets up the scene.
 *
 * @author Wei Chen
 * @version 1.0
 */
public class MainApp extends Application {
    /**
     * The main entry point of the application.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the primary stage of the application.
     * Loads the main FXML layout and sets up the scene with a fixed size.
     * @param primaryStage The primary stage of the JavaFX application.
     * @throws Exception If there is an error loading the FXML file or setting up the scene.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/src/main/resources/Category_story6_7/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        primaryStage.setTitle("Transaction Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}