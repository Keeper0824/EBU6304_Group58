package main;

import controller.UserController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import view.UserView;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 初始化控制器和视图
        UserController controller = new UserController();
        UserView view = new UserView(primaryStage);

        // 设置提交按钮的动作
        view.setSubmitButtonAction(() -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.setStatus("用户名和密码不能为空", true);
                return;
            }

            User user = new User(username, password);
            boolean success = controller.saveUser(user);

            if (success) {
                view.setStatus("用户数据保存成功!", false);
                view.clearFields();
            } else {
                view.setStatus("保存失败，请重试", true);
            }
        });

        // 显示界面
        view.show();
    }
}