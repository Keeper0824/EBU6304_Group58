package src.main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/ui.fxml"));
        Scene scene = new Scene(loader.load());

        // Get the controller instance and initialize it
        UserController controller = loader.getController();
        controller.initialize();  // Initialize with user data

        // Apply the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/style.css").toExternalForm());

        primaryStage.setTitle("VIP Membership Status");
        primaryStage.setScene(scene);

        // Set window size to 1600x900
        primaryStage.setWidth(1600);
        primaryStage.setHeight(900);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
