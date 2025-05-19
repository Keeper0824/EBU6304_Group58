package src.main.java.Category_story6_7;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import src.main.java.Login_story1_3.User;
import src.main.java.Session;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Title      : MainController.java
 * Description: This JavaFX controller manages a financial transaction management system.
 *              It handles displaying, adding, deleting, and editing transactions,
 *              as well as providing budget analysis and suggestions.
 *
 * @author Wei Chen
 * @version 1.0
 * @author Zhengxuan Han
 * @version 1.1
 */
public class MainController {
    private final String currentUser = Session.getCurrentNickname();

    @FXML
    private TableColumn<Transaction, Void> actionsColumn;
    @FXML
    private TableView<Transaction> tableView;
    @FXML
    private TableColumn<Transaction, String> transactionColumn;
    @FXML
    private TableColumn<Transaction, Double> priceColumn;
    @FXML
    private TableColumn<Transaction, String> classificationColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    @FXML
    private TableColumn<Transaction, String> IOColumn;
    @FXML
    private TextField budgetField;

    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    /**
     * Initializes the main controller, setting up the table view and loading transactions.
     * This method is called when the FXML file is loaded.
     */
    public void initialize() {
        transactionColumn.setCellValueFactory(new PropertyValueFactory<>("transaction"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        IOColumn.setCellValueFactory(new PropertyValueFactory<>("IOType"));
        tableView.setPlaceholder(new Label("No data available"));

        // Make all columns auto-resize and remove empty space on the right
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Center text in all columns
        transactionColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setStyle("-fx-alignment: CENTER;");
        classificationColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setStyle("-fx-alignment: CENTER;");
        IOColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setStyle("-fx-alignment: CENTER;");

        loadTransactions();

        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Edit");

            {
                btn.setOnAction(e -> {
                    Transaction item = getTableView().getItems().get(getIndex());
                    // Open edit window
                    openEditWindow(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    /**
     * Displays an alert dialog with an error message.
     * @param title The title of the alert dialog.
     * @param message The error message to be displayed.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Loads transactions from a CSV file and populates the table view.
     * This method reads the CSV file, creates Transaction objects, and adds them to the table view.
     */
    private void loadTransactions() {
        // 1. Dynamically build the file path
        String csvFilePath = "data/" + currentUser + "_transaction.csv";
        File csvFile = new File(csvFilePath);

        // Check if currentUser is null or empty
        if (currentUser == null || currentUser.isEmpty()) {
            showAlert("Error", "Failed to retrieve current user information, cannot load transactions");
            return;
        }

        // 2. If the file doesn't exist, create the directory and file, then write header
        if (!csvFile.exists()) {
            try {
                csvFile.getParentFile().mkdirs(); // Ensure data/ directory exists
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
                    // Write header based on Transaction fields
                    bw.write("Id,Transaction,Price,Classification,Date,IOType");
                    bw.newLine();
                }
                System.out.println("Created new transaction file for user " + currentUser + ": " + csvFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Cannot create transaction file: " + e.getMessage());
                return;
            }
        }

        // 3. Read and load transactions
        ObservableList<Transaction> list = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length != 6) {
                    System.err.println("CSV format error: incorrect number of fields: " + line);
                    continue;
                }
                try {
                    double price = Double.parseDouble(data[2]);
                    Transaction tx = new Transaction(
                            data[0], data[1], price, data[3], data[4], data[5]
                    );
                    list.add(tx);
                } catch (NumberFormatException e) {
                    System.err.println("Price format error: " + data[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error loading transactions: " + e.getMessage());
        }

        // 4. Bind data to the table
        tableView.setItems(list);
    }

    /**
     * Handles the add button click event.
     * Opens the add transaction window and updates the table view if a new transaction is added.
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/add.fxml"));
            Pane addPane = loader.load();
            AddController addController = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(addPane));
            stage.show();

            // Wait for add window to close
            stage.setOnHidden(e -> {
                if (addController.isAdded()) { // Check if new data was added
                    Transaction newTransaction = addController.getTransaction();
                    transactions.add(newTransaction);
                    tableView.refresh();
                }
                loadTransactions();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the delete button click event.
     * Deletes the selected transactions from the table view and the CSV file.
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleDelete(ActionEvent event) {
        ObservableList<Transaction> selectedRows = tableView.getSelectionModel().getSelectedItems();
        for (Transaction transaction : selectedRows) {
            transactions.remove(transaction);
            deleteTransactionFromCSV(transaction);
        }
        loadTransactions();
    }

    /**
     * Deletes a transaction from the CSV file.
     * @param transaction The transaction to be deleted.
     */
    private void deleteTransactionFromCSV(Transaction transaction) {
        List<String> lines = new ArrayList<>();
        String csvFilePath = "data/" + currentUser + "_transaction.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals(transaction.getId() + "," + transaction.getTransaction() + "," + transaction.getPrice() + "," + transaction.getClassification() + "," +
                        transaction.getDate() + "," + transaction.getIOType())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the refresh button click event.
     * Reloads the transactions from the CSV file and updates the table view.
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadTransactions();
    }

    /**
     * Opens the edit window for a transaction.
     * @param transaction The transaction to be edited.
     */
    private void openEditWindow(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/edit.fxml"));
            Pane editPane = loader.load();
            EditController editController = loader.getController();
            editController.setTransaction(transaction);

            Stage stage = new Stage();
            stage.setScene(new Scene(editPane));
            stage.show();

            // Wait for edit window to close
            stage.setOnHidden(e -> {
                if (editController.isEdited()) {
                    Transaction editedTransaction = editController.getTransaction();
                    int index = transactions.indexOf(transaction);
                    if (index != -1) {
                        transactions.set(index, editedTransaction);
                        tableView.refresh();
                        updateTransactionInCSV(transaction, editedTransaction);
                    } else {
                        System.err.println("Transaction not found in the list: " + transaction);
                    }
                }
                loadTransactions();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a transaction in the CSV file.
     * @param oldTransaction The original transaction.
     * @param newTransaction The updated transaction.
     */
    private void updateTransactionInCSV(Transaction oldTransaction, Transaction newTransaction) {
        List<String> lines = new ArrayList<>();
        String csvFilePath = "data/" + currentUser + "_transaction.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(oldTransaction.getTransaction() + "," + oldTransaction.getPrice() + "," + oldTransaction.getClassification() + "," + oldTransaction.getDate() + "," + oldTransaction.getIOType())) {
                    lines.add(newTransaction.getTransaction() + "," + newTransaction.getPrice() + "," + newTransaction.getClassification() + "," + newTransaction.getDate() + "," + newTransaction.getIOType());
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of transactions.
     * @return The observable list of transactions.
     */
    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Handles the suggestion button click event.
     * Opens the suggestion window.
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleSuggestion(ActionEvent event) {
        try {
            // Ensure the path is correct, relative to the resources directory
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/suggestion.fxml"));
            Pane suggestionPane = loader.load();

            // Open a new window and display suggestions
            Stage suggestionStage = new Stage();
            suggestionStage.setTitle("Suggestion");
            suggestionStage.setScene(new Scene(suggestionPane));
            suggestionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open suggestion page: " + e.getMessage());
        }
    }

    /**
     * Handles the compare button click event.
     * Checks if the current user is a VIP user and performs budget analysis if they are.
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleCompare(ActionEvent event) {
        // Check VIP status first
        if (!isCurrentUserVIP()) {
            showAlert("Access Denied", "This feature is only available for VIP users.");
            return;
        }

        try {
            double budget = Double.parseDouble(budgetField.getText());
            if (budget <= 0) {
                showAlert("Error", "Budget must be a positive number");
                return;
            }

            // Rest of the existing compare logic...
            List<Transaction> currentExpenses = getCurrentMonthExpenses();
            if (currentExpenses.isEmpty()) {
                showAlert("Info", "No expenses recorded for the current month");
                return;
            }

            String analysis = analyzeBudgetWithAI(budget, currentExpenses);

            // Show the analysis report in a new window
            showAnalysisReport(analysis);

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid budget number");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to analyze budget: " + e.getMessage());
        }
    }

    /**
     * Checks if the current user is a VIP user.
     * @return true if the user is a VIP, false otherwise.
     */
    private boolean isCurrentUserVIP() {
        String currentUser = Session.getCurrentNickname();
        if (currentUser == null || currentUser.isEmpty()) {
            System.err.println("Error: Current user is null or empty!");
            return false;
        }

        String csvFilePath = "data/user.csv"; // or use the full path instead
        File csvFile = new File(csvFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                System.out.println("Debug - CSV line: " + Arrays.toString(data)); // Debug

                // Check if the number of columns is sufficient and if the user names match (ignoring case and Spaces).
                if (data.length >= 7 && data[1].trim().equalsIgnoreCase(currentUser.trim())) {
                    boolean isVIP = "VIP".equalsIgnoreCase(data[6].trim());
                    System.out.println("Debug - VIP status: " + isVIP); // Debug
                    return isVIP;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user.csv: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Shows the analysis report in a new window.
     * @param reportText The analysis report text to be displayed.
     */
    private void showAnalysisReport(String reportText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/AnalysisReport.fxml"));
            Pane reportPane = loader.load();
            AnalysisReportController reportController = loader.getController();
            reportController.setReportText(reportText);

            Stage stage = new Stage();
            stage.setScene(new Scene(reportPane, 1200, 675));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the current month's expenses from the table view.
     * @return A list of transactions representing the current month's expenses.
     */
    private List<Transaction> getCurrentMonthExpenses() {
        List<Transaction> expenses = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        for (Transaction tx : tableView.getItems()) {
            try {
                LocalDate txDate = LocalDate.parse(tx.getDate());
                if (tx.getIOType().equalsIgnoreCase("expense") && txDate.getMonthValue() == currentMonth && txDate.getYear() == currentYear) {
                    expenses.add(tx);
                }
            } catch (Exception e) {
                System.err.println("The transaction date format is incorrect and cannot be parsed: " + tx.getDate());
            }
        }
        return expenses;
    }

    /**
     * Analyzes the budget with an AI service.
     * @param budget The user's budget.
     * @param expenses The list of expenses.
     * @return The analysis report from the AI service.
     * @throws Exception If there is an error in the analysis process.
     */
    private String analyzeBudgetWithAI(double budget, List<Transaction> expenses) throws Exception {
        // Group expenses by category
        Map<String, Double> categorySpending = new HashMap<>();
        double totalSpent = 0;

        for (Transaction tx : expenses) {
            String category = tx.getClassification();
            double amount = tx.getPrice();
            categorySpending.put(category, categorySpending.getOrDefault(category, 0.0) + amount);
            totalSpent += amount;
        }

        // Prepare prompt for AI
        StringBuilder prompt = new StringBuilder();
        prompt.append("I have set a monthly budget of ¥").append(budget)
                .append(". My actual spending this month is ¥").append(totalSpent)
                .append(". Here's the breakdown by category:\n");

        for (Map.Entry<String, Double> entry : categorySpending.entrySet()) {
            prompt.append("- ").append(entry.getKey()).append(": ¥").append(entry.getValue())
                    .append(" (").append(String.format("%.1f", (entry.getValue() / totalSpent) * 100))
                    .append("% of total spending)\n");
        }

        prompt.append("\nPlease analyze my spending compared to my budget and provide specific recommendations. ")
                .append("Focus on categories where I might be overspending. ")
                .append("Also suggest how I could better allocate my budget next month. ")
                .append("Keep the response concise but insightful.");

        // Call AI API
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen2.5-72b-instruct");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt.toString());
        messages.add(message);
        requestBody.put("messages", messages);

        requestBody.put("max_tokens", 500);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + "sk-10283adb0b75447fa0d33a27ac317074")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Map<String, Object> responseMap = mapper.readValue(response.body(), Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

            if (choices != null &&!choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> messageMap = (Map<String, Object>) choice.get("message");
                return (String) messageMap.get("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("AI analysis request failed:" + e.getMessage());
        }


        return "Sorry, couldn't get analysis from AI. Please try again later.";
    }

}
