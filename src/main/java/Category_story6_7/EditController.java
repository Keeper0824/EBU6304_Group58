package src.main.java.Category_story6_7;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EditController {
    @FXML
    private TextField transactionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField classificationField;
    @FXML
    private TextField dateField;

    private Transaction transaction;
    private boolean isEdited = false;
    private Transaction transaction1;

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        transaction1=transaction;
        // 初始化表单数据
        transactionField.setText(transaction.getTransaction());
        priceField.setText(String.valueOf(transaction.getPrice()));
        classificationField.setText(transaction.getClassification());
        dateField.setText(transaction.getDate());
    }

    @FXML
    private void handleSave() {
        // 获取表单数据
        String newTransaction = transactionField.getText(); // 新的 transaction 值
        double price = Double.parseDouble(priceField.getText());
        String classification = classificationField.getText();
        String date = dateField.getText();

        // 保存原始的 transaction 值
        String originalTransaction = this.transaction.getTransaction();

        // 更新交易对象
        this.transaction.setTransaction(newTransaction);
        this.transaction.setPrice(price);
        this.transaction.setClassification(classification);
        this.transaction.setDate(date);

        System.out.println("Updated Transaction: " + this.transaction);

        // 设置编辑标志为 true
        isEdited = true;

        // 写入 CSV 文件
        String filePath = "data/transactions.csv"; // CSV 文件路径
        List<String[]> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                System.out.println("CSV Line: " + line); // 打印 CSV 文件中的每一行
                String[] values = line.split(",");

                // 使用原始的 transaction 值进行匹配
                if (values.length >= 4 && values[0].equals(originalTransaction) && values[3].equals(date)) {
                    // 更新为新的值
                    lines.add(new String[]{newTransaction, String.valueOf(price), classification, date});
                    found = true;
                } else {
                    // 保留原始行
                    lines.add(values);
                }
            }
            if (!found) {
                System.out.println("Transaction not found in the list: " + this.transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] line : lines) {
                bw.write(String.join(",", line));
                bw.newLine();
            }
            System.out.println("Data successfully written to CSV file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭编辑窗口
        Stage stage = (Stage) transactionField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        isEdited = false;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}