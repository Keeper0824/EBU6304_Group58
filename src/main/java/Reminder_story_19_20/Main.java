package src.main.java.Login_story1_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/MainLayout.fxml"));
        Parent root = loader.load();
        // 传 HostServices
        LayoutController ctrl = loader.getController();
        ctrl.setHostServices(getHostServices());
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/Login_story1_3/styles.css").toExternalForm());
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.setWidth(1600);
        stage.setHeight(900);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

