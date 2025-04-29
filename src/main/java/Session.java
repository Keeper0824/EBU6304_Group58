package src.main.java;


public class Session {
    private static String currentNickname;

    public static void setCurrentNickname(String nickname) {
        currentNickname = nickname;
    }

    public static String getCurrentNickname() {
        return currentNickname;
    }
}

