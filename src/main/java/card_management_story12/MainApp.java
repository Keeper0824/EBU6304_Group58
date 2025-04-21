package src.main.java.card_management_story12;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        File fxmlFile = new File("src/main/resources/card_management_story12/card_manager.fxml");
        URL fxmlUrl = fxmlFile.toURI().toURL();
        if (fxmlUrl == null) {
            throw new RuntimeException("FXML文件未找到！");
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setTitle("Bank Card Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}