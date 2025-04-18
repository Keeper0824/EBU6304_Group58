package com.example.consumptionmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 加载主界面FXML
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/consumptionmanager/views/MainView.fxml"));

        // 创建背景图片
        Image backgroundImage = new Image(getClass().getResourceAsStream("/com/example/images/2.jpg"));
        ImageView backgroundView = new ImageView(backgroundImage);

        // 设置背景图片填充方式
        backgroundView.setFitWidth(1600);
        backgroundView.setFitHeight(900);
        backgroundView.setPreserveRatio(false);

        // 创建StackPane布局，将背景和内容叠加
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, root);

        // 创建场景并设置大小
        Scene scene = new Scene(stackPane, 1600, 900);

        // 设置舞台
        primaryStage.setTitle("消费信息管理系统");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // 禁止调整窗口大小
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}