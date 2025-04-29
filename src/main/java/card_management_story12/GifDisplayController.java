package src.main.java.card_management_story12;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class GifDisplayController {

    @FXML
    private ImageView gifImageView;

    public void initialize() {
        // 加载 GIF 动图
        Image gifImage = new Image(getClass().getResource("/src/main/resources/card_management_story12/images/imageonline-co-gifimage.gif").toExternalForm());
        gifImageView.setImage(gifImage);

        // 设置播放时间为 4.5 秒
        PauseTransition pause = new PauseTransition(Duration.seconds(4.5));
        pause.setOnFinished(event -> {
            // 播放完 GIF 后切换回卡片录入界面
            BankCardController bankCardController = new BankCardController();
            bankCardController.returnToPreviousPage();  // 通过实例调用方法
        });
        pause.play();
    }
}
