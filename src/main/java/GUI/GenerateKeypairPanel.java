package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import app.KeyGen;

public class GenerateKeypairPanel extends JPanel {
	private JTextField identityField;
	private JPasswordField passphraseField;

	public GenerateKeypairPanel() {
		setLayout(new BorderLayout());

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel identityLabel = new JLabel("Identity (Email or Name):");
		identityField = new JTextField(20);
		JLabel passphraseLabel = new JLabel("Passphrase:");
		passphraseField = new JPasswordField(20);

		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(identityLabel, gbc);
		gbc.gridx = 1;
		formPanel.add(identityField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(passphraseLabel, gbc);
		gbc.gridx = 1;
		formPanel.add(passphraseField, gbc);

		// Add Generate button
		JButton generateButton = new JButton("Generate Keypair");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		formPanel.add(generateButton, gbc);

		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String identity = identityField.getText();
				String passphrase = new String(passphraseField.getPassword());
				try {
					String[] keys = KeyGen.generatePGPKeyPair(identity, passphrase);
					JOptionPane.showMessageDialog(GenerateKeypairPanel.this, "Keypair generated!\nPublic Key:\n" + keys[0] + "\n\nPrivate Key:\n" + keys[1], "Success", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(GenerateKeypairPanel.this, "Error generating keypair: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		add(new JLabel("Generate Keypair Screen", SwingConstants.CENTER), BorderLayout.NORTH);
		add(formPanel, BorderLayout.CENTER);
	}
}
