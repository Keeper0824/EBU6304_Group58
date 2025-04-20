package src.main.java.ViewMembershipTime_story14;

import src.main.java.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserLoader {
    private final static String currentUser = Session.getCurrentNickname();

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static User loadUserFromCSV(String path) {
        // 在真正使用时再拿一次昵称
        String currentNickname = Session.getCurrentNickname();
        if (currentNickname == null) {
            System.err.println("NO USER LOGIN!");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // ☆ 1. 跳过表头
            br.readLine();

            String line;
            // ☆ 2. 从第二行开始，一行行遍历到文件末尾
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;     // 跳过空行

                String[] parts = line.split(",");
                if (parts.length < 8) continue;         // 列数不够也跳过

                // ☆ 3. 找到匹配昵称就返回
                if (parts[1].trim().equals(currentNickname)) {
                    String membershipType = parts[6].trim();
                    LocalDate expiryDate = null;
                    if ("VIP".equalsIgnoreCase(membershipType)) {
                        String dateStr = parts[7].trim();
                        if (!dateStr.isEmpty() && !"null".equalsIgnoreCase(dateStr)) {
                            expiryDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                        }
                    }
                    return new User(currentNickname, expiryDate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 全部遍历完都没 return，就说明找不到喵
        return null;
    }
}
