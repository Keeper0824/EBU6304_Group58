package card_management;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;
import java.util.regex.Pattern;

public class BankCardManagerFX extends Application {
    private ObservableList<CreditCard> cardData = FXCollections.observableArrayList();
    private TableView<CreditCard> tableView = new TableView<>();

    // 输入字段
    private TextField cardNumberField = new TextField();
    private TextField cardHolderField = new TextField();
    private TextField expiryDateField = new TextField();
    private TextField cvvField = new TextField();

    // 视图容器
    private StackPane rootContainer = new StackPane();
    private VBox inputView;
    private VBox tableViewView;

    @Override
    public void start(Stage primaryStage) {
        initializeData();
        setupUI(primaryStage);
    }

    private void initializeData() {
        List<CreditCard> savedCards = CSVUtils.loadCards();
        cardData.addAll(savedCards);
    }

    private void setupUI(Stage stage) {
        // 创建界面组件
        createTableView();
        createInputView();
        createTableViewContainer();

        // 设置背景（关键修改）
        setupBackground();

        // 初始化视图状态
        rootContainer.getChildren().addAll(inputView, tableViewView);
        tableViewView.setVisible(false);

        // 场景设置
        Scene scene = new Scene(rootContainer, 1600, 900);
        stage.setTitle("Bank Card Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void createTableView() {
        TableColumn<CreditCard, String> numberCol = new TableColumn<>("Card Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        numberCol.setMinWidth(300);

        TableColumn<CreditCard, String> holderCol = new TableColumn<>("Card Holder");
        holderCol.setCellValueFactory(new PropertyValueFactory<>("cardHolder"));
        holderCol.setMinWidth(200);

        TableColumn<CreditCard, String> expiryCol = new TableColumn<>("Expiry Date");
        expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        expiryCol.setMinWidth(150);

        TableColumn<CreditCard, String> cvvCol = new TableColumn<>("CVV");
        cvvCol.setCellValueFactory(new PropertyValueFactory<>("cvv"));
        cvvCol.setMinWidth(80);

        tableView.getColumns().addAll(numberCol, holderCol, expiryCol, cvvCol);
        tableView.setItems(cardData);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-font-size: 14pt; -fx-background-color: rgba(255,255,255,0.7);");
    }

    private void createInputView() {
        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(20);
        inputGrid.setVgap(20);
        inputGrid.setPadding(new Insets(30));

        // 表单样式（半透明背景）
        String fieldStyle = "-fx-font-size: 16pt; -fx-pref-width: 500; -fx-pref-height: 45;";
        String labelStyle = "-fx-font-size: 16pt; -fx-font-weight: bold; -fx-text-fill: #333;";

        addFormRow(inputGrid, 0, "Card Number:", cardNumberField, labelStyle, fieldStyle);
        addFormRow(inputGrid, 1, "Card Holder:", cardHolderField, labelStyle, fieldStyle);
        addFormRow(inputGrid, 2, "Expiry Date (MM/YY):", expiryDateField, labelStyle, fieldStyle);
        addFormRow(inputGrid, 3, "CVV:", cvvField, labelStyle, fieldStyle);

        // 操作按钮
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                createActionButton("Add Card", this::handleAddCard),
                createNavigationButton("Show Cards", false)
        );

        inputView = new VBox(30, inputGrid, buttonBox);
        inputView.setAlignment(Pos.CENTER);
        inputView.setStyle("-fx-background-color: rgba(255,255,255,0.7);");
        inputView.setMaxWidth(1000);
    }

    private void createTableViewContainer() {
        Button backButton = createNavigationButton("Return", true);
        VBox tableBox = new VBox(20, tableView, backButton);
        tableBox.setAlignment(Pos.CENTER);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color: rgba(255,255,255,0.7);");

        tableViewView = new VBox(tableBox);
        tableViewView.setAlignment(Pos.CENTER);
    }

    private Button createActionButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font-size: 14pt; -fx-pref-width: 200; -fx-pref-height: 45; "
                + "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private Button createNavigationButton(String text, boolean showInput) {
        Button btn = createActionButton(text, () -> switchView(showInput));
        btn.setStyle(btn.getStyle() + "-fx-background-color: #2196F3;");
        return btn;
    }

    private void addFormRow(GridPane grid, int row, String labelText, TextField field, String labelStyle, String fieldStyle) {
        Label label = new Label(labelText);
        label.setStyle(labelStyle);
        field.setStyle(fieldStyle);
        grid.add(label, 0, row);
        grid.add(field, 1, row);
    }

    private void setupBackground() {
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/card_management/bg.png"));
            BackgroundSize bgSize = new BackgroundSize(
                    100, 100,
                    true, true, true, false
            );
            BackgroundImage background = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    bgSize
            );
            rootContainer.setBackground(new Background(background));
        } catch (Exception e) {
            System.err.println("背景加载错误: " + e.getMessage());
            rootContainer.setBackground(new Background(new BackgroundFill(
                    Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY))
            );
        }
    }

    private void switchView(boolean showInput) {
        inputView.setVisible(showInput);
        inputView.setManaged(showInput);
        tableViewView.setVisible(!showInput);
        tableViewView.setManaged(!showInput);
        if (!showInput) tableView.refresh();
    }

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

    private boolean validateInput(String cardNumber, String cardHolder,
                                  String expiryDate, String cvv) {
        if (!Pattern.matches("^\\d{16}$", cardNumber.replaceAll("\\s+", ""))) {
            showError("Invalid card number. Must be 16 digits.");
            return false;
        }

        if (!Pattern.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)+$", cardHolder)) {
            showError("Invalid card holder name. Use full name.");
            return false;
        }

        if (!Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$", expiryDate)) {
            showError("Invalid expiry date. Use MM/YY format.");
            return false;
        }

        if (!Pattern.matches("^\\d{3}$", cvv)) {
            showError("Invalid CVV. Must be 3 digits.");
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
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}