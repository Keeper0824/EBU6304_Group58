package src.main.java.ViewMembershipTime_story14;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
    private ImageView arrowImage;

    // Method to initialize the UI with User information
    public void initialize() {
        // Load user data from CSV
        User user = UserLoader.loadUserFromCSV("data/user.csv");  // Replace with actual CSV file path

        if (user != null) {
            // Update the greeting text dynamically
            greetingText.setText("Hello " + user.getUsername() + ",\nYour VIP expires on " + user.getMembershipExpiryDate());

            // Update the membership label with expiration information
            if (user.isMembershipActive()) {
                membershipLabel.setText("Membership expires on: " + user.getMembershipExpiryDate() +
                        " (" + user.getRemainingDays() + " days left)");
            } else {
                membershipLabel.setText("Membership Expired");
                membershipLabel.setStyle("-fx-text-fill: red;");
            }

             image1.setImage(new javafx.scene.image.Image("/src/main/resources/ViewMembershipTime_story14/images/background_14_2.png"));
             image2.setImage(new javafx.scene.image.Image("/src/main/resources/ViewMembershipTime_story14/images/background_14_3.png"));
             arrowImage.setImage(new javafx.scene.image.Image("/src/main/resources/ViewMembershipTime_story14/images/background_14_4.png"));




        } else {
            membershipLabel.setText("No user found.");
        }
    }
}
