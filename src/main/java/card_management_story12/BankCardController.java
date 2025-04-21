package src.main.java.card_management_story12;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import src.main.java.Login_story1_3.MainMenuApp;
import src.main.java.Login_story1_3.User;

import java.util.regex.Pattern;

public class BankCardController {
    @FXML private StackPane rootContainer;
    @FXML private VBox inputView;
    @FXML private VBox tableViewView;
    @FXML private TableView<CreditCard> cardTable;
    @FXML private TextField cardNumberField;
    @FXML private TextField cardHolderField;
    @FXML private TextField expiryDateField;
    @FXML private TextField cvvField;

    private ObservableList<CreditCard> cardData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupBackground();
        loadTableData();
        configTableColumns();
    }

    private void setupBackground() {
        try {
            Image bgImage = new Image(
                    getClass().getResource("/src/main/resources/card_management_story12/images/bg.png").toExternalForm()
            );

            BackgroundImage background = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            100, 100,
                            true, true, false, true
                    )
            );
            rootContainer.setBackground(new Background(background));
        } catch (Exception e) {
            System.err.println("背景加载失败: " + e.getMessage());
            rootContainer.setBackground(new Background(new BackgroundFill(
                    Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)
            ));
        }
    }

    private void loadTableData() {
        cardData.addAll(CSVUtils.loadCards());
        cardTable.setItems(cardData);
    }

    private void configTableColumns() {
        // 动态配置表格列宽
        cardTable.getColumns().forEach(col -> {
            switch (col.getText()) {
                case "Card Number": col.setPrefWidth(300); break;
                case "Card Holder": col.setPrefWidth(200); break;
                case "Expiry Date": col.setPrefWidth(150); break;
                case "CVV": col.setPrefWidth(80); break;
            }
        });
    }

    @FXML
    private void handleBackToMain() {
        try {
            // Close current window
            Stage currentStage = (Stage) rootContainer.getScene().getWindow();
            currentStage.close();

            // Return to main menu with the current user
            User currentUser = MainMenuApp.getCurrentUser();
            new MainMenuApp(currentUser).start(new Stage());
        } catch (Exception e) {
            System.err.println("Failed to return to main menu: " + e.getMessage());
        }
    }

    // 事件处理方法
    @FXML
    private void handleAddCard() {
        String cardNumber = cardNumberField.getText().trim();
        String cardHolder = cardHolderField.getText().trim();
        String expiryDate = expiryDateField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (validateInput(cardNumber, cardHolder, expiryDate, cvv)) {
            cardData.add(new CreditCard(
                    cardNumber.replaceAll("\\s+", ""),
                    cardHolder,
                    expiryDate,
                    cvv
            ));
            CSVUtils.saveCards(cardData);
            clearFields();
        }
    }

    @FXML
    private void handleShowCards() {
        inputView.setVisible(false);
        tableViewView.setVisible(true);
        cardTable.refresh();
    }

    @FXML
    private void handleReturn() {
        inputView.setVisible(true);
        tableViewView.setVisible(false);
    }

    // 验证逻辑
    private boolean validateInput(String cardNumber, String cardHolder,
                                  String expiryDate, String cvv) {
        if (!Pattern.matches("^\\d{16}$", cardNumber.replaceAll("\\s+", ""))) {
            showError("卡号必须为16位数字");
            return false;
        }

        if (!Pattern.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)+$", cardHolder)) {
            showError("持卡人姓名需包含全名");
            return false;
        }

        if (!Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$", expiryDate)) {
            showError("有效期格式应为MM/YY");
            return false;
        }

        if (!Pattern.matches("^\\d{3}$", cvv)) {
            showError("CVV必须为3位数字");
            return false;
        }

        return true;
    }

    private void clearFields() {
        cardNumberField.clear();
        cardHolderField.clear();
        expiryDateField.clear();
        cvvField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("输入验证错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}