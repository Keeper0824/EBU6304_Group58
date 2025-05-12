package src.main.java.CashFlowVisualization_story15;

import src.main.java.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Title      : CashFlowView.java
 * Description: Entry point for the Cash Flow Visualization feature (Story15).
 *              Loads the UI from FXML, initializes the controller with the current user's
 *              transaction data, applies styling, and displays the main window. Ensures
 *              proper shutdown of background tasks when the window is closed.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class CashFlowView extends Application {

    /**
     * The nickname of the current user, retrieved from the session.
     */
    private static final String currentUser = Session.getCurrentNickname();

    /**
     * Starts the JavaFX application for cash flow visualization.
     * Loads the FXML layout, passes the current user's CSV path to the controller,
     * sets up the scene and stylesheet, and shows the primary stage. Also registers
     * a close request handler to stop any background tasks in the controller.
     *
     * @param primaryStage the main stage provided by the JavaFX runtime
     * @throws IOException if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/src/main/resources/CashFlowVisualization_story15/ui.fxml"
                )
        );
        AnchorPane root = loader.load();

        CashFlowController controller = loader.getController();
        String csvFilePath = "data/" + currentUser + "_transaction.csv";
        try {
            controller.loadTransactionsFromCSV(csvFilePath);
        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
        }

        Scene scene = new Scene(root, 1600, 1100);
        scene.getStylesheets().add(
                getClass().getResource(
                        "/src/main/resources/CashFlowVisualization_story15/styles.css"
                ).toExternalForm()
        );

        primaryStage.setTitle("Cash Flow Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ensure background threads/tasks are stopped on window close
        primaryStage.setOnCloseRequest(e -> controller.stop());
    }

    /**
     * Launches the Cash Flow Visualization application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
