package src.main.java.AI_story_11_21_22;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumptionHealthAnalyzer {
    private static final String API_KEY = "sk-10283adb0b75447fa0d33a27ac317074";
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String MODEL_NAME = "qwen2.5-72b-instruct";

    public static Map<String, String> getHealthScoreAndSuggestions(List<Transaction> transactions) throws IOException, InterruptedException {
        System.out.println("\n【AI模型调用】开始获取健康评分和建议...");

        // 生成提示词
        String prompt = generateHealthScorePrompt(transactions);
        System.out.println("【AI模型调用】生成的提示词:\n" + prompt);

        // 准备请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL_NAME);

        // 构建 messages 参数
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        requestBody.put("max_tokens", 2000);

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
            String responseText = (String) messageMap.get("content");

            // 解析健康评分和建议
            String[] parts = responseText.split("建议:");
            String score = parts[0].trim();
            String suggestions = parts.length > 1 ? parts[1].trim() : "";

            Map<String, String> result = new HashMap<>();
            result.put("score", score);
            result.put("suggestions", suggestions);
            return result;
        }

        System.out.println("【AI模型调用】警告: 未获取到有效结果");
        Map<String, String> result = new HashMap<>();
        result.put("score", "0");
        result.put("suggestions", "无有效建议");
        return result;
    }

    private static String generateHealthScorePrompt(List<Transaction> transactions) {
        StringBuilder prompt = new StringBuilder("根据以下消费记录，给出一个0 - 100的健康评分，并给出一些消费建议：\n");
        for (Transaction transaction : transactions) {
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
        prompt.append("格式：评分: [分数]\n建议: [建议内容]");
        return prompt.toString();
    }
}