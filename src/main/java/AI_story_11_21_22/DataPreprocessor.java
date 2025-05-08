package src.main.java.AI_story_11_21_22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPreprocessor {
    public static List<Transaction> loadTransactions(String filePath) throws IOException {
        System.out.println("\n【数据加载】开始加载交易数据...");
        System.out.println("【数据加载】文件路径: " + new File(filePath).getAbsolutePath());

        List<Transaction> transactions = new ArrayList<>();
        File file = new File(filePath);

        if(!file.exists()) {
            System.err.println("【数据加载】错误: 文件不存在");
            throw new IOException("文件不存在: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            int skipCount = 0;
            int successCount = 0;

            // 跳过标题行
            String header = br.readLine();
            System.out.println("【数据加载】跳过的标题行: " + header);

            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();
                if(line.isEmpty()) {
                    skipCount++;
                    continue;
                }

                System.out.printf("【数据加载】正在处理第%d行: %s%n", lineCount, line);

                String[] parts = line.split(",");
                if (parts.length != 6) {
                    System.err.printf("【数据加载】警告: 第%d行数据列数不正确(应为6列，实际%d列): %s%n",
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

                    System.out.printf("【数据加载】成功解析: %s - ¥%.2f (%s)%n",
                            transaction.getDescription(),
                            transaction.getPrice(),
                            transaction.getIoType());

                    transactions.add(transaction);
                    successCount++;
                } catch (NumberFormatException e) {
                    System.err.printf("【数据加载】错误: 第%d行价格解析失败: %s%n",
                            lineCount, parts[2]);
                    skipCount++;
                }
            }

            System.out.printf("【数据加载】完成，共处理%d行: 成功%d条，跳过%d条%n",
                    lineCount, successCount, skipCount);

        } catch (Exception e) {
            System.err.println("【数据加载】发生异常:");
            e.printStackTrace();
            throw e;
        }

        return transactions;
    }
}