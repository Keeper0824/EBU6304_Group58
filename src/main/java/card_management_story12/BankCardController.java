package src.main.java.card_management_story12;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.collections.FXCollections;

public class BankCardController {
    // 这三行是之前就有的
    @FXML private VBox inputView;
    @FXML private VBox tableViewView;
    @FXML private TableView<CreditCard> tableView;

    // 新增：和 input_form.fxml 里一一对应
    @FXML private TextField cardNumberField;
    @FXML private TextField cardHolderField;
    @FXML private TextField expiryDateField;
    @FXML private TextField cvvField;

    private ObservableList<CreditCard> cardData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadViews();
        initializeData();
        configureTableColumns();
    }

    private void loadViews() {
        try {
            // 加载输入表单和表格视图（路径已修正）
            VBox inputForm = FXMLLoader.load(
                    getClass().getResource("/src/main/resources/card_management_story12/input_form.fxml")
            );
            VBox tableBox = FXMLLoader.load(
                    getClass().getResource("/src/main/resources/card_management_story12/table_view.fxml")
            );

            // 清空容器并添加新内容
            inputView.getChildren().clear();
            tableViewView.getChildren().clear();
            inputView.getChildren().add(inputForm);
            tableViewView.getChildren().add(tableBox);

            // 设置输入视图的背景图片
            inputView.setStyle(
                    "-fx-background-image: url('/src/main/resources/card_management_story12/images/bg.png');" +
                            "-fx-background-size: cover;"
            );

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fatal Error");
            alert.setHeaderText("UI Component Loading Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException("FXML loading failed", e);
        }
    }

    private void initializeData() {
        // 从CSV加载数据并绑定到表格
        cardData.addAll(CSVUtils.loadCards());
        tableView.setItems(cardData);
    }

    private void configureTableColumns() {
        // 动态配置表格列（与CreditCard属性严格对应）
        TableColumn<CreditCard, String>[] columns = new TableColumn[4];
        String[] properties = {"cardNumber", "cardHolder", "expiryDate", "cvv"};
        String[] titles = {"Card Number", "Card Holder", "Expiry Date", "CVV"};

        for (int i = 0; i < columns.length; i++) {
            columns[i] = new TableColumn<>(titles[i]);
            columns[i].setCellValueFactory(new PropertyValueFactory<>(properties[i]));
        }
        tableView.getColumns().addAll(columns);
    }

    @FXML
    private void handleAddCard() {
        // 保存数据到CSV（示例需补充具体添加逻辑）
        CSVUtils.saveCards(cardData);
    }

    @FXML
    private void showTableView() {
        tableViewView.setVisible(true);
        inputView.setVisible(false);
    }

    @FXML
    private void showInputView() {
        inputView.setVisible(true);
        tableViewView.setVisible(false);
    }
}