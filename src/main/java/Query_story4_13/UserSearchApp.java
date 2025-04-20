package src.main.java.Query_story4_13;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.main.java.Login_story1_3.LoginApp;

public class UserSearchApp extends Application {

    // In UserSearchApp.java, modify the start method
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Query_story4_13/UserSearch.fxml"));
        VBox root = loader.load();

        // Add logout button to the layout
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 18px; -fx-pref-height: 40px;");
        logoutButton.setOnAction(e -> {
            try {
                new LoginApp().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().add(logoutButton);
        VBox.setMargin(logoutButton, new Insets(20, 0, 0, 0));

        // Set background image
        Image bgImage = new Image(getClass().getResource("/src/main/resources/Query_story4_13/images/background.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root);
        primaryStage.setTitle("User Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
