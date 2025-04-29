package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/Login.fxml"));
        Parent root = loader.load();

        // 设置场景大小为屏幕比例 (1600x900)
        Scene scene = new Scene(root, 1600, 900);

        // 设置窗口初始位置居中
        primaryStage.centerOnScreen();

        // 设置窗口最小尺寸
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);

        primaryStage.setTitle("Login System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}