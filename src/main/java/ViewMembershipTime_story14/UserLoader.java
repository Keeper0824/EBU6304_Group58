package src.main.java.ViewMembershipTime_story14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserLoader {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static User loadUserFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // 跳过表头
            String line = br.readLine();
            // 继续读，直到遇到非空行或到文件末尾
            while ((line = br.readLine()) != null && line.trim().isEmpty()) {
                // nothing, just skip blank
            }

            if (line != null) {
                String[] parts = line.split(",");
                // 至少要有 8 个字段，才能安全访问 parts[6] 和 parts[7]
                if (parts.length >= 8) {
                    String username       = parts[1].trim();
                    String membershipType = parts[6].trim();  // VIP 或 Normal

                    LocalDate expiryDate = null;
                    if ("VIP".equalsIgnoreCase(membershipType)) {
                        String dateStr = parts[7].trim();
                        if (!dateStr.isEmpty() && !"null".equalsIgnoreCase(dateStr)) {
                            expiryDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                        }
                    }
                    return new User(username, expiryDate);
                } else {
                    System.err.println("The CSV fields are insufficient. It is expected that there will be more than or equal to 8 columns. In reality:" + parts.length);
                }
            } else {
                System.err.println("There are no data rows in the CSV file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
