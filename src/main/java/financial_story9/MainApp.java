// 修改后的MainApp类
package src.main.java.financial_story9;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/financial_story9/main_view.fxml"));
        Parent root = loader.load();

        // 设置根布局的背景图片
        root.setStyle("-fx-background-image: url('/src/main/resources/financial_story9/images/background.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center;");

        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setTitle("Financial Analysis System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}