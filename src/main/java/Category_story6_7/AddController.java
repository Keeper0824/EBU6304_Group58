package src.main.java.Category_story6_7;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AddController {
    @FXML
    private TextField transactionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField classificationField;
    @FXML
    private TextField dateField;

    private boolean added = false;
    private Transaction newTransaction;

    @FXML
    private void handleSave() {
        // 获取用户输入
        String transaction = transactionField.getText();
        double price = Double.parseDouble(priceField.getText());
        String classification = classificationField.getText();
        String date = dateField.getText();
        added = true;

        // 创建新的 Transaction 对象
        Transaction newTransaction = new Transaction(transaction, price, classification, date);

        // 将新记录保存到 CSV 文件
        saveTransactionToCSV(newTransaction);

        // 关闭当前窗口并返回主页面
        Stage stage = (Stage) transactionField.getScene().getWindow();
        stage.close();
    }

    private void saveTransactionToCSV(Transaction transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/transactions_6.csv", true))) {
            // 将新记录追加到文件末尾
            bw.write(transaction.getTransaction() + "," + transaction.getPrice() + "," + transaction.getClassification() + "," + transaction.getDate());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // 可以在这里添加错误处理逻辑，例如显示错误消息
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving transaction to CSV file.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        // 关闭当前窗口并返回主页面
        Stage stage = (Stage) transactionField.getScene().getWindow();
        stage.close();
    }

    public boolean isAdded() {
        return added;
    }

    public Transaction getTransaction() {
        return newTransaction;
    }

}