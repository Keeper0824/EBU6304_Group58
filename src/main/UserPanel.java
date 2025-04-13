package src.main;
import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    private JLabel membershipLabel;

    public UserPanel(User user) {
        setLayout(new FlowLayout());

        membershipLabel = new JLabel();
        updateMembershipDisplay(user);

        add(membershipLabel);
    }

    private void updateMembershipDisplay(User user) {
        if (user == null) {
            membershipLabel.setText("No user found.");
            return;
        }

        if (user.isMembershipActive()) {
            membershipLabel.setText("Membership expires on: " + user.getMembershipExpiryDate()
                    + " (" + user.getRemainingDays() + " days left)");
        } else {
            membershipLabel.setText("Membership Expired");
            membershipLabel.setForeground(Color.RED);
        }
    }
}

