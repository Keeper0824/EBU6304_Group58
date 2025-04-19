package com.example.transaction;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
         FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/main/main.fxml"));
         Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        primaryStage.setTitle("Transaction Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}