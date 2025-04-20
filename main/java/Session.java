package src.main.java;


public class Session {
    // 只保存昵称
    private static String currentNickname;

    public static void setCurrentNickname(String nickname) {
        currentNickname = nickname;
    }

    public static String getCurrentNickname() {
        return currentNickname;
    }
}

