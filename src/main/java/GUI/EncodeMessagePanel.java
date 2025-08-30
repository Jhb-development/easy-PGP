package GUI;

import javax.swing.*;
import java.awt.*;
import app.KeyTableLoader;
import app.PGPMessageEncoder;

public class EncodeMessagePanel extends JPanel {
	private JTextArea inputTextArea;
	private JTextArea outputTextArea;
	private JButton selectKeysButton;
	private JButton encodeButton;

	private String selectedPublicKeyFile;

	public EncodeMessagePanel() {
		setLayout(new BorderLayout(10, 10));

		// Input text area
		inputTextArea = new JTextArea(6, 40);
		inputTextArea.setLineWrap(true);
		inputTextArea.setWrapStyleWord(true);
		JScrollPane inputScroll = new JScrollPane(inputTextArea);
		inputScroll.setBorder(BorderFactory.createTitledBorder("Message to Encode"));

		// Output text area
		outputTextArea = new JTextArea(6, 40);
		outputTextArea.setLineWrap(true);
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setEditable(false);
		JScrollPane outputScroll = new JScrollPane(outputTextArea);
		outputScroll.setBorder(BorderFactory.createTitledBorder("Encoded Output"));

		// Buttons
		selectKeysButton = new JButton("Select Keys");
	encodeButton = new JButton("Encode Message");

	selectKeysButton.addActionListener(e -> openKeySelectionDialog());
	encodeButton.addActionListener(e -> encodeMessage());

	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	buttonPanel.add(selectKeysButton);
	buttonPanel.add(encodeButton);

	// Layout
	JPanel centerPanel = new JPanel();
	centerPanel.setLayout(new GridLayout(2, 1, 10, 10));
	centerPanel.add(inputScroll);
	centerPanel.add(outputScroll);

	add(centerPanel, BorderLayout.CENTER);
	add(buttonPanel, BorderLayout.SOUTH);
	}

	private void encodeMessage() {
		if (selectedPublicKeyFile == null || selectedPublicKeyFile.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please select a public key first.", "No Key Selected", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String message = inputTextArea.getText();
		if (message == null || message.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a message to encode.", "No Message", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			String keyPath = "src/main/resources/keys/" + selectedPublicKeyFile;
			String encoded = PGPMessageEncoder.encodeMessage(message, keyPath);
			outputTextArea.setText(encoded);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error encoding message: " + ex.getMessage(), "Encoding Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openKeySelectionDialog() {
		String[] columnNames = {"Identity", "Public Key File", "Private Key File"};
		Object[][] data = KeyTableLoader.loadKeyPairs();
		KeySelectionDialog dialog = new KeySelectionDialog((Frame) SwingUtilities.getWindowAncestor(this), data, columnNames);
		dialog.setVisible(true);
		String selectedKey = dialog.getSelectedKey();
		if (selectedKey != null) {
			selectedPublicKeyFile = selectedKey;
			JOptionPane.showMessageDialog(this, "Selected public key: " + selectedKey, "Key Selected", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
