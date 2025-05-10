package src.main.java.ViewMembershipTime_story14;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RechargePopupController {

    @FXML
    private Button oneMonthButton;

    @FXML
    private Button oneQuarterButton;

    @FXML
    private Button oneYearButton;

    @FXML
    private void initialize() {
        if (oneMonthButton != null) {
            oneMonthButton.setOnAction(e -> handleMembershipSelection(1));
        }
        if (oneQuarterButton != null) {
            oneQuarterButton.setOnAction(e -> handleMembershipSelection(3));
        }
        if (oneYearButton != null) {
            oneYearButton.setOnAction(e -> handleMembershipSelection(12));
        }
    }

    private void handleMembershipSelection(int months) {
        Stage stage = (Stage) oneMonthButton.getScene().getWindow();
        UserController userController = new UserController();
        userController.handleMembershipSelection(months, stage);
        stage.close();

        //userController.refreshMembershipInfo();
    }
}