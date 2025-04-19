/*package com.example.transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class createCSV {
    public static void main(String[] args) {
        String csvFilePath = "transactions.csv";
        String[] header = {"Transaction", "Price", "Classification", "Date"};
        String[][] data = {
                {"iphone 15(1TB)", "9900", "recreation","2025-3-18"},
                {"Nike T-shirt", "350", "clothing","2025-3-21"},
        };

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            // 写入表头
            writer.write(String.join(",", header));
            writer.newLine();

            // 写入数据
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }

            System.out.println("CSV 文件写入完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/