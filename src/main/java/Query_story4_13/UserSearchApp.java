package src.main.java.Query_story4_13;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.main.java.Login_story1_3.LoginApp;

/**
 * Title      : UserSearchApp.java
 * Description: Launches the User Search interface with a logout button and background image.
 *              This class loads the corresponding FXML layout, sets a background, and manages
 *              navigation to the login screen upon logout.
 *
 * @author
 * @version     1.0
 */
public class UserSearchApp extends Application {

    /**
     * Starts the JavaFX application by setting up the user interface for user search.
     * Loads the layout from FXML, adds a logout button, and sets a background image.
     *
     * @param primaryStage the primary window of this application
     * @throws Exception if the FXML file cannot be loaded or other errors occur
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Query_story4_13/UserSearch.fxml"));
        VBox root = loader.load();

        // Create and configure the logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 18px; -fx-pref-height: 40px;");
        logoutButton.setOnAction(e -> {
            try {
                new LoginApp().start(new Stage()); // Launch the login screen
                primaryStage.close();              // Close the current window
            } catch (Exception ex) {
                ex.printStackTrace();             // Log any exceptions
            }
        });

        // Add the logout button to the layout with margin
        root.getChildren().add(logoutButton);
        VBox.setMargin(logoutButton, new Insets(20, 0, 0, 0));

        // Load and set the background image for the UI
        Image bgImage = new Image(getClass().getResource("/src/main/resources/Query_story4_13/images/background.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        root.setBackground(new Background(backgroundImage));

        // Finalize and show the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("User Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method launches the JavaFX application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
