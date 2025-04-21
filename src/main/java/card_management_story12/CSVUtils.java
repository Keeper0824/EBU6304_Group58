package src.main.java.card_management_story12;

import src.main.java.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    private final static String currentUser = Session.getCurrentNickname();
    private static final String CSV_FILE = "data/" + currentUser + "_cards.csv";

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
            System.out.println("无历史数据文件");
        }
        return cards;
    }

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
            System.err.println("保存失败: " + e.getMessage());
        }
    }
}