package src.main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuApp extends Application {
    private User currentUser;  // 存储当前登录的用户信息

    // 构造方法，接收用户对象
    public MainMenuApp(User user) {
        this.currentUser = user;
    }

    // 无参构造方法，保留给Application启动用
    public MainMenuApp() {
        // 如果直接启动MainMenuApp，currentUser将为null
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 加载FXML文件
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));

        // 设置控制器工厂，以便传递用户对象
        loader.setControllerFactory(param -> new MainMenuController(currentUser));

        Parent root = loader.load();

        // 设置场景为1600x900，与RegistrationApp保持一致
        Scene scene = new Scene(root, 1600, 900);

        // 窗口居中显示
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}