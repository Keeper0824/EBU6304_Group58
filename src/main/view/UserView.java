package main.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserView {
    private final Stage primaryStage;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button submitButton;
    private final Label statusLabel;

    public UserView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // 创建UI组件
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // 用户名输入
        Label usernameLabel = new Label("用户名:");
        usernameField = new TextField();
        usernameField.setPromptText("输入用户名");
        GridPane.setConstraints(usernameLabel, 0, 0);
        GridPane.setConstraints(usernameField, 1, 0);

        // 密码输入
        Label passwordLabel = new Label("密码:");
        passwordField = new PasswordField();
        passwordField.setPromptText("输入密码");
        GridPane.setConstraints(passwordLabel, 0, 1);
        GridPane.setConstraints(passwordField, 1, 1);

        // 提交按钮
        submitButton = new Button("提交");
        GridPane.setConstraints(submitButton, 1, 2);

        // 状态标签
        statusLabel = new Label("");
        GridPane.setConstraints(statusLabel, 1, 3);

        // 添加所有组件到网格
        grid.getChildren().addAll(usernameLabel, usernameField, passwordLabel, 
                                passwordField, submitButton, statusLabel);

        // 创建场景
        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("用户数据录入");
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    public void setSubmitButtonAction(Runnable action) {
        submitButton.setOnAction(e -> action.run());
    }

    public void show() {
        primaryStage.show();
    }
}
