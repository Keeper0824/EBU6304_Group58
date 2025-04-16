// BankCardManagerFX.java
package card_management;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.regex.Pattern;

public class BankCardManagerFX extends Application {
    private ObservableList<CreditCard> cardData = FXCollections.observableArrayList();
    private TableView<CreditCard> tableView = new TableView<>();

    private TextField cardNumberField = new TextField();
    private TextField cardHolderField = new TextField();
    private TextField expiryDateField = new TextField();
    private TextField cvvField = new TextField();

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
        // Create table columns
        TableColumn<CreditCard, String> numberCol = new TableColumn<>("Card Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));

        TableColumn<CreditCard, String> holderCol = new TableColumn<>("Card Holder");
        holderCol.setCellValueFactory(new PropertyValueFactory<>("cardHolder"));

        TableColumn<CreditCard, String> expiryCol = new TableColumn<>("Expiry Date");
        expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        TableColumn<CreditCard, String> cvvCol = new TableColumn<>("CVV");
        cvvCol.setCellValueFactory(new PropertyValueFactory<>("cvv"));

        tableView.getColumns().addAll(numberCol, holderCol, expiryCol, cvvCol);
        tableView.setItems(cardData);

        // Create input form
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));

        inputGrid.add(new Label("Card Number:"), 0, 0);
        inputGrid.add(cardNumberField, 1, 0);
        inputGrid.add(new Label("Card Holder:"), 0, 1);
        inputGrid.add(cardHolderField, 1, 1);
        inputGrid.add(new Label("Expiry Date (MM/YY):"), 0, 2);
        inputGrid.add(expiryDateField, 1, 2);
        inputGrid.add(new Label("CVV:"), 0, 3);
        inputGrid.add(cvvField, 1, 3);

        // Create buttons
        Button addButton = new Button("Add Card");
        addButton.setOnAction(e -> handleAddCard());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> handleDeleteCard());

        HBox buttonBox = new HBox(10, addButton, deleteButton);
        buttonBox.setPadding(new Insets(10));

        // Main layout
        BorderPane root = new BorderPane();
        root.setTop(inputGrid);
        root.setCenter(tableView);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Bank Card Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void handleAddCard() {
        String cardNumber = cardNumberField.getText().trim();
        String cardHolder = cardHolderField.getText().trim();
        String expiryDate = expiryDateField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (validateInput(cardNumber, cardHolder, expiryDate, cvv)) {
            CreditCard newCard = new CreditCard(
                    cardNumber.replaceAll("\\s+", ""),
                    cardHolder,
                    expiryDate,
                    cvv
            );

            cardData.add(newCard);
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

    private void handleDeleteCard() {
        CreditCard selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a card to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete selected card?");
        confirm.setContentText("This action cannot be undone");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            cardData.remove(selected);
            CSVUtils.saveCards(cardData);
        }
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