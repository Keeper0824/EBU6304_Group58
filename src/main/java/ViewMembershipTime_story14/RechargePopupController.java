package src.main.java.ViewMembershipTime_story14;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Title      : RechargePopupController.java
 * Description: Controller for the membership recharge popup window.
 * Provides buttons for selecting 1-month, 3-month (quarter), or 12-month (year) VIP membership.
 * Sets up action handlers to forward the selected duration to the UserController and close the popup.
 *
 * @author Wei Chen
 * @version 1.0
 */
public class RechargePopupController {

    @FXML
    private Button oneMonthButton;

    @FXML
    private Button oneQuarterButton;

    @FXML
    private Button oneYearButton;

    /**
     * Initializes the popup by wiring each button to its corresponding
     * membership selection handler. This method is automatically called
     * by the FXMLLoader after the FXML fields have been injected.
     */
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

    /**
     * Handles the user's choice of membership duration.
     * Retrieves the current popup stage, delegates the selection to the UserController,
     * and then closes this popup window.
     *
     * @param months the number of months for the chosen membership plan (1, 3, or 12)
     */
    private void handleMembershipSelection(int months) {
        Stage stage = (Stage) oneMonthButton.getScene().getWindow();
        UserController userController = new UserController();
        userController.handleMembershipSelection(months, stage);
        stage.close();
    }
}
