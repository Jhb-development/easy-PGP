package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeySelectionDialog extends JDialog {
    private JTable keyTable;
    private String selectedKey;

    public KeySelectionDialog(Frame parent, Object[][] data, String[] columnNames) {
        super(parent, "Select Key", true);
        setLayout(new BorderLayout());

        keyTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(keyTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            int row = keyTable.getSelectedRow();
            if (row != -1) {
                selectedKey = (String) keyTable.getValueAt(row, 1); // Public Key File
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a key.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 300);
        setLocationRelativeTo(parent);
    }

    public String getSelectedKey() {
        return selectedKey;
    }
}
