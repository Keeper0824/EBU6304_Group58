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
 * SuggestionController.java
 *
 * Controller responsible for handling user interactions in the AI-based spending suggestion view.
 * It reads transaction data of the current user, sends it to the AI model, and displays the returned advice.
 */
public class SuggestionController {

    @FXML
    private Text suggestionText;  // UI element displaying a heading or hint text

    @FXML
    private TextArea suggestionArea;  // UI text area for displaying AI suggestions

    /**
     * Event handler for the "Get AI Advice" button.
     * Retrieves the current user's transaction history and fetches spending advice from the AI model.
     */
    @FXML
    private void handleGetAIAdvice() {
        try {
            // Get the currently logged-in user's nickname
            String currentUser = Session.getCurrentNickname();

            // Construct the path to that user's transaction CSV file
            String csvFilePath = "data/" + currentUser + "_transaction.csv";

            // Load transaction data from file
            List<Transaction> transactions = readTransactionsFromCSV(csvFilePath);

            // Generate the prompt and get advice from the AI model
            String aiAdvice = AIModelAPI.getAIAdvice(generateAdvicePrompt(transactions));

            // Display the result in the suggestion area
            suggestionArea.setText(aiAdvice);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("Error", "Error while getting AI advice: " + e.getMessage());
        }
    }

    /**
     * Reads transaction records from a given CSV file path.
     *
     * @param filePath Path to the transaction CSV file
     * @return List of parsed Transaction objects
     * @throws IOException If an error occurs during file reading
     */
    private List<Transaction> readTransactionsFromCSV(String filePath) throws IOException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
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
     * Displays an alert popup with a given title and message.
     *
     * @param title   The title of the alert window
     * @param message The message to display
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Generates a prompt to be sent to the AI model based on user transaction data.
     *
     * @param transactions List of user transactions
     * @return A formatted prompt string suitable for the AI model
     */
    private String generateAdvicePrompt(List<Transaction> transactions) {
        StringBuilder prompt = new StringBuilder("Based on the following transaction data, provide advice on how to save expenses:\n");

        for (Transaction transaction : transactions) {
            // Only consider expenses for saving suggestions
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
}
