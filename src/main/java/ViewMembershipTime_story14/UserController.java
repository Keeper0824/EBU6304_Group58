package src.main.java.ViewMembershipTime_story14;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;

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
    private Button vipRechargeButton;

    @FXML
    public void initialize() {
        vipRechargeButton.setOnAction(e -> openRechargePopup());

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
    }

    @FXML
    private void openRechargePopup() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Choose Membership Duration");

        VBox vbox = new VBox();
        Button oneMonthButton = new Button("1 Month");
        Button oneQuarterButton = new Button("1 Quarter");
        Button oneYearButton = new Button("1 Year");

        oneMonthButton.setOnAction(e -> handleMembershipSelection(1, dialogStage));
        oneQuarterButton.setOnAction(e -> handleMembershipSelection(3, dialogStage));
        oneYearButton.setOnAction(e -> handleMembershipSelection(12, dialogStage));

        vbox.getChildren().addAll(oneMonthButton, oneQuarterButton, oneYearButton);
        Scene dialogScene = new Scene(vbox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void handleMembershipSelection(int months, Stage dialogStage) {
        // Get the current user's data (Email for matching)
        User user = UserLoader.loadUserFromCSV("data/user.csv");

        // Calculate the new expiration date
        LocalDate currentExpiryDate = user.getMembershipExpiryDate();
        if (currentExpiryDate == null || currentExpiryDate.isBefore(LocalDate.now())) {
            currentExpiryDate = LocalDate.now();
        }
        LocalDate newExpiryDate = currentExpiryDate.plusMonths(months);

        // Update CSV with new VIP status and expiration date
        updateUserMembershipStatus(user.getUsername(), newExpiryDate);

        // Close the popup (only the recharge window, not the main window)
        dialogStage.close();
    }

    private void updateUserMembershipStatus(String username, LocalDate newExpiryDate) {
        String filePath = "data/user.csv";
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder();  // 用来存储文件的更新内容
        boolean isUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[1].trim().equals(username)) {
                    // 更新 isVIP 和 ExpiryDate
                    parts[6] = "VIP"; // 设置 VIP
                    parts[7] = newExpiryDate.toString(); // 设置新的到期日期
                    line = String.join(",", parts); // 更新为新的一行
                    isUpdated = true;
                }
                fileContent.append(line).append("\n");  // 将每行内容加入到文件内容中
            }

            if (isUpdated) {
                // 将更新后的内容写回原文件
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
}

