package src.main.java.AI_story11_21_22;

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

/**
 * Title      : ConsumptionHealthAnalyzer.java
 * Description: This class analyzes the consumption health based on transaction data.
 *              It uses an AI model to generate a financial health score and spending suggestions.
 *
 * @author Wei Muchi
 * @version 1.0
 */
public class ConsumptionHealthAnalyzer {
    private static final String API_KEY = "sk-10283adb0b75447fa0d33a27ac317074";
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String MODEL_NAME = "qwen2.5-72b-instruct";

    /**
     * Gets the financial health score and spending suggestions based on the given transaction data.
     *
     * @param transactions a list of Transaction objects representing the transaction data
     * @return a map containing the health score and suggestions
     * @throws IOException          if an I/O error occurs during the API call
     * @throws InterruptedException if the API call is interrupted
     */
    public static Map<String, String> getHealthScoreAndSuggestions(List<Transaction> transactions) throws IOException, InterruptedException {
        System.out.println("\n[AI Model Call] Starting to get health score and suggestions...");

        // Generate prompt
        String prompt = generateHealthScorePrompt(transactions);
        System.out.println("[AI Model Call] Generated prompt:\n" + prompt);

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL_NAME);

        // Build messages parameter
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        requestBody.put("max_tokens", 500); // Reduced from 2000 since we want concise output

        // Print request body
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        System.out.println("[AI Model Call] Request JSON:\n" + jsonBody);

        // Create HTTP request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        System.out.println("[AI Model Call] Sending request to API...");

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("[AI Model Call] Received API response, status code: " + response.statusCode());
        System.out.println("[AI Model Call] Response content:\n" + response.body());

        // Parse response
        Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> messageMap = (Map<String, Object>) choice.get("message");
            String responseText = (String) messageMap.get("content");

            // Parse health score and suggestions
            String[] parts = responseText.split("Suggestions:");
            String score = parts[0].trim().replace("Score:", "").trim();
            String suggestions = parts.length > 1 ? parts[1].trim() : "";

            Map<String, String> result = new HashMap<>();
            result.put("score", score);
            result.put("suggestions", suggestions);
            return result;
        }

        System.out.println("[AI Model Call] Warning: No valid results received");
        Map<String, String> result = new HashMap<>();
        result.put("score", "0");
        result.put("suggestions", "No valid suggestions received. Please try again later.");
        return result;
    }

    /**
     * Generates a prompt for the AI model to get the health score and suggestions based on the given transaction data.
     *
     * @param transactions a list of Transaction objects representing the transaction data
     * @return the generated prompt string
     */
    private static String generateHealthScorePrompt(List<Transaction> transactions) {
        StringBuilder prompt = new StringBuilder("Analyze these transactions and provide:\n");
        prompt.append("1. A financial health score (0-100)\n");
        prompt.append("2. Exactly 3 practical spending suggestions in English\n\n");
        prompt.append("Each suggestion should be 2 sentences long - first identifying an area for improvement, then recommending specific action.\n\n");
        prompt.append("Transaction records:\n");

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
        prompt.append("\nFormat your response exactly like this:\n");
        prompt.append("Score: [your score number]\n");
        prompt.append("Suggestions:\n");
        prompt.append("1. [First suggestion - 3 sentences]\n");
        prompt.append("2. [Second suggestion - 3 sentences]\n");
        prompt.append("3. [Third suggestion - 3 sentences]");

        return prompt.toString();
    }
}