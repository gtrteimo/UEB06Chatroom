package net.tfobz.chatroom.chat.client;

import javax.swing.*;
import java.awt.*;

public class CustomWarningDialog extends JDialog {
	public CustomWarningDialog(String message) {
		setTitle("Warning");
		setModal(true);
		setSize(400, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(10, 10));

		JLabel iconLabel = new JLabel();

		try {
			iconLabel.setIcon(new ImageIcon(getClass().getResource("/icons/warningIco.png")));
		} catch (NullPointerException e) {
			System.out.println("Error: Couldnt find Image (warningIco)");
		}
		panel.add(iconLabel, BorderLayout.WEST);

		JLabel messageLabel = new JLabel(message);
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 26));
		messageLabel.setForeground(Color.black);
		panel.add(messageLabel, BorderLayout.CENTER);

		JButton okButton = new JButton("OK");
		okButton.setFont(new Font("Arial", Font.PLAIN, 14));
		okButton.setForeground(Color.black);
		okButton.setFocusPainted(false);
		okButton.setBorderPainted(false);
		okButton.addActionListener(e -> dispose());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(192, 192, 192));
		buttonPanel.add(okButton);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public static void showWarning(String message) {
		CustomWarningDialog dialog = new CustomWarningDialog(message);
		dialog.setVisible(true);
	}

	public static void main(String[] args) {
		showWarning("Please check your input!");
	}
}
