package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Title      : UserManagementApp.java
 * Description: JavaFX application class for the user management view.
 *              It loads the FXML file, sets up the controller, and displays the window.
 *
 * @author Zhengxuan Han
 * @version 1.0
 */

public class UserManagementApp extends Application {
    private User user;

    /**
     * Default constructor.
     */
    public UserManagementApp() {
        // Default constructor
    }

    /**
     * Constructor with a user parameter.
     *
     * @param user The user object.
     */
    public UserManagementApp(User user) {
        this.user = user;
    }

    /**
     * Set the user object.
     *
     * @param user The user object.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * The start method is the entry point for JavaFX applications.
     * It sets up the user management window with the specified FXML file, passes the user object to the controller,
     * and displays the window.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception If there is an error loading the FXML file or other exceptions occur.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the user management screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/UserManagement.fxml"));
        Parent root = loader.load();

        // Get the controller of the FXML file
        UserManagementController controller = loader.getController();
        // If the user object is not null, set it to the controller
        if (user != null) {
            controller.setUser(user);
        }

        // Set the title of the window
        primaryStage.setTitle("User Management");
        // Set the scene for the primary stage with the specified size
        primaryStage.setScene(new Scene(root, 1200, 590));
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