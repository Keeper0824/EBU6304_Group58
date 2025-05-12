package src.main.java.ViewMembershipTime_story14;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.time.LocalDate;

/**
 * Title      : UserController.java
 * Description: Controller for the main VIP membership view.
 * It initializes the UI with user data loaded from CSV,
 * displays greeting, membership status, and remaining days,
 * and handles opening the recharge popup and updating membership.
 *
 * @author Haoran Sun
 * @version 1.0
 * @author Zhengxuan Han
 * @version 1.1
 * @author Yudian Wang
 * @version 1.2
 * @author Wei chen
 * @version 1.3
 */
public class UserController {

    @FXML
    private javafx.scene.text.Text greetingText;

    @FXML
    private javafx.scene.text.Text dueDateText;

    @FXML
    private Label membershipLabel;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private Button backButton;

    @FXML
    private Button vipRechargeButton;

    /**
     * Initializes the main view after FXML injection.
     * Sets up the recharge button action and loads user data to populate UI fields.
     */
    @FXML
    public void initialize() {
        vipRechargeButton.setOnAction(e -> openRechargePopup());

        // Load user data from CSV
        User user = UserLoader.loadUserFromCSV("data/user.csv");

        if (user != null) {
            if (user.getMembershipExpiryDate() != null) {
                greetingText.setText("Hello " + user.getUsername() +
                        ",\nYour VIP expires on " +
                        user.getMembershipExpiryDate());

                if (user.isMembershipActive()) {
                    membershipLabel.setText("Membership expires on: " +
                            user.getMembershipExpiryDate() +
                            " (" + user.getRemainingDays() +
                            " days left)");
                } else {
                    membershipLabel.setText("Membership Expired");
                    membershipLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                greetingText.setText("Hello " + user.getUsername() +
                        ",\nYou are not our VIP member yet.");
                membershipLabel.setText("Membership Expired");
                membershipLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            membershipLabel.setText("No user found.");
        }
    }

    /**
     * Opens a modal popup allowing the user to choose a membership duration.
     * Loads the popup FXML, applies stylesheet, and shows the dialog.
     */
    @FXML
    private void openRechargePopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/src/main/resources/ViewMembershipTime_story14/recharge_popup.fxml"
                    )
            );
            GridPane gridPane = loader.load();
            Scene dialogScene = new Scene(gridPane, 640, 360);
            dialogScene.getStylesheets().add(
                    getClass().getResource(
                            "/src/main/resources/ViewMembershipTime_story14/rechargestyle.css"
                    ).toExternalForm()
            );

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Choose Membership Duration");
            dialogStage.setScene(dialogScene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called by the recharge popup controller when the user selects a plan.
     * Updates the user's expiry date and writes the change to CSV.
     *
     * @param months      number of months to extend membership (1, 3, or 12)
     * @param dialogStage the stage of the recharge popup to close
     */
    public void handleMembershipSelection(int months, Stage dialogStage) {
        User user = UserLoader.loadUserFromCSV("data/user.csv");

        LocalDate currentExpiryDate = user.getMembershipExpiryDate();
        if (currentExpiryDate == null || currentExpiryDate.isBefore(LocalDate.now())) {
            currentExpiryDate = LocalDate.now();
        }
        LocalDate newExpiryDate = currentExpiryDate.plusMonths(months);

        updateUserMembershipStatus(user.getUsername(), newExpiryDate);
        dialogStage.close();
    }

    /**
     * Reads the CSV, updates the VIP status and expiry date for the given user,
     * and writes the modified content back to the file.
     *
     * @param username      the username to match in CSV
     * @param newExpiryDate the new expiry date to set
     */
    private void updateUserMembershipStatus(String username, LocalDate newExpiryDate) {
        String filePath = "data/user.csv";
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder();
        boolean isUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[1].trim().equals(username)) {
                    parts[6] = "VIP";
                    parts[7] = newExpiryDate.toString();
                    line = String.join(",", parts);
                    isUpdated = true;
                }
                fileContent.append(line).append("\n");
            }

            if (isUpdated) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    bw.write(fileContent.toString());
                }
            } else {
                System.out.println("No matching user found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Optional method to refresh UI after update; uncomment if needed.
    // public void refreshMembershipInfo() { ... }

    /*public void refreshMembershipInfo() {
        // Load user data from CSV
        User user = UserLoader.loadUserFromCSV("data/user.csv");

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
            } else {
                greetingText.setText("Hello " + user.getUsername() + ",\nYou are not our VIP member yet.");
                membershipLabel.setText("Membership Expired");
                membershipLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            membershipLabel.setText("No user found.");
        }
    }*/

}