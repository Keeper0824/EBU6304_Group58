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

/**
 * Title      : AIModelAPI.java
 * Description: This class provides methods to interact with an AI model API.
 *              It can predict the next month's consumption based on transaction data
 *              and get AI advice based on a given prompt.
 *
 * @author Wei Muchi
 * @version 1.0
 */
public class AIModelAPI {
    private static final String API_KEY = "sk-10283adb0b75447fa0d33a27ac317074";
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String MODEL_NAME = "qwen2.5-72b-instruct";

    /**
     * Predicts the total expenses for the next month based on the given transaction data.
     *
     * @param transactions a list of Transaction objects representing the transaction data
     * @return the predicted total expenses for the next month
     * @throws IOException          if an I/O error occurs during the API call
     * @throws InterruptedException if the API call is interrupted
     */
    public static double predictNextMonthConsumption(List<Transaction> transactions) throws IOException, InterruptedException {
        System.out.println("\n[AI Model Invocation] Starting the prediction process...");

        // Generate the prompt
        String prompt = generatePrompt(transactions);
        System.out.println("[AI Model Invocation] Generated prompt words:\n" + prompt);

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

        requestBody.put("max_tokens", 50);

        // Print request body
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        System.out.println("[AI Model Invocation] Request body JSON:\n" + jsonBody);

        // Create HTTP request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        System.out.println("[AI Model Invocation] Sending request to API...");

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("[AI Model Invocation] Received API response, status code: " + response.statusCode());
        System.out.println("[AI Model Invocation] Response content:\n" + response.body());

        // Parse response
        Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> messageMap = (Map<String, Object>) choice.get("message");
            String forecastText = (String) messageMap.get("content");
            System.out.println("[AI Model Invocation] Original forecast text: " + forecastText);

            double forecast = extractForecastValue(forecastText);
            System.out.println("[AI Model Invocation] Extracted forecast value: " + forecast);

            return forecast;
        }

        System.out.println("[AI Model Invocation] Warning: No valid prediction result obtained");
        return 0.0;
    }

    /**
     * Generates a prompt for the AI model based on the given transaction data.
     *
     * @param transactions a list of Transaction objects representing the transaction data
     * @return the generated prompt string
     */
    private static String generatePrompt(List<Transaction> transactions) {
        StringBuilder prompt = new StringBuilder("Predict the total expenses for the next month based on the following transactions:\n");
        for (Transaction transaction : transactions) {
            if ("expense".equals(transaction.getIoType())) {  // Only count expenses
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
        prompt.append("Please predict the total expenses for the next month (return only the number, without any other words or symbols): ");
        return prompt.toString();
    }

    /**
     * Extracts the forecast value from the given forecast text.
     *
     * @param forecastText the text containing the forecast value
     * @return the extracted forecast value as a double
     */
    private static double extractForecastValue(String forecastText) {
        // More robust numeric extraction method
        forecastText = forecastText.replaceAll("[^0-9.]", ""); // Remove non-numeric characters
        if (forecastText.isEmpty()) {
            System.err.println("[Numeric Extraction] Warning: No valid number found in the forecast text");
            return 0.0;
        }

        try {
            double value = Double.parseDouble(forecastText);
            System.out.println("[Numeric Extraction] Successfully extracted value: " + value);
            return value;
        } catch (NumberFormatException e) {
            System.err.println("[Numeric Extraction] Error: Unable to parse forecast text: " + forecastText);
            return 0.0;
        }
    }

    /**
     * Gets AI advice based on the given prompt text.
     *
     * @param promptText the prompt text for the AI model
     * @return the AI advice as a string
     * @throws IOException          if an I/O error occurs during the API call
     * @throws InterruptedException if the API call is interrupted
     */
    public static String getAIAdvice(String promptText) throws IOException, InterruptedException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL_NAME);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", promptText);
        messages.add(message);
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 300);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> messageMap = (Map<String, Object>) choice.get("message");
            return (String) messageMap.get("content");
        }
        return "No advice received, please try again.";
    }
}
