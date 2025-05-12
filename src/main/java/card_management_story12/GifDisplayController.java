package src.main.java.card_management_story12;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Title      : GifDisplayController.java
 * Description: Controller for displaying a confirmation GIF after adding a credit card.
 *              It loads the GIF into the ImageView and, after a 4.5-second pause,
 *              invokes the returnToPreviousPage() method on BankCardController
 *              to switch back to the card entry view.
 *
 * @author Yudian Wang
 * @version 1.0
 */
public class GifDisplayController {

    @FXML
    private ImageView gifImageView;

    /**
     * Initializes the controller by loading the GIF into the ImageView
     * and starting a 4.5-second PauseTransition. Once the pause finishes,
     * it returns to the previous card entry screen.
     */
    @FXML
    public void initialize() {
        // Load GIF animation
        Image gifImage = new Image(
                getClass().getResource(
                        "/src/main/resources/card_management_story12/images/imageonline-co-gifimage.gif"
                ).toExternalForm()
        );
        gifImageView.setImage(gifImage);

        // Play for 4.5 seconds, then return to entry form
        PauseTransition pause = new PauseTransition(Duration.seconds(4.5));
        pause.setOnFinished(event -> {
            BankCardController bankCardController = new BankCardController();
            bankCardController.returnToPreviousPage();
        });
        pause.play();
    }
}
