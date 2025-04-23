package src.main.java.AI_story11_21_22;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AIModelAPI {
    private static final String API_KEY = "sk-10283adb0b75447fa0d33a27ac317074";
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String MODEL_NAME = "qwen2.5-72b-instruct";

    public static double predictNextMonthConsumption(List<Transaction> transactions) throws IOException, InterruptedException {
        System.out.println("\n【AI模型调用】开始预测流程...");

        // 生成提示词
        String prompt = generatePrompt(transactions);
        System.out.println("【AI模型调用】生成的提示词:\n" + prompt);

        // 准备请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model","qwen2.5-72b-instruct");

        // 构建 messages 参数
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        requestBody.put("max_tokens", 50);

        // 打印请求体
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        System.out.println("【AI模型调用】请求体JSON:\n" + jsonBody);

        // 创建HTTP请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        System.out.println("【AI模型调用】发送请求到API...");

        // 发送请求
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("【AI模型调用】收到API响应，状态码: " + response.statusCode());
        System.out.println("【AI模型调用】响应内容:\n" + response.body());

        // 解析响应
        Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> messageMap = (Map<String, Object>) choice.get("message");
            String forecastText = (String) messageMap.get("content");
            System.out.println("【AI模型调用】原始预测文本: " + forecastText);

            double forecast = extractForecastValue(forecastText);
            System.out.println("【AI模型调用】提取后的预测值: " + forecast);

            return forecast;
        }

        System.out.println("【AI模型调用】警告: 未获取到有效预测结果");
        return 0.0;
    }

    private static String generatePrompt(List<Transaction> transactions) {
        StringBuilder prompt = new StringBuilder("Predict the total expenses for the next month based on the following transactions:\n");
        for (Transaction transaction : transactions) {
            if("expense".equals(transaction.getIoType())) {  // 只统计支出
                prompt.append(transaction.getDate())
                        .append(", ")
                        .append(transaction.getDescription())
                        .append(", ¥")
                        .append(transaction.getPrice())
                        .append(", ")
                        .append(transaction.getClassification())
                        .append(", ")
                        .append(transaction.getIoType())
                        .append("\n");
            }
        }
        prompt.append("请预测下个月的总支出金额（只返回数字，不要包含任何其他文字或符号）: ");
        return prompt.toString();
    }

    private static double extractForecastValue(String forecastText) {
        // 更健壮的数值提取方法
        forecastText = forecastText.replaceAll("[^0-9.]", ""); // 移除非数字字符
        if(forecastText.isEmpty()) {
            System.err.println("【数值提取】警告: 预测文本中未找到有效数字");
            return 0.0;
        }

        try {
            double value = Double.parseDouble(forecastText);
            System.out.println("【数值提取】成功提取数值: " + value);
            return value;
        } catch (NumberFormatException e) {
            System.err.println("【数值提取】错误: 无法解析预测文本: " + forecastText);
            return 0.0;
        }
    }
}