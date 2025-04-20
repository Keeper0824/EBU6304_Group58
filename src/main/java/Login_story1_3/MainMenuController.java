package src.main.java.Login_story1_3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import src.main.java.CashFlowVisualization_story15.CashFlowView;
import src.main.java.card_management_story12.BankCardManagerFX;

public class MainMenuController {
    private User currentUser;

    // Constructor with user
    public MainMenuController(User user) {
        this.currentUser = user;
    }

    // Default constructor
    public MainMenuController() {
    }

    // Setter for user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    @FXML
    private void handleUserInformation(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/UserManagement.fxml"));
            Parent root = loader.load();

            UserManagementController controller = loader.getController();
            controller.setUser(currentUser);
            controller.setReturnToMainMenuCallback(() -> {
                try {
                    new MainMenuApp(currentUser).start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Stage stage = new Stage();
            stage.setTitle("User Information");
            stage.setScene(new Scene(root, 1600, 900));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open User Information window: " + e.getMessage());
        }
    }

    @FXML
    private void handleVIPCenter(ActionEvent event) {
        try {
            // Close current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Launch the VIP Membership Status view
            new src.main.java.ViewMembershipTime_story14.Main().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open VIP Center: " + e.getMessage());
        }
    }
    @FXML
    private void handleCharts(ActionEvent event) {
        try {
            // Close current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Launch the Cash Flow Visualization
            new CashFlowView().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Charts: " + e.getMessage());
        }
    }

    @FXML
    private void handleCards(ActionEvent event) {
        try {
            // Close current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Launch the Bank Card Manager
            new BankCardManagerFX().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Cards Management: " + e.getMessage());
        }
    }

    @FXML
    private void handleTransaction(ActionEvent event) {
        try {
            // Close current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the transaction management interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/main.fxml"));
            Parent root = loader.load();

            // Create new stage
            Stage stage = new Stage();
            stage.setTitle("Transaction Management");
            stage.setScene(new Scene(root, 1600, 900));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Transaction Management: " + e.getMessage());
        }
    }

    private void loadNewWindow(String title, String fxmlFile, ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 1600, 900));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open " + title + " window: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Login_story1_3/Login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root, 1600, 900));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}