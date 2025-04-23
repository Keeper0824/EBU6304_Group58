package src.main.java.Category_story6_7;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.main.java.Session;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;

public class AddController {
    private final static String currentUser = Session.getCurrentNickname();
    @FXML
    private TextField transactionField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> classificationField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<String> IOTypeField;

    private boolean added = false;
    private Transaction newTransaction;

    @FXML
    private void initialize() {
        // 初始化分类下拉列表
        classificationField.getItems().addAll(
                "Income",
                "Food",
                "Clothing",
                "Household equipment and services",
                "Medical care",
                "Transportation and Communication",
                "Entertainment",
                "Educational supplies and services",
                "Residence",
                "Other goods and services"
        );

        // 初始化IO类型下拉列表
        IOTypeField.getItems().addAll("Income", "Expense");
    }

    @FXML
    private void handleSave() {
        // 获取用户输入
        String id = getNextId(); // 使用新方法获取ID
        String transaction = transactionField.getText();
        double price = Double.parseDouble(priceField.getText());
        String classification = classificationField.getValue();
        LocalDate localDate = dateField.getValue();
        String date = formatDate(localDate);
        String IOType = IOTypeField.getValue();
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/"+currentUser+"_transaction.csv", true))) {
            // 将新记录追加到文件末尾
            bw.write(transaction.getId() + "," +transaction.getTransaction() + "," + transaction.getPrice() + "," + transaction.getClassification() + ","
                    + transaction.getDate()+"," + transaction.getIOType());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // 可以在这里添加错误处理逻辑，例如显示错误消息
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving "+ currentUser +"_transaction.csv to CSV file.");
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
        try (BufferedReader br = new BufferedReader(new FileReader("data/"+currentUser+"_transaction.csv"))) {
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
        return "1"; // 如果文件为空或出错，就从1开始
    }

    private String formatDate(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }
}