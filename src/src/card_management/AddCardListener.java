package src.src.card_management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class AddCardListener implements ActionListener {
    private final JTextField cardNumberField;
    private final JTextField cardHolderField;
    private final JTextField expiryDateField;
    private final JTextField cvvField;
    private final DefaultTableModel tableModel;

    public AddCardListener(JTextField cardNumberField,
                           JTextField cardHolderField,
                           JTextField expiryDateField,
                           JTextField cvvField,
                           DefaultTableModel tableModel) {
        this.cardNumberField = cardNumberField;
        this.cardHolderField = cardHolderField;
        this.expiryDateField = expiryDateField;
        this.cvvField = cvvField;
        this.tableModel = tableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cardNumber = cardNumberField.getText().trim();
        String cardHolder = cardHolderField.getText().trim();
        String expiryDate = expiryDateField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (validateCardDetails(cardNumber, cardHolder, expiryDate, cvv)) {
            tableModel.addRow(new String[]{
                    formatCardNumber(cardNumber),
                    cardHolder.toUpperCase(),
                    expiryDate,
                    cvv
            });
            clearInputFields();
        }
    }

    private boolean validateCardDetails(String cardNumber, String cardHolder,
                                        String expiryDate, String cvv) {
        if (!Pattern.matches("^\\d{16}$", cardNumber)) {
            showError("Invalid card number. Must be 16 digits.");
            return false;
        }

        if (!Pattern.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)+$", cardHolder)) {
            showError("Invalid card holder name. Use full name.");
            return false;
        }

        if (!Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$", expiryDate)) {
            showError("Invalid expiry date. Use MM/YY format.");
            return false;
        }

        if (!Pattern.matches("^\\d{3}$", cvv)) {
            showError("Invalid CVV. Must be 3 digits.");
            return false;
        }

        return true;
    }

    private String formatCardNumber(String cardNumber) {
        return cardNumber.replaceAll("(.{4})", "$1 ").trim();
    }

    private void clearInputFields() {
        cardNumberField.setText("");
        cardHolderField.setText("");
        expiryDateField.setText("");
        cvvField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }
}