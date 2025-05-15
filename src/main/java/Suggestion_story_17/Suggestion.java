package src.main.java.Suggestion_story_17;

import src.main.java.AI_story11_21_22.AIModelAPI;
import src.main.java.AI_story11_21_22.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Title       : Suggestion.java
 * Description : Provides functionality to predict next month's expenses and offer saving suggestions
 *               based on the user's transaction history.
 *
 * Main functionalities include:
 * 1. Adding user transaction records;
 * 2. Predicting next month's expenses using an AI model;
 * 3. Retrieving saving suggestions from the AI model;
 * 4. Compiling and displaying both prediction and suggestions.
 *
 * Author      :
 * Version     : 1.0
 */
public class Suggestion {

    /**
     * Stores user transaction records.
     * These records are assumed to be loaded from a database or CSV file.
     */
    private List<Transaction> transactions;

    /**
     * Constructor that initializes the transaction list.
     */
    public Suggestion() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a transaction record to the list.
     *
     * @param transaction The user's transaction to add
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Predicts the total expenses for the next month using the AI model.
     *
     * @return The predicted expense amount (in RMB)
     * @throws IOException           if an I/O error occurs while accessing the model
     * @throws InterruptedException  if the model call is interrupted
     */
    public double getNextMonthPrediction() throws IOException, InterruptedException {
        return AIModelAPI.predictNextMonthConsumption(this.transactions);
    }

    /**
     * Retrieves practical saving suggestions based on the current transaction history.
     * Requests the AI model to return 3 concise, actionable tips.
     *
     * @return A string containing the saving suggestions
     * @throws IOException           if an I/O error occurs while accessing the model
     * @throws InterruptedException  if the model call is interrupted
     */
    public String getSavingSuggestions() throws IOException, InterruptedException {
        StringBuilder prompt = new StringBuilder("Here is the user's spending history. Please provide saving suggestions:\n");

        for (Transaction t : transactions) {
            prompt.append(t.getDate()).append(", ")
                    .append(t.getDescription()).append(", ¥")
                    .append(t.getPrice()).append(", ")
                    .append(t.getClassification()).append("\n");
        }

        prompt.append("Please give 3 concise and practical suggestions for reducing expenses, in Chinese:");

        return AIModelAPI.getAIAdvice(prompt.toString());
    }

    /**
     * Generates a prediction for next month's expenses along with saving suggestions.
     * Useful for UI display or user reports.
     *
     * @return A compiled string of predicted expenses and suggestions;
     *         if an error occurs, returns a failure message.
     */
    public String generatePredictionAndSuggestions() {
        try {
            double predictedExpense = getNextMonthPrediction();
            String savingSuggestions = getSavingSuggestions();

            return "Predicted Expense for Next Month: ¥" + predictedExpense + "\n\nSaving Suggestions:\n" + savingSuggestions;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Failed to retrieve AI prediction or suggestions. Please try again later.";
        }
    }
}
