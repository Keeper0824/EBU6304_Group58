package src.main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CashFlowView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/ui.fxml"));
        AnchorPane root = loader.load();

        CashFlowController controller = loader.getController();
        // Load transactions from CSV
        String csvFilePath = "data/InOutcome.csv";  // Replace with your CSV file path
        try {
            controller.loadTransactionsFromCSV(csvFilePath);
        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
        }

        Scene scene = new Scene(root, 1600, 1100);
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/styles.css").toExternalForm());

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
