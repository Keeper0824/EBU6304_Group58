package src.main.java.card_management_story12;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankCardManagerFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/card_mangement_story12/main_view.fxml"));
        Scene scene = new Scene(loader.load(), 1600, 900);
        stage.setScene(scene);
        stage.setTitle("Bank Card Manager");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}