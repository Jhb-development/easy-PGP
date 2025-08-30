package GUI;

import javax.swing.*;

public class EasyPGP {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Easy PGP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create panel to hold buttons
        JPanel panel = new JPanel();

        // Create buttons
        JButton btnGenerateKeypair = new JButton("Generate New Keypair");
        JButton btnEncodeMessage = new JButton("Encode Message");
        JButton btnImportCollection = new JButton("Import Collection");
        JButton btnViewKeys = new JButton("View Keys");

        // Add buttons to panel
        panel.add(btnGenerateKeypair);
        panel.add(btnEncodeMessage);
        panel.add(btnImportCollection);
        panel.add(btnViewKeys);

        // Button actions to open new screens
        btnGenerateKeypair.addActionListener(e -> {
            JFrame newFrame = new JFrame("Generate New Keypair");
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setSize(400, 300);
            newFrame.add(new GenerateKeypairPanel());
            newFrame.setVisible(true);
        });

        btnEncodeMessage.addActionListener(e -> {
            JFrame newFrame = new JFrame("Encode Message");
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setSize(400, 300);
            newFrame.add(new EncodeMessagePanel());
            newFrame.setVisible(true);
        });

        btnImportCollection.addActionListener(e -> {
            JFrame newFrame = new JFrame("Import Collection");
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setSize(400, 300);
            newFrame.add(new ImportCollectionPanel());
            newFrame.setVisible(true);
        });

        btnViewKeys.addActionListener(e -> {
            JFrame newFrame = new JFrame("View Keys");
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setSize(400, 300);
            newFrame.add(new ViewKeysPanel());
            newFrame.setVisible(true);
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
