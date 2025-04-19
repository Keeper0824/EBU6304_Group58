package src.main.java.card_management_story12;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    private static final String CSV_FILE = "data/cards.csv"; // 添加默认文件路径

    // 修改loadCards方法签名（移除参数）
    public static List<CreditCard> loadCards() {
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
            System.out.println("No existing data file");
        }
        return cards;
    }

    // 修改saveCards方法签名（移除path参数）
    public static void saveCards(List<CreditCard> cards) {
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
            System.err.println("Save error: " + e.getMessage());
        }
    }
}