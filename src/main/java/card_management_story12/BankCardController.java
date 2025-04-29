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

public class BankCardController {

    @FXML private StackPane rootContainer;
    @FXML private VBox inputView;      // 卡片录入界面
    @FXML private VBox gifView;        // 显示GIF的界面
    @FXML private VBox tableViewView;  // 表格视图
    @FXML private TableView<CreditCard> cardTable;
    @FXML private TextField cardNumberField;
    @FXML private TextField cardHolderField;
    @FXML private TextField expiryDateField;
    @FXML private TextField cvvField;
    @FXML private ImageView gifImageView;  // 用于显示GIF

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
    private void handleBackToCardEntry() {
        // 切换回卡片录入视图
        gifView.setVisible(false);  // 隐藏GIF视图
        inputView.setVisible(true); // 显示卡片录入视图
    }

    // 事件处理方法
    @FXML
    private void handleAddCard() {
        String cardNumber = cardNumberField.getText().trim();
        String cardHolder = cardHolderField.getText().trim();
        String expiryDate = expiryDateField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (validateInput(cardNumber, cardHolder, expiryDate, cvv)) {
            // 添加新卡
            cardData.add(new CreditCard(
                    cardNumber.replaceAll("\\s+", ""),
                    cardHolder,
                    expiryDate,
                    cvv
            ));
            CSVUtils.saveCards(cardData);

            // 清空输入框
            clearFields();

            // 切换到GIF视图
            showGifView();
        }
    }

    private void showGifView() {
        // 隐藏卡片录入视图，显示GIF视图
        inputView.setVisible(false);
        gifView.setVisible(true);

        // 加载 GIF 动图
        Image gifImage = new Image(getClass().getResource("/src/main/resources/card_management_story12/images/imageonline-co-gifimage.gif").toExternalForm());
        gifImageView.setImage(gifImage);

        // 设置播放时间为 4.5 秒
        PauseTransition pause = new PauseTransition(Duration.seconds(4.5));
        pause.setOnFinished(event -> {
            // 播放完 GIF 后切换回卡片录入界面
            returnToPreviousPage();
        });
        pause.play();
    }

    // 将此方法设置为静态方法，以便从其他类中调用
    public void returnToPreviousPage() {
        // 使用实例变量来切换视图
        inputView.setVisible(true);  // 显示卡片录入视图
        gifView.setVisible(false);   // 隐藏GIF视图
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

    @FXML
    private void handleBackToMain() {
        try {
            // 当前窗口关闭
            Stage currentStage = (Stage) rootContainer.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("返回主菜单失败: " + e.getMessage());
        }
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
