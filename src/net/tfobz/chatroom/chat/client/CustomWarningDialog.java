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
		try {
			setIconImage(new ImageIcon(getClass().getResource("/icons/warningIco.png")).getImage());
		} catch (NullPointerException e) {
			System.out.println("Error: Couldnt find Image (warningIco)");
		}

		JPanel panel = new JPanel();
		panel.setBounds(0,0,384,127);
		panel.setBackground(new Color(192, 192, 192));
		panel.setLayout(null);
		
		JLabel iconLabel = new JLabel();
		iconLabel.setBounds(10,33,40,50);
		try {
			iconLabel.setIcon(new ImageIcon(getClass().getResource("/icons/warningIco.png")));
		} catch (NullPointerException e) {
			System.out.println("Error: Couldnt find Image (warningIco)");
		}
		iconLabel.setVisible(true);
		panel.add(iconLabel);

		JLabel messageLabel = new JLabel(message);
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 28));
		messageLabel.setForeground(Color.black);
		messageLabel.setBounds(60,33,314,50);
		panel.add(messageLabel);


		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0,126,384,35);
		JButton okButton = new JButton("OK");
		okButton.setBackground(buttonPanel.getForeground());
		okButton.setFont(new Font("Arial", Font.PLAIN, 14));
		okButton.setForeground(Color.black);
		okButton.setFocusPainted(false);
		okButton.setBorderPainted(false);
		okButton.setBounds(0,50,50,50);
		okButton.addActionListener(e -> dispose());
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
