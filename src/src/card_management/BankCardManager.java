package src.src.card_management;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BankCardManager extends JFrame {
    private DefaultTableModel tableModel;
    private JTable cardTable;
    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField expiryDateField;
    private JTextField cvvField;

    public BankCardManager() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Bank Card Manager");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 初始化输入组件
        JPanel inputPanel = createInputPanel();

        // 创建表格
        String[] columnNames = {"Card Number", "Card Holder", "Expiry Date", "CVV"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cardTable = new JTable(tableModel);
        cardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 创建按钮和监听器
        JButton addButton = new JButton("Add Card");
        addButton.addActionListener(new AddCardListener(
                cardNumberField, cardHolderField, expiryDateField, cvvField, tableModel
        ));

        JButton deleteButton = new JButton("Delete Selected Card");
        deleteButton.addActionListener(new DeleteCardListener(cardTable, tableModel));

        // 布局管理
        setLayout(new BorderLayout(5, 5));
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(cardTable), BorderLayout.CENTER);
        add(addButton, BorderLayout.WEST);
        add(deleteButton, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Add New Card"));

        cardNumberField = new JTextField();
        cardHolderField = new JTextField();
        expiryDateField = new JTextField();
        cvvField = new JTextField();

        panel.add(new JLabel("Card Number:"));
        panel.add(cardNumberField);
        panel.add(new JLabel("Card Holder:"));
        panel.add(cardHolderField);
        panel.add(new JLabel("Expiry Date (MM/YY):"));
        panel.add(expiryDateField);
        panel.add(new JLabel("CVV:"));
        panel.add(cvvField);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankCardManager().setVisible(true);
        });
    }
}
