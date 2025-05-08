package src.main.java.AI_story_11_21_22;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/AI_story11_21_22/ConsumptionForecast.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1600, 900));
        primaryStage.setTitle("AI Empowered Personal Finance Tracker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}