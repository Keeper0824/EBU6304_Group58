package src.main.java.card_management_story12;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.regex.Pattern;

/**
 * Title      : BankCardController.java
 * Description: Controller for the bank card management view (Story 12).
 *              It handles card entry validation, displays success GIF,
 *              shows saved cards in a table, and manages navigation back
 *              to the main menu or entry form. Uses CSVUtils for persistence.
 *
 * @author Yudian Wang
 * @version 1.0
 * @author Haoran Sun
 * @version 1.1
 */
public class BankCardController {

    @FXML private StackPane rootContainer;
    @FXML private VBox inputView;      // Card entry interface
    @FXML private VBox gifView;        // GIF display interface
    @FXML private VBox tableViewView;  // Table view interface
    @FXML private TableView<CreditCard> cardTable;
    @FXML private TextField cardNumberField;
    @FXML private TextField cardHolderField;
    @FXML private TextField expiryDateField;
    @FXML private TextField cvvField;
    @FXML private ImageView gifImageView;  // For displaying GIF

    private ObservableList<CreditCard> cardData = FXCollections.observableArrayList();

    /**
     * Initializes the controller after FXML injection.
     * Sets up the background image, loads saved cards into the table,
     * and configures the table columns.
     */
    @FXML
    public void initialize() {
        setupBackground();
        loadTableData();
        configTableColumns();
    }

    /**
     * Loads and applies the background image; falls back to a light gray fill on error.
     */
    private void setupBackground() {
        try {
            Image bgImage = new Image(
                    getClass().getResource(
                            "/src/main/resources/card_management_story12/images/bg.png"
                    ).toExternalForm()
            );
            BackgroundImage background = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true)
            );
            rootContainer.setBackground(new Background(background));
        } catch (Exception e) {
            System.err.println("Background loading failed: " + e.getMessage());
            rootContainer.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY
                            )
                    )
            );
        }
    }

    /**
     * Loads saved credit cards from CSV using CSVUtils into the table's data list.
     */
    private void loadTableData() {
        cardData.addAll(CSVUtils.loadCards());
        cardTable.setItems(cardData);
    }

    /**
     * Configures each table column's preferred width based on its header text.
     */
    private void configTableColumns() {
        cardTable.getColumns().forEach(col -> {
            switch (col.getText()) {
                case "Card Number": col.setPrefWidth(300); break;
                case "Card Holder": col.setPrefWidth(200); break;
                case "Expiry Date": col.setPrefWidth(150); break;
                case "CVV":         col.setPrefWidth(80);  break;
            }
        });
    }

    /**
     * Returns from the GIF view back to the card entry form.
     */
    @FXML
    private void handleBackToCardEntry() {
        gifView.setVisible(false);
        inputView.setVisible(true);
    }

    /**
     * Validates input fields and, on success, saves a new card,
     * clears the fields, and shows a confirmation GIF.
     */
    @FXML
    private void handleAddCard() {
        String cardNumber  = cardNumberField.getText().trim();
        String cardHolder  = cardHolderField.getText().trim();
        String expiryDate  = expiryDateField.getText().trim();
        String cvv         = cvvField.getText().trim();

        if (validateInput(cardNumber, cardHolder, expiryDate, cvv)) {
            cardData.add(new CreditCard(
                    cardNumber.replaceAll("\\s+", ""),
                    cardHolder,
                    expiryDate,
                    cvv
            ));
            CSVUtils.saveCards(cardData);
            clearFields();
            showGifView();
        }
    }

    /**
     * Displays the success GIF for 4.5 seconds, then returns to the entry form.
     */
    private void showGifView() {
        inputView.setVisible(false);
        gifView.setVisible(true);

        Image gifImage = new Image(
                getClass().getResource(
                        "/src/main/resources/card_management_story12/images/imageonline-co-gifimage.gif"
                ).toExternalForm()
        );
        gifImageView.setImage(gifImage);

        PauseTransition pause = new PauseTransition(Duration.seconds(4.5));
        pause.setOnFinished(event -> returnToPreviousPage());
        pause.play();
    }

    /**
     * Returns from the GIF view to the card entry form.
     * Made public for potential external calls.
     */
    public void returnToPreviousPage() {
        inputView.setVisible(true);
        gifView.setVisible(false);
    }

    /**
     * Shows the saved cards table view.
     */
    @FXML
    private void handleShowCards() {
        inputView.setVisible(false);
        tableViewView.setVisible(true);
        cardTable.refresh();
    }

    /**
     * Returns from the table view to the card entry form.
     */
    @FXML
    private void handleReturn() {
        inputView.setVisible(true);
        tableViewView.setVisible(false);
    }

    /**
     * Closes this window and returns to the main menu.
     */
    @FXML
    private void handleBackToMain() {
        try {
            Stage currentStage = (Stage) rootContainer.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Failed to return to main menu: " + e.getMessage());
        }
    }

    /**
     * Validates the card input fields: 16-digit number, full name,
     * MM/YY expiry format, and 3-digit CVV. Shows an error alert on failure.
     *
     * @param cardNumber the card number text
     * @param cardHolder the card holder name
     * @param expiryDate the expiry date text
     * @param cvv        the CVV text
     * @return true if all inputs are valid; false otherwise
     */
    private boolean validateInput(String cardNumber, String cardHolder,
                                  String expiryDate, String cvv) {
        if (!Pattern.matches("^\\d{16}$", cardNumber.replaceAll("\\s+", ""))) {
            showError("Card number must be a 16-digit number");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)+$", cardHolder)) {
            showError("Card holder name must include full name");
            return false;
        }
        if (!Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$", expiryDate)) {
            showError("Expiry date format must be MM/YY");
            return false;
        }
        if (!Pattern.matches("^\\d{3}$", cvv)) {
            showError("CVV must be a 3-digit number");
            return false;
        }
        return true;
    }

    /**
     * Clears all input fields in the card entry form.
     */
    private void clearFields() {
        cardNumberField.clear();
        cardHolderField.clear();
        expiryDateField.clear();
        cvvField.clear();
    }

    /**
     * Displays an error alert with the specified message.
     *
     * @param message the error message to show
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
