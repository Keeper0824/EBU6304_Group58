package src.main.java.card_management_story12;

import src.main.java.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Title      : CSVUtils.java
 * Description: Utility class for loading and saving credit card data for the current user.
 *              Each user's cards are stored in a CSV file named "data/{username}_cards.csv".
 *              loadCards() reads existing entries into a List<CreditCard>, returning an empty list
 *              if no file is found or an error occurs. saveCards() writes the provided list of
 *              CreditCard objects to the CSV, overwriting any existing content.
 *
 * @author Yudian Wang
 * @version 1.0
 * @author Haoran Sun
 * @version 1.1
 */
public class CSVUtils {

    /**
     * Loads credit cards from the current user's CSV file.
     * If the file does not exist or an I/O error occurs, an empty list is returned.
     *
     * @return a List of CreditCard objects parsed from the user's CSV
     */
    public static List<CreditCard> loadCards() {
        String currentUser = Session.getCurrentNickname();
        String CSV_FILE = "data/" + currentUser + "_cards.csv";
        List<CreditCard> cards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    cards.add(new CreditCard(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("无历史数据文件");
        }
        return cards;
    }

    /**
     * Saves the provided list of credit cards to the current user's CSV file.
     * Overwrites any existing file content. Each card is written as a comma-separated line:
     * cardNumber,cardHolder,expiryDate,cvv
     *
     * @param cards the List of CreditCard objects to save
     */
    public static void saveCards(List<CreditCard> cards) {
        String currentUser = Session.getCurrentNickname();
        String CSV_FILE = "data/" + currentUser + "_cards.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (CreditCard card : cards) {
                bw.write(String.join(",",
                        card.getCardNumber(),
                        card.getCardHolder(),
                        card.getExpiryDate(),
                        card.getCvv()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存失败: " + e.getMessage());
        }
    }
}
