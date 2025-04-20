package src.main.java.Category_story6_7;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import src.main.java.Login_story1_3.MainMenuApp;
import src.main.java.Login_story1_3.User;
import src.main.java.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private final static String currentUser = Session.getCurrentNickname();
    @FXML
    private TableColumn<Transaction, Void> actionsColumn;
    @FXML
    private TableView<Transaction> tableView;
    @FXML
    private TableColumn<Transaction, String> transactionColumn;
    @FXML
    private TableColumn<Transaction, Double> priceColumn;
    @FXML
    private TableColumn<Transaction, String> classificationColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    @FXML
    private TableColumn<Transaction, String> IOColumn;

    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    public void initialize() {
        transactionColumn.setCellValueFactory(new PropertyValueFactory<>("transaction"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        IOColumn.setCellValueFactory(new PropertyValueFactory<>("IOType"));

        // 让所有列自动平铺，去掉右侧空白
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 全部文字居中
        transactionColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn        .setStyle("-fx-alignment: CENTER;");
        classificationColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn         .setStyle("-fx-alignment: CENTER;");
        IOColumn           .setStyle("-fx-alignment: CENTER;");
        actionsColumn      .setStyle("-fx-alignment: CENTER;");

        loadTransactions();

        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Edit");

            {
                btn.setOnAction(e -> {
                    Transaction item = getTableView().getItems().get(getIndex());
                    // 弹出编辑窗口
                    openEditWindow(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    @FXML
    private void handleBackToMainMenu(ActionEvent event) {
        try {
            // Close current transaction window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Get the current user from MainMenuApp
            User currentUser = MainMenuApp.getCurrentUser();

            // Open MainMenu with the same user
            new MainMenuApp(currentUser).start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to Main Menu: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void loadTransactions() {
        // 1. 动态拼路径
        String csvFilePath = "data/" + currentUser + "_transaction.csv";
        File csvFile = new File(csvFilePath);

        // 2. 如果文件不存在，就创建父目录和文件，并写入表头
        if (!csvFile.exists()) {
            try {
                csvFile.getParentFile().mkdirs(); // 确保 data/ 目录存在
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
                    // 根据你的 Transaction 字段顺序写表头
                    bw.write("Id,Transaction,Price,Classification,Date,IOType");
                    bw.newLine();
                }
                System.out.println("已为用户 " + currentUser + " 创建新的交易文件：" + csvFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "无法创建交易文件：" + e.getMessage());
                return;
            }
        }

        // 3. 读取并加载交易
        ObservableList<Transaction> list = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // 跳过表头
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length == 6) {
                    try {
                        double price = Double.parseDouble(data[2]);
                        Transaction tx = new Transaction(
                                data[0], data[1], price, data[3], data[4], data[5]
                        );
                        list.add(tx);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price: " + data[2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "加载交易时出错：" + e.getMessage());
        }

        // 4. 将数据绑定到表格
        tableView.setItems(list);
    }


    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/add.fxml"));
            Pane addPane = loader.load();
            AddController addController = loader.getController(); // 获取子控制器
            Stage stage = new Stage();
            stage.setScene(new Scene(addPane));
            stage.show();

            // 等待添加窗口关闭
            stage.setOnHidden(e -> {
                if (addController.isAdded()) { // 检查是否添加了新数据
                    Transaction newTransaction = addController.getTransaction(); // 获取新添加的交易
                    transactions.add(newTransaction); // 添加到数据列表
                    tableView.refresh(); // 刷新表格
                }
                loadTransactions();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        ObservableList<Transaction> selectedRows = tableView.getSelectionModel().getSelectedItems();
        for (Transaction transaction : selectedRows) {
            transactions.remove(transaction);
            deleteTransactionFromCSV(transaction);
        }
        loadTransactions();
    }

    private void deleteTransactionFromCSV(Transaction transaction) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/"+currentUser+"_transaction.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals(transaction.getId() + "," + transaction.getTransaction() + "," + transaction.getPrice() + "," + transaction.getClassification() + "," +
                        transaction.getDate() + "," + transaction.getIOType())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/"+currentUser+"_transaction.csv"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadTransactions();
    }

    private void openEditWindow(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/edit.fxml"));
            Pane editPane = loader.load();
            EditController editController = loader.getController(); // 获取编辑窗口的控制器
            editController.setTransaction(transaction); // 将当前选中的交易数据传递给编辑控制器

            Stage stage = new Stage();
            stage.setScene(new Scene(editPane));
            stage.show();

            // 等待编辑窗口关闭
            stage.setOnHidden(e -> {
                if (editController.isEdited()) { // 检查是否进行了编辑
                    Transaction editedTransaction = editController.getTransaction(); // 获取编辑后的交易数据
                    int index = transactions.indexOf(transaction);
                    if (index != -1) {
                        transactions.set(index, editedTransaction); // 更新表格数据
                        tableView.refresh();
                        updateTransactionInCSV(transaction, editedTransaction); // 更新CSV文件
                    } else {
                        System.err.println("Transaction not found in the list: " + transaction);
                    }
                }
                loadTransactions();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTransactionInCSV(Transaction oldTransaction, Transaction newTransaction) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/"+currentUser+"_transaction.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(oldTransaction.getTransaction() + "," + oldTransaction.getPrice() + "," + oldTransaction.getClassification() + "," + oldTransaction.getDate()+ "," + oldTransaction.getIOType()  )) {
                    lines.add(newTransaction.getTransaction() + "," + newTransaction.getPrice() + "," + newTransaction.getClassification() + "," + newTransaction.getDate()+ "," + newTransaction.getIOType()  );
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/"+currentUser+"_transaction.csv"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}