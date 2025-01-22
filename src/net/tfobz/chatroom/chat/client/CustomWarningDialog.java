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
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		JLabel iconLabel = new JLabel();
		iconLabel.setBounds(50,50,60,60);
		try {
			iconLabel.setIcon(new ImageIcon(getClass().getResource("/icons/warningIco.png")));
		} catch (NullPointerException e) {
			System.out.println("Error: Couldnt find Image (warningIco)");
		}
		
		panel.add(iconLabel);

		JLabel messageLabel = new JLabel(message);
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 26));
		messageLabel.setForeground(Color.black);
		messageLabel.setBounds(0,0,50,50);
		panel.add(messageLabel);

		JButton okButton = new JButton("OK");
		okButton.setFont(new Font("Arial", Font.PLAIN, 14));
		okButton.setForeground(Color.black);
		okButton.setFocusPainted(false);
		okButton.setBorderPainted(false);
		okButton.setBounds(0,50,50,50);
		okButton.addActionListener(e -> dispose());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(192, 192, 192));
		buttonPanel.setBounds(0,126,384,35);
		buttonPanel.add(okButton);

		getContentPane().add(panel);
		getContentPane().add(buttonPanel);
	}

	public static void showWarning(String message) {
		CustomWarningDialog dialog = new CustomWarningDialog(message);
		dialog.setVisible(true);
	}

	public static void main(String[] args) {
		showWarning("Please check your input!");
	}
}
