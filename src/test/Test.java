package src.test;
import javax.swing.*;
import src.main.*;

public class Test {
    public static void main(String[] args) {
        User user = UserLoader.loadUserFromCSV("data/user.csv");

        JFrame frame = new JFrame("Membership Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLocationRelativeTo(null); // 居中

        UserPanel panel = new UserPanel(user);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}

