package src.main.java;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class UserLoader {
    public static User loadUserFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // 跳过表头
            String line = br.readLine(); // 读取第一行用户数据
            if (line != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                return new User(username, date);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

