package net.tfobz.chatroom.chat.client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;

/**
 * @author Michael
 * @see http://www.vorlesungen.uos.de/informatik/b06/
 */
public class ChatClient extends JFrame {
	private final static Color colours[][] = {
        {
            new Color(50, 50, 75),
            new Color(171, 181, 216),
            new Color(225, 225, 225),
            new Color(11, 9, 10),
            new Color(111, 116, 146)
        }
    };
	private static int ClientIDCounter = 0;
	
	public final int CLIENT_ID = ClientIDCounter++;
	
	public static final int DEFAULT_PORT = 65535;
	public static final String DEFAULT_IP = "localhost";
	public static final String DEFAULT_USERNAME = "user";
	
	private int port = DEFAULT_PORT;
	private String ip = DEFAULT_IP;
	private String username = DEFAULT_USERNAME + CLIENT_ID;
	
	private ChatLogin login;
	
	Container contentPane;
	
	JTextArea textArea;
	JTextField textField;
	JButton button;
	
	public ChatClient () {
		super();
		setBounds(25, 25, 1080, 720);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Chatroom");
		
		contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(colours[0][1]);
		
		int w = getWidth() - 6;
		int h = getHeight() - 40;
		
		textArea = new JTextArea();
		textField = new JTextField();
		button = new JButton();
		
		button.setText("Send");
		
		textArea.setBounds(10, 10, w - 20, h - 20 - 60);
		textField.setBounds(10, h - 60, w - 200, 50);
		button.setBounds(10 + w - 190, h - 60, 170, 50);
		
		textArea.setFont(new Font ("Arial", Font.PLAIN, textField.getHeight() / 2 + 1));
		textField.setFont(new Font ("Arial", Font.PLAIN, textField.getHeight() / 2 + 1));
		button.setFont(new Font ("Arial", Font.PLAIN, button.getHeight() / 2 + 1));
		
		textArea.setBackground(colours[0][2]);
		textArea.setForeground(colours[0][3]);
		textField.setBackground(colours[0][2]);
		textField.setForeground(colours[0][3]);
		button.setBackground(colours[0][4]);
		button.setForeground(colours[0][2]);
		
		textArea.setBorder(null);
		textField.setBorder(null);
		button.setBorder(null);
		
		button.setFocusPainted(false);
		
		contentPane.add(textArea);
		contentPane.add(textField);
		contentPane.add(button);
	}
	
	private void connect () {
		Socket client = null;
		try {
			client = new Socket(ip, port);
			BufferedReader in = 
				new BufferedReader( new InputStreamReader(client.getInputStream()));
			PrintStream out = new PrintStream(client.getOutputStream());
			BufferedReader consoleIn =
				new BufferedReader(new InputStreamReader(System.in));
			
			out.println(username);
			
			new ChatClientThread(in).start();
			
			while (true) {
				String line = consoleIn.readLine();
				if (line == null)
					// pressed [Ctrl]+Z to sign out
					break;
				out.println(line);
			}
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try { client.close(); } catch (Exception e1) { ; }
		}
	}
	
	public static void main(String[] args) {
		ChatClient c = new ChatClient();
		c.setVisible(true);
		c.login = c.new ChatLogin(c);
		c.login.setVisible(true);
	}
	
	@SuppressWarnings("serial")
	private class ChatLogin extends JDialog {
		
		private Container contentPane;
		
		private JLabel labelIP;
		private JLabel labelPort;
		private JLabel labelUsername;
		
		private JTextField textFieldIP;
		private JTextField textFieldPort;
		private JTextField textFieldUsername;
		
		private JButton buttonConnect;
		
		public ChatLogin (ChatClient owner) {
			super(owner, "Log In", true);
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			setBounds(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight());
			
			contentPane = getContentPane();
			contentPane.setLayout(null);
			contentPane.setBackground(colours[0][1]);
			
			labelIP = new JLabel();
			labelPort = new JLabel();
			labelUsername = new JLabel();

			textFieldIP = new JTextField();
			textFieldPort = new JTextField();
			textFieldUsername = new JTextField();
			
			buttonConnect = new JButton();
			
			labelIP.setText("Enter IP: ");
			labelPort.setText("Enter Port: ");
			labelUsername.setText("Enter Username: ");
			
			buttonConnect.setText("Connect");
			
//			labelIP.setBounds(10, y, width, height);
//			labelPort.setBounds(10, y, width, height);
//			labelUsername.setBounds(10, y, width, height);
//			
//			textFieldIP.setBounds(x, y, width, height);
//			textFieldPort.setBounds(x, y, width, height);
//			textFieldUsername.setBounds(x, y, width, height);
			
			buttonConnect.setBounds(10, getHeight() - 60, getWidth()-20, 50);
			
			contentPane.add(labelIP);
			contentPane.add(labelPort);
			contentPane.add(labelUsername);
			
			contentPane.add(textFieldIP);
			contentPane.add(textFieldPort);
			contentPane.add(textFieldUsername);

			contentPane.add(buttonConnect);
		}
	}
}
