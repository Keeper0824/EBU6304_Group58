package src.main.java.Suggestion_story_17;

import src.main.java.AI_story11_21_22.AIModelAPI;
import src.main.java.AI_story11_21_22.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Suggestion {

    private List<Transaction> transactions;  // 假设这是从数据库或CSV加载的交易记录

    public Suggestion() {
        this.transactions = new ArrayList<>();
    }

    // 添加交易记录的方法（假设有一些外部方法添加交易）
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    // 获取下月的消费预测
    public double getNextMonthPrediction() throws IOException, InterruptedException {
        // 使用AI模型来预测下个月的消费
        return AIModelAPI.predictNextMonthConsumption(this.transactions);
    }

    // 获取节省建议
    public String getSavingSuggestions() throws IOException, InterruptedException {
        // 构建用于AI模型的提示词
        StringBuilder prompt = new StringBuilder("以下是用户的支出记录，请提供节省建议：\n");
        for (Transaction t : transactions) {
            prompt.append(t.getDate()).append("，")
                    .append(t.getDescription()).append("，¥")
                    .append(t.getPrice()).append("，")
                    .append(t.getClassification()).append("\n");
        }
        prompt.append("请用简洁中文给出3条实际可行的削减开支建议：");

        // 调用AI模型并获取建议
        return AIModelAPI.getAIAdvice(prompt.toString());
    }

    // 获取下个月的消费预测并展示节省建议
    public String generatePredictionAndSuggestions() {
        try {
            // 预测下个月的消费
            double predictedExpense = getNextMonthPrediction();
            // 获取节省建议
            String savingSuggestions = getSavingSuggestions();

            // 合成最终的结果
            String result = "下月预测支出：¥" + predictedExpense + "\n\n节省建议：\n" + savingSuggestions;
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "获取AI预测或建议失败，请稍后再试。";
        }
    }

}
