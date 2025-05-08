package src.main.java.AI_story11_21_22;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.main.java.Session;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConsumptionForecastController {

    @FXML
    private Label forecastLabel;
    @FXML
    private Label healthScoreLabel;
    @FXML
    private Label suggestionsLabel;

    @FXML
    private void initialize() {
        try {
            System.out.println("\n=== 开始消费预测流程 ===");
            System.out.println("【1】开始初始化预测界面...");

            // 获取当前用户昵称
            String nickname = Session.getCurrentNickname();
            if (nickname == null || nickname.isEmpty()) {
                throw new IOException("未获取到当前用户信息，请先登录");
            }
            System.out.println("【2】当前用户昵称: " + nickname);

            // 构建文件路径
            String filePath = "data/" + nickname + "_transaction.csv";
            File transactionFile = new File(filePath);
            System.out.println("【3】尝试加载交易文件: " + transactionFile.getAbsolutePath());

            if (!transactionFile.exists()) {
                throw new IOException("找不到用户的交易数据文件: " + filePath);
            }

            // 加载交易数据
            List<Transaction> transactions = DataPreprocessor.loadTransactions(filePath);
            System.out.println("【4】成功加载交易记录数: " + transactions.size());

            // 打印前5条交易记录（用于验证数据）
            if (!transactions.isEmpty()) {
                System.out.println("【5】前5条交易记录样例:");
                for (int i = 0; i < Math.min(5, transactions.size()); i++) {
                    Transaction t = transactions.get(i);
                    System.out.printf("  - %s | %s | ¥%.2f | %s%n",
                            t.getDate(), t.getDescription(), t.getPrice(), t.getClassification());
                }
            } else {
                System.out.println("【5】警告: 没有加载到任何交易记录");
                throw new IOException("交易记录为空");
            }

            // 调用预测API
            System.out.println("【6】开始调用AI预测API...");
            double forecast = AIModelAPI.predictNextMonthConsumption(transactions);
            System.out.println("【7】API返回的预测值: " + forecast);

            // 获取健康评分和建议
            Map<String, String> scoreAndSuggestions = ConsumptionHealthAnalyzer.getHealthScoreAndSuggestions(transactions);
            String score = scoreAndSuggestions.get("score");
            String suggestions = scoreAndSuggestions.get("suggestions");

            // 更新UI
            forecastLabel.setText("您的下个月预计消费金额是: ¥" + String.format("%.2f", forecast));
            healthScoreLabel.setText("您的消费健康评分为: " + score);
            suggestionsLabel.setText("消费建议: " + suggestions);

            System.out.println("【8】预测结果显示完成");
            System.out.println("=== 消费预测流程结束 ===\n");

        } catch (IOException | InterruptedException e) {
            System.err.println("\n【ERROR】初始化过程中出现错误:");
            e.printStackTrace();

            String errorMessage = "加载数据时出错: " + e.getMessage();
            if (e instanceof IOException && e.getMessage().contains("文件不存在")) {
                errorMessage = "找不到用户的交易记录，请先添加交易数据";
            }

            forecastLabel.setText(errorMessage);
            healthScoreLabel.setText("");
            suggestionsLabel.setText("");
        }
    }


}