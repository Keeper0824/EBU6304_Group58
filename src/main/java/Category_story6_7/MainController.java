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
import src.main.java.Login_story1_3.MainMenuApp;
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

    public void initialize() {
        transactionColumn.setCellValueFactory(new PropertyValueFactory<>("transaction"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        IOColumn.setCellValueFactory(new PropertyValueFactory<>("IOType"));

        // 让所有列自动平铺，去掉右侧空白
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 全部文字居中
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
                    // 弹出编辑窗口
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

    @FXML
    private void handleBackToMainMenu(ActionEvent event) {
        try {
            // Close current transaction window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Get the current user from MainMenuApp
            User currentUser = MainMenuApp.getCurrentUser();

            // Open MainMenu with the same user
            new MainMenuApp(currentUser).start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to Main Menu: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadTransactions() {
        // 1. 动态拼路径
        String csvFilePath = "data/" + currentUser + "_transaction.csv";
        File csvFile = new File(csvFilePath);

        // 检查currentUser是否为空或null
        if (currentUser == null || currentUser.isEmpty()) {
            showAlert("Error", "当前用户信息获取失败，无法加载交易数据");
            return;
        }

        // 2. 如果文件不存在，就创建父目录和文件，并写入表头
        if (!csvFile.exists()) {
            try {
                csvFile.getParentFile().mkdirs(); // 确保 data/ 目录存在
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
                    // 根据你的 Transaction 字段顺序写表头
                    bw.write("Id,Transaction,Price,Classification,Date,IOType");
                    bw.newLine();
                }
                System.out.println("已为用户 " + currentUser + " 创建新的交易文件：" + csvFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "无法创建交易文件：" + e.getMessage());
                return;
            }
        }

        // 3. 读取并加载交易
        ObservableList<Transaction> list = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // 跳过表头
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length != 6) {
                    System.err.println("CSV文件格式错误，该行数据字段数量不正确: " + line);
                    continue;
                }
                try {
                    double price = Double.parseDouble(data[2]);
                    Transaction tx = new Transaction(
                            data[0], data[1], price, data[3], data[4], data[5]
                    );
                    list.add(tx);
                } catch (NumberFormatException e) {
                    System.err.println("价格数据格式错误: " + data[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "加载交易时出错：" + e.getMessage());
        }

        // 4. 将数据绑定到表格
        tableView.setItems(list);
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/add.fxml"));
            Pane addPane = loader.load();
            AddController addController = loader.getController(); // 获取子控制器
            Stage stage = new Stage();
            stage.setScene(new Scene(addPane));
            stage.show();

            // 等待添加窗口关闭
            stage.setOnHidden(e -> {
                if (addController.isAdded()) { // 检查是否添加了新数据
                    Transaction newTransaction = addController.getTransaction(); // 获取新添加的交易
                    transactions.add(newTransaction); // 添加到数据列表
                    tableView.refresh(); // 刷新表格
                }
                loadTransactions();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        ObservableList<Transaction> selectedRows = tableView.getSelectionModel().getSelectedItems();
        for (Transaction transaction : selectedRows) {
            transactions.remove(transaction);
            deleteTransactionFromCSV(transaction);
        }
        loadTransactions();
    }

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

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadTransactions();
    }

    private void openEditWindow(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/edit.fxml"));
            Pane editPane = loader.load();
            EditController editController = loader.getController(); // 获取编辑窗口的控制器
            editController.setTransaction(transaction); // 将当前选中的交易数据传递给编辑控制器

            Stage stage = new Stage();
            stage.setScene(new Scene(editPane));
            stage.show();

            // 等待编辑窗口关闭
            stage.setOnHidden(e -> {
                if (editController.isEdited()) { // 检查是否进行了编辑
                    Transaction editedTransaction = editController.getTransaction(); // 获取编辑后的交易数据
                    int index = transactions.indexOf(transaction);
                    if (index != -1) {
                        transactions.set(index, editedTransaction); // 更新表格数据
                        tableView.refresh();
                        updateTransactionInCSV(transaction, editedTransaction); // 更新CSV文件
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

    // 新增的 getTransactions() 方法
    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    @FXML
    private void handleSuggestion(ActionEvent event) {
        try {
            // 确保路径正确，相对于 resources 目录
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/suggestion.fxml"));
            Pane suggestionPane = loader.load();

            // 打开新的窗口并显示建议页面
            Stage suggestionStage = new Stage();
            suggestionStage.setTitle("Suggestion");
            suggestionStage.setScene(new Scene(suggestionPane));
            suggestionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open suggestion page: " + e.getMessage());
        }
    }

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
                showAlert("Info", "No expenses recorded for current month");
                return;
            }

            String analysis = analyzeBudgetWithAI(budget, currentExpenses);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Budget Analysis");
            alert.setHeaderText("Budget Comparison Result");
            alert.setContentText(analysis);
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid budget number");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to analyze budget: " + e.getMessage());
        }
    }

    private boolean isCurrentUserVIP() {
        String currentUser = Session.getCurrentNickname();
        if (currentUser == null || currentUser.isEmpty()) {
            System.err.println("Error: Current user is null or empty!");
            return false;
        }

        String csvFilePath = "data/user.csv"; // 或改用完整路径
        File csvFile = new File(csvFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // 跳过表头
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                System.out.println("Debug - CSV line: " + Arrays.toString(data)); // 调试输出

                // 检查列数是否足够，且用户名匹配（忽略大小写和空格）
                if (data.length >= 7 && data[1].trim().equalsIgnoreCase(currentUser.trim())) {
                    boolean isVIP = "VIP".equalsIgnoreCase(data[6].trim());
                    System.out.println("Debug - VIP status: " + isVIP); // 调试输出
                    return isVIP;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user.csv: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void showAnalysisReport(String reportText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/resources/Category_story6_7/AnalysisReport.fxml"));
            Pane reportPane = loader.load();
            AnalysisReportController reportController = loader.getController();
            reportController.setReportText(reportText);

            Stage stage = new Stage();
            stage.setScene(new Scene(reportPane, 1200, 675)); // 设置窗口大小为 1600x900
            stage.setResizable(false); // 禁止调整窗口大小
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                System.err.println("交易日期格式错误，无法解析: " + tx.getDate());
            }
        }
        return expenses;
    }

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
            throw new Exception("AI分析请求失败: " + e.getMessage());
        }


        return "Sorry, couldn't get analysis from AI. Please try again later.";
    }

}
