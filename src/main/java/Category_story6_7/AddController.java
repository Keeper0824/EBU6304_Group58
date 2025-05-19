package src.main.java.Category_story6_7;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.main.java.Session;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This controller manages the add transaction window in the Transaction Management System.
 * It allows users to input new transaction details and saves them to a CSV file.
 * The controller provides a user-friendly interface for adding transactions and handles data validation and persistence.
 *
 * @author Wei Chen
 * @version 1.0
 */
public class AddController {
    private final String currentUser = Session.getCurrentNickname(); // Current user's nickname
    @FXML
    private TextField transactionField; // TextField for transaction description
    @FXML
    private TextField priceField; // TextField for transaction price
    @FXML
    private ComboBox<String> classificationField; // ComboBox for transaction classification
    @FXML
    private DatePicker dateField; // DatePicker for transaction date
    @FXML
    private ComboBox<String> IOTypeField; // ComboBox for transaction type (Income/Expense)

    private boolean added = false; // Flag to indicate if a transaction has been added
    private Transaction newTransaction; // The newly added transaction

    /**
     * Initializes the add transaction window by setting up the classification and IO type ComboBoxes.
     */
    @FXML
    private void initialize() {
        // Initialize classification ComboBox
        classificationField.getItems().addAll(
                "Income",
                "Food",
                "Clothing",
                "Household equipment and services",
                "Medical care",
                "Transportation and Communication",
                "Entertainment",
                "Educational supplies and services",
                "Residence",
                "Other goods and services"
        );

        // Initialize IO type ComboBox
        IOTypeField.getItems().addAll("Income", "Expense");
    }

    /**
     * Handles the save button click event.
     * Validates user input, creates a new transaction, and saves it to the CSV file.
     */
    @FXML
    private void handleSave() {
        // Get user input
        String id = getNextId(); // Get the next available ID
        String transaction = transactionField.getText();
        double price = Double.parseDouble(priceField.getText());
        String classification = classificationField.getValue();
        LocalDate localDate = dateField.getValue();
        String date = formatDate(localDate);
        String IOType = IOTypeField.getValue();
        added = true;

        // Create a new Transaction object
        newTransaction = new Transaction(id, transaction, price, classification, date, IOType);

        // Save the transaction to the CSV file
        saveTransactionToCSV(newTransaction);

        // Close the window
        Stage stage = (Stage) transactionField.getScene().getWindow();
        stage.close();
    }

    /**
     * Saves a transaction to the CSV file.
     * @param transaction The transaction to be saved.
     */
    private void saveTransactionToCSV(Transaction transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/" + currentUser + "_transaction.csv", true))) {
            // Append the new record to the file
            bw.write(transaction.getId() + "," + transaction.getTransaction() + "," + transaction.getPrice() + "," +
                    transaction.getClassification() + "," + transaction.getDate() + "," + transaction.getIOType());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // Show an error alert if saving fails
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving to CSV file.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the cancel button click event.
     * Closes the add transaction window without saving any changes.
     */
    @FXML
    private void handleCancel() {
        // Close the current window and return to the main page
        Stage stage = (Stage) transactionField.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks if a transaction has been added.
     * @return true if a transaction has been added, false otherwise.
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * Gets the newly added transaction.
     * @return The newly added transaction object.
     */
    public Transaction getTransaction() {
        return newTransaction;
    }

    /**
     * Generates the next available ID for a new transaction.
     * @return The next available ID as a string.
     */
    private String getNextId() {
        String lastLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + currentUser + "_transaction.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!lastLine.isEmpty()) {
            String[] parts = lastLine.split(",");
            try {
                int lastId = Integer.parseInt(parts[0]);
                return String.valueOf(lastId + 1);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return "1"; // Start from 1 if the file is empty or an error occurs
    }

    /**
     * Formats a LocalDate object to a string in "yyyy-MM-dd" format.
     * @param localDate The LocalDate object to be formatted.
     * @return The formatted date string.
     */
    private String formatDate(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }
}