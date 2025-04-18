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
        System.out.println("FXML Path: " + getClass().getResource("/src/main/resources/ui.fxml")); // 检查路径是否正确
        System.out.println("CSS Path: " + getClass().getResource("/src/main/resources/styles.css"));
        System.out.println("Image Path: " + getClass().getResource("/src/main/resources/images/background.png"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/ui.fxml"));
        AnchorPane root = loader.load();
        System.out.println("1");
        // Get controller and set initial data
        CashFlowController controller = loader.getController();
        List<Transaction> initialTransactions = generateInitialData();
        controller.setTransactions(initialTransactions);

        Scene scene = new Scene(root, 1600, 900);
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/styles.css").toExternalForm());

        primaryStage.setTitle("Cash Flow Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle window close to stop background threads
        primaryStage.setOnCloseRequest(e -> controller.stop());
    }

    private List<Transaction> generateInitialData() {
        List<Transaction> transactions = new ArrayList<>();
        // Add some initial transactions
        transactions.add(new Transaction("income", 1500));
        transactions.add(new Transaction("income", 800));
        transactions.add(new Transaction("expense", 400));
        transactions.add(new Transaction("expense", 300));
        transactions.add(new Transaction("expense", 200));
        return transactions;
    }

    public static void main(String[] args) {
        launch(args);
    }
}