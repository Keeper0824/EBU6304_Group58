package src.main.java.Login_story1_3;

import src.main.java.Session;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.scene.transform.Scale;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Title      : LayoutController.java
 * Description: Controller for the main application layout (Story 1–3).
 *              Manages navigation between views by loading FXML into the content pane,
 *              updates the section title, displays the current user's name,
 *              handles VIP-only access for AI Forecast, logout functionality,
 *              and integrates HostServices for external links.
 *
 * @author Haoran Sun
 * @version 1.0
 * @author Zhengxuan Han
 * @version 1.1
 */
public class LayoutController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Pane cardWrapper;

    @FXML
    private AnchorPane cardContainer;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label sectionTitle;

    /** Scale transform used for responsive resizing (currently unused). */
    private final Scale scale = new Scale(1, 1, 0, 0);

    /** Provides methods to open external links. */
    private HostServices hostServices;

    /**
     * Called by FXMLLoader after fields are injected.
     * Sets the username label from Session, schedules initial dashboard load,
     * and prepares responsive scaling (if enabled).
     */
    @FXML
    public void initialize() {
        String currentUser = Session.getCurrentNickname();
        if (currentUser != null) {
            usernameLabel.setText(currentUser);
        } else {
            usernameLabel.setText("Guest");
        }
        Platform.runLater(this::onDashboard);
        // Uncomment to enable dynamic scaling:
        // cardContainer.getTransforms().add(scale);
        // addResizeListeners();
    }

    /**
     * Adjusts the scale transform to fit cardContainer inside cardWrapper.
     * Calculates scale factors based on their widths and heights.
     */
    private void adjustScale() {
        double wrapperW = cardWrapper.getWidth();
        double wrapperH = cardWrapper.getHeight();
        double contentW = cardContainer.getWidth();
        double contentH = cardContainer.getHeight();
        if (contentW <= 0 || contentH <= 0) return;
        double scaleX = wrapperW / contentW;
        double scaleY = wrapperH / contentH;
        double finalScale = Math.min(scaleX, scaleY);
        scale.setX(finalScale);
        scale.setY(finalScale);
    }

    /**
     * Loads the dashboard view into the content pane.
     */
    @FXML
    private void onDashboard() {
        loadView("/src/main/resources/CashFlowVisualization_story15/ui.fxml", "Dashboard");
    }

    /**
     * Loads the user management view into the content pane.
     *
     * @param e the action event triggering this method
     */
    @FXML
    private void onUserInfo(ActionEvent e) {
        loadView("/src/main/resources/Login_story1_3/UserManagement.fxml", "UserManagement");
    }

    /**
     * Loads the membership center view into the content pane.
     *
     * @param e the action event triggering this method
     */
    @FXML
    private void onVipCenter(ActionEvent e) {
        loadView("/src/main/resources/ViewMembershipTime_story14/ui.fxml", "Membership");
    }

    /**
     * Loads the financial charts view into the content pane.
     *
     * @param e the action event triggering this method
     */
    @FXML
    private void onCharts(ActionEvent e) {
        loadView("/src/main/resources/financial_story9/main_view.fxml", "Chart");
    }

    /**
     * Loads the card management view into the content pane.
     *
     * @param e the action event triggering this method
     */
    @FXML
    private void onCards(ActionEvent e) {
        loadView("/src/main/resources/card_management_story12/card_manager.fxml", "CardManager");
    }

    /**
     * Loads the transaction view into the content pane.
     *
     * @param e the action event triggering this method
     */
    @FXML
    private void onTransaction(ActionEvent e) {
        loadView("/src/main/resources/Category_story6_7/main.fxml", "Transaction");
    }

    /**
     * Attempts to load the AI Forecast view; only available to VIP users.
     * Shows an error if the current user is not VIP.
     *
     * @param e the action event triggering this method
     */
    @FXML
    private void onAIForecast(ActionEvent e) {
        if (!isCurrentUserVIP()) {
            showError("This feature is only available for VIP users.");
            return;
        }
        loadView("/src/main/resources/AI_story11_21_22/ConsumptionForecast.fxml", "AI Forecast");
    }

    /**
     * Loads the notification (reminder) view into the content pane.
     */
    @FXML
    private void onNotification() {
        loadView("/src/main/resources/Reminder_story_19_20/ReminderView.fxml", "Notification");
    }

    /**
     * Opens the project GitHub page in the default browser.
     */
    @FXML
    private void onVisitSite() {
        if (hostServices != null) {
            hostServices.showDocument("https://github.com/Keeper0824/EBU6304_Group58");
        }
    }

    /**
     * Logs out the current user by clearing the session nickname,
     * then loads the login view to replace the current scene.
     *
     * @param e the action event triggering logout
     */
    @FXML
    private void onLogout(ActionEvent e) {
        try {
            Session.setCurrentNickname(null);
            Parent root = FXMLLoader.load(getClass().getResource("/src/main/resources/Login_story1_3/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Failed to logout");
        }
    }

    /**
     * Checks if the current session user has VIP status by reading data/user.csv.
     *
     * @return true if the user is VIP; false otherwise
     */
    private boolean isCurrentUserVIP() {
        String currentUser = Session.getCurrentNickname();
        if (currentUser == null || currentUser.isEmpty()) {
            return false;
        }
        File csvFile = new File("data/user.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 7 && data[1].equals(currentUser)) {
                    return "VIP".equalsIgnoreCase(data[6]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Displays an error alert with the given message.
     *
     * @param msg the error message to show
     */
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    /**
     * Sets the HostServices instance for this controller,
     * allowing it to open external links.
     *
     * @param services the HostServices to use
     */
    public void setHostServices(HostServices services) {
        this.hostServices = services;
    }

    /**
     * Loads the specified FXML into the content pane and updates the section title.
     *
     * @param fxmlPath the path to the FXML resource
     * @param title    the title to display in the section header
     */
    private void loadView(String fxmlPath, String title) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
            sectionTitle.setText(title);
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "加载失败: " + fxmlPath).showAndWait();
        }
    }
}
