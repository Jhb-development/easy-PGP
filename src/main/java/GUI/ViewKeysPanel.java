package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ViewKeysPanel extends JPanel {
	public ViewKeysPanel() {
		setLayout(new BorderLayout());

		String[] columnNames = {"Identity", "Public Key File", "Private Key File"};
		Object[][] data = loadKeyPairs();

		// Use DefaultTableModel for dynamic updates
		javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		// Delete button
		JButton deleteButton = new JButton("Delete Selected Keypair");
		deleteButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a keypair to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String identity = (String) model.getValueAt(selectedRow, 0);
			String pubFile = (String) model.getValueAt(selectedRow, 1);
			String privFile = (String) model.getValueAt(selectedRow, 2);
			int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the keypair for '" + identity + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				boolean deleted = true;
				if (pubFile != null && !pubFile.isEmpty()) {
					java.io.File f = new java.io.File("src/main/resources/keys/" + pubFile);
					if (f.exists() && !f.delete()) deleted = false;
				}
				if (privFile != null && !privFile.isEmpty()) {
					java.io.File f = new java.io.File("src/main/resources/keys/" + privFile);
					if (f.exists() && !f.delete()) deleted = false;
				}
				if (deleted) {
					model.removeRow(selectedRow);
					JOptionPane.showMessageDialog(this, "Keypair deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Failed to delete one or more key files.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// View button
		JButton viewButton = new JButton("View Selected Key(s)");
		viewButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a keypair to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String pubFile = (String) model.getValueAt(selectedRow, 1);
			String privFile = (String) model.getValueAt(selectedRow, 2);
			StringBuilder sb = new StringBuilder();
			if (pubFile != null && !pubFile.isEmpty()) {
				sb.append("Public Key:\n");
				sb.append(readKeyFile("src/main/resources/keys/" + pubFile));
				sb.append("\n\n");
			}
			if (privFile != null && !privFile.isEmpty()) {
				sb.append("Private Key:\n");
				sb.append(readKeyFile("src/main/resources/keys/" + privFile));
			}
			JTextArea textArea = new JTextArea(sb.toString());
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			JScrollPane scrollPaneKey = new JScrollPane(textArea);
			scrollPaneKey.setPreferredSize(new java.awt.Dimension(600, 400));
			JOptionPane.showMessageDialog(this, scrollPaneKey, "Key(s) Viewer", JOptionPane.INFORMATION_MESSAGE);
		});

		// Copy Public Key button
		JButton copyButton = new JButton("Copy Public Key");
		copyButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a keypair to copy the public key.", "No Selection", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String pubFile = (String) model.getValueAt(selectedRow, 1);
			if (pubFile == null || pubFile.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No public key file found for this keypair.", "No Public Key", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String pubKey = readKeyFile("src/main/resources/keys/" + pubFile);
			if (pubKey != null) {
				java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(pubKey);
				java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
				JOptionPane.showMessageDialog(this, "Public key copied to clipboard.", "Copied", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Failed to read public key file.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(viewButton);
		buttonPanel.add(copyButton);
		buttonPanel.add(deleteButton);
		topPanel.add(new JLabel("View Keys Screen", SwingConstants.CENTER), BorderLayout.CENTER);
		topPanel.add(buttonPanel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

	}

	private String readKeyFile(String path) {
		try {
			java.nio.file.Path filePath = java.nio.file.Paths.get(path);
			return java.nio.file.Files.readString(filePath);
		} catch (Exception ex) {
			return null;
		}
	}

	private Object[][] loadKeyPairs() {
		File keysDir = new File("src/main/resources/keys");
		if (!keysDir.exists() || !keysDir.isDirectory()) {
			return new Object[0][3];
		}
		File[] files = keysDir.listFiles();
		java.util.Map<String, String[]> keyMap = new java.util.HashMap<>();
		if (files != null) {
			for (File file : files) {
				String name = file.getName();
				if (name.endsWith("_public.asc")) {
					String identity = name.substring(0, name.length() - 11);
					keyMap.putIfAbsent(identity, new String[2]);
					keyMap.get(identity)[0] = name;
				} else if (name.endsWith("_private.asc")) {
					String identity = name.substring(0, name.length() - 12);
					keyMap.putIfAbsent(identity, new String[2]);
					keyMap.get(identity)[1] = name;
				}
			}
		}
		Object[][] data = new Object[keyMap.size()][3];
		int i = 0;
		for (java.util.Map.Entry<String, String[]> entry : keyMap.entrySet()) {
			data[i][0] = entry.getKey();
			data[i][1] = entry.getValue()[0] != null ? entry.getValue()[0] : "";
			data[i][2] = entry.getValue()[1] != null ? entry.getValue()[1] : "";
			i++;
		}
		return data;
	}
}
