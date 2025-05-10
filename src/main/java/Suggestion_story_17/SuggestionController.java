package src.main.java.Suggestion_story_17;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import src.main.java.Category_story6_7.MainController;
import src.main.java.AI_story11_21_22.AIModelAPI;
import src.main.java.Category_story6_7.Transaction;
import src.main.java.Session;  // 导入Session来获取当前用户

import java.io.*;
import java.util.List;

public class SuggestionController {

    @FXML
    private Text suggestionText;  // 显示标题或提示信息

    @FXML
    private TextArea suggestionArea;  // 显示建议的区域

    // 获取建议按钮的事件处理
    @FXML
    private void handleGetAIAdvice() {
        // 实现获取 AI 节省建议的逻辑
        try {
            // 获取当前用户信息
            String currentUser = Session.getCurrentNickname();  // 获取当前用户

            // 根据当前用户生成文件路径
            String csvFilePath = "data/" + currentUser + "_transaction.csv";  // 根据当前用户构建文件路径

            // 读取文件中的交易数据
            List<Transaction> transactions = readTransactionsFromCSV(csvFilePath);

            // 调用AI模型获取建议
            String aiAdvice = AIModelAPI.getAIAdvice(generateAdvicePrompt(transactions));

            // 在 TextArea 中显示建议
            suggestionArea.setText(aiAdvice);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("Error", "获取 AI 建议时出错: " + e.getMessage());
        }
    }

    // 读取交易数据文件的方法
    private List<Transaction> readTransactionsFromCSV(String filePath) throws IOException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // 跳过表头
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length != 6) {
                    System.err.println("CSV文件格式错误，该行数据字段数量不正确: " + line);
                    continue;
                }
                try {
                    double price = Double.parseDouble(data[2]);
                    Transaction tx = new Transaction(
                            data[0], data[1], price, data[3], data[4], data[5]
                    );
                    transactions.add(tx);
                } catch (NumberFormatException e) {
                    System.err.println("价格数据格式错误: " + data[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("加载交易数据时出错：" + e.getMessage());
        }

        return transactions;
    }

    // 显示提示框
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 生成用于 AI 分析的提示词
    private String generateAdvicePrompt(List<Transaction> transactions) {
        StringBuilder prompt = new StringBuilder("根据以下交易数据，给出节省开支的建议：\n");

        // 添加交易数据
        for (Transaction transaction : transactions) {
            if ("expense".equals(transaction.getIOType())) {  // 确保是支出类型的交易
                prompt.append(transaction.getDate())
                        .append(", ")
                        .append(transaction.getTransaction())
                        .append(", ¥")
                        .append(transaction.getPrice())
                        .append(", ")
                        .append(transaction.getClassification())
                        .append(", ")
                        .append(transaction.getIOType())
                        .append("\n");
            }
        }
        prompt.append("\n请根据上述数据给出节省开支的建议，特别是在哪些分类上可以减少开支。");
        return prompt.toString();
    }
}
