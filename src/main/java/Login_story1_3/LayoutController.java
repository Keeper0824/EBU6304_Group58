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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.main.java.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class LayoutController {
    @FXML
    private StackPane contentPane;
    @FXML
    private Pane cardWrapper;
    @FXML
    private AnchorPane cardContainer;
    @FXML private Label usernameLabel;  // FXML 中的 Label 控件
    // 缩放 Transform
    private Scale scale = new Scale(1, 1, 0, 0);
    // 根据需求，你也可以保留 currentUser 字段，这里省略
    // private User currentUser;

    @FXML
    public void initialize() {
        // 从 Session 获取当前用户名，并设置到 Label 中
        String currentUser = Session.getCurrentNickname();
        if (currentUser != null) {
            usernameLabel.setText(currentUser);
        } else {
            usernameLabel.setText("Guest");  // 如果没有用户名则显示 "Guest"
        }
        // 用 Platform.runLater 保证所有控件都 ready 之后再调用
        Platform.runLater(() -> onDashboard());
        // 在 cardContainer 上附加比例变换
//        cardContainer.getTransforms().add(scale);
//
//
//        // 监听容器大小变化 & 内容真实大小，动态调整 scaleX, scaleY
//        ChangeListener<Number> listener = new ChangeListener<>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
//                adjustScale();
//            }
//        };
//        // 监听 wrapper 和 content 的大小
//        cardWrapper.widthProperty().addListener(listener);
//        cardWrapper.heightProperty().addListener(listener);
//        cardContainer.widthProperty().addListener(listener);
//        cardContainer.heightProperty().addListener(listener);
    }


    private void adjustScale() {
        double wrapperW = cardWrapper.getWidth();
        double wrapperH = cardWrapper.getHeight();
        double contentW = cardContainer.getWidth();
        double contentH = cardContainer.getHeight();

        if (contentW <= 0 || contentH <= 0) return;

        // 允许既放大也缩小
        double scaleX = wrapperW / contentW;
        double scaleY = wrapperH / contentH;
        double finalScale = Math.min(scaleX, scaleY);

        scale.setX(finalScale);
        scale.setY(finalScale);
    }

    @FXML
    private void onDashboard() {
        loadView("/src/main/resources/CashFlowVisualization_story15/ui.fxml", "Dashboard");
    }

    @FXML
    private void onUserInfo(ActionEvent e) {
        loadView("/src/main/resources/Login_story1_3/UserManagement.fxml", "UserManagement");
    }

    @FXML
    private void onVipCenter(ActionEvent e) {
        loadView("/src/main/resources/ViewMembershipTime_story14/ui.fxml", "Membership");
    }

    @FXML
    private void onCharts(ActionEvent e) {
        loadView("/src/main/resources/financial_story9/main_view.fxml", "Chart");
    }

    @FXML
    private void onCards(ActionEvent e) {
        loadView("/src/main/resources/card_management_story12/card_manager.fxml", "CardManager");
    }

    @FXML
    private void onTransaction(ActionEvent e) {
        loadView("/src/main/resources/Category_story6_7/main.fxml", "Transaction");
    }

    @FXML
    private void onAIForecast(ActionEvent e) {
        // Check if user is VIP by reading user.csv
        if (!isCurrentUserVIP()) {
            showError("This feature is only available for VIP users.");
            return;
        }
        loadView("/src/main/resources/AI_story11_21_22/ConsumptionForecast.fxml", "AI Forecast");
    }

    // Helper method to check VIP status
    private boolean isCurrentUserVIP() {
        String currentUser = Session.getCurrentNickname();
        if (currentUser == null || currentUser.isEmpty()) {
            return false;
        }

        String csvFilePath = "user.csv";
        File csvFile = new File(csvFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header
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

    @FXML
    private void onLogout(ActionEvent e) {
        try {
            Session.setCurrentNickname(null);
            // 加载 Login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/src/main/resources/Login_story1_3/Login.fxml"));
            Scene scene = new Scene(root);

            // 获取当前舞台并设置新场景
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Failed to logout");
        }
    }


    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }


    // 这个地方别动了
    private HostServices hostServices;

    // JavaFX 会在 Application 初始化后注入
    public void setHostServices(HostServices services) {
        this.hostServices = services;
    }

    @FXML
    private void onNotification() {
        loadView("/src/main/resources/Reminder_story_19_20/ReminderView.fxml", "Notification");
    }

    @FXML
    private void onVisitSite() {
        if (hostServices != null) {
            hostServices.showDocument("https://github.com/Keeper0824/EBU6304_Group58");
        }
    }

    /**
     * 通用加载方法，第一个参数是 fxml 路径，第二个是你想显示的标题
     */

    @FXML
    private Label sectionTitle;

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
