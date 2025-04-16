// CSVUtils.java
package card_management;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    private static final String CSV_FILE = "cards.csv";
    private static final String CSV_SEPARATOR = ",";

    public static List<CreditCard> loadCards() {
        List<CreditCard> cards = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                if (data.length == 4) {
                    cards.add(new CreditCard(
                            data[0],
                            data[1],
                            data[2],
                            data[3]
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("No existing data file, starting fresh");
        }
        return cards;
    }

    public static void saveCards(List<CreditCard> cards) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (CreditCard card : cards) {
                String rawCardNumber = card.getCardNumber().replaceAll("\\s+", "");
                String line = String.join(CSV_SEPARATOR,
                        rawCardNumber,
                        card.getCardHolder(),
                        card.getExpiryDate(),
                        card.getCvv()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            showError("Error saving data: " + e.getMessage());
        }
    }

    private static void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}