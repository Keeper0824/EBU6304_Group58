package src.main.java.Category_story6_7;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {
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

    private void loadTransactions() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader("data/transaction.csv"))) {
            String line;
            br.readLine(); // 跳过标题行
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) { // 确保数据行有四列
                    try {
                        double price = Double.parseDouble(data[2]); // 尝试解析价格
                        Transaction transaction = new Transaction(data[0],data[1], price, data[3], data[4],data[5]);
                        transactions.add(transaction);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price format: " + data[2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableView.setItems(transactions);
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
        try (BufferedReader br = new BufferedReader(new FileReader("data/transaction.csv"))) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/transaction.csv"))) {
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
        try (BufferedReader br = new BufferedReader(new FileReader("data/transaction.csv"))) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/transaction.csv"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}