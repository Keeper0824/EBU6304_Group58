package src.main.java.Category_story6_7;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.main.java.Session;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Title      : EditController.java
 * Description: This controller manages the edit transaction window in the Transaction Management System.
 * It allows users to modify existing transactions and updates the changes in the CSV file.
 *
 * @author Wei Chen
 * @version 1.0
 */
public class EditController {
    private final String currentUser = Session.getCurrentNickname(); // Current user's nickname
    @FXML
    private TextField transactionField; // TextField for transaction name
    @FXML
    private TextField priceField; // TextField for transaction price
    @FXML
    private ComboBox<String> classificationField; // ComboBox for transaction classification
    @FXML
    private DatePicker dateField; // DatePicker for transaction date
    @FXML
    private ComboBox<String> IOTypeField; // ComboBox for transaction type (Income/Expense)

    private Transaction transaction; // The transaction being edited
    private boolean isEdited = false; // Flag to indicate if the transaction has been edited
    private Transaction transaction1; // Backup of the original transaction

    /**
     * Initializes the edit window by setting up the classification and IO type ComboBoxes.
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
     * Sets the transaction to be edited and populates the form fields with its data.
     * @param transaction The transaction object to be edited.
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        transaction1 = transaction;

        // Populate form fields with transaction data
        transactionField.setText(transaction.getTransaction());
        priceField.setText(String.valueOf(transaction.getPrice()));
        classificationField.setValue(transaction.getClassification());
        dateField.setValue(LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        IOTypeField.setValue(transaction.getIOType());
    }

    /**
     * Handles the save button click event.
     * Updates the transaction data and writes the changes to the CSV file.
     */
    @FXML
    private void handleSave() {
        // Get form data
        String newTransaction = transactionField.getText();
        double price = Double.parseDouble(priceField.getText());
        String classification = classificationField.getValue();
        LocalDate date = dateField.getValue();
        String formattedDate = formatDate(date);
        String IOType = IOTypeField.getValue();

        String originalTransaction = this.transaction.getTransaction();
        String id = this.transaction.getId();

        // Update transaction object
        this.transaction.setTransaction(newTransaction);
        this.transaction.setPrice(price);
        this.transaction.setClassification(classification);
        this.transaction.setDate(formattedDate);
        this.transaction.setIOType(IOType);

        // Set edited flag to true
        isEdited = true;

        // Update CSV file
        String filePath = "data/" + currentUser + "_transaction.csv";
        List<String[]> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length >= 6 && values[1].equals(originalTransaction)) {
                    lines.add(new String[]{id, newTransaction, String.valueOf(price), classification, formattedDate, IOType});
                    found = true;
                } else {
                    lines.add(values);
                }
            }
            if (!found) {
                System.out.println("Transaction not found in the list: " + this.transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] line : lines) {
                bw.write(String.join(",", line));
                bw.newLine();
            }
            System.out.println("Data successfully written to CSV file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close edit window
        Stage stage = (Stage) transactionField.getScene().getWindow();
        stage.close();
    }

    /**
     * Formats a LocalDate object to a string in "yyyy-MM-dd" format.
     * @param date The LocalDate object to be formatted.
     * @return The formatted date string.
     */
    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    /**
     * Handles the cancel button click event.
     * Sets the edited flag to false.
     */
    @FXML
    private void handleCancel() {
        isEdited = false;
    }

    /**
     * Checks if the transaction has been edited.
     * @return true if the transaction has been edited, false otherwise.
     */
    public boolean isEdited() {
        return isEdited;
    }

    /**
     * Gets the edited transaction object.
     * @return The edited transaction object.
     */
    public Transaction getTransaction() {
        return transaction;
    }
}