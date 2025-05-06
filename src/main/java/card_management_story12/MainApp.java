package src.main.java.card_management_story12;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 加载FXML文件并设置控制器
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/card_management_story12/card_manager.fxml"));
            Scene scene = new Scene(loader.load());

            // 设置页面大小为 1600x1000
            primaryStage.setWidth(1600);
            primaryStage.setHeight(1000);

            // 显示主窗口
            primaryStage.setScene(scene);
            primaryStage.setTitle("Card Management");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
