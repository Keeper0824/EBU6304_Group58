package src.test.financial_story9;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainAppTest {

    @BeforeAll
    public static void setup() {
        // 启动 JavaFX 应用线程
        Platform.startup(() -> {});
    }

    @Test
    public void testFXMLLoading() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/financial_story9/main_view.fxml"));
            Parent root = loader.load();
            assertNotNull(root, "FXML file should be loaded successfully.");
        } catch (Exception e) {
            // 失败时抛出异常
            e.printStackTrace();
            assert false : "FXML loading failed";
        }
    }

    @Test
    public void testSceneCreation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/financial_story9/main_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1600, 900);
            assertNotNull(scene, "Scene should be created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Scene creation failed";
        }
    }

    // 可以添加更多的测试来验证 `MainApp` 的其它行为
}
