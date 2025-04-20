package src.main.java.card_management_story12;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.collections.FXCollections;

public class BankCardController {
    @FXML private VBox inputView;
    @FXML private VBox tableViewView;
    @FXML private TableView<CreditCard> tableView;

    private ObservableList<CreditCard> cardData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadViews();
        initializeData();
        configureTableColumns();
    }

    private void loadViews() {
        try {
            VBox inputForm = FXMLLoader.load(getClass().getResource("../../resources/card_mangement_story12/input_form.fxml"));
            VBox tableView = FXMLLoader.load(getClass().getResource("../../resources/card_mangement_story12/table_view.fxml"));

            inputView.getChildren().add(inputForm);
            tableViewView.getChildren().add(tableView);
        } catch (IOException e) {
            throw new RuntimeException("FXML loading error", e);
        }
    }

    private void configureTableColumns() {
        TableColumn<CreditCard, String>[] columns = new TableColumn[4];
        String[] properties = {"cardNumber", "cardHolder", "expiryDate", "cvv"};
        String[] titles = {"Card Number", "Card Holder", "Expiry Date", "CVV"};

        for (int i = 0; i < columns.length; i++) {
            columns[i] = new TableColumn<>(titles[i]);
            columns[i].setCellValueFactory(new PropertyValueFactory<>(properties[i]));
            // 添加单元格样式配置...
        }
        tableView.getColumns().addAll(columns);
    }

    // 保持原有的事件处理和数据操作方法...
    @FXML private void handleAddCard() { /* 原有逻辑 */ }
    @FXML private void showTableView() { /* 视图切换逻辑 */ }
    @FXML private void showInputView() { /* 视图切换逻辑 */ }
}