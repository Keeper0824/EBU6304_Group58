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
import src.main.java.Session;

import java.io.*;
import java.util.List;

/**
 * Title      : SuggestionController.java
 * Description: Controller for the AI-based spending suggestion interface.
 *              Reads the current user's transaction history, filters expenses,
 *              sends a formatted prompt to the AI model, and displays generated saving suggestions.
 *              Handles input validation, error display, and CSV parsing.
 *
 * @author Kaiyu Liu
 * @version 1.0
 */
public class SuggestionController {

    @FXML
    private Text suggestionText;  // Label or heading for the suggestion area

    @FXML
    private TextArea suggestionArea;  // Text area to display AI-generated saving advice

    /**
     * Event handler triggered by the "Get AI Advice" button.
     * Loads the current user's expense history, generates an AI prompt,
     * and displays advice returned by the AI model.
     */
    @FXML
    private void handleGetAIAdvice() {
        try {
            // Get the currently logged-in user's nickname
            String currentUser = Session.getCurrentNickname();

            // Construct file path to user's transaction CSV
            String csvFilePath = "data/" + currentUser + "_transaction.csv";

            // Load transaction data from file
            List<Transaction> transactions = readTransactionsFromCSV(csvFilePath);

            // Generate a prompt and send it to the AI model
            String aiAdvice = AIModelAPI.getAIAdvice(generateAdvicePrompt(transactions));

            // Display AI response in the UI
            suggestionArea.setText(aiAdvice);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("Error", "Error while getting AI advice: " + e.getMessage());
        }
    }

    /**
     * Reads transaction records from a CSV file.
     * Parses each line into a Transaction object, skipping malformed or invalid lines.
     *
     * @param filePath Absolute or relative path to the CSV file
     * @return A list of Transaction objects parsed from the file
     * @throws IOException If file reading fails
     */
    public List<Transaction> readTransactionsFromCSV(String filePath) throws IOException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header row

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");

                if (data.length != 6) {
                    System.err.println("CSV format error (wrong number of fields): " + line);
                    continue;
                }

                try {
                    double price = Double.parseDouble(data[2]);
                    Transaction tx = new Transaction(
                            data[0], data[1], price, data[3], data[4], data[5]
                    );
                    transactions.add(tx);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format: " + data[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to load transaction data: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * Generates a natural language prompt from the user's transaction data.
     * Only includes transactions where IOType is "expense".
     *
     * @param transactions List of transactions to summarize for the AI model
     * @return A formatted string representing the AI input prompt
     */
    public String generateAdvicePrompt(List<Transaction> transactions) {
        StringBuilder prompt = new StringBuilder("Based on the following transaction data, provide advice on how to save expenses:\n");

        for (Transaction transaction : transactions) {
            if ("expense".equals(transaction.getIOType())) {
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

        prompt.append("\nPlease provide specific suggestions on which categories to reduce spending on based on the above data.");
        return prompt.toString();
    }

    /**
     * Displays an alert dialog box with a given error title and message.
     *
     * @param title   Title of the alert window
     * @param message Message to display in the alert content
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
