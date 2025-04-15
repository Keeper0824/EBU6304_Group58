package src.main;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import java.util.Arrays;
import java.util.List;

public class CashFlowView extends Application {
    @Override
    public void start(Stage primaryStage) {
        // 假数据
        List<Transaction> data = Arrays.asList(
                new Transaction("income", 1000),
                new Transaction("expense", 400),
                new Transaction("income", 600),
                new Transaction("expense", 300)
        );

        CashFlowChartController controller = new CashFlowChartController(data);
        VBox layout = new VBox();
        layout.getChildren().addAll(
                controller.generatePieChart(),
                controller.generateBarChart()
        );

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setTitle("Cash Flow Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

