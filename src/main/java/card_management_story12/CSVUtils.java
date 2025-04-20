package src.main.java.card_management_story12;

import src.main.java.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    private final static String currentUser = Session.getCurrentNickname();
    private static final String DATA_DIR = "data/";
    private static final String CSV_FILE = DATA_DIR + currentUser + "cards.csv";

    static {
        // 自动创建数据存储目录
        File dir = new File(DATA_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create data directory: " + DATA_DIR);
        }
    }

    public static List<CreditCard> loadCards() {
        List<CreditCard> cards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 4); // 安全分割防止字段含逗号
                if (data.length == 4) {
                    cards.add(new CreditCard(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim()
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing data, starting fresh.");
        } catch (IOException e) {
            System.err.println("Read error: " + e.getMessage());
        }
        return cards;
    }

    public static void saveCards(List<CreditCard> cards) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (CreditCard card : cards) {
                // 使用转义处理字段中的逗号（需实现escapeCsv方法）
                String line = String.join(",",
                        escapeCsv(card.getCardNumber()),
                        escapeCsv(card.getCardHolder()),
                        escapeCsv(card.getExpiryDate()),
                        escapeCsv(card.getCvv())
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    private static String escapeCsv(String field) {
        // 简单转义：如果包含逗号或换行，用双引号包裹
        if (field.contains(",") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}