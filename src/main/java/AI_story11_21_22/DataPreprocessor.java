package src.main.java.AI_story11_21_22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Title      : DataPreprocessor.java
 * Description: This class is responsible for loading transaction data from a CSV file.
 *              It validates the data format, skips invalid lines, and creates Transaction objects.
 *
 * @author Wei Muchi
 * @version 1.0
 */
public class DataPreprocessor {

    /**
     * Loads transaction data from a given file path.
     *
     * @param filePath the path to the CSV file containing transaction data
     * @return a list of Transaction objects representing the loaded data
     * @throws IOException if the file does not exist or an error occurs during reading
     */
    public static List<Transaction> loadTransactions(String filePath) throws IOException {
        System.out.println("\n[Data Loading] Starting to load transaction data...");
        System.out.println("[Data Loading] File path: " + new File(filePath).getAbsolutePath());

        List<Transaction> transactions = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("[Data Loading] Error: File does not exist");
            throw new IOException("File not found: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            int skipCount = 0;
            int successCount = 0;

            // Skip header line
            String header = br.readLine();
            System.out.println("[Data Loading] Skipped header line: " + header);

            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();
                if (line.isEmpty()) {
                    skipCount++;
                    continue;
                }

                System.out.printf("[Data Loading] Processing line %d: %s%n", lineCount, line);

                String[] parts = line.split(",");
                if (parts.length != 6) {
                    System.err.printf("[Data Loading] Warning: Line %d has incorrect number of columns (expected 6, actual %d): %s%n",
                            lineCount, parts.length, line);
                    skipCount++;
                    continue;
                }

                try {
                    Transaction transaction = new Transaction(
                            parts[0].trim(),
                            parts[1].trim(),
                            Double.parseDouble(parts[2].trim()),
                            parts[3].trim(),
                            parts[4].trim(),
                            parts[5].trim()
                    );

                    System.out.printf("[Data Loading] Successfully parsed: %s - ¥%.2f (%s)%n",
                            transaction.getDescription(),
                            transaction.getPrice(),
                            transaction.getIoType());

                    transactions.add(transaction);
                    successCount++;
                } catch (NumberFormatException e) {
                    System.err.printf("[Data Loading] Error: Failed to parse price on line %d: %s%n",
                            lineCount, parts[2]);
                    skipCount++;
                }
            }

            System.out.printf("[Data Loading] Finished processing %d lines: %d successful, %d skipped%n",
                    lineCount, successCount, skipCount);

        } catch (Exception e) {
            System.err.println("[Data Loading] Exception occurred:");
            e.printStackTrace();
            throw e;
        }

        return transactions;
    }
}
