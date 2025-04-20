package src.main.java.Category_story6_7;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class AddController {
    @FXML
    private TextField transactionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField classificationField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField IOTypeField;

    private boolean added = false;
    private Transaction newTransaction;

    @FXML
    private void handleSave() {
        // 获取用户输入
        String id = getNextId(); // 使用新方法获取ID
        String transaction = transactionField.getText();
        double price = Double.parseDouble(priceField.getText());
        String classification = classificationField.getText();
        String date = dateField.getText();
        String IOType = IOTypeField.getText();
        added = true;

        // 创建新的 Transaction 对象
        newTransaction = new Transaction(id, transaction, price, classification, date, IOType);

        // 保存到 CSV 文件
        saveTransactionToCSV(newTransaction);

        // 关闭窗口
        Stage stage = (Stage) transactionField.getScene().getWindow();
        stage.close();
    }


    private void saveTransactionToCSV(Transaction transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/1_transaction.csv", true))) {
            // 将新记录追加到文件末尾
            bw.write(transaction.getId() + "," +transaction.getTransaction() + "," + transaction.getPrice() + "," + transaction.getClassification() + ","
                    + transaction.getDate()+"," + transaction.getIOType());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // 可以在这里添加错误处理逻辑，例如显示错误消息
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving 1_transaction.csv to CSV file.");
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

    private String getNextId() {
        String lastLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader("data/1_transaction.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!lastLine.isEmpty()) {
            String[] parts = lastLine.split(",");
            try {
                int lastId = Integer.parseInt(parts[0]);
                return String.valueOf(lastId + 1);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return "1"; // 如果文件为空或出错，就从1开始喵
    }


}