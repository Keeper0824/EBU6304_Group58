package src.main.java.ViewMembershipTime_story14;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/ViewMembershipTime_story14/ui.fxml"));
        Scene scene = new Scene(loader.load(), 1600, 900);  // Set fixed size

        // Get the controller instance and initialize it
        UserController controller = loader.getController();
        controller.initialize();

        // Apply the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/src/main/resources/ViewMembershipTime_story14/style.css").toExternalForm());

        primaryStage.setTitle("VIP Membership Status");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}