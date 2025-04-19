package src.main.java;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MainMenuController {
    private User currentUser;

    public MainMenuController(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleUserInformation(ActionEvent event) {  // 添加ActionEvent参数
        try {
            // 关闭当前窗口
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // 加载UserInformation界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManagement.fxml"));
            Parent root = loader.load();

            // 获取控制器并设置当前用户和返回功能
            UserManagementController controller = loader.getController();
            controller.setUser(currentUser);
            controller.setReturnToMainMenuCallback(() -> {
                try {
                    new MainMenuApp(currentUser).start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // 创建新窗口
            Stage stage = new Stage();
            stage.setTitle("User Information");
            stage.setScene(new Scene(root, 1600, 900));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open User Information window: " + e.getMessage());
        }
    }

    // 其他按钮处理方法也需要添加ActionEvent参数
    @FXML
    private void handleVIPCenter(ActionEvent event) {
        loadNewWindow("VIP Center", "VIPCenter.fxml", event);
    }

    @FXML
    private void handleCharts(ActionEvent event) {
        loadNewWindow("Charts", "Charts.fxml", event);
    }

    @FXML
    private void handleCards(ActionEvent event) {
        loadNewWindow("Cards", "Cards.fxml", event);
    }

    @FXML
    private void handleTransaction(ActionEvent event) {
        loadNewWindow("Transaction", "Transaction.fxml", event);
    }

    private void loadNewWindow(String title, String fxmlFile, ActionEvent event) {
        try {
            // 关闭当前窗口
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
            // 关闭当前窗口
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // 重新打开登录界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
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