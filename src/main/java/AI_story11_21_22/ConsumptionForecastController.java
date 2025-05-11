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
            System.out.println("\n=== Starting consumption forecast process ===");
            System.out.println("[1] Initializing forecast interface...");

            // Get current user nickname
            String nickname = Session.getCurrentNickname();
            if (nickname == null || nickname.isEmpty()) {
                throw new IOException("User information not available, please login first");
            }
            System.out.println("[2] Current user nickname: " + nickname);

            // Build file path
            String filePath = "data/" + nickname + "_transaction.csv";
            File transactionFile = new File(filePath);
            System.out.println("[3] Attempting to load transaction file: " + transactionFile.getAbsolutePath());

            if (!transactionFile.exists()) {
                throw new IOException("User transaction data file not found: " + filePath);
            }

            // Load transaction data
            List<Transaction> transactions = DataPreprocessor.loadTransactions(filePath);
            System.out.println("[4] Successfully loaded transaction records: " + transactions.size());

            // Print first 5 transaction records (for data verification)
            if (!transactions.isEmpty()) {
                System.out.println("[5] Sample of first 5 transaction records:");
                for (int i = 0; i < Math.min(5, transactions.size()); i++) {
                    Transaction t = transactions.get(i);
                    System.out.printf("  - %s | %s | ¥%.2f | %s%n",
                            t.getDate(), t.getDescription(), t.getPrice(), t.getClassification());
                }
            } else {
                System.out.println("[5] Warning: No transaction records loaded");
                throw new IOException("Transaction records are empty");
            }

            // Call prediction API
            System.out.println("[6] Calling AI prediction API...");
            double forecast = AIModelAPI.predictNextMonthConsumption(transactions);
            System.out.println("[7] API returned forecast value: " + forecast);

            // Get health score and suggestions
            Map<String, String> scoreAndSuggestions = ConsumptionHealthAnalyzer.getHealthScoreAndSuggestions(transactions);
            String score = scoreAndSuggestions.get("score");
            String suggestions = scoreAndSuggestions.get("suggestions");

            // Update UI
            forecastLabel.setText("Your predicted consumption for next month is: ¥" + String.format("%.2f", forecast));
            healthScoreLabel.setText("Your score: " + score);
            suggestionsLabel.setText("Suggestions: " + suggestions);

            System.out.println("[8] Forecast results displayed");
            System.out.println("=== Consumption forecast process completed ===\n");

        } catch (IOException | InterruptedException e) {
            System.err.println("\n[ERROR] Error during initialization:");
            e.printStackTrace();

            String errorMessage = "Error loading data: " + e.getMessage();
            if (e instanceof IOException && e.getMessage().contains("file not found")) {
                errorMessage = "User transaction records not found, please add transaction data first";
            }

            forecastLabel.setText(errorMessage);
            healthScoreLabel.setText("");
            suggestionsLabel.setText("");
        }
    }
}