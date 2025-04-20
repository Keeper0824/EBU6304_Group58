package src.main.java.ViewMembershipTime_story14;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import src.main.java.Login_story1_3.MainMenuApp;
import src.main.java.Login_story1_3.User;

public class UserController {

    @FXML
    private Text greetingText;

    @FXML
    private Text dueDateText;

    @FXML
    private Label membershipLabel;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Initialize back button
        backButton.setOnAction(e -> handleBackToMainMenu());

        // Load user data from CSV
        src.main.java.ViewMembershipTime_story14.User user = UserLoader.loadUserFromCSV("data/user.csv");

        if (user != null) {
            if (user.getMembershipExpiryDate() != null) {
                greetingText.setText("Hello " + user.getUsername() + ",\nYour VIP expires on " + user.getMembershipExpiryDate());

                if (user.isMembershipActive()) {
                    membershipLabel.setText("Membership expires on: " + user.getMembershipExpiryDate() +
                            " (" + user.getRemainingDays() + " days left)");
                } else {
                    membershipLabel.setText("Membership Expired");
                    membershipLabel.setStyle("-fx-text-fill: red;");
                }
            }
            else {
                greetingText.setText("Hello " + user.getUsername() + ",\nYou are not our VIP member yet. Welcome to the VIP group!");
            }

            image1.setImage(new javafx.scene.image.Image("/src/main/resources/ViewMembershipTime_story14/images/background_14_2.png"));
            image2.setImage(new javafx.scene.image.Image("/src/main/resources/ViewMembershipTime_story14/images/background_14_3.png"));
        } else {
            membershipLabel.setText("No user found.");
        }
    }

    private void handleBackToMainMenu() {
        try {
            // Close current window
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            // Open Main Menu with the current user
            User currentUser = MainMenuApp.getCurrentUser();
            new MainMenuApp(currentUser).start(new Stage());
        } catch (Exception e) {
            System.err.println("Failed to return to Main Menu: " + e.getMessage());
        }
    }
}