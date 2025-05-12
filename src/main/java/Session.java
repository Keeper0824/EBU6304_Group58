package src.main.java;

/**
 * Title      : Session.java
 * Description: A simple session manager that stores the current user's nickname
 * in a static field for global access across the application. Useful for passing
 * the logged-in user's identity between controllers without reloading from CSV.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class Session {
    /**
     * The nickname of the currently logged-in user.
     */
    private static String currentNickname;

    /**
     * Sets the nickname of the current user in this session.
     *
     * @param nickname the user's nickname to store
     */
    public static void setCurrentNickname(String nickname) {
        currentNickname = nickname;
    }

    /**
     * Retrieves the nickname of the current user stored in this session.
     *
     * @return the stored nickname, or {@code null} if none has been set
     */
    public static String getCurrentNickname() {
        return currentNickname;
    }
}
