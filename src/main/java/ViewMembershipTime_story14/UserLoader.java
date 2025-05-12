package src.main.java.ViewMembershipTime_story14;

import src.main.java.Session;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Title      : UserLoader.java
 * Description: Utility class for loading User objects from a CSV file.
 *              It reads the CSV file, skips the header row, and returns
 *              the User matching the current session nickname. If the user
 *              has VIP status, it also parses the expiry date.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class UserLoader {

    /**
     * Date formatter for parsing membership expiry dates in 'yyyy-MM-dd' format.
     */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Loads the User corresponding to the currently logged-in nickname from the given CSV file.
     * The CSV is expected to have these columns in order:
     * ID, Nickname, Password, Email, Gender, DateOfBirth, isVIP, ExpiryDate
     *
     * @param path the file path to the CSV containing user data
     * @return a User object with username and expiry date (null expiryDate if not VIP),
     *         or null if no matching record is found or on error
     */
    public static User loadUserFromCSV(String path) {
        String currentNickname = Session.getCurrentNickname();
        if (currentNickname == null) {
            System.err.println("NO USER LOGIN!");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // 1. Skip header row
            br.readLine();

            String line;
            // 2. Read each subsequent line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;     // Skip empty lines

                String[] parts = line.split(",");
                if (parts.length < 8) continue;         // Skip malformed lines

                // 3. Match nickname
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

        // No match found
        return null;
    }
}
