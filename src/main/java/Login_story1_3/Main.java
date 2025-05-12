package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Title      : Main.java
 * Description: Entry point for the Main Menu JavaFX application.
 *              It loads the MainLayout.fxml, injects HostServices into the controller,
 *              applies the stylesheet, and displays the primary stage at 1600×900.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Initializes and shows the primary stage.
     * Loads the FXML layout, sets HostServices on the controller,
     * applies the CSS stylesheet, and configures stage dimensions.
     *
     * @param stage the primary stage for this application
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/src/main/resources/Login_story1_3/MainLayout.fxml")
        );
        Parent root = loader.load();

        // Pass HostServices to the layout controller
        LayoutController ctrl = loader.getController();
        ctrl.setHostServices(getHostServices());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/src/main/resources/Login_story1_3/styles.css")
                        .toExternalForm()
        );

        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.setWidth(1600);
        stage.setHeight(900);
        stage.show();
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
