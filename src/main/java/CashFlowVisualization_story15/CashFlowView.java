package src.main.java.CashFlowVisualization_story15;

import src.main.java.Session;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CashFlowView extends Application {

    private final static String currentUser = Session.getCurrentNickname();

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/CashFlowVisualization_story15/ui.fxml"));
        AnchorPane root = loader.load();

        CashFlowController controller = loader.getController();
        // Load transactions from CSV
        String csvFilePath = "data/" + currentUser + "_transaction.csv";
        try {
            controller.loadTransactionsFromCSV(csvFilePath);
        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
        }

        Scene scene = new Scene(root, 1600, 1100);  // Changed to match other windows
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/CashFlowVisualization_story15/styles.css").toExternalForm());

        primaryStage.setTitle("Cash Flow Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle window close to stop background threads
        primaryStage.setOnCloseRequest(e -> controller.stop());
    }

    public static void main(String[] args) {
        launch(args);
    }
}